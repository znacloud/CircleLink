package com.android.zna.fivecircles;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class Config {
	
	public static final String APP_ID = "0d503dc1eb65942b6f1cae681ef36f41";
	public static final String KEY_TOKEN = "token";
	public static final String USER_NAME = "username";
    public static final String APP_PACKAGE ="com.android.zna.fivecircles" ;
    public static final String APP_DIR = "/Android/data/"+APP_PACKAGE;
    public  static final String TEMP_PHOTO_FILE = "/head.jpeg";

    /**
	 * get cached token string
	 * @param context app context
	 * @return token string
	 */
	public static String getCachedToken(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_TOKEN, null);
	}
	
	/**
	 * store token to sharedPreferences
	 * @param context app context
	 * @param token token to be set
	 */
	public static  void setToken(Context context,String token){
		Editor configEditor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.edit();
		configEditor.putString(KEY_TOKEN, token);
		configEditor.commit();
	}

}
