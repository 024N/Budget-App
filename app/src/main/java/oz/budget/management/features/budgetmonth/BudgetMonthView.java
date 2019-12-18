package oz.budget.management.features.budgetmonth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import oz.budget.management.model.PresentationBalance;
import oz.budget.management.model.PresentationBudget;
import java.util.ArrayList;

interface BudgetMonthView {
  void onBudgetLoaded(@NonNull ArrayList<PresentationBudget> budgets);

  void onBalanceLoaded(@NonNull PresentationBalance balance);

  void onError(@Nullable String error);
}
