package com.android.zna.fivecircles.data;

/**
 * Created by ZNA on 2015/1/22.
 */

public class FamilyUser {
    //    private static  final long serialVersionUID = 2L;
    private String mUsername;
    private String mPassword;
    private String mNickname;
    private String mRealname;
    private int mSex;
    private String mSelfDesc;//Self description
    private String mAvatar;

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getNickname() {
        return mNickname;
    }

    public void setUsername(String pUsername) {
        mUsername = pUsername;
    }

    public void setPassword(String pPassword) {
        mPassword = pPassword;
    }

    public void setNickname(String pNickname) {
        mNickname = pNickname;
    }

    public void setRealname(String pRealname) {
        mRealname = pRealname;
    }

    public String getRealname() {

        return mRealname;
    }

    public void setSex(int pSex) {
        mSex = pSex;
    }

    public void setSelfDesc(String pSelfDesc) {
        mSelfDesc = pSelfDesc;
    }

    public int getSex() {
        return mSex;
    }

    public String getSelfDesc() {
        return mSelfDesc;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String pAvatar) {
        mAvatar = pAvatar;
    }
}
