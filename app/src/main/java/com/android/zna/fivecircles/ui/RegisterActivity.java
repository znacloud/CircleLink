package com.android.zna.fivecircles.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zna.fivecircles.R;
import com.android.zna.fivecircles.data.FamilyUser;
import com.android.zna.fivecircles.net.NetUtil;

import cn.bmob.v3.listener.SaveListener;


/**
 * Created by ZNA on 2014/12/1.
 */
public class RegisterActivity extends ActionBarActivity{
    private static String LOG_TAG = "ResigterActivity";
    private EditText mUserNameEdit;
    private EditText mPasswordEdit;
    private EditText mNickNameEdit;
    private EditText mGroupCodeEdit;
    private CheckBox mProtocolCheck;
    private Button mRegisterBtn;

    private ActionBar mToolbar;
    private TextView mUserNameErrorTv;
    private TextView mPasswordErrorTv;
    private TextView mNicknameErrorTv;
    private TextView mGroupErrorTv;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.register_layout);
        //setup widget
        //toolbar
        mToolbar = getSupportActionBar();
        mToolbar.setTitle(R.string.regist_user);
        mToolbar.setDisplayHomeAsUpEnabled(true);
        mToolbar.setHomeButtonEnabled(true);

        //Input field
        mUserNameEdit = (EditText) findViewById(R.id.et_username);
        mPasswordEdit = (EditText) findViewById(R.id.et_password);
        mNickNameEdit = (EditText) findViewById(R.id.et_nickname);
        mGroupCodeEdit = (EditText) findViewById(R.id.et_group);
        mProtocolCheck = (CheckBox) findViewById(R.id.ckb_protocol);
        mRegisterBtn = (Button) findViewById(R.id.btn_register);

        //Error field
        mUserNameErrorTv =(TextView) findViewById(R.id.tv_username_error);
        mPasswordErrorTv =(TextView) findViewById(R.id.tv_password_error);
        mNicknameErrorTv =(TextView) findViewById(R.id.tv_nickname_error);
        mGroupErrorTv =(TextView) findViewById(R.id.tv_group_error);

        //setup widget listener
        mUserNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                android.util.Log.d("ZNA_DEBUG","beforeTextChange");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                android.util.Log.d("ZNA_DEBUG","onTextChange");
            }

            @Override
            public void afterTextChanged(Editable s) {
                android.util.Log.d("ZNA_DEBUG","afterTextChange");
                mUserNameErrorTv.setVisibility(View.GONE);
            }
        });
        mPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                android.util.Log.d("ZNA_DEBUG", "beforeTextChange");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                android.util.Log.d("ZNA_DEBUG", "onTextChange");
            }

            @Override
            public void afterTextChanged(Editable s) {
                android.util.Log.d("ZNA_DEBUG", "afterTextChange");
                mPasswordErrorTv.setVisibility(View.GONE);
            }
        });
        mGroupCodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                android.util.Log.d("ZNA_DEBUG", "beforeTextChange");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                android.util.Log.d("ZNA_DEBUG", "onTextChange");
            }

            @Override
            public void afterTextChanged(Editable s) {
                android.util.Log.d("ZNA_DEBUG", "afterTextChange");
                mGroupErrorTv.setVisibility(View.GONE);
            }
        });
        mNickNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                android.util.Log.d("ZNA_DEBUG", "beforeTextChange");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                android.util.Log.d("ZNA_DEBUG", "onTextChange");
            }

            @Override
            public void afterTextChanged(Editable s) {
                android.util.Log.d("ZNA_DEBUG", "afterTextChange");
                mNicknameErrorTv.setVisibility(View.GONE);
            }
        });

        mProtocolCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mRegisterBtn.setEnabled(isChecked);
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkValid()) return;

                //create a new user
                FamilyUser user = new FamilyUser();
                user.setUsername(mUserNameEdit.getText().toString());
                user.setPassword(NetUtil.md5(mPasswordEdit.getText().toString()));
                user.setNick(mNickNameEdit.getText().toString());
                user.setFamilyGroup(mGroupCodeEdit.getText().toString());

                final ProgressDialog progressDialog = ProgressDialog.show(RegisterActivity.this,"Message","Register...",true,false);
                //register on Bmob server
                user.signUp(RegisterActivity.this,new SaveListener() {
                    @Override
                    public void onSuccess() {
                        if(progressDialog != null && progressDialog.isShowing()){
                            progressDialog.dismiss();

                        }
                        Toast.makeText(RegisterActivity.this,"Register successfully",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        if(progressDialog != null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                        Toast.makeText(RegisterActivity.this,"Register failsd:"+i+" "+s,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
          if(item.getItemId() == android.R.id.home){
              onBackPressed();
          }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkValid(){
        boolean valid = true;
        if(!isEmailaddress(mUserNameEdit.getText().toString())){
            valid = false;
            mUserNameErrorTv.setVisibility(View.VISIBLE);
        }
        if(!isPasswdValid(mPasswordEdit.getText().toString())){
            valid = false;
            mPasswordErrorTv.setVisibility(View.VISIBLE);
        }
        if(!isNicknameValid(mNickNameEdit.getText().toString())){
            valid = false;
            mNicknameErrorTv.setVisibility(View.VISIBLE);
        }
        if(!isGroupValid(mGroupCodeEdit.getText().toString())){
            valid = false;
            mGroupErrorTv.setVisibility(View.VISIBLE);
        }
        return valid;
    }

    private boolean isEmailaddress(String emailAddress) {
        String emailPattern = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";
        return emailAddress.matches(emailPattern);
    }

    private boolean isPasswdValid(String password){
        String passwdPattern = "[0-9a-zA-Z]{6,16}";
        return password.matches(passwdPattern);
    }

    private boolean isNicknameValid(String nickname){
        String nicknamePattern = "[-0-9a-zA-Z\u4e00-\u9fa5]{4,24}";
        return nickname.matches(nicknamePattern);
    }

    private boolean isGroupValid(String groupCode){
        String groupCodePattern = "";
        return true;//TO-DO:
    }
}
