package oz.budget.management.features.transactionform;

import android.support.annotation.NonNull;
import oz.budget.management.model.Transaction;
import oz.budget.management.util.BasePresenter;

interface TransactionFormPresenter extends BasePresenter<TransactionFormView> {

  void addTransaction(@NonNull Transaction transaction);

  void updateTransaction(@NonNull Transaction transaction);

  void loadCategoriesAndBudgets();
}
