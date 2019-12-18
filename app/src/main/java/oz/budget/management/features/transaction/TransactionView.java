package oz.budget.management.features.transaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import oz.budget.management.model.TransactionItem;
import java.util.ArrayList;

interface TransactionView {
  void onTransactionLoaded(@NonNull ArrayList<TransactionItem> transactions);

  void onError(@Nullable String error);

  @NonNull Context getContext();
}
