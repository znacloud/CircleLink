package com.android.zna.fivecircles.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.zna.fivecircles.Config;
import com.android.zna.fivecircles.R;
import com.android.zna.fivecircles.data.FamilyUser;
import com.android.zna.fivecircles.view.CustomProgressDialog;
import com.android.zna.fivecircles.view.CustomToast;

import java.io.File;
import java.io.IOException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class BasicInfoFinishAcitivity extends ActionBarActivity {
    private static final int REQUEST_PICK_IMAGE = 0;
    private static final int REQUEST_CROP_IMAGE = 1;

    private ActionBar mActionBar;
    private ImageView mHeadIv;
    private EditText mNicknameEt;
    private EditText mRealnameEt;
    private RadioGroup mSexGp;
    private EditText mDescriptionEt;

    private Button modifyBtn;

    private Uri headImageUri;
    private FamilyUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_info_layout);

        currentUser = (FamilyUser) getIntent().getSerializableExtra("user");

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        mHeadIv = (ImageView) findViewById(R.id.head_img);
        mNicknameEt = (EditText) findViewById(R.id.et_nickname);
        mRealnameEt = (EditText) findViewById(R.id.et_realname);
        mDescriptionEt = (EditText) findViewById(R.id.et_description);
        mSexGp = (RadioGroup) findViewById(R.id.gp_sex);
        modifyBtn = (Button) findViewById(R.id.btn_modify);


        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                // currentUser.setAvatar(headImageUri);
                currentUser.setRealName(mRealnameEt.getText().toString().trim());
                currentUser.setNick(mNicknameEt.getText().toString().trim());
                currentUser.setSex(mSexGp.getCheckedRadioButtonId() == R.id.rb_sex_male ? 0 : 1);
                currentUser.setSelfDesc(mDescriptionEt.getText().toString().trim());


                final CustomProgressDialog progressDialog = CustomProgressDialog.show
                        (BasicInfoFinishAcitivity.this, R.string.progress_register);
                //register on Bmob server
                currentUser.signUp(BasicInfoFinishAcitivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        CustomToast.show(BasicInfoFinishAcitivity.this, R.string.register_success,
                                Toast.LENGTH_LONG);

                        startActivity(new Intent(BasicInfoFinishAcitivity.this,LoginActivity.class));
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        String errorMsg = BasicInfoFinishAcitivity.this.getString(R.string.Register_failed);
                        CustomToast.show(BasicInfoFinishAcitivity.this, errorMsg + i
                                + " " + s, Toast.LENGTH_LONG);
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isSDCARDMounted() {
        return Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED);
    }

    private File getTempFile() {
        if (isSDCARDMounted()) {
            File dir = new File(Environment.getExternalStorageDirectory(), Config.APP_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File headImageFile = new File(dir, Config.TEMP_PHOTO_FILE);
            try {
                if (!headImageFile.exists()) {
                    headImageFile.createNewFile();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                CustomToast.show(this, "create temp file failed:" + e.getMessage(),
                        Toast.LENGTH_LONG);
            }
            return headImageFile;
        } else {
            CustomToast.show(this, R.string.sdcard_unavailable, Toast.LENGTH_LONG);
            return null;
        }
    }

    public void pickAvatar(View view) {
        if (!isSDCARDMounted()) {
            CustomToast.show(this, R.string.sdcard_unavailable, Toast.LENGTH_LONG);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media
                .EXTERNAL_CONTENT_URI);
        //intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    public void cropAvatar(Uri uri) {
        headImageUri = Uri.fromFile(getTempFile());
        CustomToast.show(this, "headImgUri:" + headImageUri, Toast.LENGTH_LONG);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*")
                .putExtra("crop", true)
                .putExtra("aspectX", 1)
                .putExtra("aspectY", 1)
                .putExtra("outputX", 144)
                .putExtra("outputY", 144)
                .putExtra("scale", true)
                .putExtra("return-data", false)
                .putExtra("output", headImageUri)
                .putExtra("outputFormat", Bitmap.CompressFormat.JPEG)
                .putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CROP_IMAGE);

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

    private void updateAvatar() {
        if (!isSDCARDMounted()) {
            CustomToast.show(this, R.string.sdcard_unavailable, Toast.LENGTH_LONG);
            return;
        }
        final CustomProgressDialog progressDialog = CustomProgressDialog.show(this,R.string.upload_head_image);
        //upload head image file to server
        final BmobFile bf = new BmobFile(getTempFile());
        bf.upload(this,new UploadFileListener() {
            @Override
            public void onSuccess() {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                //set head image url to server url
                currentUser.setAvatar(bf.getFileUrl(BasicInfoFinishAcitivity.this));
                Bitmap bitmap = BitmapFactory.decodeFile(getTempFile().getAbsolutePath());
                if(bitmap != null) {
                    mHeadIv.setImageBitmap(bitmap);
                }else{
                    //rollback
                    bf.delete(BasicInfoFinishAcitivity.this);
                    currentUser.setAvatar("");
                    CustomToast.show(BasicInfoFinishAcitivity.this,
                            getResources().getString(R.string.set_headimg_failed),
                            Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                CustomToast.show(BasicInfoFinishAcitivity.this,
                        getResources().getString(R.string.set_headimg_failed)+":"+i+s,
                        Toast.LENGTH_LONG);
            }
        });
    }

}
