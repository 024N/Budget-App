package oz.budget.management.features.budgets;

import android.support.annotation.NonNull;
import oz.budget.management.model.Budget;
import oz.budget.management.util.BasePresenter;

interface BudgetPresenter extends BasePresenter<BudgetView> {

  void loadBudgets();

  void deleteBudget(@NonNull Budget budget);
}
