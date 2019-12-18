package oz.budget.management.features.categoryform;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import oz.budget.management.model.Budget;
import java.util.ArrayList;

interface CategoryFormView {

  void onBudgetLoaded(@NonNull ArrayList<Budget> budgets);

  void onError(@Nullable String error);

  void onAddSucceeded();

  void onUpdateSucceeded();

  @NonNull Context getContext();
}
