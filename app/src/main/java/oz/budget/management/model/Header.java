package oz.budget.management.model;

import android.os.Parcel;
import android.os.Parcelable;
import oz.budget.management.R;

public class Header implements Parcelable, TransactionItem {

  private String title;

  public Header(String title) {
    this.title = title;
  }

  public Header(Parcel in) {
    title = in.readString();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int i) {
    dest.writeString(title);
  }

  @Override public String getTitle() {
    return title;
  }

  @Override public int getLayout() {
    return R.layout.item_header;
  }

  public static final Parcelable.Creator<Header> CREATOR = new Parcelable.Creator<Header>() {
    @Override public Header createFromParcel(Parcel source) {
      return new Header(source);
    }

    @Override public Header[] newArray(int size) {
      return new Header[size];
    }
  };

  @Override public int hashCode() {
    return title != null ? title.hashCode() : 0;
  }
}
