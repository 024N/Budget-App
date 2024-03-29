package oz.budget.management.database;

import android.content.Context;
import android.support.annotation.NonNull;
import com.squareup.sqlbrite2.BriteDatabase;
import oz.budget.management.model.Budget;
import oz.budget.management.model.Category;
import oz.budget.management.model.PresentationBudget;
import oz.budget.management.model.PresentationHistory;
import oz.budget.management.model.Transaction;
import oz.budget.management.util.DbUtil;
import io.reactivex.Observable;

import java.util.ArrayList;

public final class QueryUtil extends Database {

  public QueryUtil(@NonNull Context context) {
    super(context);
  }

  public Observable<ArrayList<PresentationBudget>> getAll(int month, int year) {
    return db.createQuery(TABLE_TRANSACTION, "SELECT SUM("
        + Transaction.VALUE
        + ") AS "
        + PresentationBudget.OUT
        + ", "
        + Budget.TITLE
        + ", "
        + Budget.VALUE
        + ", "
        + Budget.ID
        + " FROM "
        + TABLE_BUDGET
        + " JOIN "
        + TABLE_TRANSACTION
        + " ON "
        + Budget.ID
        + " = "
        + Transaction.ID_BUDGET
        + " WHERE "
        + Transaction.MONTH
        + " = ? AND "
        + Transaction.YEAR
        + " = ? "
        + " GROUP BY "
        + Budget.ID
        + " ORDER BY "
        + Budget.TITLE, String.valueOf(month), String.valueOf(year))
        .map(super::getCursor)
        .map(cursor -> {
          try {
            ArrayList<PresentationBudget> budgets = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
              PresentationBudget budget =
                  new PresentationBudget(DbUtil.getLong(cursor, Budget.ID),
                      DbUtil.getString(cursor, Budget.TITLE),
                      DbUtil.getDouble(cursor, Budget.VALUE), DbUtil.getDouble(cursor, PresentationBudget.OUT));
              budgets.add(budget);
            }
            return budgets;
          } finally {
            cursor.close();
          }
        });
  }

  public Observable<ArrayList<PresentationHistory>> getHistory() {
    return db.createQuery(TABLE_TRANSACTION, "SELECT SUM("
        + Transaction.VALUE
        + ") AS "
        + PresentationBudget.OUT
        + ", "
        + Transaction.MONTH
        + ", "
        + Transaction.YEAR
        + " FROM "
        + TABLE_TRANSACTION
        + " GROUP BY "
        + Transaction.YEAR
        + ", "
        + Transaction.MONTH
        + " ORDER BY "
        + Transaction.YEAR
        + " DESC, "
        + Transaction.MONTH
        + " DESC").map(super::getCursor).map(cursor -> {
      try {
        ArrayList<PresentationHistory> histories = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
          PresentationHistory history =
              new PresentationHistory(DbUtil.getInt(cursor, Transaction.MONTH),
                  DbUtil.getInt(cursor, Transaction.YEAR), DbUtil.getDouble(cursor, PresentationBudget.OUT));
          histories.add(history);
        }
        return histories;
      } finally {
        cursor.close();
      }
    });
  }

  public void initializeDatabase(@NonNull ArrayList<Budget> budgets, @NonNull ArrayList<Category> categories) {
    if (budgets.size() != categories.size()) {
      throw new IllegalStateException("Lists are not equals in size");
    }

    BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      for(int i =0; i < budgets.size(); i++) {
        Budget budget = budgets.get(i);
        Category category = categories.get(i);

        long idBudget = db.insert(TABLE_BUDGET, getContentValues(budget));

        category.setIdBudget(idBudget);
        db.insert(TABLE_CATEGORY, getContentValues(category));
      }
      transaction.markSuccessful();
    } finally {
      transaction.end();
    }
  }
}
