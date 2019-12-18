package oz.budget.management.features.transactionform;

import android.support.annotation.NonNull;
import oz.budget.management.R;
import oz.budget.management.model.Budget;
import oz.budget.management.model.Transaction;
import oz.budget.management.util.DataManager;
import oz.budget.management.util.PresenterImpl;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

final class TransactionFormPresenterImpl extends PresenterImpl<TransactionFormView>
    implements TransactionFormPresenter {

  TransactionFormPresenterImpl(@NonNull final DataManager dataManager) {
    super(dataManager);
  }

  @Override public void addTransaction(@NonNull final Transaction transaction) {
    mSubscription.add(mDataManager.addTransaction(transaction)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(id -> {
          TransactionFormView view = getView();
          if (view != null) {
            view.onAddSucceeded();
          }
        }, err -> {
          TransactionFormView view = getView();
          if (view != null) {
            view.onError(err.getMessage());
          }
        }));
  }

  @Override public void updateTransaction(@NonNull final Transaction transaction) {
    mSubscription.add(mDataManager.updateTransaction(transaction)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(id -> {
          TransactionFormView view = getView();
          if (view != null) {
            view.onUpdateSucceeded();
          }
        }, err -> {
          TransactionFormView view = getView();
          if (view != null) {
            view.onError(err.getMessage());
          }
        }));
  }

  @Override public void loadCategoriesAndBudgets() {
    mSubscription.add(mDataManager.getBudgets()
        .map(budgets -> {
          TransactionFormView view = getView();
          if (view != null) {
            budgets.add(0,
                new Budget(Budget.NONE, getView().getContext().getString(R.string.none), 0));
            return budgets;
          }
          return null;
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(budgets -> {
          TransactionFormView view = getView();
          if (view != null) {
            view.onBudgetsLoaded(budgets);
          }
        })
        .observeOn(Schedulers.io())
        .flatMap(ignored -> mDataManager.getCategories())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(categories -> {
          TransactionFormView view = getView();
          if (view != null) {
            view.onCategoriesLoaded(categories);
          }
        }, error -> {
          TransactionFormView view = getView();
          if (view != null) {
            view.onError(error.getMessage());
          }
        }));
  }
}
