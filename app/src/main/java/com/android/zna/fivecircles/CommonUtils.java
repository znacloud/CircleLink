package com.android.zna.fivecircles;

import android.os.Environment;
import android.widget.Toast;

import com.android.zna.fivecircles.view.CustomToast;

import java.io.File;
import java.io.IOException;

/**
 * Created by nianan.zeng
 */
public class CommonUtils {

    /**
     * check whether or not ExternalStorage is available
     * @return true if externalStorage is available,false otherwise
     */
    public static boolean isSDCARDMounted() {
        return Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED);
    }

    /**
     * get file which used store head image of current user,maybe return null
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
}
