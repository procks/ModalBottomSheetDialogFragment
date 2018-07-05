package com.commit451.modalbottomsheetdialogfragment;

import android.os.Parcel;
import android.os.Parcelable;

public class OptionHolder implements Parcelable {
    public Integer resource;
    public OptionRequest optionRequest;

    public OptionHolder(Integer resource, OptionRequest optionRequest) {
        this.resource = resource;
        this.optionRequest = optionRequest;
    }

    private OptionHolder(Parcel in) {
        this((Integer) in.readValue(Integer.TYPE.getClassLoader()), (OptionRequest) in.readParcelable(OptionRequest.class.getClassLoader()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.resource);
        dest.writeParcelable(optionRequest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OptionHolder> CREATOR = new Creator<OptionHolder>() {
        @Override
        public OptionHolder createFromParcel(Parcel in) {
            return new OptionHolder(in);
        }

        @Override
        public OptionHolder[] newArray(int size) {
            return new OptionHolder[size];
        }
    };

//    protected OptionHolder(Parcel in) {
//        if (in.readByte() == 0) {
//            resource = null;
//        } else {
//            resource = in.readInt();
//        }
//        optionRequest = in.readParcelable(OptionRequest.class.getClassLoader());
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        if (resource == null) {
//            dest.writeByte((byte) 0);
//        } else {
//            dest.writeByte((byte) 1);
//            dest.writeInt(resource);
//        }
//        dest.writeParcelable(optionRequest, flags);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<OptionHolder> CREATOR = new Creator<OptionHolder>() {
//        @Override
//        public OptionHolder createFromParcel(Parcel in) {
//            return new OptionHolder(in);
//        }
//
//        @Override
//        public OptionHolder[] newArray(int size) {
//            return new OptionHolder[size];
//        }
//    };
}
