package com.example.shailendra.quickshop.retailerdata.fragmentclass;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ScrollingView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.retailerdata.RetRecyclerListAdaptor;
import com.example.shailendra.quickshop.retailerdata.RetRecyclerListModel;
import com.example.shailendra.quickshop.retailerdata.Ret_HomePageAdaptor;
import com.example.shailendra.quickshop.retailerdata.Ret_HomePageModel;
import com.example.shailendra.quickshop.retailerdata.X_OfferRecyclerViewAdaptor;
import com.example.shailendra.quickshop.retailerdata.X_OfferRecyclerViewModel;
import com.example.shailendra.quickshop.retailerdata.X_ret_grid_adaptor;

import java.util.ArrayList;
import java.util.List;

public class RetHomeFragment extends Fragment {

    public RetHomeFragment(){

    }

    //---------- offer recycler View in horizontal --------------
    private TextView offerTitle;
    private Button offerViewAllBtn;
    private RecyclerView offerRecyclerView;
    //---------- offer recycler View in horizontal --------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_ret_home,container,false );

        //---------- offer recycler View in horizontal --------------
        offerTitle = view.findViewById( R.id.offer_title );
        offerViewAllBtn = view.findViewById( R.id.offer_view_all_btn );
        offerRecyclerView = view.findViewById( R.id.offer_recycler_view );

        ArrayList<X_OfferRecyclerViewModel> x_offerRecyclerViewModelList = new ArrayList <>(  );

        x_offerRecyclerViewModelList.add( new X_OfferRecyclerViewModel( R.drawable.pic_a,"Rs. 45/-","Mobile" ) );
        x_offerRecyclerViewModelList.add( new X_OfferRecyclerViewModel( R.drawable.pic_a,"Rs. 45/-","Mobile" ) );
        x_offerRecyclerViewModelList.add( new X_OfferRecyclerViewModel( R.drawable.pic_b,"Rs. 45/-","Mobile" ) );
        x_offerRecyclerViewModelList.add( new X_OfferRecyclerViewModel( R.drawable.pic_c,"Rs. 45/-","Mobile" ) );
        x_offerRecyclerViewModelList.add( new X_OfferRecyclerViewModel( R.drawable.pic_d,"Rs. 45/-","Mobile" ) );

        X_OfferRecyclerViewAdaptor x_offerRecyclerViewAdaptor = new X_OfferRecyclerViewAdaptor( x_offerRecyclerViewModelList );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.HORIZONTAL );
        offerRecyclerView.setLayoutManager( linearLayoutManager );
        offerRecyclerView.setAdapter( x_offerRecyclerViewAdaptor );

        x_offerRecyclerViewAdaptor.notifyDataSetChanged();

        //---------- offer recycler View in horizontal --------------
        //---------- ret GridView Code ---------------- --------------

        ArrayList<X_OfferRecyclerViewModel> x_ret_grid_List = new ArrayList <>(  );

        TextView retGridTitle = view.findViewById( R.id.ret_grid_title );
        TextView retGridViewAllTxt = view.findViewById( R.id.ret_grid_view_all_txt );
        GridView retGridLayoutContent = view.findViewById( R.id.ret_grid_layout_content );

        retGridLayoutContent.setAdapter( new X_ret_grid_adaptor( x_offerRecyclerViewModelList ) );

        //---------- ret GridView Code ---------------- --------------
        //---------- ret Recycler List layout Code ---------------- --------------
        TextView listHeading = view.findViewById( R.id.ret_recycler_list_heading );
        TextView listViewAllText = view.findViewById( R.id.ret_recycler_list_view_all_txt );
        RecyclerView listRecyclerListContent = view.findViewById( R.id.ret_recycler_list_content );

        ArrayList<RetRecyclerListModel> recyclerListModelArrayList = new ArrayList <>(  );

        recyclerListModelArrayList.add( new RetRecyclerListModel( "Item One","mobile", R.drawable.pic_b ) );
        recyclerListModelArrayList.add( new RetRecyclerListModel( "Item One","mobile", R.drawable.pic_b ) );
        recyclerListModelArrayList.add( new RetRecyclerListModel( "Item One","mobile", R.drawable.pic_b ) );

        RetRecyclerListAdaptor retRecyclerListAdaptor = new RetRecyclerListAdaptor( recyclerListModelArrayList );
        LinearLayoutManager linearLayoutManagerRecyclerList = new LinearLayoutManager( getContext() );

        listRecyclerListContent.setLayoutManager( linearLayoutManagerRecyclerList );
        listRecyclerListContent.setAdapter( retRecyclerListAdaptor );

        //---------- ret Recycler List layout Code ---------------- --------------

        ////////////////////////////////////////////////////////////////////////////////////////////

        List<Ret_HomePageModel> ret_homePageModelList = new ArrayList <>(  );

        ret_homePageModelList.add( new Ret_HomePageModel( 0,"Deal Wacky",x_offerRecyclerViewModelList ) );
        ret_homePageModelList.add( new Ret_HomePageModel( 0,"Deal of The Day",x_offerRecyclerViewModelList ) );

        ret_homePageModelList.add( new Ret_HomePageModel( 0,"Tarun Wacky",x_offerRecyclerViewModelList ) );
        ret_homePageModelList.add( new Ret_HomePageModel( 1,"Tarun Wacky",x_offerRecyclerViewModelList ) );

        ret_homePageModelList.add( new Ret_HomePageModel( 0,"Tarun Wacky",x_offerRecyclerViewModelList ) );
        ret_homePageModelList.add( new Ret_HomePageModel( 1,"Tarun Wacky",x_offerRecyclerViewModelList ) );

        ret_homePageModelList.add( new Ret_HomePageModel( 0,"Deal Wacky",x_offerRecyclerViewModelList ) );
        ret_homePageModelList.add( new Ret_HomePageModel( 2,recyclerListModelArrayList,"List Wacky Title" ) );

        ret_homePageModelList.add( new Ret_HomePageModel( 0,"Tarun Wacky",x_offerRecyclerViewModelList ) );
        ret_homePageModelList.add( new Ret_HomePageModel( 2,recyclerListModelArrayList,"s Wacky Title" ) );
        ret_homePageModelList.add( new Ret_HomePageModel( 1,"Tarun Wacky",x_offerRecyclerViewModelList ) );


        RecyclerView retHomeTesting = view.findViewById( R.id.ret_home_testing );
        LinearLayoutManager retHomeTestLLM = new LinearLayoutManager( getContext() );
        retHomeTestLLM.setOrientation( LinearLayoutManager.VERTICAL );
        retHomeTesting.setVerticalScrollBarEnabled( true );
        retHomeTesting.smoothScrollToPosition( ret_homePageModelList.size() );
        retHomeTesting.setLayoutManager( retHomeTestLLM );

        Ret_HomePageAdaptor homePageAdaptor = new Ret_HomePageAdaptor( ret_homePageModelList );
        retHomeTesting.setAdapter( homePageAdaptor );
        homePageAdaptor.notifyDataSetChanged();

        ////////////////////////////////////////////////////////////////////////////////////////////

        return view;
    }
}
