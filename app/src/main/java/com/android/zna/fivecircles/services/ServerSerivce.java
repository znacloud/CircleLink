package com.android.zna.fivecircles.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.android.zna.fivecircles.data.FamilyUser;

import java.io.File;

public abstract class ServerSerivce {
    public static final int VALID_TYPE_EMAIL = 0;
    public static final int VALID_TYPE_PHONE = 1;

    public static final int SERV_TYPE_BMOB = 0;
    public static final int SERV_TYPE_OTHER = 1;

    protected final Context mContext;

    public static interface ResultListener{
        public void onSuccess(Object pObj);
        public void onFailure(int pErrorCode,String pErrorMsg);
    }
    public ServerSerivce(Context pContext) {
        mContext = pContext;
    }

    /**
     * return  server instance specified by pServerType
     * @param pContext
     * @param pServerType
     * @return instance fo subclass of ServerService
     * TO-DO: add UnsupportedServerType Exception
     */
    public static ServerSerivce getService(Context pContext,int pServerType){
        if(pServerType == SERV_TYPE_BMOB){
            return new BmobServerService(pContext);
        }else if(pServerType == SERV_TYPE_OTHER){
            //TO-DO: maybe use other app server
            return null;
        }
        return null;
    }

    public abstract void login(FamilyUser pUser,ResultListener pListener);
    public abstract void register(FamilyUser pUser,ResultListener pListener);
    public abstract void getValidCode(int pType,int pTarget);
    public abstract void uploadFile(File pFile,ResultListener pListener);
    public abstract void deleteFile(String pFileUri);
}
