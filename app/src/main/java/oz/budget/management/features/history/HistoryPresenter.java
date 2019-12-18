package oz.budget.management.features.history;

import oz.budget.management.util.BasePresenter;

interface HistoryPresenter extends BasePresenter<HistoryView> {

  void loadHistory();
}
