package com.android.zna.fivecircles.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zna.fivecircles.R;

/**
 * A default fragment for UsrActivity
 * used to show direct things
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout mTabChatLy;
    private RelativeLayout mTabGalleryLy;
    private RelativeLayout mTabRecordLy;
    private ImageView mTabRecordIv;
    private TextView mTabRecordTv;
    private ImageView mTabRecordIvExt;
    private ImageView mTabChatIv;
    private TextView mTabChatTv;
    private ImageView mTabChatIvExt;
    private ImageView mTabGalleryIv;
    private TextView mTabGalleryTv;
    private ImageView mTabGalleryIvExt;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View pRootView) {
        mTabChatLy = (RelativeLayout) pRootView.findViewById(R.id.tab_chat);
        mTabChatIv = (ImageView) mTabChatLy.findViewById(R.id.iv_tab_icon_chat);
        mTabChatTv = (TextView) mTabChatLy.findViewById(R.id.tv_tab_chat);
        mTabChatIvExt = (ImageView) mTabChatLy.findViewById(R.id.iv_extra_icon_chat);
        mTabChatLy.setOnClickListener(this);

        mTabGalleryLy = (RelativeLayout) pRootView.findViewById(R.id.tab_gallery);
        mTabGalleryIv = (ImageView) mTabGalleryLy.findViewById(R.id.iv_tab_icon_gallery);
        mTabGalleryTv = (TextView) mTabGalleryLy.findViewById(R.id.tv_tab_gallery);
        mTabGalleryIvExt = (ImageView) mTabGalleryLy.findViewById(R.id.iv_extra_icon_gallery);
        mTabGalleryLy.setOnClickListener(this);

        mTabRecordLy = (RelativeLayout) pRootView.findViewById(R.id.tab_record);
        mTabRecordIv = (ImageView) mTabRecordLy.findViewById(R.id.iv_tab_icon_record);
        mTabRecordTv = (TextView) mTabRecordLy.findViewById(R.id.tv_tab_record);
        mTabRecordIvExt = (ImageView) mTabRecordLy.findViewById(R.id.iv_extra_icon_record);
        mTabRecordLy.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        resetTabStatus();
        if (v.getId() == R.id.tab_chat) {
            mTabChatIv.setImageResource(R.drawable.ic_chat);
            mTabChatTv.setTextColor(getResources().getColor(R.color.base_color));
        } else if (v.getId() == R.id.tab_gallery) {
            mTabGalleryIv.setImageResource(R.drawable.ic_gallery);
            mTabGalleryTv.setTextColor(getResources().getColor(R.color.base_color));
        } else if (v.getId() == R.id.tab_record) {
            mTabRecordIv.setImageResource(R.drawable.ic_record);
            mTabRecordTv.setTextColor(getResources().getColor(R.color.base_color));
        }
        //TODO:switch fragment
    }

    private void resetTabStatus() {
        mTabChatIv.setImageResource(R.drawable.ic_chat_gray);
        mTabChatTv.setTextColor(getResources().getColor(R.color.base_color_gray));

        mTabGalleryIv.setImageResource(R.drawable.ic_gallery_gray);
        mTabGalleryTv.setTextColor(getResources().getColor(R.color.base_color_gray));

        mTabRecordIv.setImageResource(R.drawable.ic_record_gray);
        mTabRecordTv.setTextColor(getResources().getColor(R.color.base_color_gray));
    }
}
