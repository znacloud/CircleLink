package com.android.zna.fivecircles.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zna.fivecircles.R;
import com.android.zna.fivecircles.data.FamilyUser;
import com.android.zna.fivecircles.net.NetUtil;
import com.android.zna.fivecircles.view.CustomProgressDialog;
import com.android.zna.fivecircles.view.CustomToast;

import cn.bmob.v3.listener.SaveListener;


/**
 * Created by ZNA on 2014/12/1.
 */
public class RegisterActivity extends ActionBarActivity {
    private static String LOG_TAG = "ResigterActivity";
    private EditText mUserNameEdit;
    private EditText mPasswordEdit;
    private EditText mRepasswordEdit;
    private CheckBox mProtocolCheck;
    private Button mRegisterBtn;

    private ActionBar mToolbar;
    private TextView mUserNameErrorTv;
    private TextView mPasswordErrorTv;
    private TextView mRepasswordErrorTv;

    @Override
    public void onCreate(Bundle savedInstance) {
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
        mRepasswordEdit = (EditText) findViewById(R.id.et_repassword);
        mProtocolCheck = (CheckBox) findViewById(R.id.ckb_protocol);
        mRegisterBtn = (Button) findViewById(R.id.btn_register);

        //Error field
        mUserNameErrorTv = (TextView) findViewById(R.id.tv_username_error);
        mPasswordErrorTv = (TextView) findViewById(R.id.tv_password_error);
        mRepasswordErrorTv = (TextView) findViewById(R.id.tv_repassword_error);

        //setup textwatcher listener
        mUserNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                android.util.Log.d("ZNA_DEBUG", "afterTextChange");
                mUserNameErrorTv.setVisibility(View.GONE);
                //if username changed,we reset password
                mPasswordEdit.setText("");
                mRepasswordEdit.setText("");
            }
        });
        mPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                android.util.Log.d("ZNA_DEBUG", "afterTextChange");
                mPasswordErrorTv.setVisibility(View.GONE);
            }
        });
        mRepasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                android.util.Log.d("ZNA_DEBUG", "afterTextChange");
                mRepasswordErrorTv.setVisibility(View.GONE);
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
                if (!checkValid()) return;

                //create a new user
                FamilyUser user = new FamilyUser();
                user.setUsername(mUserNameEdit.getText().toString().trim());
                user.setPassword(NetUtil.md5(mPasswordEdit.getText().toString()));

                final CustomProgressDialog progressDialog = CustomProgressDialog.show
                        (RegisterActivity.this, R.string.progress_register);
                //register on Bmob server
                user.signUp(RegisterActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        CustomToast.show(RegisterActivity.this, R.string.register_success,
                                Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        String errorMsg = RegisterActivity.this.getString(R.string.Register_failed);
                        CustomToast.show(RegisterActivity.this, errorMsg + i
                                + " " + s, Toast.LENGTH_LONG);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * check content of each input
     * if valid return true
     * else show error tips
     * and return false
     */
    private boolean checkValid() {
        boolean valid = true;
        if (!isEmailaddress(mUserNameEdit.getText().toString().trim())) {
            valid = false;
            mUserNameErrorTv.setVisibility(View.VISIBLE);
        }
        if (!isPasswdValid(mPasswordEdit.getText().toString())) {
            valid = false;
            mPasswordErrorTv.setVisibility(View.VISIBLE);
        } else if (!isPasswdConsistent(mPasswordEdit.getText().toString(),
                mRepasswordEdit.getText().toString())) {
            valid = false;
            mRepasswordErrorTv.setVisibility(View.VISIBLE);
        }
        return valid;
    }

    private boolean isEmailaddress(String emailAddress) {
        String emailPattern = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";
        return emailAddress.matches(emailPattern);
    }

    private boolean isPasswdValid(String password) {
        String passwdPattern = "[0-9a-zA-Z]{6,16}";
        return password.matches(passwdPattern);
    }

    private boolean isPasswdConsistent(String password, String repassword) {
        return isPasswdValid(password) && password.contentEquals(repassword);
    }

    private boolean isNicknameValid(String nickname) {
        String nicknamePattern = "[-0-9a-zA-Z\u4e00-\u9fa5]{4,24}";
        return nickname.matches(nicknamePattern);
    }

    private boolean isGroupValid(String groupCode) {
        String groupCodePattern = "";
        return true;//TO-DO:
    }
}
