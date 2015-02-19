package com.android.zna.fivecircles.data;

import android.graphics.drawable.Drawable;

public class NavItem {
    private String mTargetFragmentTag;
    private Drawable mIcon;
    private Drawable mIconFocus;
    private String mDisplayText;

    public NavItem(Drawable icon, Drawable iconFocus, String text) {
        mIcon = icon;
        mIconFocus = iconFocus;
        mDisplayText = text;
    }

    private int generateNavItemId() {
        //TODO:generate unique ID
        return 0;
    }

    public String getDisplayText() {

        return mDisplayText;
    }

    public Drawable getIcon(boolean focused) {

        return focused ? mIconFocus : mIcon;
    }

    public String getTargetFragmentTag() {

        return mTargetFragmentTag;
    }

    public void setTargetFragmentTag(String pTag){
        mTargetFragmentTag = pTag;
    }

    public void setIcon(Drawable pIcon) {
        mIcon = pIcon;
    }

    public void setIconFocus(Drawable pIconFocus) {
        mIconFocus = pIconFocus;
    }

    public void setDisplayText(String pDisplayText) {
        mDisplayText = pDisplayText;
    }
}
