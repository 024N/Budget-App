package oz.budget.management.features.history;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.View;
import oz.budget.management.R;
import oz.budget.management.app.Toolbar;
import oz.budget.management.features.budgetmonth.BudgetMonthFragment;
import oz.budget.management.model.PresentationHistory;
import oz.budget.management.util.DataManager;
import oz.budget.management.util.DateUtil;
import oz.budget.management.util.FragmentRecycler;
import oz.budget.management.util.SimpleItemAdapter;
import java.util.ArrayList;

public final class HistoryFragment
    extends FragmentRecycler<HistoryPresenter, SimpleItemAdapter<PresentationHistory>>
    implements HistoryView, SimpleItemAdapter.OnSimpleItemClick<PresentationHistory> {

  public static HistoryFragment newInstance() {
    return new HistoryFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mPresenter = new HistoryPresenterImpl(DataManager.getInstance(getActivity()));
    mAdapter = new SimpleItemAdapter<>(this, true);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initializeRecycler();
    initializeFAB();
    initializeToolbar();

    mPresenter.attach(this);
    mPresenter.loadHistory();
  }

  private void initializeRecycler() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    mView.recycler.setLayoutManager(layoutManager);
    mView.recycler.setAdapter(mAdapter);
  }

  @SuppressLint("RestrictedApi")
  private void initializeFAB() {
    mView.fab.setVisibility(View.GONE);
  }

  private void initializeToolbar() {
    Toolbar toolbar = (Toolbar) getActivity();
    toolbar.setToolbarTitle(getString(R.string.history));
  }

  @Override public void onHistoryLoaded(@NonNull ArrayList<PresentationHistory> histories) {
    mAdapter.addAll(histories);
    if (histories.isEmpty()) {
      mView.included.text.setText(getString(R.string.no_history));
      mView.included.noItem.setVisibility(View.VISIBLE);
    } else {
      mView.included.noItem.setVisibility(View.GONE);
    }
  }

  @Override public void onError(@Nullable String error) {
    super.onError(error);
  }

  @Override public void onSimpleItemClick(@NonNull PresentationHistory history) {
    String title = DateUtil.formatDate(history.getYear(), history.getMonth());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      displayFragmentWithAnimation(
          BudgetMonthFragment.newInstance(title, history.getMonth(), history.getYear()));
    } else {
      displayFragment(BudgetMonthFragment.newInstance(title, history.getMonth(), history.getYear()));
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void displayFragmentWithAnimation(@NonNull Fragment fragment) {
    TransitionSet transitionSet = new TransitionSet();
    //transitionSet.addTransition(new Fade().addTarget(R.id.header).addTarget(R.id.tabs).setDuration(200));
    //transitionSet.addTransition(new Slide(Gravity.BOTTOM).addTarget(R.id.viewpager).setDuration(250));
    transitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);

    fragment.setExitTransition(new Fade(Fade.OUT).setDuration(200));
    fragment.setEnterTransition(transitionSet);
    displayFragment(fragment);
  }

  private void displayFragment(@NonNull Fragment fragment) {
    getFragmentManager().beginTransaction()
        .replace(R.id.frame_container, fragment)
        .addToBackStack("PagerFragment")
        .commit();
  }
}
