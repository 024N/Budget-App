package oz.budget.management.features.categories;

import android.support.annotation.NonNull;
import oz.budget.management.model.Category;
import oz.budget.management.util.BasePresenter;

interface CategoryPresenter extends BasePresenter<CategoryView> {
  void loadCategory();

  void deleteCategory(@NonNull Category category);
}
