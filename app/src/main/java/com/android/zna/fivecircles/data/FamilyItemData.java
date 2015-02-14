package com.android.zna.fivecircles.data;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.android.zna.fivecircles.R;

/**
 * Created by ZNA on 2015/1/4.
 */
public class FamilyItemData {
    public Drawable headDrawable;
    public String displayText;

    public FamilyItemData(Context context) {
        headDrawable = context.getDrawable(R.drawable.head_img_default);
        displayText = context.getString(R.string.display_text_defualt);
    }

    public FamilyItemData(Drawable headDrawable, String displayText) {
        this.headDrawable = headDrawable;
        this.displayText = displayText;
    }

}
