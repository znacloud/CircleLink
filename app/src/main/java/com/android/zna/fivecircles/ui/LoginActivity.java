package com.android.zna.fivecircles.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.zna.fivecircles.R;
import com.android.zna.fivecircles.Config;
import com.android.zna.fivecircles.net.LoginServer;

public class LoginActivity extends ActionBarActivity {

	private EditText mEtUsername;
	private EditText mEtPasswd;
	private Button mBtnLogin;
	private TextView mTvRegist;
	private TextView mTvForgetPasswd;
	private TextView mTvErrorMsg;

	public class LoginTask extends AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			return LoginServer.checkAuth(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(Integer integer) {
			if (integer == LoginServer.CheckStatus.SUCCESS) {
				Intent intent = new Intent(LoginActivity.this, UserActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//intent.putExtra(UserConfig.USER_NAME,userName);
				LoginActivity.this.startActivity(intent);
				LoginActivity.this.finish();
			} else {
				mTvErrorMsg.setVisibility(View.VISIBLE);
				//TO-DO:show Alert Message to User
				if (LoginServer.CheckStatus.DATA_ERROR == integer) {
					mTvErrorMsg.setText(R.string.name_pass_error);
				} else if (LoginServer.CheckStatus.NET_ERROR == integer) {
					mTvErrorMsg.setText(R.string.net_error);
				} else {
					mTvErrorMsg.setText(R.string.unknown);
				}
			}
		}
	}

	public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.login_layout);

		//find view
		mEtUsername = (EditText) findViewById(R.id.user_name);
		mEtPasswd = (EditText) findViewById(R.id.password);
		mBtnLogin = (Button) findViewById(R.id.btn_login);
		mTvErrorMsg = (TextView) findViewById(R.id.error_msg);

		//set login button action
		mBtnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String userName = mEtUsername.getText().toString();
				String passwd = mEtPasswd.getText().toString();
				new LoginTask().execute(userName, passwd);

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
				intent.putExtra(Config.USER_NAME, mEtUsername.getText().toString());
				LoginActivity.this.startActivity(intent);
			}
		});
	}
}