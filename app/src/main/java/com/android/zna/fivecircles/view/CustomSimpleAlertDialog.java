package com.android.zna.fivecircles.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zna.fivecircles.R;

/**
 * Cutom dialog used to show confirm message
 */
public class CustomSimpleAlertDialog extends Dialog {
    public Context mContext;
    //component field
    private ImageView mIcon;
    private TextView mTitleTv;

    private ImageView mContentIcon;
    private TextView mMessageTv;

    private Button mPositiveBtn;
    private Button mNegativeBtn;

    DialogParams mParams;
    private View mBtnDivider;

    private CustomSimpleAlertDialog(Context context) {
        super(context,R.style.CustomAlertDialogStyle);
        mContext = context;
    }

    private CustomSimpleAlertDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle pBundle) {
        super.onCreate(pBundle);
        setContentView(R.layout.custom_dialog_layout);
        findViews();
        setupContent();

        //set cancel action
        setCancelable(mParams.mCancellable);
        setCanceledOnTouchOutside(mParams.mCancellable);
        setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (mParams.mOnCancelListenr != null) {
                    mParams.mOnCancelListenr.onCancel(dialog);
                    dialog.dismiss();
                }
            }
        });

        //set dismiss action
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mParams.mOnDismissListenr != null) {
                    mParams.mOnDismissListenr.onDismiss(dialog);
                }
            }
        });


    }

    private void findViews() {
        mIcon = (ImageView) findViewById(R.id.icon);
        mTitleTv = (TextView) findViewById(R.id.tv_title);

        mContentIcon = (ImageView) findViewById(R.id.content_icon);
        mMessageTv = (TextView) findViewById(R.id.tv_message);

        mPositiveBtn = (Button) findViewById(R.id.btn_positive);
        mNegativeBtn = (Button) findViewById(R.id.btn_negative);
        mBtnDivider = findViewById(R.id.divider_btn);

    }

    private void setupContent() {
        if (mParams.mIconDrawable != null) {
            mIcon.setImageDrawable(mParams.mIconDrawable);
            mIcon.setVisibility(View.VISIBLE);
        } else {
            mIcon.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mParams.mTitle)) {
            mTitleTv.setText(mParams.mTitle);
        } else {
            throw new IllegalArgumentException("Dialog title can not be empty !");
        }

        if (mParams.mContentDrawable != null) {
            mContentIcon.setImageDrawable(mParams.mContentDrawable);
            mContentIcon.setVisibility(View.VISIBLE);
        } else {
            mContentIcon.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mParams.mMessage)) {
            mMessageTv.setText(mParams.mMessage);
        } else {
            throw new IllegalArgumentException("Dialog message can not be empty !");
        }

        if (!TextUtils.isEmpty(mParams.mNegativeBtnStr)) {
            mNegativeBtn.setText(mParams.mNegativeBtnStr);
            mNegativeBtn.setVisibility(View.VISIBLE);
            mBtnDivider.setVisibility(View.VISIBLE);

            //set up OnClickListener for button
            mNegativeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mParams.mNegativeBtnClickListener != null) {
                        mParams.mNegativeBtnClickListener.onClick(v);
                    }
                    cancel();
                }
            });
        } else {
            mNegativeBtn.setVisibility(View.GONE);
            mBtnDivider.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(mParams.mPositiveBtnStr)) {
            mPositiveBtn.setText(mParams.mPositiveBtnStr);
        } else {
            throw new IllegalArgumentException("Dialog positive button name can not be empty !");
        }

        mPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mParams.mPositiveBtnClickListener != null) {
                    mParams.mPositiveBtnClickListener.onClick(v);
                }
                dismiss();
            }
        });
    }

    private void apply(DialogParams pParams) {
        mParams = pParams;
    }

    /**
     * a builder class to create dialog
     */
    public static class Builder {

        private final Context bContext;
        private DialogParams p;

        public Builder(Context pContext){
            bContext = pContext;
            p = new DialogParams();
        }

        public CustomSimpleAlertDialog create(){
            CustomSimpleAlertDialog dialog = new CustomSimpleAlertDialog(bContext);
            dialog.apply(p);
            return dialog;
        }

        public CustomSimpleAlertDialog show(){
            CustomSimpleAlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

        public Builder setIconDrawable(Drawable pIconDrawable) {
            p.mIconDrawable = pIconDrawable;
            return this;
        }

        public Builder setTitle(String pTitle) {
            p.mTitle = pTitle;
            return this;
        }

        public Builder setTitle(int pTitleId) {
            p.mTitle = bContext.getString(pTitleId);
            return this;
        }

        public Builder setContentDrawable(Drawable pContentDrawable) {
            p.mContentDrawable = pContentDrawable;
            return this;
        }

        public Builder setContentDrawable(int pContentDrawableId) {
            p.mContentDrawable = bContext.getDrawable(pContentDrawableId);
            return this;
        }

        public Builder setMessage(String pMessage) {
            p.mMessage = pMessage;
            return this;
        }
        public Builder setMessage(int pMessageId) {
            p.mMessage = bContext.getString(pMessageId);
            return this;
        }

        public Builder setPositiveButton(String pPositiveBtnStr,View.OnClickListener pListener) {
            p.mPositiveBtnStr = pPositiveBtnStr;
            p.mPositiveBtnClickListener = pListener;
            return this;
        }
        public Builder setPositiveButton(int pPositiveBtnStrId,View.OnClickListener pListener) {
            p.mPositiveBtnStr = bContext.getString(pPositiveBtnStrId);
            p.mPositiveBtnClickListener = pListener;
            return this;
        }

        public Builder setNegativeButton(String pNegativeBtnStr,View.OnClickListener pListener) {
            p.mNegativeBtnStr = pNegativeBtnStr;
            p.mNegativeBtnClickListener = pListener;
            return this;
        }
        public Builder setNegativeButton(int pNegativeBtnStrId,View.OnClickListener pListener) {
            p.mNegativeBtnStr = bContext.getString(pNegativeBtnStrId);
            p.mNegativeBtnClickListener = pListener;
            return this;
        }

        public Builder setCancellable(boolean pCancellable) {
            p.mCancellable = pCancellable;
            return this;
        }

        public Builder setNegativeBtnClickListener(View.OnClickListener pNegativeBtnClickListener) {
            p.mNegativeBtnClickListener = pNegativeBtnClickListener;
            return this;
        }

        public Builder setOnCancelListenr(OnCancelListener pOnCancelListenr) {
            p.mOnCancelListenr = pOnCancelListenr;
            return this;
        }

        public Builder setOnDismissListenr(OnDismissListener pOnDismissListenr) {
            p.mOnDismissListenr = pOnDismissListenr;
            return this;
        }

        public Builder setPositiveBtnClickListener(View.OnClickListener pPositiveBtnClickListener) {
            p.mPositiveBtnClickListener = pPositiveBtnClickListener;
            return this;
        }
    }


    /**
     * a inner class to store Dialog parameters such as drawable title message
     */
    private static class DialogParams {
        //content field
        public Drawable mIconDrawable;
        public String mTitle;

        public Drawable mContentDrawable;
        public String mMessage;

        public String mPositiveBtnStr;
        public String mNegativeBtnStr;

        //other field

        public boolean mCancellable = true;
        public View.OnClickListener mNegativeBtnClickListener;
        public OnCancelListener mOnCancelListenr;
        public OnDismissListener mOnDismissListenr;
        public View.OnClickListener mPositiveBtnClickListener;
    }
}
