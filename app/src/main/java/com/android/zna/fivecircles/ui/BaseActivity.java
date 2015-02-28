package com.android.zna.fivecircles.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.zna.fivecircles.R;
import com.android.zna.fivecircles.services.ServerSerivce;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;

public abstract class BaseActivity extends ActionBarActivity {
    protected ActionBar mToolbar;
    protected ServerSerivce mServerSerivce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);


        mServerSerivce = ServerSerivce.getService(this,ServerSerivce.SERV_TYPE_BMOB);

        mToolbar = getSupportActionBar();
        mToolbar.setDefaultDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
//        }else if(id == android.R.id.home){
//            onBackPressed();
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ServerSerivce getServerSerivce() {
        return mServerSerivce;
    }
}
