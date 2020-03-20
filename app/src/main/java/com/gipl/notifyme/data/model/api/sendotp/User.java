package com.gipl.notifyme.data.model.api.sendotp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
    @SerializedName("sEmpCode")
    @Expose
    private String empId;
    @SerializedName("sMobileNo")
    @Expose
    private String mobileNumber;
    @SerializedName("sName")
    @Expose
    private String name;

    public User(String empId, String mobileNumber, String name) {
        this.empId = empId;
        this.mobileNumber = mobileNumber;
        this.name = name;
    }

    protected User(Parcel in) {
        empId = in.readString();
        mobileNumber = in.readString();
        name = in.readString();
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

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(empId);
        dest.writeString(mobileNumber);
        dest.writeString(name);
    }
}
