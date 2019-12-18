package oz.budget.management.features.budgetform;

import android.support.annotation.Nullable;

interface BudgetFormView {

  void onAddSucceeded();

  void onUpdateSucceeded();

  void onError(@Nullable String error);
}
