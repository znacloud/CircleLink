package com.android.zna.fivecircles.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zna.fivecircles.R;
import com.android.zna.fivecircles.WelcomeActivity;
import com.android.zna.fivecircles.data.FamilyUser;
import com.android.zna.fivecircles.services.ServerSerivce;
import com.android.zna.fivecircles.view.CustomSimpleAlertDialog;
import com.android.zna.fivecircles.view.CustomToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {

    private BaseActivity mActivity;


    public static  SettingsFragment newInstance(){
        SettingsFragment fragment = new SettingsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity pActivity){
        super.onAttach(pActivity);
        mActivity =(BaseActivity)pActivity;
    }

    @Override
    public void onCreate(Bundle pBundle){
        super.onCreate(pBundle);
        addPreferencesFromResource(R.xml.settings_preference);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen pScreen,Preference pPreference){
            String  key = pPreference.getKey();
            android.util.Log.e("ZNA_DEBUG","KEY == "+key);
            if(key != null && key.contentEquals("logout")){

                CustomSimpleAlertDialog dialog = new CustomSimpleAlertDialog.Builder(getActivity())
                        .setTitle(R.string.logout_msg_title)
                        .setMessage(R.string.logout_msg)
                        .setNegativeButton(android.R.string.cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO:you should not call dismiss or cancel
                                //because it be called in super already
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO:logout action
                                logout();
                            }
                        }).show();
            }else if(key != null && key.contentEquals("user_info")){
                mActivity.startActivity(new Intent(mActivity, UserInfoActivity.class));
            }
        return super.onPreferenceTreeClick(pScreen,pPreference);
    }

    private void logout() {
        mActivity.getServerSerivce().logout(null,new ServerSerivce.ResultListener() {
            @Override
            public void onSuccess(Object pObj) {
                Intent intent = new Intent(mActivity,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                mActivity.finish();
            }

            @Override
            public void onFailure(int pErrorCode, String pErrorMsg) {
                CustomToast.show(mActivity,pErrorMsg, Toast.LENGTH_LONG);
            }
        });
    }

}
