package com.gmyscl.ecom.firstorder.communicate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.gmyscl.ecom.firstorder.R;


public class HelpFragment extends Fragment {


    public HelpFragment() {
        // Required empty public constructor
    }

    private TextView privacyPolicyBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate( R.layout.fragment_help, container, false );

        privacyPolicyBtn = view.findViewById( R.id.privacy_policy_read );

        privacyPolicyBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Jump to
                startActivity( new Intent( getActivity(), HelpActivity.class ) );
            }
        } );

        return view;

    }

}

