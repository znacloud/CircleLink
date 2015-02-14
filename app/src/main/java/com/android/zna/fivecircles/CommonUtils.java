package com.android.zna.fivecircles;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by nianan.zeng
 */
public class CommonUtils {

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
}
