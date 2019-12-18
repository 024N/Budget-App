package oz.budget.management.util;

public interface BasePresenter<T> {
  void attach(T view);

  void detach();
}
