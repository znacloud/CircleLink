package com.android.zna.fivecircles.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.zna.fivecircles.R;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.makeramen.RoundedImageView;

public class FamilyDetailActivity extends ActionBarActivity {
    Toolbar mToolbar;
    RoundedImageView mHeadImg;
    CardView mInfoCard1,mInfoCard2,mInfoCard3;
    FloatingActionButton mFloatBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bitmap bitmap = intent.getParcelableExtra("bitmap");

        setContentView(R.layout.activity_family_detail);
	    mHeadImg = (RoundedImageView)findViewById(R.id.head_img);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mInfoCard1 = (CardView)findViewById(R.id.info1);
        mInfoCard2 = (CardView)findViewById(R.id.info2);
        mInfoCard3 = (CardView)findViewById(R.id.info3);
        mFloatBtn = (FloatingActionButton) findViewById(R.id.fab_button);
        mHeadImg.setImageBitmap(bitmap);

        Palette.generateAsync(bitmap,new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getDarkVibrantSwatch();
                Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
                Palette.Swatch muted = palette.getDarkMutedSwatch();
                if(vibrant != null) {
                    findViewById(R.id.header_view).setBackgroundColor(vibrant.getRgb());
                    mToolbar.setBackgroundColor(vibrant.getRgb());

                    mFloatBtn.setColorNormal(vibrant.getRgb());
                }

                if(lightVibrant != null) {
                    mInfoCard1.setCardBackgroundColor(lightVibrant.getRgb());
                    mInfoCard2.setCardBackgroundColor(lightVibrant.getRgb());
                    mInfoCard3.setCardBackgroundColor(lightVibrant.getRgb());
                    mFloatBtn.setColorPressed(lightVibrant.getRgb());
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                     if(vibrant != null){
                        mToolbar.getNavigationIcon().setTint(vibrant.getTitleTextColor());
                        mToolbar.setTitleTextColor(vibrant.getTitleTextColor());
                    }
                }else{
                    mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
                }
            }
        });
        mToolbar.setTitle("FamilyDetail");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.family_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
