package com.android.zna.fivecircles.ui;


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

import com.android.zna.fivecircles.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {


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
    public void onCreate(Bundle pBundle){
        super.onCreate(pBundle);
        addPreferencesFromResource(R.xml.settings_preference);
        getPreferenceScreen().setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {


            @Override
            public boolean onPreferenceClick(Preference pPreference) {
                String  key = pPreference.getKey();
                android.util.Log.e("ZNA_DEBUG","KEY == "+key);
                if(key != null && key.contentEquals("logout")){

                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Confirm")
                            .setMessage("Are you sure to logout this account ???")
                            .setNegativeButton("cancel",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface pDialogInterface, int i) {
                                    pDialogInterface.dismiss();
                                }
                            })
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface pDialogInterface, int i) {
                                    //TODO:logout action
                                }
                            }).create();
                    dialog.show();

                    return true;
                }else if(key != null && key.contentEquals("user_info")){
                    getActivity().startActivity(new Intent(getActivity(),UserInfoActivity.class));
                }
                return false;
            }
        });

    }

}
