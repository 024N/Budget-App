package oz.budget.management.features.history;

import android.support.annotation.NonNull;
import oz.budget.management.util.DataManager;
import oz.budget.management.util.PresenterImpl;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

final class HistoryPresenterImpl extends PresenterImpl<HistoryView> implements HistoryPresenter {

  HistoryPresenterImpl(@NonNull DataManager dataManager) {
    super(dataManager);
  }

  @Override public void loadHistory() {
    mSubscription.add(mDataManager.getHistory()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(histories -> {
          HistoryView view = getView();
          if (view != null) {
            view.onHistoryLoaded(histories);
          }
        }, error -> {
          HistoryView view = getView();
          if (view != null) {
            view.onError(error.getMessage());
          }
        }));
  }
}
