package com.android.zna.fivecircles;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.android.zna.fivecircles.services.ServerSerivce;
import com.android.zna.fivecircles.ui.LoginActivity;
import com.android.zna.fivecircles.ui.UserActivity;

import cn.bmob.im.BmobChat;

public class WelcomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        //Bmob.initialize(this,Config.APP_ID);
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            BmobChat.getInstance(this).init(Config.APP_ID);
//        }
        //for android L,reinstall may cause exception for unknown reason
        try {
            BmobChat.getInstance(this).init(Config.APP_ID);
        }catch (Exception e){
            e.printStackTrace();
        }
        setContentView(R.layout.welcome);
        final TextView appInfoView = (TextView) findViewById(R.id.app_info);

        //get APP VERSION
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(Config.APP_PACKAGE, 0);
            String verNum = "version " + pi.versionName;
            appInfoView.setText(verNum);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        //set font type
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/descFont.TTF");
        TextView tvWelcome = (TextView) findViewById(R.id.tv_welcome);
        tvWelcome.setTypeface(tf, Typeface.BOLD);


        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (isLogined()) {
                    //jump to activity_main activity
                    Intent intent = new Intent(WelcomeActivity.this, UserActivity.class);
                    intent.putExtra(Config.KEY_TOKEN, Config.getCachedToken(WelcomeActivity.this));
                    startActivity(intent);
                } else {
                    //jump to login activity
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2900);//3 seconds later jump to activity_main activity

    }

    /**
     * check if user has login
     *
     * @return true if logined
     */
    private boolean isLogined() {
        return null != ServerSerivce.getService(this,ServerSerivce.SERV_TYPE_BMOB).getCurrentUser();
    }

    /**
     * Override onBackPressed
     * to avoid back to launcher from Welcome Activity
     */
    @Override
    public void onBackPressed() {
    }

}
