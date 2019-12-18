package oz.budget.management.features.budgetform;

import android.support.annotation.NonNull;
import oz.budget.management.model.Budget;
import oz.budget.management.util.DataManager;
import oz.budget.management.util.PresenterImpl;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

final class BudgetFormPresenterImpl extends PresenterImpl<BudgetFormView> implements
    BudgetFormPresenter {

  BudgetFormPresenterImpl(@NonNull final DataManager dataManager) {
    super(dataManager);
  }

  @Override public void addBudget(@NonNull final Budget budget) {
    mSubscription.add(mDataManager.addBudget(budget)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(id -> {
          BudgetFormView view = getView();
          if (view != null) {
            view.onAddSucceeded();
          }
        }, err -> {
          BudgetFormView view = getView();
          if (view != null) {
            view.onError(err.getMessage());
          }
        }));
  }

  @Override public void updateBudget(@NonNull final Budget budget) {
    mSubscription.add(mDataManager.updateBudget(budget)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(id -> {
          BudgetFormView view = getView();
          if (view != null) {
            view.onUpdateSucceeded();
          }
        }, err -> {
          BudgetFormView view = getView();
          if (view != null) {
            view.onError(err.getMessage());
          }
        }));
  }
}
