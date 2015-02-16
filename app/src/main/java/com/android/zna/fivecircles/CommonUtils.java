package com.android.zna.fivecircles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by nianan.zeng
 */
public class CommonUtils {

    private static HttpClient mCustomHttpClient;

    /**
     * private constructor to prevent create instance
     */
    private CommonUtils(){

    }

    /**
     * check whether or not ExternalStorage is available
     *
     * @return true if externalStorage is available,false otherwise
     */
    public static boolean isSDCARDMounted() {
        return Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED);
    }

    /**
     * get file which used store head image of current user,maybe return null
     *
     * @return File if success,null if SDCard is not available
     */
    public static File getHeadImageTempFile() {
        if (isSDCARDMounted()) {
            File dir = new File(Environment.getExternalStorageDirectory(), Config.APP_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File headImageFile = new File(dir, Config.TEMP_PHOTO_FILE);
            if (!headImageFile.exists()) {
                try {
                    headImageFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    headImageFile = null;
                }
            }
            return headImageFile;
        } else {
            return null;
        }
    }

    /**
     * generate SHA code
     * @param s String used to generate SHA code
     * @return
     */
    public static String makeSHA(String s) {
        try {
            byte[] bytes = s.getBytes("UTF-8");
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(bytes);
            return new String(messageDigest.digest(), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            android.util.Log.e("ZNA_DEBUG", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            android.util.Log.e("ZNA_DEBUG", e.getMessage());
        }
        return s;
    }

    /**
     * generate scaled circle view from drawable resources
     * @param pContext application context
     * @param pRes drawable resources id
     * @param pRadius Radius of generated view,it will be scaled to this size
     * @return Drawable the circel view with specified radius
     */
    public static Drawable generateScaledCircleDrawable(Context pContext,int pRes,int pRadius){
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(pContext.getResources(),
                pRes),2*pRadius,2*pRadius,false);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(pContext.getResources(), bitmap);
        drawable.setCornerRadius(pRadius);
        return drawable;
    }

    /**
     * generate scaled circle view from drawable resources
     * @param pContext application context
     * @param pBitmap bitmap used to generate circle drawable
     * @param pRadius Radius of generated view,it will be scaled to this size
     * @return Drawable the circel view with specified radius
     */
    public static Drawable generateScaledCircleDrawable(Context pContext,Bitmap pBitmap,int pRadius){
        Bitmap bitmap = Bitmap.createScaledBitmap(pBitmap,2*pRadius,2*pRadius,false);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(pContext.getResources(), bitmap);
        drawable.setCornerRadius(pRadius);
        return drawable;
    }

    /**
     * get single HttpClient instance
     * @return HttpClient
     */
    public static synchronized HttpClient getHttpClient(){
        if(mCustomHttpClient == null){
            HttpParams params = new BasicHttpParams();
            //basic params
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpProtocolParams.setUseExpectContinue(params,true);
            HttpProtocolParams.setUserAgent(params,"circlelink-http-agent-zna");

            //connection params
            ConnManagerParams.setTimeout(params,1000);

            HttpConnectionParams.setConnectionTimeout(params,5000);
            HttpConnectionParams.setSoTimeout(params,10000);

            //schemes
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),80));
            schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(),443));

            ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params,schReg);
            mCustomHttpClient = new DefaultHttpClient(conMgr,params);
        }
        return mCustomHttpClient;
    }

}
