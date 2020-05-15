package com.gmyscl.ecom.firstorder.homecatframe;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gmyscl.ecom.firstorder.CheckInternetConnection;
import com.gmyscl.ecom.firstorder.DialogsClass;
import com.gmyscl.ecom.firstorder.FakeAdaptor;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.database.StaticValues;
import com.gmyscl.ecom.firstorder.userprofile.AreaCodeAndName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.gmyscl.ecom.firstorder.database.DBquery.getQueryCategoryIcon;
import static com.gmyscl.ecom.firstorder.database.DBquery.getQuerySetFragmentData;
import static com.gmyscl.ecom.firstorder.database.DBquery.homeCategoryIconModelList;
import static com.gmyscl.ecom.firstorder.database.DBquery.homeCategoryList;
import static com.gmyscl.ecom.firstorder.database.DBquery.homeCategoryListName;
import static com.gmyscl.ecom.firstorder.database.StaticValues.areaCodeAndNameList;
import static com.gmyscl.ecom.firstorder.database.StaticValues.areaNameList;
import static com.gmyscl.ecom.firstorder.database.StaticValues.cityNameList;
import static com.gmyscl.ecom.firstorder.homecatframe.HorizontalItemViewModel.hrViewType;


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
    public static TextView locationText;


    private FakeAdaptor fakeAdaptor_2;
    private FakeAdaptor fakeAdaptor_1;
    private RecyclerView categoryIconRecycler;
    private RecyclerView homeLayoutContainerRecycler;

    private HomeFragmentAdaptor homeFragmentAdaptor;

    // Required empty public constructor
    public HomeFragment() {

    }

    //==================================== OnCreateView ===============================

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_home, container, false );

        locationText = view.findViewById( R.id.location_textView );

        if (StaticValues.userAreaName != null){
            locationText.setText( StaticValues.userCityName + ", " + StaticValues.userAreaName );
        }else{
            locationText.setText( StaticValues.userCityName );
        }

        locationText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Select Location..
                getLocationAddress(getContext());
            }
        } );


        // Assign value for horizontal product view in box shape...
        hrViewType = 0;
        // Refresh Progress...
        homeSwipeRefreshLayout = view.findViewById( R.id.home_swipe_refresh_layout );
        homeSwipeRefreshLayout.setColorSchemeColors( getContext().getResources().getColor( R.color.colorPrimary ),
                getContext().getResources().getColor( R.color.colorPrimary ),
                getContext().getResources().getColor( R.color.colorPrimary ));
        // Refresh Progress...

        // Category icon set...
        categoryIconRecycler = view.findViewById( R.id.category_icon_recycler );
        LinearLayoutManager catLinearLayoutManager = new LinearLayoutManager( view.getContext() );
        catLinearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
        categoryIconRecycler.setLayoutManager( catLinearLayoutManager );
        // Category icon set...

        // -------- Home List....

         homeLayoutContainerRecycler = view.findViewById( R.id.home_layout_container_recycler );
        LinearLayoutManager homeLinearLayoutManager = new LinearLayoutManager( view.getContext() );
        homeLinearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        homeLayoutContainerRecycler.setLayoutManager( homeLinearLayoutManager );
        // -------- Home List....

        // Fake Adaptor,... 2
         fakeAdaptor_2 = new FakeAdaptor( 2 );
         fakeAdaptor_1 = new FakeAdaptor( 1 );
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
                    getQuerySetFragmentData( getContext(), homeLayoutContainerRecycler, 0, "HOME" );
                }
            } else {
                if (homeCategoryList.get( 0 ).size() == 0){
                    if (!checkInternetCON.checkInternet( getContext() )) {
                        getQuerySetFragmentData(getContext(), homeLayoutContainerRecycler, 0, "HOME" );
                    }
                }else{
                    homeFragmentAdaptor = new HomeFragmentAdaptor( homeCategoryList.get( 0 ) );
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
                    hrViewType = 0;
                    // Access data again from database...
                    getQueryCategoryIcon( categoryIconRecycler, getContext() );

                    homeCategoryListName.add("HOME");
                    homeCategoryList.add( new ArrayList<HomeFragmentModel>() );
                    getQuerySetFragmentData(getContext(), homeLayoutContainerRecycler, 0, "HOME");

                }

            }
        });
        // ----= Refresh Layout...

        return view;
    }
    //==================================== End OnCreateView ===============================


    @Override
    public void onResume() {
        super.onResume();
        hrViewType = 0;
    }


    // ----------  showToast
    private void showToast(String msg){
        Toast.makeText( getContext(),msg, Toast.LENGTH_SHORT ).show();
    }

    ////////////////////////////////////////////////////
    // ---------------- Location Address................
    private String tempCityName = null;
    private String tempAreaCode = null;
    private String tempAreaName = null;
    private ArrayAdapter<String> areaAdapter;
    private ArrayAdapter<String> cityAdapter;
    public void getLocationAddress(Context context){
        final Dialog dialog = new DialogsClass().progressDialog( context );

        /// Sample Button click...
        final Dialog quantityDialog = new Dialog( context );
        quantityDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        quantityDialog.setContentView( R.layout.dialog_select_location );
        quantityDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        quantityDialog.setCancelable( false );
        Button okBtn = quantityDialog.findViewById( R.id.location_ok_btn );
        final Button CancelBtn = quantityDialog.findViewById( R.id.location_cancel_btn );
        final Spinner citySpinner = quantityDialog.findViewById( R.id.city_spinner );
        final Spinner areaSpinner = quantityDialog.findViewById( R.id.area_spinner );

        // Set Area Code spinner...
        areaAdapter = new ArrayAdapter <String>(context,
                android.R.layout.simple_spinner_item, areaNameList);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaAdapter);
        areaSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if ( position != 0){
                    tempAreaName = areaNameList.get( position );
                    tempAreaCode = areaCodeAndNameList.get( position ).getAreaCode();
                }
                else
                    tempAreaCode = null;
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//                if (cityList.size() == 1 || ! isUploadImages)
//                    showToast( "Upload Images first..!" );
            }
        } );


        // Set city code Spinner
        cityAdapter = new ArrayAdapter <String>(context,
                android.R.layout.simple_spinner_item, StaticValues.cityNameList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if ( position != 0){
                    tempCityName = StaticValues.cityNameList.get( position );
                    dialog.show();
                    getAreaListQuery(dialog, areaAdapter, tempCityName);

//                    if ( ( areaNameList.size() == 0) || (tempCityName != StaticValues.userCityName ) ){
//                        dialog.show();
//                        getAreaListQuery(dialog, areaAdapter, tempCityName);
//                    }
                }
                else
                    tempCityName = null;
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//                if (cityList.size() == 1 || ! isUploadImages)
//                    showToast( "Upload Images first..!" );
            }
        } );

        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tempCityName != null && tempAreaCode != null && tempAreaCode != null){
                    // Accept...
                    StaticValues.userCityName = tempCityName;
                    StaticValues.userAreaCode = tempAreaCode;
                    StaticValues.tempProductAreaCode = tempAreaCode;
                    StaticValues.userAreaName = tempAreaName;
                    // : Refresh... the layout.!
                    quantityDialog.dismiss();

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
                        getQuerySetFragmentData(getContext(), homeLayoutContainerRecycler, 0, "HOME");

                    }

                }else if (tempCityName == null){
                    showToast( "Please select City.!" );
                }else{
                    showToast( "Please Select Your Area..!" );
                }

            }
        } );
        CancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityDialog.dismiss();
            }
        } );
        quantityDialog.show();

        // Request query to get city List...
        if (cityNameList.size() == 0){
            dialog.show();
            getCityNameListQuery( dialog, cityAdapter );
        }

    }

    public void getCityNameListQuery(final Dialog dialog, @Nullable final ArrayAdapter<String> arrayAdapter) {

        // City Request..
        StaticValues.cityNameList.clear();
        StaticValues.cityNameList.add( "Select City" );

        FirebaseFirestore.getInstance().collection( "ADMIN_PER" ).
                orderBy( "index" ).get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        StaticValues.cityNameList.add( documentSnapshot.get( "city" ).toString() );
                        if (arrayAdapter != null){
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                    //showToast( task.getException().getMessage() );
                }
            }
        } );
    }

    public void getAreaListQuery(final Dialog dialog, @Nullable final ArrayAdapter<String> arrayAdapter, String cityName ){
//        // Area Request...
        areaNameList.clear();
        areaNameList.add( "Select Area" );
        areaCodeAndNameList.clear();
        areaCodeAndNameList.add( new AreaCodeAndName( " ", "Select Area" ) );
        FirebaseFirestore.getInstance().collection( "ADMIN_PER" ).document(cityName.toUpperCase())
                .collection( "SUB_LOCATION" ).orderBy( "location_id" ).get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                areaNameList.add( documentSnapshot.get( "location_name" ).toString() );
                                areaCodeAndNameList.add( new AreaCodeAndName( documentSnapshot.get( "location_id" ).toString()
                                        , documentSnapshot.get( "location_name" ).toString()  ) );

                                if (arrayAdapter != null){
                                    arrayAdapter.notifyDataSetChanged();
                                }

                            }
                            dialog.dismiss();

                        }else{
                            dialog.dismiss();

                        }
                    }
                } );

    }





}


