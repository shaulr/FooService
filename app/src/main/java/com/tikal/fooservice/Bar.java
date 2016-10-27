package com.tikal.fooservice;

import android.os.Parcel;
import android.os.Parcelable;

public class Bar implements Parcelable {

    public static final Parcelable.Creator<Bar> CREATOR = new Parcelable.Creator<Bar>() {
        public Bar createFromParcel(Parcel parcel) {
            return new Bar(parcel.readInt(), parcel.readString());
        }
        public Bar[] newArray(int size) {
            return new Bar[size];
        }
    };

    private int id;
    private String data;

    public Bar(int id, String data) {
        this.id = id;
        this.data = data;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.id);
        parcel.writeString(this.data);
    }

    public void readFromParcel(Parcel parcel) {
        this.id = parcel.readInt();
        this.data = parcel.readString();
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }
}
