package com.commit451.modalbottomsheetdialogfragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;

public class OptionRequest implements Parcelable {
    public int id;
    public String title;
    @DrawableRes
    public Integer icon;

    public OptionRequest(int id, @NonNull String title, @DrawableRes @Nullable Integer icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    private OptionRequest(Parcel in) {
        this(in.readInt(), in.readString(), in.readInt());
    }

    public static final Creator<OptionRequest> CREATOR = new Creator<OptionRequest>() {
        @Override
        public OptionRequest createFromParcel(Parcel in) {
            return new OptionRequest(in);
        }

        @Override
        public OptionRequest[] newArray(int size) {
            return new OptionRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(icon);
    }

    public Option toOption(Context context) {
        Drawable drawable = null;
        if (icon != null) {
            drawable = ResourcesCompat.getDrawable(context.getResources(), icon, context.getTheme());
        }

        return new Option(id, title, drawable);
    }
}
