package com.android.zna.fivecircles.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.zna.fivecircles.R;
import com.android.zna.fivecircles.data.FamilyUser;

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

        currentUser = (FamilyUser)getIntent().getParcelableExtra("user");

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        mHeadIv = (ImageView) findViewById(R.id.head_img);
        mNicknameEt = (EditText) findViewById(R.id.et_nickname);
        mRealnameEt = (EditText) findViewById(R.id.et_realname);
        mDescriptionEt = (EditText) findViewById(R.id.et_description);
        mSexGp = (RadioGroup) findViewById(R.id.gp_sex);
        modifyBtn = (Button) findViewById(R.id.btn_modify);

        //get cache dir to store head image
        headImageUri = Uri.parse(getCacheDir().getPath()+"/head.jpeg");

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
               // currentUser.setAvatar(headImageUri);
                currentUser.setRealName(mRealnameEt.getText().toString().trim());
                currentUser.setNick(mNicknameEt.getText().toString().trim());
                currentUser.setSex(mSexGp.getCheckedRadioButtonId() == R.id.sex_male ? 0:1);
                currentUser.setSelfDesc(mDescriptionEt.getText().toString().trim());
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

    public void pickAvatar(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_PICK_IMAGE);
    }

    public void cropAvatar(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*")
                .putExtra("crop",true)
                .putExtra("aspectX", 1)
                .putExtra("aspectY",1)
                .putExtra("outputX",144)
                .putExtra("outputY",144)
                .putExtra("scale",true)
                .putExtra("return-data",true)
                .putExtra("output",headImageUri)
                .putExtra("outputFormat", Bitmap.CompressFormat.JPEG)
                .putExtra("noFaceDetection", true);
        startActivityForResult(intent,REQUEST_CROP_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_PICK_IMAGE){
                if(data != null && data.getData() != null){
                    cropAvatar(data.getData());
                }
            }else if(requestCode == REQUEST_CROP_IMAGE){
                if(data != null){
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if(bitmap != null) {
                        mHeadIv.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }
}
