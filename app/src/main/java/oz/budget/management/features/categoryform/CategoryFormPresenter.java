package oz.budget.management.features.categoryform;

import android.support.annotation.NonNull;
import oz.budget.management.model.Category;
import oz.budget.management.util.BasePresenter;

interface CategoryFormPresenter extends BasePresenter<CategoryFormView> {

  void addCategory(@NonNull Category category);

  void updateCategory(@NonNull Category category);

  void loadBudget();
}
