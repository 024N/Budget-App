package oz.budget.management.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.onesignal.OneSignal;

import oz.budget.management.R;
import oz.budget.management.databinding.ActivityMainBinding;
import oz.budget.management.features.budgetform.BudgetFormActivity;
import oz.budget.management.features.budgetmonth.BudgetMonthFragment;
import oz.budget.management.features.budgets.BudgetFragment;
import oz.budget.management.features.categories.CategoryFragment;
import oz.budget.management.features.history.HistoryFragment;
import oz.budget.management.features.transaction.TransactionFragment;
import oz.budget.management.features.transactionform.TransactionFormActivity;
import oz.budget.management.model.Budget;
import oz.budget.management.model.Category;
import oz.budget.management.util.DataManager;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;

public final class MainActivity extends AppCompatActivity
    implements Toolbar, BottomNavigationView.OnNavigationItemSelectedListener,
    FragmentManager.OnBackStackChangedListener {

  private AdView mAdView;
  private InterstitialAd mInterstitialAd;

  private static final String ACTION_ADD_BUDGET = "oz.budget.management.ADD_BUDGET";
  private static final String ACTION_ADD_TRANSACTION = "oz.budget.management.ADD_TRANSACTION";

  public static final String TOOLBAR_TITLE = "toolbar_title";

  private ActivityMainBinding mView;
  private Disposable mSubscription;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTheme(R.style.AppTheme);
    super.onCreate(savedInstanceState);

    // OneSignal Initialization
    OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();

    mView = DataBindingUtil.setContentView(this, R.layout.activity_main);

    // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
    MobileAds.initialize(this, "ca-app-pub-1479098748222652~5930496028");

    mAdView = findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);

    AdRequest adRequest2 = new AdRequest.Builder().build();
    mInterstitialAd = new InterstitialAd(this);
    mInterstitialAd.setAdUnitId("ca-app-pub-1479098748222652/9873734176");
    mInterstitialAd.loadAd(adRequest2);

    mInterstitialAd.setAdListener(new AdListener() {
      @Override
      public void onAdLoaded() {
        // Code to be executed when an ad finishes loading.
          Log.e("XXXXXXXXX", "No error just tried onAdLoaded");
          mInterstitialAd.show();
      }

      @Override
      public void onAdFailedToLoad(int errorCode) {
        Log.e("XXXXXXXXXXX", "interstaler failed");
        // Code to be executed when an ad request fails.
      }

      @Override
      public void onAdOpened() {
        // Code to be executed when the ad is displayed.
      }

      @Override
      public void onAdLeftApplication() {
        // Code to be executed when the user has left the app.
      }

      @Override
      public void onAdClosed() {
        // Code to be executed when the interstitial ad is closed.
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

      }
    });

    initializeToolbar();
    initializeBottomNavigationView();
    handleFirstLaunch();
    getFragmentManager().addOnBackStackChangedListener(this);

    if (savedInstanceState == null) {
      switch (getIntent().getAction()) {
        case ACTION_ADD_BUDGET:
          startActivity(BudgetFormActivity.newInstance(this));
          break;

        case ACTION_ADD_TRANSACTION:
          startActivity(TransactionFormActivity.newInstance(this));
          break;

        default:
      }
      displayFragment(BudgetMonthFragment.newInstance());
    }
  }

  @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    setToolbarTitle(savedInstanceState.getString(TOOLBAR_TITLE));
  }

  @Override protected void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    if (getSupportActionBar() != null && !TextUtils.isEmpty(getSupportActionBar().getTitle())) {
      savedInstanceState.putString(TOOLBAR_TITLE, String.valueOf(getSupportActionBar().getTitle()));
    }
  }

  @Override protected void onStop() {
    super.onStop();
    if (mSubscription != null) {
      mSubscription.dispose();
    }
  }

  private void initializeToolbar() {
    setSupportActionBar(mView.included.toolbar);
  }

  private void initializeBottomNavigationView() {
    BottomNavigationView navigation = mView.navigation;
    navigation.setOnNavigationItemSelectedListener(this);
  }

  private void displayFragment(@NonNull Fragment fragment) {
    getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
  }

  @Override public void setToolbarTitle(CharSequence title) {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(title);
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
        return true;

      default:
        return false;
    }
  }

  @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    clearBackStack();

    switch (item.getItemId()) {
      case R.id.home:
        displayFragment(BudgetMonthFragment.newInstance());
        return true;

      case R.id.transactions:
        displayFragment(TransactionFragment.newInstance());
        return true;

      case R.id.categories:
        displayFragment(CategoryFragment.newInstance());
        return true;

      case R.id.budget:
        displayFragment(BudgetFragment.newInstance());
        return true;

      case R.id.history:
        displayFragment(HistoryFragment.newInstance());
        return true;

      default:
        return true;
    }
  }

  private void handleFirstLaunch() {
    mSubscription = DataManager.getInstance(this)
        .isDatabaseEmpty()
        .filter(isDatabaseEmpty -> isDatabaseEmpty)
        .doOnNext(ignored -> initializeDatabase())
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe();
  }

  private void initializeDatabase() {
    ArrayList<Budget> budgets = new ArrayList<>(1);
    budgets.add(new Budget(getString(R.string.shopping), 1000));

    ArrayList<Category> categories = new ArrayList<>(1);
    categories.add(
        new Category(getString(R.string.shopping), ContextCompat.getColor(this, R.color.category_shopping),
            R.mipmap.ic_shopping));
//    categories.add(new Category(getString(R.string.diner),
//        ContextCompat.getColor(this, R.color.category_diner), R.mipmap.ic_diner));
//    categories.add(new Category(getString(R.string.hobby),
//        ContextCompat.getColor(this, R.color.category_hobby), R.mipmap.ic_hobby));
//    categories.add(new Category(getString(R.string.shopping),
//        ContextCompat.getColor(this, R.color.category_shopping), R.mipmap.ic_shopping));
//    categories.add(new Category(getString(R.string.transport),
//        ContextCompat.getColor(this, R.color.category_transport), R.mipmap.ic_transport));
//
    DataManager.getInstance(this).initializeDatabase(budgets, categories);
  }

  private void clearBackStack() {
    FragmentManager fm = getFragmentManager();
    for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
      fm.popBackStack();
    }
  }

  @Override public void onBackStackChanged() {
    final boolean enableArrow = getFragmentManager().getBackStackEntryCount() > 0;
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(enableArrow);
    }
  }
}
