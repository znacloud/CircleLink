package com.android.zna.fivecircles.data;

/**
 * Created by ZNA on 2015/1/22.
 */
import cn.bmob.im.bean.BmobChatUser;
public class FamilyUser extends BmobChatUser {
    private String familyGroup;

    public void setFamilyGroup(String pFamilyGroup){
        familyGroup = pFamilyGroup;
    }

    public String getFamilyGroup(){
        return familyGroup;
    }
}
