package com.android.zna.fivecircles.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zna.fivecircles.CommonUtils;
import com.android.zna.fivecircles.R;
import com.android.zna.fivecircles.Config;
import com.android.zna.fivecircles.data.FamilyUser;
import com.android.zna.fivecircles.net.LoginServer;
import com.android.zna.fivecircles.services.ServerSerivce;
import com.android.zna.fivecircles.view.CustomProgressDialog;
import com.android.zna.fivecircles.view.CustomToast;

public class LoginActivity extends ActionBarActivity {

	private EditText mUsernameEdit;
	private EditText mPasswdEdit;
	private Button mLoginBtn;
	private TextView mTvRegist;
	private TextView mTvForgetPasswd;
	private TextView mErrorMsgTv;
    private ServerSerivce mServerService;

	public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.login_layout);

        //get remote server
        mServerService = ServerSerivce.getService(this,ServerSerivce.SERV_TYPE_BMOB);

		//find view
		mUsernameEdit = (EditText) findViewById(R.id.user_name);
		mPasswdEdit = (EditText) findViewById(R.id.password);
		mLoginBtn = (Button) findViewById(R.id.btn_login);

		//set login button action
		mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomProgressDialog customProgressDialog = CustomProgressDialog.show(LoginActivity.this,R.string.progress_login);
                FamilyUser user = new FamilyUser();
                user.setUsername(mUsernameEdit.getText().toString());
                user.setPassword(CommonUtils.makeSHA(mPasswdEdit.getText().toString()+mUsernameEdit.getText().toString()));
                mServerService.login(user,new ServerSerivce.ResultListener() {
                    @Override
                    public void onSuccess(Object pObj) {
                        if(customProgressDialog!= null && customProgressDialog.isShowing()){
                            customProgressDialog.dismiss();
                        }
                        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //intent.putExtra(UserConfig.USER_NAME,userName);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void onFailure(int pErrorCode, String pErrorMsg) {
                        if(customProgressDialog!= null && customProgressDialog.isShowing()){
                            customProgressDialog.dismiss();
                        }
                        CustomToast.show(LoginActivity.this,
                                getResources().getString(R.string.login_fail)
                                        +":"+pErrorCode+"-"+pErrorMsg,Toast.LENGTH_LONG);
                    }
                });

            }
        });

		//set regist button action
		mTvRegist = (TextView) findViewById(R.id.regist_link);
		mTvRegist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TO-DO: regist
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				LoginActivity.this.startActivity(intent);
			}
		});
		//set forgetpasswd action
		mTvForgetPasswd = (TextView) findViewById(R.id.forget_pass);
		mTvForgetPasswd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TO-DO: reset passwd
				Intent intent = new Intent(LoginActivity.this, ResetPasswdActivity.class);
				intent.putExtra(Config.USER_NAME, mUsernameEdit.getText().toString());
				LoginActivity.this.startActivity(intent);
			}
		});
	}
}