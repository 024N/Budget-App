package oz.budget.management.features.budgets;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import oz.budget.management.model.Budget;
import java.util.ArrayList;

interface BudgetView {

  void onBudgetsLoaded(@NonNull ArrayList<Budget> budgets);

  void onError(@Nullable String error);
}
