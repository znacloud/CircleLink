package com.android.zna.fivecircles.services;

import android.content.Context;

import com.android.zna.fivecircles.data.FamilyUser;

import java.io.File;

public abstract class ServerSerivce {
    public static final int VALID_TYPE_EMAIL = 0;
    public static final int VALID_TYPE_PHONE = 1;

    public static final int SERV_TYPE_BMOB = 0;
    public static final int SERV_TYPE_OTHER = 1;

    protected final Context mContext;

    public static interface ResultListener {
        public void onSuccess(Object pObj);

        public void onFailure(int pErrorCode, String pErrorMsg);
    }

    public ServerSerivce(Context pContext) {
        mContext = pContext;
    }

    /**
     * return  server instance specified by pServerType
     *
     * @param pContext
     * @param pServerType
     * @return instance fo subclass of ServerService
     * TO-DO: add UnsupportedServerType Exception
     */
    public static ServerSerivce  getService(Context pContext, int pServerType) {
        if (pServerType == SERV_TYPE_BMOB) {
            return BmobServerService.getInstance(pContext);
        } else if (pServerType == SERV_TYPE_OTHER) {
            //TO-DO: maybe use other app server
            return null;
        }
        return null;
    }

    /**
     * login remote server,FamilyUser contains at least username and password information
     * @param pUser Ther user to login server
     * @param pListener callback to notify the result of login action
     */
    public abstract void login(FamilyUser pUser, ResultListener pListener);

    /**
     * logout remote server
     * @param pUser Ther user to logout server
     * @param pListener callback to notify the result of login action
     */
    public abstract void logout(FamilyUser pUser, ResultListener pListener);

    /**
     * register remote server,FamilyUser contains basic information of an new user
     * @param pUser Ther user to register server
     * @param pListener callback to notify the result of register action
     */
    public abstract void register(FamilyUser pUser, ResultListener pListener);

    /**
     * get validation code from server for phone number or email used to register.
     * it will send valicode to the specified phone number or email address.
     * the valicode will also be returned to client for validation compare.
     * @param pType validation type ,Email or Phone.
     * @param pTarget target email address or phone number
     * @param pListener callback to notify the result
     */
    public abstract void getValidCode(int pType, String pTarget,ResultListener pListener);

    /**
     * upload file to remote server
     * @param pFile The file to upload to server
     * @param pListener callback to notify the result of upload action
     */
    public abstract void uploadFile(File pFile, ResultListener pListener);

    public abstract void deleteFile(String pFileUri);

    /**
     * download image file from remote server
     * @param pUrl Ther image url to be downloaded
     * @param pListener callback to notify the result of download action
     */
    public abstract void downloadImage(String pUrl,ResultListener pListener);

    /**
     * get current user who has login on this devices
     * @return
     */
    public abstract FamilyUser getCurrentUser();

    /**
     * check duplidate user email address
     */
    public abstract void checkDuplicateAccount(String pTarget,ResultListener pListener);
}
