package com.android.zna.fivecircles.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.zna.fivecircles.CommonUtils;
import com.android.zna.fivecircles.Config;
import com.android.zna.fivecircles.R;
import com.android.zna.fivecircles.data.FamilyUser;
import com.android.zna.fivecircles.data.NavItem;
import com.android.zna.fivecircles.services.ServerSerivce;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class UserActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mLvNavItem;
    private FrameLayout mLvContentView;
    private LinearLayout mLeftDrawView;
    private Toolbar mToolbar;

    private TextView mUsernameTv;
    private ImageView mSexIv;
    private TextView mSelfDescTv;
    private TextView mAccountName;
    private ImageView mHeadIv;

    private ActionBarDrawerToggle mToggle;
    private int mCurrentPositon = 0;

    private FragmentManager mFragmentManager;
    private SlidingMenu mSlidingMenuLayout;
    private ServerSerivce mServerService;
    private FamilyUser mCurrentUser;
    private String mCurrentFragment;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);

        //get server and current user
        mServerService = ServerSerivce.getService(this,ServerSerivce.SERV_TYPE_BMOB);
        mCurrentUser = mServerService.getCurrentUser();
        if(mCurrentUser == null){
            startActivity(new Intent(this,LoginActivity.class));
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mFragmentManager = getFragmentManager();
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mSlidingMenuLayout =(SlidingMenu) findViewById(R.id.sliding_menu_layout);
        mLvNavItem = (ListView) findViewById(R.id.item_list);
        mLvContentView = (FrameLayout) findViewById(R.id.content_frame);
//        mLeftDrawView = (LinearLayout) findViewById(R.id.left_drawer);

        //setup NavItem list
        NavItemAdapter adapter = new NavItemAdapter(this);
        mCurrentPositon = 0;
        mCurrentFragment = ((NavItem)adapter.getItem(mCurrentPositon)).getTargetFragmentTag();
        mLvNavItem.setAdapter(adapter);

        NavItemClickListener listener = new NavItemClickListener(this);
        mLvNavItem.setOnItemClickListener(listener);

        mLvNavItem.setItemChecked(mCurrentPositon, true);
        mFragmentManager.beginTransaction()
                .add(R.id.content_frame,HomeFragment.newInstance(),
                       mCurrentFragment).commit();

        //init user information
        mUsernameTv = (TextView) findViewById(R.id.tv_nickname);
        mUsernameTv.setText(mCurrentUser.getNickname());

        mAccountName = (TextView) findViewById(R.id.tv_account);
        mAccountName.setText(mCurrentUser.getUsername());

        mSexIv = (ImageView) findViewById(R.id.iv_sex);
        mSexIv.setImageResource(mCurrentUser.getSex() == 0 ? R.drawable.ic_male : R.drawable.ic_female);

        mSelfDescTv = (TextView) findViewById(R.id.tv_selfdesc);
        mSelfDescTv.setText(mCurrentUser.getSelfDesc());
        //set font type
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/descFont.TTF");
        mSelfDescTv.setTypeface(tf);

        mHeadIv = (ImageView) findViewById(R.id.iv_head);


        //set user's real name as title
        mToolbar.setTitle(mCurrentUser.getRealname());
        //generate Circle head image drawable
        mServerService.downloadImage(mCurrentUser.getAvatar(),new ServerSerivce.ResultListener() {
            @Override
            public void onSuccess(Object pObj) {
                Bitmap bitmap = (Bitmap)pObj;
                //menu layout head image
                mHeadIv.setImageBitmap(bitmap);

                Drawable drawable = CommonUtils.generateScaledCircleDrawable(UserActivity.this,bitmap,
                        (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,18,
                                getResources().getDisplayMetrics()));
                //set head image as navigation icon
                mToolbar.setNavigationIcon(drawable);
            }

            @Override
            public void onFailure(int pErrorCode, String pErrorMsg) {
                //if failed to get head image,set default
                Drawable drawable = CommonUtils.generateScaledCircleDrawable(UserActivity.this,R.drawable.head_img_default,
                        (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,18,
                                getResources().getDisplayMetrics()));
                mToolbar.setNavigationIcon(drawable);
                mHeadIv.setImageDrawable(drawable);
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingMenuLayout.toggle();//toggle menu
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(TypedValue.applyDimension(TypedValue
                            .COMPLEX_UNIT_DIP,
                    0, getResources().getDisplayMetrics()));
        }



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //in OptionsItemSelected event,to do mToggle event
//        if (mToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        // TODO: other Item selected event

        return super.onOptionsItemSelected(item);
    }

    private class NavItemAdapter extends BaseAdapter {
        private Context mContext;
        private Drawable[] mIcons;
        private Drawable[] mIconFocus;
        private String[] mDisplayNames;
        private String[] mTags;
        private NavItem[] mNavItems;

        public NavItemAdapter(Context context) {
            mContext = context;
            initNavItems(context);
        }

        private void initNavItems(Context context) {
            mDisplayNames = context.getResources().getStringArray(R.array.nav_item_dislpay);
            //get drawable array
            TypedArray iconArray = mContext.getResources().obtainTypedArray(R.array.nav_item_icons);
            int len = iconArray.length();
            mIcons = new Drawable[len];
            for (int i = 0; i < len; i++) {
                mIcons[i] = iconArray.getDrawable(i);
            }
            iconArray.recycle();

            //get focused drawable array
            TypedArray iconArray2 = mContext.getResources().obtainTypedArray(R.array
                    .nav_item_icons_focus);
            int len2 = iconArray2.length();
            mIconFocus = new Drawable[len];
            for (int i = 0; i < len; i++) {
                mIconFocus[i] = iconArray.getDrawable(i);
            }
            iconArray2.recycle();

            //get target fragment tags
            mTags = context.getResources().getStringArray(R.array.nav_item_target_fragments);

            //generate NavItem
            int itemNum = mIcons.length < mDisplayNames.length ? mIcons.length : mDisplayNames
                    .length;
            mNavItems = new NavItem[itemNum];
            for (int i = 0; i < itemNum; i++) {
                mNavItems[i] = new NavItem(mIcons[i], mIconFocus[i], mDisplayNames[i]);
                mNavItems[i].setTargetFragmentTag(mTags[i]);
            }
        }

        @Override
        public int getCount() {

            return mNavItems.length;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public Object getItem(int position) {
            return mNavItems[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = UserActivity.this.getLayoutInflater().inflate(R.layout.nav_item,
                        null);
            }
            NavItem itemData = (NavItem) getItem(position);
            ((ImageView) convertView.findViewById(R.id.icon)).setImageDrawable(itemData.getIcon
                    (mLvNavItem.isItemChecked(position)));
            ((TextView) convertView.findViewById(R.id.text)).setText(itemData.getDisplayText());

            if (mLvNavItem.isItemChecked(position)) {
                ((TextView) convertView.findViewById(R.id.text)).setTextColor(mContext
                        .getResources().getColor(R.color.base_color));
            } else {
                ((TextView) convertView.findViewById(R.id.text)).setTextColor(mContext
                        .getResources().getColor(R.color.base_color_gray_dark));
            }

            return convertView;
        }
    }

    private class NavItemClickListener implements AdapterView.OnItemClickListener{
        public NavItemClickListener(UserActivity activity) {
        }

        private void switchItemFragment(String fromFragmentTag,
                                        String toFragmentTag, Bundle args) {
            android.util.Log.d("ZNA_DEBUG","FROM:"+fromFragmentTag+" To:"+toFragmentTag);

            //obtain Fragment instance
            Fragment fromFragment = mFragmentManager.findFragmentByTag(fromFragmentTag);

            Fragment toFragment = mFragmentManager.findFragmentByTag(toFragmentTag);

            //if toFragment instance is null,create it
            if (toFragment == null) {
                try {
                    Class<? extends Fragment> klass = (Class<? extends Fragment>)Class.forName(Config.APP_PACKAGE+".ui."+toFragmentTag);
                    toFragment = klass.newInstance();
                    toFragment.setArguments(args);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }catch(InstantiationException e){
                    e.printStackTrace();
                }
            }

            //if there are arguments
            if (args != null && !args.isEmpty()) {
                toFragment.getArguments().putAll(args);
            }

            FragmentTransaction ft = mFragmentManager.beginTransaction();
            //if toFragment already added,show it
            //else add it
            if (toFragment.isAdded()) {
                ft.hide(fromFragment).show(toFragment);
            } else {
                ft.hide(fromFragment).add(R.id.content_frame, toFragment, toFragmentTag);
            }
            ft.commit();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            android.util.Log.d("ZNA_DEBUG","Item "+ position+"clicked");
            mLvNavItem.setItemChecked(position, true);
            mCurrentPositon = position;
            String targetFragmentTag = ((NavItem)mLvNavItem.getItemAtPosition(position)).getTargetFragmentTag();
            switchItemFragment(mCurrentFragment,targetFragmentTag,null);
            mCurrentFragment = targetFragmentTag;
            mSlidingMenuLayout.toggle();

        }
    }
}
