package com.android.zna.fivecircles.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.zna.fivecircles.R;
import com.android.zna.fivecircles.data.FamilyItemData;
import com.android.zna.fivecircles.view.FamilyItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment used to show FamilyUsers
 */
public class FamilyFragment extends Fragment {
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageButton mFabAddBtn;
    private GridLayout mFamilyItemsGridView;
    private View revealView;
    private List<FamilyItemData> mFamilyDataList;
    private static int SAMPLE_COUNT = 5;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FamilyFragment.
     */
    public static FamilyFragment newInstance() {
        FamilyFragment fragment = new FamilyFragment();
        return fragment;
    }

    public FamilyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFamilyDataList = getFamilyDatas();
    }

    private List<FamilyItemData> getFamilyDatas() {
        List<FamilyItemData> list = new ArrayList<FamilyItemData>();
        if (false) {//TODO:get data from server

        } else {//use default sample data

            String[] mDisplayNames = getActivity().getResources().getStringArray(R.array
                    .sample_dislpay_names);

            Drawable[] drawablesDefault;// = getActivity().getResources().obtainTypedArray(R
            // .array.sample_head_imgs);
            //get drawable array
            TypedArray headImgArray = getActivity().getResources().obtainTypedArray(R.array
                    .sample_head_imgs);
            int len = headImgArray.length();
            drawablesDefault = new Drawable[len];
            for (int i = 0; i < len; i++) {
                drawablesDefault[i] = headImgArray.getDrawable(i);
            }
            headImgArray.recycle();

            for (int i = 0; i < SAMPLE_COUNT; i++) {
                list.add(new FamilyItemData(drawablesDefault[i], mDisplayNames[i]));
            }
        }
        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View familyView = inflater.inflate(R.layout.fragment_family_items, container, false);
        mFamilyItemsGridView = (GridLayout) familyView.findViewById(R.id.family_list);
        revealView = familyView.findViewById(R.id.reveal_view);

        for (FamilyItemData item : mFamilyDataList) {
            FamilyItemView itemView = new FamilyItemView(getActivity());
            itemView.setHeadImageDrawable(item.headDrawable);
            itemView.setDisplayText(item.displayText);
            itemView.setOnClickListener(familyItemOnClickListener);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    16, getResources().getDisplayMetrics());
            lp.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    16, getResources().getDisplayMetrics());
            lp.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    8, getResources().getDisplayMetrics());
            lp.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    8, getResources().getDisplayMetrics());
            mFamilyItemsGridView.addView(itemView, lp);
        }

        mFabAddBtn = (ImageButton) familyView.findViewById(R.id.fab_button);
        return familyView;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mFabAddBtn.setOnClickListener(addFamilyOnClickListener);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @TargetApi(21)
    private void showRevealEffect(final View v, int centerX, int centerY,
                                  Animator.AnimatorListener lis) {

        v.setVisibility(View.VISIBLE);

        int height = v.getHeight();

        Animator anim = ViewAnimationUtils.createCircularReveal(
                v, centerX, centerY, 0, height);

        anim.setDuration(350);

        anim.addListener(lis);
        anim.start();
    }

    @TargetApi(21)
    private void hideRevealEffect(final View v, int centerX, int centerY, int initialRadius) {

        v.setVisibility(View.VISIBLE);

        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(
                v, centerX, centerY, initialRadius, 0);

        anim.setDuration(350);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.INVISIBLE);
            }
        });

        anim.start();
    }


    View.OnClickListener addFamilyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TO-DO

        }
    };


    View.OnClickListener familyItemOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            ((ImageView) v).setDrawingCacheEnabled(true);
            final Bitmap bitmap = Bitmap.createBitmap(((ImageView) v).getDrawingCache());
            ((ImageView) v).setDrawingCacheEnabled(false);

            Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {

                    int[] location = new int[2];
                    final Palette.Swatch vibrant = palette.getDarkVibrantSwatch();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.setTransitionName("user_head");
                        revealView.setBackgroundColor(vibrant.getRgb());
                        v.getLocationOnScreen(location);

                        final int cx = (location[0] + (v.getWidth() / 2));
                        final int cy = location[1] + (v.getHeight() / 2);

                        showRevealEffect(revealView, cx, cy, new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            @TargetApi(21)
                            public void onAnimationEnd(Animator animation) {
                                Intent intent = new Intent(getActivity(),
                                        FamilyDetailActivity.class);

                                Bundle bd = new Bundle();
//                                bd.putInt("bgcolor", vibrant.getRgb());
//                                bd.putInt("tttxtcolor", vibrant.getTitleTextColor());
//                                bd.putInt("bdtxtcolor", vibrant.getBodyTextColor());
//                                intent.putExtra("vibrant", bd);
                                intent.putExtra("bitmap", bitmap);
                                ActivityOptions transitionActivityOptions = ActivityOptions
                                        .makeSceneTransitionAnimation(getActivity(),
                                                Pair.create((View) mFabAddBtn, "fab"),
                                                Pair.create(v,
                                                        "user_head"));

                                startActivity(intent, transitionActivityOptions.toBundle());
                                hideRevealEffect(revealView, cx, cy, 1920);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                    } else {
                        Intent intent = new Intent(getActivity(), FamilyDetailActivity.class);
                        Bundle bd = new Bundle();
//                        bd.putInt("bgcolor", vibrant.getRgb());
//                        bd.putInt("tttxtcolor", vibrant.getTitleTextColor());
//                        bd.putInt("bdtxtcolor", vibrant.getBodyTextColor());
//                        intent.putExtra("vibrant", bd);
                        intent.putExtra("bitmap", bitmap);
                        startActivity(intent);
                    }


                }
            });


        }
    };
}
