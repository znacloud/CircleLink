package com.android.zna.fivecircles.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zna.fivecircles.R;

public class CustomProgressDialog extends Dialog {

    private Context mContext;
    private Drawable mDrawable;
    private String mMessage;

    private ImageView mProgressIv;
    private TextView mProgressTv;

    /**
     * to indicate whether a animation will take affect on mDrawable.
     *
     * @see com.android.zna.fivecircles.R.anim#progress_anim
     * always be true for now.
     * may be used for later.
     */
    private boolean animated = true;

    public CustomProgressDialog(Context context) {
        super(context, R.style.custom_progress_dialog);
        mContext = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set custom layout
        this.setContentView(R.layout.custom_progress_dialog);
        mProgressIv = (ImageView) findViewById(R.id.image);
        mProgressTv = (TextView) findViewById(R.id.text);
        this.setCancelable(false);
    }

    /**
     * show this dialog using self-defined message string
     *
     * @param context application context
     * @param message string to show in Dialog
     * @return CustomProgressDialog
     */
    public static CustomProgressDialog show(Context context, String message) {
        return show(context, message, null);
    }

    /**
     * show this dialog using self-defined message string id
     *
     * @param context   Application context
     * @param messageId String id to show in Dialog
     * @return CustomProgressDialog
     */
    public static CustomProgressDialog show(Context context, int messageId) {
        return show(context, messageId, 0);
    }

    /**
     * show this dialog using self-defined message string and drawable
     *
     * @param context  application context
     * @param message  String to show in Dialog
     * @param drawable Drawable to show in Dialog
     * @return CustomProgressDialog
     */
    public static CustomProgressDialog show(Context context, String message, Drawable drawable) {
        CustomProgressDialog dialog = new CustomProgressDialog(context);
        dialog.show();
        dialog.setMessage(message);
        dialog.setDrawable(drawable);

        if (dialog.animated) {
            dialog.mProgressIv.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.progress_anim));
        }
        return dialog;
    }

    /**
     * show this dialog using self-defined message string id and drawable id
     *
     * @param context    application context
     * @param messageId  String id to show in Dialog
     * @param drawableId Drawable id to show in Dialog
     * @return CustomProgressDialog
     */
    public static CustomProgressDialog show(Context context, int messageId, int drawableId) {
        CustomProgressDialog dialog = new CustomProgressDialog(context);
        dialog.show();
        dialog.setMessage(messageId);
        if (0 != drawableId) {
            dialog.setDrawable(drawableId);
        }
        if (dialog.animated) {
            dialog.mProgressIv.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.progress_anim));
        }
        return dialog;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setDrawable(Drawable pDrawable) {
        mDrawable = pDrawable;
        mProgressIv.setImageDrawable(mDrawable);
    }

    public void setDrawable(int pDrawableId) {
        mDrawable = mContext.getResources().getDrawable(pDrawableId);
        mProgressIv.setImageDrawable(mDrawable);
    }

    public void setMessage(String pMessage) {
        mMessage = pMessage;
        mProgressTv.setText(mMessage);
        if (TextUtils.isEmpty(mMessage)) {
            mProgressTv.setVisibility(View.GONE);
        } else {
            mProgressTv.setVisibility(View.VISIBLE);
        }


    }

    public void setMessage(int pMessageId) {
        if (pMessageId == 0) {
            mProgressTv.setVisibility(View.GONE);
        } else {
            mMessage = mContext.getString(pMessageId);
            mProgressTv.setText(mMessage);
            mProgressTv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void dismiss() {
        mProgressIv.getAnimation().cancel();//stop animation
        super.dismiss();
    }
}