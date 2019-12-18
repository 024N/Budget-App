package oz.budget.management.features.transaction;

import android.support.annotation.NonNull;
import oz.budget.management.model.Transaction;
import oz.budget.management.util.BasePresenter;

interface TransactionPresenter extends BasePresenter<TransactionView> {
  void loadTransaction();

  void loadTransaction(int year, int month, long idBudget);

  void loadTransaction(int year, int month);

  void deleteTransaction(@NonNull Transaction transaction);
}
