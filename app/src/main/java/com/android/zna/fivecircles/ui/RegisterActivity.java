package com.android.zna.fivecircles.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zna.fivecircles.CommonUtils;
import com.android.zna.fivecircles.R;
import com.android.zna.fivecircles.data.FamilyUser;
import com.android.zna.fivecircles.services.ServerSerivce;
import com.android.zna.fivecircles.view.CustomProgressDialog;
import com.android.zna.fivecircles.view.CustomToast;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ZNA on 2014/12/1.
 */
public class RegisterActivity extends ActionBarActivity implements View.OnFocusChangeListener {
    private static String LOG_TAG = "ResigterActivity";
    private static final int REQUEST_PICK_IMAGE = 0;
    private static final int REQUEST_CROP_IMAGE = 1;

    private EditText mUserNameEdit;
    private EditText mPasswordEdit;
    private EditText mRepasswordEdit;
    private CheckBox mProtocolCheck;

    private EditText mValidateEdit;
    private Button mValidateBtn;
    private Button mNextBtn;

    private ActionBar mToolbar;
    private TextView mUserNameErrorTv;
    private TextView mPasswordErrorTv;
    private TextView mRepasswordErrorTv;
    private LinearLayout mAccountInfoLayout;
    private LinearLayout mBasicInfoLayout;

    private ImageView mHeadIv;
    private EditText mNicknameEt;
    private EditText mRealnameEt;
    private RadioGroup mSexGp;
    private EditText mDescriptionEt;

    private Button mFinishBtn;

    private FamilyUser currentUser;
    private TextView mNicknameErrorTv;
    private TextView mRealnameErrorTv;
    private TextView mValidateErrorTv;

    private ServerSerivce mServerSerivce;

    private String mValiCode = "";
    private Handler validateHandler = new Handler();
    private int mValiTime;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.register_layout);

        //get remote server
        mServerSerivce = ServerSerivce.getService(this, ServerSerivce.SERV_TYPE_BMOB);

        //setup toolbar
        mToolbar = getSupportActionBar();
//        mToolbar.setTitle(R.string.regist_user);
        mToolbar.setDisplayHomeAsUpEnabled(true);
        mToolbar.setHomeButtonEnabled(true);

        currentUser = new FamilyUser();

        //setup layout
        mAccountInfoLayout = (LinearLayout) findViewById(R.id.layout_account_info);
        initAccountView(mAccountInfoLayout);
        mBasicInfoLayout = (LinearLayout) findViewById(R.id.layout_basic_info);
        initBasicView(mBasicInfoLayout);

        mAccountInfoLayout.setVisibility(View.VISIBLE);
        mBasicInfoLayout.setVisibility(View.GONE);


    }

    private void initBasicView(View rootView) {
        //input field
        mHeadIv = (ImageView) rootView.findViewById(R.id.head_img);
        mNicknameEt = (EditText) rootView.findViewById(R.id.et_nickname);
        mRealnameEt = (EditText) rootView.findViewById(R.id.et_realname);
        mDescriptionEt = (EditText) rootView.findViewById(R.id.et_description);
        mSexGp = (RadioGroup) rootView.findViewById(R.id.gp_sex);
        mFinishBtn = (Button) rootView.findViewById(R.id.btn_modify);

        //errorfield
        mNicknameErrorTv = (TextView) rootView.findViewById(R.id.tv_nickname_error);
        mRealnameErrorTv = (TextView) rootView.findViewById(R.id.tv_realname_error);

        //setup touch event listener
        final ImageView coverIv = (ImageView) rootView.findViewById(R.id.cover_img);
        mHeadIv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int actionType = event.getActionMasked();
                if (actionType == MotionEvent.ACTION_DOWN) {
                    android.util.Log.d("ZNA_DEBUG", "action down");
                    coverIv.setVisibility(View.VISIBLE);
                    return true;
                } else if (actionType == MotionEvent.ACTION_UP) {
                    android.util.Log.d("ZNA_DEBUG", "action up");
                    coverIv.setVisibility(View.GONE);
                    pickAvatar(v);
                    return true;
                } else if (actionType == MotionEvent.ACTION_CANCEL) {
                    android.util.Log.d("ZNA_DEBUG", "action outside");
                    coverIv.setVisibility(View.GONE);
                }
                return false;
            }
        });
        mNicknameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                android.util.Log.d("ZNA_DEBUG", "afterTextChange");
                mNicknameErrorTv.setVisibility(View.INVISIBLE);
            }
        });
        mRealnameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                android.util.Log.d("ZNA_DEBUG", "afterTextChange");
                mRealnameErrorTv.setVisibility(View.GONE);
            }
        });


        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                if (!checkValid2()) return;
                currentUser.setRealname(mRealnameEt.getText().toString().trim());
                currentUser.setNickname(mNicknameEt.getText().toString().trim());
                currentUser.setSex(mSexGp.getCheckedRadioButtonId() == R.id.rb_sex_male ? 0 : 1);
                currentUser.setSelfDesc(mDescriptionEt.getText().toString().trim());


                final CustomProgressDialog progressDialog = CustomProgressDialog.show
                        (RegisterActivity.this, R.string.progress_register);
                //register on server
                mServerSerivce.register(currentUser, new ServerSerivce.ResultListener() {
                    @Override
                    public void onSuccess(Object pObj) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        CustomToast.show(RegisterActivity.this, R.string.register_success,
                                Toast.LENGTH_LONG);

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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

    private void initAccountView(View rootView) {

        //Input field
        mUserNameEdit = (EditText) rootView.findViewById(R.id.et_username);
        mPasswordEdit = (EditText) rootView.findViewById(R.id.et_password);
        mRepasswordEdit = (EditText) rootView.findViewById(R.id.et_repassword);
        mValidateEdit = (EditText) rootView.findViewById(R.id.et_validate);
        mValidateBtn = (Button) rootView.findViewById(R.id.btn_validate);
        mProtocolCheck = (CheckBox) rootView.findViewById(R.id.ckb_protocol);
        mNextBtn = (Button) rootView.findViewById(R.id.btn_next);

        //Error field
        mUserNameErrorTv = (TextView) rootView.findViewById(R.id.tv_username_error);
        mPasswordErrorTv = (TextView) rootView.findViewById(R.id.tv_password_error);
        mRepasswordErrorTv = (TextView) rootView.findViewById(R.id.tv_repassword_error);
        mValidateErrorTv = (TextView) rootView.findViewById(R.id.tv_validate_error);

        mUserNameEdit.setOnFocusChangeListener(this);
        mPasswordEdit.setOnFocusChangeListener(this);
        mRepasswordEdit.setOnFocusChangeListener(this);
        mValidateEdit.setOnFocusChangeListener(this);

        mValidateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServerSerivce.getValidCode(ServerSerivce.VALID_TYPE_EMAIL,
                        mUserNameEdit.getText().toString().trim(),
                        new ServerSerivce.ResultListener() {


                    @Override
                    public void onSuccess(Object pObj) {
                        mValiCode = (String) pObj;
                        //disable validate btn for a moment
                        mValiTime = 30;
                        mValidateBtn.setEnabled(false);
                        final String format = getString(R.string.get_valid_code_format);
                        mValidateBtn.setText(String.format(format, mValiTime));
                        validateHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mValiTime--;
                                mValidateBtn.setText(String.format(format, mValiTime));
                                if (mValiTime > 0) {
                                    validateHandler.postDelayed(this, 1000);
                                } else {
                                    mValidateBtn.setText(R.string.get_valid_code);
                                    mValidateBtn.setEnabled(true);
                                }
                            }
                        }, 1000);
                    }

                    @Override
                    public void onFailure(int pErrorCode, String pErrorMsg) {
                        CustomToast.show(RegisterActivity.this, getString(R.string.vlidate_error)
                                + ":" + pErrorCode + "-" + pErrorMsg, Toast.LENGTH_LONG);
                        mValidateBtn.setText(R.string.get_valid_code);
                        mValiCode = "";
                        mValidateBtn.setEnabled(true);
                    }
                });
            }
        });

        mProtocolCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNextBtn.setEnabled(isChecked);
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkValid()) return;

                //create a new user
                currentUser.setUsername(mUserNameEdit.getText().toString().trim());
                currentUser.setPassword(CommonUtils.makeSHA(mPasswordEdit.getText().toString() +
                        mUserNameEdit.getText().toString()));
                //animation to next page
                android.util.Log.e("ZNA_DEBUG", "height:" + mAccountInfoLayout.getHeight() + "," +
                        "" + mBasicInfoLayout.getHeight());
                final int screenHeight = mAccountInfoLayout.getHeight();
                final ObjectAnimator animTopOut = ObjectAnimator.ofFloat(mAccountInfoLayout,
                        "translationY", 0, -screenHeight);
                animTopOut.setDuration(300);
                animTopOut.setInterpolator(new LinearInterpolator());

                final ObjectAnimator animBottomIn = ObjectAnimator.ofFloat(mBasicInfoLayout,
                        "translationY", screenHeight, 0);
                animBottomIn.setDuration(300);
                animBottomIn.setInterpolator(new LinearInterpolator());

                animTopOut.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator pAnimator) {
                        mBasicInfoLayout.setVisibility(View.VISIBLE);

                        animBottomIn.start();
                    }

                    @Override
                    public void onAnimationEnd(Animator pAnimator) {
                        mAccountInfoLayout.setVisibility(View.GONE);
                        mBasicInfoLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator pAnimator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator pAnimator) {

                    }
                });
                animTopOut.start();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_IMAGE) {
                if (data != null && data.getData() != null) {
                    cropAvatar(data.getData());
                }
            } else if (requestCode == REQUEST_CROP_IMAGE) {
                if (data != null) {
                    updateAvatar();
                }
            }
        }
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
            mUserNameErrorTv.setText(R.string.username_tips);
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

        if (!isValiCodeCorrect()) {
            valid = false;
            mValidateErrorTv.setVisibility(View.VISIBLE);
        }
        return valid;
    }


    private boolean checkValid2() {
        boolean valid = true;
        if (!isRealNameValid(mRealnameEt.getText().toString().trim())) {
            valid = false;
            mRealnameErrorTv.setVisibility(View.VISIBLE);
        }
        if (!isNicknameValid(mNicknameEt.getText().toString().trim())) {
            valid = false;
            mNicknameErrorTv.setVisibility(View.VISIBLE);
        }
        return valid;
    }


    public void pickAvatar(View view) {
        if (CommonUtils.getHeadImageTempFile() == null) {
            CustomToast.show(this, R.string.sdcard_unavailable, Toast.LENGTH_LONG);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media
                .EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }


    public void cropAvatar(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*")
                .putExtra("crop", true)
                .putExtra("aspectX", 1)
                .putExtra("aspectY", 1)
                .putExtra("outputX", 144)
                .putExtra("outputY", 144)
                .putExtra("scale", true)
                .putExtra("return-data", false)
                .putExtra("output", Uri.fromFile(CommonUtils.getHeadImageTempFile()))
                .putExtra("outputFormat", Bitmap.CompressFormat.JPEG)
                .putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CROP_IMAGE);

    }


    private void updateAvatar() {
        final CustomProgressDialog progressDialog = CustomProgressDialog.show(this,
                R.string.upload_head_image);
        android.util.Log.e("ZNA_DEBUG", "UPDATE AVATAR");
        //upload head image file to server
        mServerSerivce.uploadFile(CommonUtils.getHeadImageTempFile(),
                new ServerSerivce.ResultListener() {
                    @Override
                    public void onSuccess(Object pObj) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        //set head image url to server url
                        String url = (String) pObj;
                        currentUser.setAvatar(url);
                        String absolutePath = CommonUtils.getHeadImageTempFile()
                                .getAbsolutePath();
                        Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
                        if (bitmap != null) {
                            mHeadIv.setImageBitmap(bitmap);
                        } else {
                            //rollback
                            mServerSerivce.deleteFile(url);
                            currentUser.setAvatar("");
                            CustomToast.show(RegisterActivity.this,
                                    getResources().getString(R.string.set_headimg_failed),
                                    Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        CustomToast.show(RegisterActivity.this,
                                getResources().getString(R.string.set_headimg_failed) + ":" + i + s,
                                Toast.LENGTH_LONG);
                    }
                });
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

    private boolean isValiCodeCorrect() {
        return !TextUtils.isEmpty(mValiCode)&&mValiCode.contentEquals(mValidateEdit.getText().toString().trim());
    }

    private boolean isNicknameValid(String nickname) {
        String nicknamePattern = "[-0-9a-zA-Z\u4e00-\u9fa5]{4,24}";
        return nickname.matches(nicknamePattern);
    }

    private boolean isRealNameValid(String realName) {
        String realNamePattern = "[\\u4e00-\\u9fa5]{2,4}";
        return realName.matches(realNamePattern);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.et_username) {
            if (!hasFocus) {
                if (isEmailaddress(mUserNameEdit.getText().toString().trim())) {
                    mServerSerivce.checkDuplicateAccount(mUserNameEdit.getText().toString().trim
                            (), new ServerSerivce.ResultListener() {
                        @Override
                        public void onSuccess(Object pObj) {
                            boolean duplicated = (boolean) pObj;
                            if (duplicated) {
                                mUserNameErrorTv.setText(R.string.username_tips2);
                                mUserNameErrorTv.setVisibility(View.VISIBLE);
                                mValidateBtn.setEnabled(false);
                            } else {
                                mValidateBtn.setEnabled(true);
                            }
                        }

                        @Override
                        public void onFailure(int pErrorCode, String pErrorMsg) {
                            //do nothing
                        }
                    });
                } else {
                    mUserNameErrorTv.setText(R.string.username_tips);
                    mUserNameErrorTv.setVisibility(View.VISIBLE);

                }
            } else {
                mUserNameErrorTv.setVisibility(View.GONE);
            }
        }else if(v.getId() == R.id.et_password){
            if(!hasFocus){
                if(!isPasswdValid(mPasswordEdit.getText().toString())) {
                    mPasswordErrorTv.setVisibility(View.VISIBLE);
                }
            }else{
                mPasswordErrorTv.setVisibility(View.GONE);
            }
        }else if(v.getId() == R.id.et_repassword){
            if(!hasFocus){
                if(!isPasswdConsistent(mPasswordEdit.getText().toString(),mRepasswordEdit.getText().toString())){
                    mRepasswordErrorTv.setVisibility(View.VISIBLE);
                }
            }else{
                mRepasswordErrorTv.setVisibility(View.GONE);
            }
        }else if(v.getId() == R.id.et_validate){
            if(hasFocus){
                mValidateErrorTv.setVisibility(View.GONE);
            }
        }


    }
}
