package com.android.zna.fivecircles.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zna.fivecircles.R;
import com.makeramen.RoundedImageView;

/**
 * TODO: document your custom view class.
 */
public class FamilyItemView extends RelativeLayout {
    private TextView mTextView;
    private RoundedImageView mHeadImageView;
    private ImageView mExtraImageView;
    private OnClickListener mOnClickListener;

    private String mDisplayText;
    private Drawable mHeadDrawable;
    private Drawable mExtraDrawable;
    private int mTextColor;


    public FamilyItemView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public FamilyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public FamilyItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        LayoutInflater.from(context).inflate(R.layout.family_item_view, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.FamilyItemView, defStyle, 0);

        mHeadDrawable = typedArray.getDrawable(R.styleable.FamilyItemView_headImageSrc);
        mExtraDrawable = typedArray.getDrawable(R.styleable.FamilyItemView_extraDrawable);
        mDisplayText = typedArray.getString(R.styleable.FamilyItemView_displayText);
        mTextColor = typedArray.getColor(R.styleable.FamilyItemView_textColor,
                R.color.base_color_dark);

        mHeadImageView = (RoundedImageView) this.findViewById(R.id.head_img);
        if (mHeadDrawable != null) {
            mHeadImageView.setImageDrawable(mHeadDrawable);
        }

        mTextView = (TextView) this.findViewById(R.id.text);
        mTextView.setTextColor(mTextColor);
        if (mDisplayText != null) {
            mTextView.setText(mDisplayText);
        }

        mExtraImageView = (ImageView) this.findViewById(R.id.extra_img);
        if (mExtraDrawable != null) {
            mExtraImageView.setImageDrawable(mExtraDrawable);
            mExtraImageView.setVisibility(View.VISIBLE);
        } else {
            mExtraImageView.setVisibility(View.GONE);
        }

    }


    /**
     * Gets the family display string .
     *
     * @return family display string .
     */
    public String getDisplayText() {
        return mTextView.getText().toString();
    }

    /**
     * Sets family display string
     *
     * @param displayText The family display string.
     */
    public void setDisplayText(String displayText) {
        mTextView.setText(displayText);
    }

    /**
     * Gets the family display text color .
     *
     * @return family display text colors .
     */
    public int getDisplayTextColor() {
        return mTextView.getCurrentTextColor();
    }

    /**
     * Sets family display string
     *
     * @param color The family display text color.
     */
    public void setDisplayTextColor(int color) {
        mTextView.setTextColor(color);
    }


    /**
     * Gets the family head imgage drawable.
     *
     * @return The family head imgae drawable.
     */
    public Drawable getHeadImageDrawable() {
        return mHeadImageView.getDrawable();
    }

    /**
     * Sets the family head image drawable
     *
     * @param drawable The family head image drawable
     */
    public void setHeadImageDrawable(Drawable drawable) {
        mHeadImageView.setImageDrawable(drawable);
    }

    /**
     * Gets the family head imgage drawable.
     *
     * @return The family head imgae drawable.
     */
    public Drawable getExtraImageDrawable() {
        return mExtraImageView.getDrawable();
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param drawable The family head image drawable attribute value to use.
     */
    public void setExtraImageDrawable(Drawable drawable) {
        mExtraImageView.setImageDrawable(drawable);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
//		super.setOnClickListener(mOnClickListener);
        mHeadImageView.setOnClickListener(mOnClickListener);
//		mTextView.setOnClickListener(mOnClickListener);
    }
}
