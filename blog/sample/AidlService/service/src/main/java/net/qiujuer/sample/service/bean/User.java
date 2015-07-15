package net.qiujuer.sample.service.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Created by qiujuer on 15/7/15.
 */
public class User implements Parcelable {
    private UUID id;
    private int age;
    private String name;
    private Account account;

    public User(int age, String name) {
        this.age = age;
        this.name = name;
        this.id = UUID.randomUUID();
    }

    protected User(Parcel in) {
        // Id
        long m = in.readLong();
        long l = in.readLong();
        id = new UUID(m, l);

        age = in.readInt();
        name = in.readString();

        account = in.readParcelable(Account.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // ID
        long m = id.getMostSignificantBits();
        long l = id.getLeastSignificantBits();
        dest.writeLong(m);
        dest.writeLong(l);

        dest.writeInt(age);
        dest.writeString(name);

        dest.writeParcelable(account, flags);
    }

    @Override
    public String toString() {
        return "Id:" + id.toString() + " Age:" + age + " Name:" + name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }
}
