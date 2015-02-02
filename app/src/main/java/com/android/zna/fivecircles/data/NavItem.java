package com.android.zna.fivecircles.data;

import android.graphics.drawable.Drawable;

/**
 * Created by ZNA on 2014/12/2.
 */
public class NavItem {
	private int mId;
	private Drawable mIcon;
	private Drawable mIconFocus;
	private String mDisplayText;

	public NavItem(Drawable icon,Drawable iconFocus, String text) {
		mIcon = icon;
		mIconFocus = iconFocus;
		mDisplayText = text;
		mId = generateNavItemId();
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

	public int getmId() {

		return mId;
	}
}
