package com.example.shailendra.quickshop.homecatframe;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shailendra.quickshop.CheckInternetConnection;
import com.example.shailendra.quickshop.FakeAdaptor;
import com.example.shailendra.quickshop.R;

import java.util.ArrayList;
import java.util.List;
import static com.example.shailendra.quickshop.database.DBquery.getQueryCategoryIcon;
import static com.example.shailendra.quickshop.database.DBquery.getQuerySetFragmentData;
import static com.example.shailendra.quickshop.database.DBquery.homeCategoryIconModelList;
import static com.example.shailendra.quickshop.database.DBquery.homeCategoryList;
import static com.example.shailendra.quickshop.database.DBquery.homeCategoryListName;
import static com.example.shailendra.quickshop.homecatframe.HorizontalItemViewModel.hrViewType;


public class HomeFragment extends Fragment {

    // Getting Reference of CheckInternetConnection
    CheckInternetConnection checkInternetCON = new CheckInternetConnection();

    //------ View Pager for Banner Slider...
    public static List<BannerSliderModel> bannerSliderModelList;
    //------ View Pager for Banner Slider...
    // ------- Horizontal Item View ..----------------
    public static List<HorizontalItemViewModel> horizontalItemViewModelList;
    public static List<HorizontalItemViewModel> gridLayoutViewList;
    // ------- Horizontal Item View ..----------------
    // ========== Home Layout Container Recycler --------------------
    // ========== Home Layout Container Recycler --------------------
//    private

    public static SwipeRefreshLayout homeSwipeRefreshLayout;

    // Required empty public constructor
    public HomeFragment() {

    }

    //==================================== OnCreateView ===============================

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_home, container, false );

        // Assign value for horizontal product view in box shape...
        hrViewType = 0;
        // Refresh Progress...
        homeSwipeRefreshLayout = view.findViewById( R.id.home_swipe_refresh_layout );
        homeSwipeRefreshLayout.setColorSchemeColors( getContext().getResources().getColor( R.color.colorPrimary ),
                getContext().getResources().getColor( R.color.colorPrimary ),
                getContext().getResources().getColor( R.color.colorPrimary ));
        // Refresh Progress...

        // Category icon set...
        final RecyclerView categoryIconRecycler = view.findViewById( R.id.category_icon_recycler );
        LinearLayoutManager catLinearLayoutManager = new LinearLayoutManager( view.getContext() );
        catLinearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
        categoryIconRecycler.setLayoutManager( catLinearLayoutManager );
        // Category icon set...

        // -------- Home List....

        final RecyclerView homeLayoutContainerRecycler = view.findViewById( R.id.home_layout_container_recycler );
        LinearLayoutManager homeLinearLayoutManager = new LinearLayoutManager( view.getContext() );
        homeLinearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        homeLayoutContainerRecycler.setLayoutManager( homeLinearLayoutManager );
        // -------- Home List....

        // Fake Adaptor,... 2
        final FakeAdaptor fakeAdaptor_2 = new FakeAdaptor( 2 );
        final FakeAdaptor fakeAdaptor_1 = new FakeAdaptor( 1 );
        categoryIconRecycler.setAdapter( fakeAdaptor_2 );
        homeLayoutContainerRecycler.setAdapter( fakeAdaptor_1 );
        // Fake Adaptor,... 2
//        if (!checkInternetCON.checkInternet( getContext() )) {
            // check whether our list is already assigned ...!!
            if (homeCategoryIconModelList.size() == 0) {
                if (!checkInternetCON.checkInternet( getContext() )) {
                    getQueryCategoryIcon( categoryIconRecycler, getContext() );
                }
            } else {
                final HomeCategoryIconAdaptor homeCategoryIconAdaptor = new HomeCategoryIconAdaptor( homeCategoryIconModelList );
                categoryIconRecycler.setAdapter( homeCategoryIconAdaptor );
                homeCategoryIconAdaptor.notifyDataSetChanged();
            }
            // Category icon set...
            // ========== Home Layout Container Recycler --------------------
            if (homeCategoryList.size() == 0) {
                if (!checkInternetCON.checkInternet( getContext() )) {
                    getQuerySetFragmentData( homeLayoutContainerRecycler, 0, "HOME" );
                }
            } else {
                if (homeCategoryList.get( 0 ).size() == 0){
                    if (!checkInternetCON.checkInternet( getContext() )) {
                        getQuerySetFragmentData( homeLayoutContainerRecycler, 0, "HOME" );
                    }
                }else{
                    final HomeFragmentAdaptor homeFragmentAdaptor = new HomeFragmentAdaptor( homeCategoryList.get( 0 ) );
                    homeLayoutContainerRecycler.setAdapter( homeFragmentAdaptor );
                    homeFragmentAdaptor.notifyDataSetChanged();
                }

            }
            // ========== Home Layout Container Recycler --------------------
//        }

        // ----= Refresh Layout... check is Null.?
        if (homeSwipeRefreshLayout != null)

        homeSwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                homeSwipeRefreshLayout.setRefreshing( true );

                homeCategoryList.clear();
                homeCategoryListName.clear();
                homeCategoryIconModelList.clear();
                categoryIconRecycler.setAdapter( fakeAdaptor_2 );
                homeLayoutContainerRecycler.setAdapter( fakeAdaptor_1 );

                if (!checkInternetCON.checkInternet( getContext() )){
                    // Access data again from database...
                    getQueryCategoryIcon( categoryIconRecycler, getContext() );

                    homeCategoryListName.add("HOME");
                    homeCategoryList.add( new ArrayList<HomeFragmentModel>() );
                    getQuerySetFragmentData( homeLayoutContainerRecycler, 0, "HOME");

                }

            }
        });
        // ----= Refresh Layout...

        return view;
    }
    //==================================== End OnCreateView ===============================


    // ----------  showToast
    private void showToast(String msg){
        Toast.makeText( getContext(),msg, Toast.LENGTH_SHORT ).show();
    }


}


