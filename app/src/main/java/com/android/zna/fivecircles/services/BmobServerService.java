package com.android.zna.fivecircles.services;

import android.content.Context;

import com.android.zna.fivecircles.data.FamilyUser;
import com.android.zna.fivecircles.net.DownloadImageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2015/2/14.
 */
public class BmobServerService extends ServerSerivce {

    private BmobChatManager mChatManager;
    private BmobUserManager mUserManager;
    private static BmobServerService pBmobServer;

    /**
     * This inner class is used to communicate with Bmob server
     * extend to add more field
     */
    private class BmobChatUserExt extends BmobChatUser {

        private String mRealname;
        private String mSelfDesc;
        private int mSex;

        public void setRealname(String pRealname) {
            mRealname = pRealname;
        }

        public void setSelfDesc(String pSelfDesc) {
            mSelfDesc = pSelfDesc;
        }

        public void setSex(int pSex) {
            mSex = pSex;
        }

        public FamilyUser convertToFamilyUser() {
            FamilyUser user = new FamilyUser();
            user.setUsername(getUsername());
            user.setPassword(getPassword());
            user.setNickname(getNick());
            user.setAvatar(getAvatar());
            user.setRealname(mRealname);
            user.setSex(mSex);
            user.setSelfDesc(mSelfDesc);
            return user;
        }
    }

    //private to prevent create more than one instance
    private BmobServerService(Context pContext) {
        super(pContext);
        mChatManager = BmobChatManager.getInstance(pContext);
        mUserManager = BmobUserManager.getInstance(pContext);
    }

    public static synchronized BmobServerService getInstance(Context pContext) {
        if (pBmobServer == null) {
            pBmobServer = new BmobServerService(pContext);
        }
        return pBmobServer;
    }

    @Override
    public void login(FamilyUser pUser, final ResultListener pListener) {
        android.util.Log.d("ZNA_DEBUG", "username:" + pUser.getUsername());
        android.util.Log.d("ZNA_DEBUG", "password:" + pUser.getPassword());
        BmobChatUser bmobChatUser = new BmobChatUser();
        bmobChatUser.setUsername(pUser.getUsername());
        bmobChatUser.setPassword(pUser.getPassword());
        bmobChatUser.login(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                if (pListener != null) {
                    pListener.onSuccess(null);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                if (pListener != null) {
                    pListener.onFailure(i, s);
                }
            }
        });
    }

    @Override
    public void logout(FamilyUser pUser,final  ResultListener pResultListener){

        FamilyUser user = pUser;
        if(user == null){
            user = mUserManager.getCurrentUser(FamilyUser.class);
        }

        mUserManager.logout();
        //clear cache
        user.setNickname(null);
        user.setUsername(null);
        user.setPassword(null);
        user.setSex(-1);
        user.setRealname(null);
        user.setSelfDesc(null);
        user.setAvatar(null);

        //callback,always success
        if(pResultListener != null){
            pResultListener.onSuccess(null);
        }

    }

    @Override
    public void register(FamilyUser pUser, final ResultListener pListener) {
        BmobChatUserExt bmobChatUser = new BmobChatUserExt();
        bmobChatUser.setUsername(pUser.getUsername());
        bmobChatUser.setPassword(pUser.getPassword());
        bmobChatUser.setAvatar(pUser.getAvatar());
        bmobChatUser.setNick(pUser.getNickname());
        bmobChatUser.setRealname(pUser.getRealname());
        bmobChatUser.setSelfDesc(pUser.getSelfDesc());
        bmobChatUser.setSex(pUser.getSex());
        bmobChatUser.signUp(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                if (pListener != null) {
                    pListener.onSuccess(null);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                if (pListener != null) {
                    pListener.onFailure(i, s);
                }
            }
        });
    }

    @Override
    public void getValidCode(int pType, String pTarget, final ResultListener pListener) {
        //cloud end code name
        String cloudMethodName = "";
        if (pType == VALID_TYPE_EMAIL) {
            cloudMethodName = "sendValiCodeMail";
        } else if (pType == VALID_TYPE_PHONE) {
            //not implement yet
            cloudMethodName = "sendValiCodePhone";
        }
        JSONObject params = new JSONObject();
        try {
            //params to deliver to server
            params.put("toAddress", pTarget);
            AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
            cloudCode.callEndpoint(mContext, cloudMethodName, params, new CloudCodeListener() {
                @Override
                public void onSuccess(Object o) {
                    try {
                        JSONObject json = new JSONObject((String)o);
                        String valiCode = json.optString("results");
                        if (pListener != null) {
                            if (!valiCode.trim().contentEquals("")) {
                                pListener.onSuccess(valiCode);
                            } else {
                                pListener.onFailure(-1, "Results are empty");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(pListener != null){
                            pListener.onFailure(-2,e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(int i, String s) {
                    if (pListener != null) {
                        pListener.onFailure(i, s);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadFile(File pFile, final ResultListener pListener) {
        final BmobFile bmobFile = new BmobFile(pFile);
        bmobFile.upload(mContext, new UploadFileListener() {
            @Override
            public void onSuccess() {
                if (pListener != null) {
                    pListener.onSuccess(bmobFile.getFileUrl(mContext));
                }
            }

            @Override
            public void onFailure(int i, String s) {
                if (pListener != null) {
                    pListener.onFailure(i, s);
                }
            }
        });
    }

    @Override
    public void deleteFile(String pFileUri) {
        BmobFile bmobFile = new BmobFile();
        bmobFile.setUrl(pFileUri);
        bmobFile.delete(mContext);
    }

    @Override
    public void downloadImage(String pUrl, ResultListener pListener) {
        DownloadImageTask task = new DownloadImageTask(pListener);
        task.execute(pUrl);
    }

    @Override
    public FamilyUser getCurrentUser() {
        BmobChatUserExt currentUser = mUserManager.getCurrentUser(BmobChatUserExt.class);
        if (currentUser != null) {
            return currentUser.convertToFamilyUser();
        }
        return null;

    }

    @Override
    public void checkDuplicateAccount(String pTarget, final ResultListener pListener){
        mUserManager.queryUserByName(pTarget,new FindListener<BmobChatUser>() {
            @Override
            public void onSuccess(List<BmobChatUser> pBmobChatUsers) {
                if(pListener != null){
                    if (pBmobChatUsers != null && !pBmobChatUsers.isEmpty()) {
                        pListener.onSuccess(true);//true means this username is duplicated
                    }else{
                        pListener.onSuccess(false);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                if(pListener != null){
                    pListener.onFailure(i,s);
                }
            }
        });
    }

    @Override
    public  void updateUser(FamilyUser pCurrentUser, ResultListener pListener){
     //TODO:implements updateUSER
    }
}
