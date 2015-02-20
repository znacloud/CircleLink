package com.android.zna.fivecircles.ui;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.zna.fivecircles.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InteractionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InteractionFragment extends Fragment {


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InteractionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InteractionFragment newInstance(String param1, String param2) {
        InteractionFragment fragment = new InteractionFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public InteractionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interaction, container, false);
    }


}
