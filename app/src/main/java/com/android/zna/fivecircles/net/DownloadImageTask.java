package com.android.zna.fivecircles.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.android.zna.fivecircles.CommonUtils;
import com.android.zna.fivecircles.services.ServerSerivce;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
    private ServerSerivce.ResultListener mListener;
    private int mErrorCode;
    private String mErrorMsg;

    public DownloadImageTask(ServerSerivce.ResultListener pListener) {
        mListener = pListener;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return downloadImage(params);
    }

    private Bitmap downloadImage(String... pUrls) {
        try {
            HttpClient httpClient = CommonUtils.getHttpClient();
            HttpGet request = new HttpGet(pUrls[0]);
            HttpResponse httpResponse = httpClient.execute(request);
            byte[] image = EntityUtils.toByteArray(httpResponse.getEntity());
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof ClientProtocolException) {
                mErrorCode = 1;
            } else if (e instanceof ConnectTimeoutException) {
                mErrorCode = 2;
            } else if (e instanceof ConnectionPoolTimeoutException) {
                mErrorCode = 3;
            } else if (e instanceof SocketTimeoutException) {
                mErrorCode = 4;
            }
            mErrorMsg = e.getMessage();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap pBitmap) {
        if (pBitmap != null) {
            if (mListener != null) {
                mListener.onSuccess(pBitmap);
            }
        } else {
            if (mListener != null) {
                mListener.onFailure(mErrorCode, mErrorMsg);
            }
        }
    }
}
