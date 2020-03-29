package com.example.shailendra.quickshop.mycart;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.shailendra.quickshop.DialogsClass;
import com.example.shailendra.quickshop.MainActivity;
import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.database.DBquery;
import com.example.shailendra.quickshop.database.StaticValues;
import com.example.shailendra.quickshop.userprofile.MyAddressesActivity;

import static com.example.shailendra.quickshop.database.StaticValues.SELECT_ADDRESS;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCartFragment extends Fragment {

    DialogsClass dialogsClass = new DialogsClass( getContext() );
    // my cart...
    public static ConstraintLayout myCartConstLayout;

    private Button myCartContinueBtn;
    public static TextView myCartTotalAmounts2;

    private RecyclerView myCartItemRecyclerView;
    public static  MyCartAdaptor myCartAdaptor;

    public static boolean isCartEmpty = true;

    // Don't have any cart item...
    public static ConstraintLayout dontHaveCartConstLayout;
    private Button dontHaveCartBtn;

    public MyCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_my_cart, container, false );

        dontHaveCartConstLayout = view.findViewById( R.id.my_cart_dont_have_cart_ConstLayout );
        myCartConstLayout = view.findViewById( R.id.my_cart_ConstLayout );

        // Don't have any cart item...
        dontHaveCartBtn = view.findViewById( R.id.my_cart_dont_have_any_cartBtn );
        dontHaveCartBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().startActivity( new Intent(getActivity(), MainActivity.class ) );
                MainActivity.isFragmentIsMyCart = false;
            }
        } );
        // Don't have any cart item...

        // My Cart...
        myCartContinueBtn = view.findViewById( R.id.my_cart_continue_btn);
        myCartTotalAmounts2 = view.findViewById( R.id.my_cart_total_amounts2);
        myCartItemRecyclerView = view.findViewById( R.id.my_cart_recyclerView );

        // Set value of cart variables...
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( view.getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        myCartItemRecyclerView.setLayoutManager( linearLayoutManager );
        myCartAdaptor = new MyCartAdaptor( DBquery.myCartItemModelList );
        myCartItemRecyclerView.setAdapter( myCartAdaptor );

        myCartAdaptor.notifyDataSetChanged();

        myCartContinueBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myAddressIntent = new Intent(view.getContext(), MyAddressesActivity.class );
                myAddressIntent.putExtra( "MODE", SELECT_ADDRESS );
                startActivity( myAddressIntent );
                StaticValues.BUY_FROM_VALUE = StaticValues.BUY_FROM_CART;
            }
        } );

        // Check first if any item in cart..
        if ( isCartEmpty ){
            myCartConstLayout.setVisibility( View.GONE );
            dontHaveCartConstLayout.setVisibility( View.VISIBLE );
        }else {
            myCartConstLayout.setVisibility( View.VISIBLE );
            dontHaveCartConstLayout.setVisibility( View.GONE );
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (DBquery.myCartCheckList.size() > 0){
            isCartEmpty = false;
        }

        Dialog dialog = dialogsClass.progressDialog( getContext() );
        // ---- Progress Dialog...
        dialog.show();
        // ---- Progress Dialog...
        if (DBquery.myCartItemModelList.size() == 0){
            DBquery.myCartCheckList.clear();
            DBquery.cartListQuery( getContext(), true, dialog , -1);
        }else{
            isCartEmpty = false;
            dialog.dismiss();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check first if any item in cart..
        if ( isCartEmpty ){
            myCartConstLayout.setVisibility( View.GONE );
            dontHaveCartConstLayout.setVisibility( View.VISIBLE );
        }else {
            myCartConstLayout.setVisibility( View.VISIBLE );
            dontHaveCartConstLayout.setVisibility( View.GONE );
        }
    }


}
