package com.example.shailendra.quickshop.mywishlist;


import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shailendra.quickshop.DialogsClass;
import com.example.shailendra.quickshop.MainActivity;
import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.database.DBquery;
import com.example.shailendra.quickshop.productdetails.ProductDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyWishList extends Fragment {

    public static MyWishListAdaptor myWishListAdaptor;
    public static RecyclerView myWishListRecycler;
    public MyWishList() {
        // Required empty public constructor
    }
    DialogsClass dialogsClass  = new DialogsClass(  );
    // Don't have any WishList item...
    public static ConstraintLayout noItemInWishListLayout;
    private Button noItemInWishListBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_my_wish_list, container, false );

        // ---- Progress Dialog...
        Dialog dialog = dialogsClass.progressDialog( getContext() );
        dialog.show();
        // ---- Progress Dialog...

        // initialization...
        myWishListRecycler = view.findViewById( R.id.my_wish_list_recycler );
        // No WishList Item Btn
        noItemInWishListLayout = view.findViewById( R.id.my_wish_list_no_item_layout );
        noItemInWishListBtn = view.findViewById( R.id.no_item_in_wishList_Btn );
        if (DBquery.myWishList.size() == 0){
            myWishListRecycler.setVisibility( View.GONE );
            noItemInWishListLayout.setVisibility( View.VISIBLE );
        }else{
            noItemInWishListLayout.setVisibility( View.GONE );
            myWishListRecycler.setVisibility( View.VISIBLE );
        }
        // click Listener On noItemInWishListBtn..
        noItemInWishListBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().startActivity( new Intent(getActivity(), MainActivity.class ) );
                MainActivity.isFragmentIsMyCart = false;
            }
        } );

        if (DBquery.wishListModelList.size() == 0){
            DBquery.myWishList.clear();
            DBquery.wishListQuery( getContext(), dialog, true );
        }else{
            dialog.dismiss();
        }
        // Set Wish List Layout...
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( view.getContext() );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        myWishListRecycler.setLayoutManager( linearLayoutManager );
        myWishListAdaptor = new MyWishListAdaptor( DBquery.wishListModelList, true );
        myWishListRecycler.setAdapter( myWishListAdaptor );
        myWishListAdaptor.notifyDataSetChanged();

        return view;
    }

}
