package oz.budget.management.features.transaction;

import android.support.annotation.NonNull;
import oz.budget.management.model.Header;
import oz.budget.management.model.Transaction;
import oz.budget.management.model.TransactionItem;
import oz.budget.management.util.DataManager;
import oz.budget.management.util.DateUtil;
import oz.budget.management.util.PresenterImpl;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;

final class TransactionPresenterImpl extends PresenterImpl<TransactionView>
    implements TransactionPresenter {

  TransactionPresenterImpl(@NonNull DataManager dataManager) {
    super(dataManager);
  }

  @Override public void loadTransaction() {
    mSubscription.add(mDataManager.getTransactions(250)
        .map(this::addHeader)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(transactions -> {
          TransactionView view = getView();
          if (view != null) {
            view.onTransactionLoaded(transactions);
          }
        }, error -> {
          TransactionView view = getView();
          if (view != null) {
            view.onError(error.getMessage());
          }
        }));
  }

  @Override public void loadTransaction(int year, int month, long idBudget) {
    mSubscription.add(mDataManager.getTransactions(year, month, idBudget)
        .map((Function<ArrayList<Transaction>, ArrayList<TransactionItem>>) ArrayList::new)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(transactions -> {
          TransactionView view = getView();
          if (view != null) {
            view.onTransactionLoaded(transactions);
          }
        }, error -> {
          TransactionView view = getView();
          if (view != null) {
            view.onError(error.getMessage());
          }
        }));
  }

  @Override public void loadTransaction(int year, int month) {
    mSubscription.add(mDataManager.getTransactions(year, month)
        .map((Function<ArrayList<Transaction>, ArrayList<TransactionItem>>) ArrayList::new)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(transactions -> {
          TransactionView view = getView();
          if (view != null) {
            view.onTransactionLoaded(transactions);
          }
        }, error -> {
          TransactionView view = getView();
          if (view != null) {
            view.onError(error.getMessage());
          }
        }));
  }

  @Override public void deleteTransaction(@NonNull Transaction transaction) {
    mSubscription.add(mDataManager.deleteTransaction(transaction)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(() -> {
        }, error -> {
          TransactionView view = getView();
          if (view != null) {
            view.onError(error.getMessage());
          }
        }));
  }

  private ArrayList<TransactionItem> addHeader(ArrayList<Transaction> transactions) {
    int month = -1;
    ArrayList<TransactionItem> result = new ArrayList<>();

    for (Transaction transaction : transactions) {
      if (transaction.getMonth() != month) {
        result.add(new Header(DateUtil.getMonthName(transaction.getMonth())));
        month = transaction.getMonth();
      }
      result.add(transaction);
    }

    return result;
  }
}
