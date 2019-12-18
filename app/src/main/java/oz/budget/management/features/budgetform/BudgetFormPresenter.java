package oz.budget.management.features.budgetform;

import android.support.annotation.NonNull;
import oz.budget.management.model.Budget;
import oz.budget.management.util.BasePresenter;

interface BudgetFormPresenter extends BasePresenter<BudgetFormView> {

  void addBudget(@NonNull Budget budget);

  void updateBudget(@NonNull Budget budget);
}
