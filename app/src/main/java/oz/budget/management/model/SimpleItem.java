package oz.budget.management.model;

public interface SimpleItem {

  String getTitle();

  double getValue();

  boolean equals(Object o);

  int hashCode();
}
