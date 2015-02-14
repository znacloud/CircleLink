package com.android.zna.fivecircles.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zna.fivecircles.R;

/**
 * Created by Administrator on 2015/2/8.
 */
public class CustomToast extends Toast {
    private Context mContext;
    private TextView mToastTv;
    private ImageView mToastIv;

    public CustomToast(Context pContext) {
        super(pContext);
        mContext = pContext;
        View layout = LayoutInflater.from(pContext).inflate(R.layout.custom_toast, null);
        mToastTv = (TextView) layout.findViewById(R.id.text);
        mToastIv = (ImageView) layout.findViewById(R.id.icon);
        setView(layout);
    }

    public static CustomToast show(Context pContext, CharSequence message, int showLength) {
        return show(pContext, message, null, showLength);
    }

    public static CustomToast show(Context pContext, int messageId, int showLength) {
        return show(pContext, messageId, 0, showLength);
    }

    public static CustomToast show(Context pContext, CharSequence message, Drawable drawable,
                                   int showLength) {
        CustomToast toast = new CustomToast(pContext);
        toast.setToastMessage(message);
        toast.setToastIcon(drawable);
        toast.setDuration(showLength);
        toast.show();
        return toast;
    }

    public static CustomToast show(Context pContext, int messageId, int drawableId,
                                   int showLength) {
        CustomToast toast = new CustomToast(pContext);
        if (0 != messageId) {
            toast.setToastMessage(messageId);
        }
        toast.setToastIcon(drawableId);
        toast.setDuration(showLength);
        toast.show();
        return toast;
    }

    public void setToastMessage(CharSequence pMsg) {
        mToastTv.setText(pMsg);
    }

    public void setToastMessage(int pMsgId) {
        mToastTv.setText(mContext.getString(pMsgId));
    }

    public void setToastIcon(Drawable pIcon) {
        if (pIcon == null) {
            mToastIv.setVisibility(View.GONE);
        } else {
            mToastIv.setImageDrawable(pIcon);
            mToastIv.setVisibility(View.VISIBLE);
        }
    }

    public void setToastIcon(int pIconId) {
        if (pIconId == 0) {
            mToastIv.setVisibility(View.GONE);
        } else {
            mToastIv.setImageDrawable(mContext.getResources().getDrawable(pIconId));
            mToastIv.setVisibility(View.VISIBLE);
        }
    }
}
