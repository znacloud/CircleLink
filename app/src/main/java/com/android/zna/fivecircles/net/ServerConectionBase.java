package com.android.zna.fivecircles.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.URL;

/**
 * Created by ZNA on 2014/12/9.
 */
public abstract class ServerConectionBase {
	private  Context mContext;
	public  boolean isNetworkAvailble(){
		ConnectivityManager connMgr = (ConnectivityManager)
				mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		return networkInfo !=null && networkInfo.isConnected();
	};

	public abstract  boolean login(URL url);
	public abstract  boolean logout();
	public abstract  boolean regist();
	public abstract  boolean getPassword(String user);
	public abstract String doAction(String action,String... args);

}
