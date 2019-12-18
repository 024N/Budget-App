package oz.budget.management.features.transaction;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import oz.budget.management.R;
import oz.budget.management.model.TransactionItem;

final class HeaderViewHolder extends RecyclerView.ViewHolder {
  public TextView text;

  HeaderViewHolder(View v) {
    super(v);
    text = v.findViewById(R.id.text);
  }

  void bindTo(TransactionItem transactionItem) {
    text.setText(transactionItem.getTitle());
  }
}
