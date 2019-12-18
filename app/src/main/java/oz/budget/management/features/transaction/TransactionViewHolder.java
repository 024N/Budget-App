package oz.budget.management.features.transaction;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import oz.budget.management.R;
import oz.budget.management.model.Transaction;
import oz.budget.management.util.DateUtil;

final class TransactionViewHolder extends RecyclerView.ViewHolder {

  ImageView category;
  TextView description;
  TextView date;
  TextView value;

  TransactionViewHolder(View v) {
    super(v);
    category = v.findViewById(R.id.icon_category);
    description = v.findViewById(R.id.description);
    date = v.findViewById(R.id.date);
    value = v.findViewById(R.id.value);
  }

  void bindTo(Transaction transaction) {
    description.setText(transaction.getDescription());
    date.setText(
        DateUtil.formatDate(transaction.getYear(), transaction.getMonth(), transaction.getDay()));

    value.setText(value.getContext().getString(R.string.float_value, transaction.getValue()));
    category.setImageResource(transaction.getIcon());

    Drawable drawable = category.getBackground();
    drawable.setColorFilter(transaction.getColor(), PorterDuff.Mode.SRC);
  }
}
