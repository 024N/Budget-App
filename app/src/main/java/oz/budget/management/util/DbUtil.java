package oz.budget.management.util;

import android.database.Cursor;
import android.support.annotation.NonNull;

public final class DbUtil {

  public static String getString(@NonNull Cursor cursor, String columnName) {
    return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
  }

  public static int getInt(@NonNull Cursor cursor, String columnName) {
    return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
  }

  public static long getLong(@NonNull Cursor cursor, String columnName) {
    return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
  }

  public static double getDouble(@NonNull Cursor cursor, String columnName) {
    return cursor.getDouble(cursor.getColumnIndexOrThrow(columnName));
  }
}
