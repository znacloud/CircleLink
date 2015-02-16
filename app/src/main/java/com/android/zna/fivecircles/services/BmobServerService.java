package com.android.zna.fivecircles.services;

import android.content.Context;

import com.android.zna.fivecircles.data.FamilyUser;
import com.android.zna.fivecircles.net.DownloadImageTask;

import java.io.File;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobFile;
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

        public FamilyUser convertToFamilyUser(){
            FamilyUser user = new FamilyUser();
            user.setUsername(getUsername());
            user.setPassword(getPassword());
            user.setNickname(getNick());
            user.setAvatar(getAvatar());
            user.setRealname(mRealname);
            user.setSex(mSex);
            user.setSelfDesc(mSelfDesc);
            return  user;
        }
    }

    //private to prevent create more than one instance
    private BmobServerService(Context pContext) {
        super(pContext);
        mChatManager = BmobChatManager.getInstance(pContext);
        mUserManager = BmobUserManager.getInstance(pContext);
    }

    public static synchronized  BmobServerService getInstance(Context pContext){
        if(pBmobServer == null ){
            pBmobServer = new BmobServerService(pContext);
        }
        return  pBmobServer;
    }

    @Override
    public void login(FamilyUser pUser, final ResultListener pListener) {
        android.util.Log.e("ZNA_DEBUG", "username:" + pUser.getUsername());
        android.util.Log.e("ZNA_DEBUG", "password:" + pUser.getPassword());
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
    public void getValidCode(int pType, int pTarget) {

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
    public void downloadImage(String pUrl,ResultListener pListener){
        DownloadImageTask task = new DownloadImageTask(pListener);
        task.execute(pUrl);
    }

    @Override
    public FamilyUser getCurrentUser() {
        BmobChatUserExt currentUser = mUserManager.getCurrentUser(BmobChatUserExt.class);
        if (currentUser != null){
            return currentUser.convertToFamilyUser();
        }
        return null;

    }
}
