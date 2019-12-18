package oz.budget.management.features.transactionform;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import oz.budget.management.model.Budget;
import oz.budget.management.model.Category;
import java.util.ArrayList;

interface TransactionFormView {
  void onCategoriesLoaded(@NonNull ArrayList<Category> categories);

  void onBudgetsLoaded(@NonNull ArrayList<Budget> budgets);

  void onError(@Nullable String error);

  void onAddSucceeded();

  void onUpdateSucceeded();

  @NonNull Context getContext();
}
