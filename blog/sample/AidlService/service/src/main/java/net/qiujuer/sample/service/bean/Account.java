package net.qiujuer.sample.service.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by qiujuer on 15/7/15.
 */
public class Account implements Parcelable {
    private String name;

    protected Account(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
