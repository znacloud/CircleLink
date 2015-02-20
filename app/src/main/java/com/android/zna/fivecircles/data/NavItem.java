package com.android.zna.fivecircles.data;

import android.graphics.drawable.Drawable;

public class NavItem {
    private String mTargetFragmentTag;
    private Drawable mIcon;
    private String mDisplayText;

    public NavItem(Drawable icon, String text) {
        mIcon = icon;
        mDisplayText = text;
    }

    private int generateNavItemId() {
        //TODO:generate unique ID
        return 0;
    }

    public String getDisplayText() {

        return mDisplayText;
    }

    public Drawable getIcon() {

            return mIcon;
    }

    public String getTargetActivityName() {

        return mTargetFragmentTag;
    }

    public void setTargetFragmentTag(String pTag){
        mTargetFragmentTag = pTag;
    }

    public void setIcon(Drawable pIcon) {
        mIcon = pIcon;
    }

    public void setDisplayText(String pDisplayText) {
        mDisplayText = pDisplayText;
    }
}
