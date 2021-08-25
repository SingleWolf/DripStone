package com.walker.remoteserver.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author  : walker
 * Date    : 2021/8/13  1:25 下午
 * Email   : feitianwumu@163.com
 * Summary : 用户信息
 */
public class UserInfo implements Parcelable {
    private int errCode;
    private String loginName;
    private String password;
    private String userName;
    private int age;
    private int sex;
    private int type;
    private int active;

    public UserInfo(){}

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    protected UserInfo(Parcel in) {
        errCode=in.readInt();
        loginName= in.readString();
        password= in.readString();
        userName=in.readString();
        age=in.readInt();
        sex=in.readInt();
        type=in.readInt();
        active=in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(errCode);
        dest.writeString(loginName);
        dest.writeString(password);
        dest.writeString(userName);
        dest.writeInt(age);
        dest.writeInt(sex);
        dest.writeInt(type);
        dest.writeInt(active);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
