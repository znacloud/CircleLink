package com.android.zna.fivecircles.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
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

import com.android.zna.fivecircles.R;
import com.android.zna.fivecircles.data.NavItem;

public class UserActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mLvNavItem;
    private FrameLayout mLvContentView;
    private LinearLayout mLeftDrawView;
    private Toolbar mToolbar;

    private ActionBarDrawerToggle mToggle;
    private int mCurrentPositon = 0;

    private FragmentManager mFragmentManager;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLvNavItem = (ListView) findViewById(R.id.item_list);
        mLvContentView = (FrameLayout) findViewById(R.id.content_frame);
        mLeftDrawView = (LinearLayout) findViewById(R.id.left_drawer);

        mLvNavItem.setAdapter(new NavItemAdapter(this));
        mLvNavItem.setOnItemClickListener(new NavItemClickListener(this));
        mLvNavItem.setItemChecked(0, true);

        mFragmentManager = getFragmentManager();

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    10, getResources().getDisplayMetrics()));
        }
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.login, R.string.app_name);

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mToggle.onDrawerSlide(drawerView,slideOffset);
            }
            @Override
            public void onDrawerClosed(View view) {
                mToggle.onDrawerClosed(view);
                getSupportActionBar().setTitle(((NavItem) mLvNavItem.getItemAtPosition(mCurrentPositon))
                            .getDisplayText());
                invalidateOptionsMenu();
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                mToggle.onDrawerOpened(drawerView);
                    getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                mToggle.onDrawerStateChanged(newState);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //in OptionsItemSelected event,to do mToggle event
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // TODO: other Item selected event

        return super.onOptionsItemSelected(item);
    }

    private class NavItemAdapter extends BaseAdapter {
        private Context mContext;
        private Drawable[] mIcons;
        private Drawable[] mIconFocus;
        private String[] mDisplayNames;

        private NavItem[] mNavItems;

        public NavItemAdapter(Context context) {
            mContext = context;
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

            //generate NavItem
            int itemNum = mIcons.length < mDisplayNames.length ? mIcons.length : mDisplayNames
                    .length;
            mNavItems = new NavItem[itemNum];
            for (int i = 0; i < itemNum; i++) {
                mNavItems[i] = new NavItem(mIcons[i], mIconFocus[i], mDisplayNames[i]);
            }
        }

        @Override
        public int getCount() {

            return mNavItems.length;
        }

        @Override
        public long getItemId(int position) {

            return mNavItems[position].getmId();
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
                ((TextView) convertView.findViewById(R.id.text)).setTextColor(mContext.getResources().getColor(R.color.base_color_gray));
            }

            return convertView;
        }
    }

    private class NavItemClickListener implements AdapterView.OnItemClickListener {
        public NavItemClickListener(UserActivity activity) {
        }

        private void switchItemFragment(Class<? extends Fragment> fromFragmentClass, Class<? extends Fragment> toFragmentClass, Bundle args){

            //obtain Fragment instance
            String fromFragmentTag = fromFragmentClass.getSimpleName();
            Fragment fromFragment = mFragmentManager.findFragmentByTag(fromFragmentTag);

            String toFragmentTag = toFragmentClass.getSimpleName();
            Fragment toFragment = mFragmentManager.findFragmentByTag(toFragmentTag);

            //if toFragment instance is null,create it
            if(toFragment == null){
                try {
                    toFragment = toFragmentClass.newInstance();
                    toFragment.setArguments(args);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            //if there are arguments
            if(args != null && !args.isEmpty()){
                toFragment.getArguments().putAll(args);
            }

            FragmentTransaction ft = mFragmentManager.beginTransaction();
            //if toFragment already added,show it
            //else add it
            if(toFragment.isAdded()){
                ft.hide(fromFragment).show(toFragment);
            }else{
                ft.hide(fromFragment).add(R.id.content_frame,toFragment,toFragmentTag);
            }
            ft.commit();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mLvNavItem.setItemChecked(position, true);
            mCurrentPositon = position;
            if (((NavItem) mLvNavItem.getItemAtPosition(position)).getDisplayText().equals("Family")) {
                mFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, FamilyFragment.newInstance("", ""))
                        .commit();
            } else {
                //TODO:
            }
            mDrawerLayout.closeDrawer(mLeftDrawView);
        }
    }
}
