package oz.budget.management.features.budgets;

import android.support.annotation.NonNull;
import oz.budget.management.model.Budget;
import oz.budget.management.util.DataManager;
import oz.budget.management.util.PresenterImpl;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

final class BudgetPresenterImpl extends PresenterImpl<BudgetView>
    implements BudgetPresenter {

  BudgetPresenterImpl(@NonNull DataManager dataManager) {
    super(dataManager);
  }

  @Override public void loadBudgets() {
    mSubscription.add(mDataManager.getBudgets()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(budgets -> {
          BudgetView view = getView();
          if (view != null) {
            view.onBudgetsLoaded(budgets);
          }}, error -> {
          BudgetView view = getView();
          if (view != null) {
            view.onError(error.getMessage());
          }
        }));
  }

  @Override public void deleteBudget(@NonNull Budget budget) {
    mSubscription.add(mDataManager.deleteBudget(budget)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe());
  }
}
