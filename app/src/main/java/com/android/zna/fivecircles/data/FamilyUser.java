package com.android.zna.fivecircles.data;

/**
 * Created by ZNA on 2015/1/22.
 */
import java.util.EnumMap;

import cn.bmob.im.bean.BmobChatUser;
public class FamilyUser extends BmobChatUser {
    private String realname;
    private int sex;
    private String selfDesc;//Self description

    public void setRealName(String pRealname){
        realname = pRealname;
    }

    public String getRealname(){

        return realname;
    }

    public void setSex(int pSex) {
        sex = pSex;
    }

    public void setSelfDesc(String pSelfDesc) {
        selfDesc = pSelfDesc;
    }

    public int getSex() {
        return sex;
    }

    public String getSelfDesc() {
        return selfDesc;
    }
}
