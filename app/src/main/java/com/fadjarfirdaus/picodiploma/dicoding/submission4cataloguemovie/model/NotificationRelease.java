package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationRelease implements Parcelable {
    int id;
    String release,title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.release);
        dest.writeString(this.title);
    }

    public NotificationRelease() {
    }

    protected NotificationRelease(Parcel in) {
        this.id = in.readInt();
        this.release = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<NotificationRelease> CREATOR = new Parcelable.Creator<NotificationRelease>() {
        @Override
        public NotificationRelease createFromParcel(Parcel source) {
            return new NotificationRelease(source);
        }

        @Override
        public NotificationRelease[] newArray(int size) {
            return new NotificationRelease[size];
        }
    };
}
