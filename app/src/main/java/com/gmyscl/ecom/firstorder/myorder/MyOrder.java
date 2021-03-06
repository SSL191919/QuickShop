package com.gmyscl.ecom.firstorder.myorder;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmyscl.ecom.firstorder.CheckInternetConnection;
import com.gmyscl.ecom.firstorder.DialogsClass;
import com.gmyscl.ecom.firstorder.MainActivity;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.database.DBquery;

public class MyOrder extends Fragment {

    public static MyOrderAdaptor myOrderAdaptor;
    DialogsClass dialogsClass = new DialogsClass( getContext() );

    public static RecyclerView myOrderRecycler;
    public MyOrder() {
        // Required empty public constructor
    }
    // Don't have any Order item...
    public static ConstraintLayout noOrderHistoryLayout;
    private Button noOrderGoToShopBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_my_order, container, false );
        myOrderRecycler = view.findViewById( R.id.my_order_recycler );

        // No Order Btn
        noOrderHistoryLayout = view.findViewById( R.id.my_order_no_order_layout );
        noOrderGoToShopBtn = view.findViewById( R.id.no_order_history_btn );
        // Set Visibility Layout...
        if (DBquery.myOrderList.size() == 0){
            myOrderRecycler.setVisibility( View.GONE );
            noOrderHistoryLayout.setVisibility( View.VISIBLE );
        }else{
            noOrderHistoryLayout.setVisibility( View.GONE );
            myOrderRecycler.setVisibility( View.VISIBLE );
        }
        // click Listener On NoOrderBtn..
        noOrderGoToShopBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().startActivity( new Intent(getActivity(), MainActivity.class ) );
                MainActivity.isFragmentIsMyCart = false;
            }
        } );

        // Set Order Content...
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( view.getContext() );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        myOrderAdaptor = new MyOrderAdaptor( DBquery.myOrderModelArrayList );
        myOrderRecycler.setLayoutManager( linearLayoutManager );
        myOrderRecycler.setAdapter( myOrderAdaptor );
        myOrderAdaptor.notifyDataSetChanged();

        return view;
    }
    // OnCreateView End
    // OnCreate...


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Dialog dialog = dialogsClass.progressDialog( getContext() );
        // ---- Progress Dialog...
        dialog.show();

        if (DBquery.myOrderModelArrayList.size() == 0){
            DBquery.myOrderList.clear();
            DBquery.orderListQuery( getContext(), dialog , true);
        }else{
            dialog.dismiss();
        }

    }

    private boolean w_isInternetConnect() {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        return !checkInternetCON.checkInternet( getActivity() );

    }


}

