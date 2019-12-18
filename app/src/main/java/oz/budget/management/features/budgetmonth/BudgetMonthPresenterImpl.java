package oz.budget.management.features.budgetmonth;

import android.support.annotation.NonNull;
import oz.budget.management.model.Budget;
import oz.budget.management.model.PresentationBalance;
import oz.budget.management.model.PresentationBudget;
import oz.budget.management.model.Transaction;
import oz.budget.management.util.DataManager;
import oz.budget.management.util.PresenterImpl;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;

final class BudgetMonthPresenterImpl extends PresenterImpl<BudgetMonthView> implements
    BudgetMonthPresenter {

  BudgetMonthPresenterImpl(@NonNull DataManager dataManager) {
    super(dataManager);
  }

  @Override public void loadBudgets(int month, int year) {
    mSubscription.add(
        Observable.combineLatest(mDataManager.getBudgets(month, year), mDataManager.getBudgets(),
            (presentationBudgets, budgets) -> {
              ArrayList<PresentationBudget> presBudgets = new ArrayList<>(budgets.size());
              presBudgets.addAll(presentationBudgets);

              addEmptyBudgets(presBudgets, budgets);
              return presBudgets;
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(presentationBudgets -> {
              BudgetMonthView view = getView();
              if (view != null) {
                view.onBudgetLoaded(presentationBudgets);
              }
            }, error -> {
              BudgetMonthView view = getView();
              if (view != null) {
                view.onError(error.getMessage());
              }
            }));
  }

  @Override public void loadBalance(int month, int year) {
    mSubscription.add(mDataManager.getTransactions(year, month).map(transactions -> {
      double incomes = 0;
      double expenses = 0;
      for (Transaction transaction : transactions) {
        if (transaction.getValue() >= 0) {
          incomes += transaction.getValue();
        } else {
          expenses -= transaction.getValue();
        }
      }
      return new PresentationBalance(incomes, expenses, incomes - expenses);
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(balance -> {
      BudgetMonthView view = getView();
      if (view != null) {
        view.onBalanceLoaded(balance);
      }
    }, error -> {
      BudgetMonthView view = getView();
      if (view != null) {
        view.onError(error.getMessage());
      }
    }));
  }

  private void addEmptyBudgets(ArrayList<PresentationBudget> presentationBudgets,
      ArrayList<Budget> budgets) {
    for (Budget budget : budgets) {
      if (!isInclude(presentationBudgets, budget)) {
        PresentationBudget presentationBudget =
            new PresentationBudget(budget.getId(), budget.getTitle(), budget.getValue(), 0);
        presentationBudgets.add(presentationBudget);
      }
    }
  }

  private boolean isInclude(ArrayList<PresentationBudget> budgets, @NonNull Budget budget) {
    for (PresentationBudget presentationBudget : budgets) {
      if (budget.getId() == presentationBudget.getId()) {
        return true;
      }
    }
    return false;
  }
}
