package oz.budget.management.features.budgetmonth;

import oz.budget.management.util.BasePresenter;

interface BudgetMonthPresenter extends BasePresenter<BudgetMonthView> {
  void loadBudgets(int year, int month);

  void loadBalance(int month, int year);
}
