package com.gmyscl.ecom.firstorder.buyprocess;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.gmyscl.ecom.firstorder.MainActivity;
import com.gmyscl.ecom.firstorder.R;

public class ContinueShopping extends Fragment {


    public ContinueShopping() {
        // Required empty public constructor
    }

    private FrameLayout continueShoppingFrameLayout;

    public static LinearLayout waitingLayout;
    public static LinearLayout orderSuccessLinearLayout;
    public static TextView orderIdText;
    private Button continueShoppingBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_continue_shopping, container, false );

        waitingLayout = view.findViewById( R.id.waitLinearLayout );
        orderSuccessLinearLayout = view.findViewById( R.id.successOrderTextLinearLayout );
        continueShoppingFrameLayout = view.findViewById( R.id.continue_frameLayout );
        continueShoppingBtn = view.findViewById( R.id.continueShoppingBtn );
        orderIdText = view.findViewById( R.id.order_id_text );
        orderSuccessLinearLayout.setVisibility( View.GONE );

        continueShoppingBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jump To MainActivity....
                // SetVisibility default...After success order...
                if (MainActivity.isFragmentIsMyCart){
                    MainActivity.isFragmentIsMyCart = false;
                    MainActivity.mainActivityForCart.finish();
                }
                MainActivity.mainActivity.finish();
                MainActivity.wCurrentFragment = 0;
                getActivity().finishAffinity();
                startActivity( new Intent( getActivity(), MainActivity.class ) );
                getActivity().finish();
            }
        } );

        return view;
    }


//    @Override
//    public void onDestroyView() {
////        ViewGroup mContainer = (ViewGroup) getActivity().findViewById(R.id.sign_in_frameLayout);
////        mContainer.removeAllViews();
//        continueShoppingFrameLayout.removeAllViews();
//        super.onDestroyView();
//    }

    // Fragment Transaction...
//    public void setFragment(Fragment showFragment){
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
////        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
//        fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right );
////        onDestroyView();
//        fragmentTransaction.replace( continueShoppingFrameLayout.getId(),showFragment );
//        fragmentTransaction.commit();
//    }


}
