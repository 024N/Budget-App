package oz.budget.management.features.categories;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import oz.budget.management.model.Category;
import java.util.ArrayList;

interface CategoryView {

  void onCategoryLoaded(@NonNull ArrayList<Category> categories);

  void onError(@Nullable String error);
}
