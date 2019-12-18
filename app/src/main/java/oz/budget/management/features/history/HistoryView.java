package oz.budget.management.features.history;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import oz.budget.management.model.PresentationHistory;
import java.util.ArrayList;

interface HistoryView {

  void onHistoryLoaded(@NonNull ArrayList<PresentationHistory> histories);

  void onError(@Nullable String error);
}
