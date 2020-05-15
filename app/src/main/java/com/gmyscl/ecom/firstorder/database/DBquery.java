package com.gmyscl.ecom.firstorder.database;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gmyscl.ecom.firstorder.CheckInternetConnection;
import com.gmyscl.ecom.firstorder.MainActivity;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.activityforitems.ViewAllActivity;
import com.gmyscl.ecom.firstorder.categoryItemClass.CategoriesItemsViewActivity;
import com.gmyscl.ecom.firstorder.homecatframe.BannerSliderModel;
import com.gmyscl.ecom.firstorder.homecatframe.HomeCategoryIconAdaptor;
import com.gmyscl.ecom.firstorder.homecatframe.HomeCategoryIconModel;
import com.gmyscl.ecom.firstorder.homecatframe.HomeFragment;
import com.gmyscl.ecom.firstorder.homecatframe.HomeFragmentAdaptor;
import com.gmyscl.ecom.firstorder.homecatframe.HomeFragmentModel;
import com.gmyscl.ecom.firstorder.mycart.MyCartCheckModel;
import com.gmyscl.ecom.firstorder.mycart.MyCartFragment;
import com.gmyscl.ecom.firstorder.mycart.MyCartItemModel;
import com.gmyscl.ecom.firstorder.myorder.MyOrder;
import com.gmyscl.ecom.firstorder.myorder.MyOrderModel;
import com.gmyscl.ecom.firstorder.mywishlist.MyWishList;
import com.gmyscl.ecom.firstorder.mywishlist.MyWishListModel;
import com.gmyscl.ecom.firstorder.notifications.NotificationActivity;
import com.gmyscl.ecom.firstorder.notifications.NotificationModel;
import com.gmyscl.ecom.firstorder.productdetails.ProductDetails;
import com.gmyscl.ecom.firstorder.userprofile.AddAddressActivity;
import com.gmyscl.ecom.firstorder.userprofile.MyAccountFragment;
import com.gmyscl.ecom.firstorder.userprofile.MyAddressRecyclerModel;
import com.gmyscl.ecom.firstorder.userprofile.MyAddressesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gmyscl.ecom.firstorder.database.StaticValues.BANNER_AD_LAYOUT_CONTAINER;
import static com.gmyscl.ecom.firstorder.database.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static com.gmyscl.ecom.firstorder.database.StaticValues.CATEGORIES_ITEMS_VIEW_ACTIVITY;
import static com.gmyscl.ecom.firstorder.database.StaticValues.GRID_ITEM_LAYOUT_CONTAINER;
import static com.gmyscl.ecom.firstorder.database.StaticValues.HORIZONTAL_ITEM_LAYOUT_CONTAINER;
import static com.gmyscl.ecom.firstorder.database.StaticValues.MAIN_ACTIVITY;
import static com.gmyscl.ecom.firstorder.database.StaticValues.NOTIFY_ORDER_UPDATE;
import static com.gmyscl.ecom.firstorder.database.StaticValues.PRODUCT_DETAILS_ACTIVITY;
import static com.gmyscl.ecom.firstorder.database.StaticValues.REMOVE_ONE_ITEM;
import static com.gmyscl.ecom.firstorder.database.StaticValues.STRIP_AD_LAYOUT_CONTAINER;
import static com.gmyscl.ecom.firstorder.database.StaticValues.VIEW_ALL_ACTIVITY;
import static com.gmyscl.ecom.firstorder.database.StaticValues.userAreaCode;
import static com.gmyscl.ecom.firstorder.database.StaticValues.userCityName;
import static com.gmyscl.ecom.firstorder.homecatframe.HomeFragment.bannerSliderModelList;
import static com.gmyscl.ecom.firstorder.homecatframe.HomeFragment.gridLayoutViewList;
import static com.gmyscl.ecom.firstorder.homecatframe.HomeFragment.horizontalItemViewModelList;

public class DBquery {

    public static final int QUERY_TO_ADD_ADDRESS = 20;
    public static final int QUERY_TO_REMOVE_ADDRESS = 21;
    public static final int QUERY_TO_UPDATE_ADDRESS = 22;
    // Private Temp Variables...
    private static int tempIndex = 0;
    private static String dbTempListItem;
    private static String dbTempListItem2;

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // get Current User Reference ...
    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    // Home screen Category ...
    public static List <HomeCategoryIconModel> homeCategoryIconModelList = new ArrayList <>();
    // Home Fragment model List...
//    public static List<HomeFragmentModel> homeFragmentModelList = new ArrayList <>();

    public static List <String> homeCategoryListName = new ArrayList <>();
    public static List <List <HomeFragmentModel>> homeCategoryList = new ArrayList <>();
    // wish List...
    public static List <String> myWishList = new ArrayList <>();
    public static List <MyWishListModel> wishListModelList = new ArrayList <>();
    // wish List...
    public static List <MyCartCheckModel> myCartCheckList = new ArrayList <>();
    public static List <MyCartItemModel> myCartItemModelList = new ArrayList <>();
    // Order List...
    public static List <String> myOrderList = new ArrayList <>();
    public static List <MyOrderModel> myOrderModelArrayList = new ArrayList <>();
    // Add new address...
    public static List <MyAddressRecyclerModel> myAddressRecyclerModelList = new ArrayList <>();

    // Notification List...
    public static List<NotificationModel> notificationModelList = new ArrayList <>();
    private static ListenerRegistration notificationLR;

    // User Imformation List...
    public static List<String> userInformationList = new ArrayList <>();

    // Query to Load category Icons....
    public static void getQueryCategoryIcon(final RecyclerView categoryIconRecycler, Context context) {

        if ( userCityName != null && userAreaCode != null){
            StaticValues.getFirebaseDocumentReference( userCityName, userAreaCode )
            .collection( "CATEGORIES" ).orderBy( "index" ).get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task <QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            homeCategoryIconModelList.add( new HomeCategoryIconModel( documentSnapshot.get( "icon" ).toString(),
                                    documentSnapshot.get( "categoryName" ).toString() ) );
                            // Below adding category Blank Index...
                            homeCategoryListName.add( documentSnapshot.get( "categoryName" ).toString() );
                            homeCategoryList.add( new ArrayList <HomeFragmentModel>() );
                        }
                        HomeCategoryIconAdaptor homeCategoryIconAdaptor = new HomeCategoryIconAdaptor( homeCategoryIconModelList );
                        // $ Changes...
                        if (categoryIconRecycler != null){
                            categoryIconRecycler.setAdapter( homeCategoryIconAdaptor );
                            homeCategoryIconAdaptor.notifyDataSetChanged();
                        }

                    } else {
                        //showToast( task.getException().getMessage() );
                    }
                }
            } );
        }else{
            if (userCityName == null){
                showToast( context, "Please select City.!" );
            }
            if (userAreaCode == null){
                showToast( context, "Please select City and Area Name.!" );
            }

        }

    }

    // Query to Load Fragment Data like homepage items etc...
    public static void getQuerySetFragmentData(final Context context, final RecyclerView homeLayoutContainerRecycler, final int index, String catName) {

        if ( userCityName != null && userAreaCode != null){

            StaticValues.getFirebaseDocumentReference( userCityName, userAreaCode )
            .collection( "CATEGORIES" ).document( catName.toUpperCase() )
                    .collection( "LAYOUTS" ).orderBy( "index" ).get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task <QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (homeCategoryList.size() == 0){
                            homeCategoryList.add( new ArrayList <HomeFragmentModel>() );
                        }
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                            showToast( context, "City : " + userCityName );
                            if ((long) documentSnapshot.get( "view_type" ) == BANNER_SLIDER_LAYOUT_CONTAINER) {
                                // TODO : add banners
//                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                                bannerSliderModelList = new ArrayList <>();
                                long no_of_banners = (long) documentSnapshot.get( "no_of_banners" );
                                for (long i = 1; i < no_of_banners + 1; i++) {
                                    // access the banners from database...
                                    bannerSliderModelList.add( new BannerSliderModel( documentSnapshot.get( "banner_" + i ).toString(),
                                            documentSnapshot.get( "banner_" + i + "_bg" ).toString() ) );
                                }
                                // add the banners list in the home recycler list...
                                if (bannerSliderModelList.size() >= 2){
                                    homeCategoryList.get( index ).add( new HomeFragmentModel( BANNER_SLIDER_LAYOUT_CONTAINER, bannerSliderModelList ) );
                                }

                            } else
                            if ((long) documentSnapshot.get( "view_type" ) == STRIP_AD_LAYOUT_CONTAINER) {
                                // TODO : for strip ad
//                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                                homeCategoryList.get( index ).add( new HomeFragmentModel( STRIP_AD_LAYOUT_CONTAINER,
                                        documentSnapshot.get( "strip_ad" ).toString(), documentSnapshot.get( "strip_bg" ).toString() ) );

                            } else
                            if ((long) documentSnapshot.get( "view_type" ) == HORIZONTAL_ITEM_LAYOUT_CONTAINER) {
                                // TODO : for horizontal products
//                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                                horizontalItemViewModelList = new ArrayList <>();
                                List<String> hrAndGridProductIdList = new ArrayList <>();
                                long no_of_products = (long) documentSnapshot.get( "no_of_products" );
                                for (long i = 1; i < no_of_products + 1; i++) {
                                    // Load Product Data List After set Adaptor... on View Time...
                                    // Below we load only product Id...
                                    hrAndGridProductIdList.add( documentSnapshot.get( "product_id_" + i ).toString() );
                                }
                                // add list in home fragment model
                                //  TODO : To User
                                if (hrAndGridProductIdList.size()>=3){
                                    homeCategoryList.get( index ).add( new HomeFragmentModel( HORIZONTAL_ITEM_LAYOUT_CONTAINER, hrAndGridProductIdList,
                                            horizontalItemViewModelList, documentSnapshot.get( "layout_title" ).toString() ) );
                                }
//                            // TODO : To Admin
//                            commonCatList.get( index ).add( new CommonCatModel( HORIZONTAL_ITEM_LAYOUT_CONTAINER, layout_id
//                                    , documentSnapshot.get( "layout_title" ).toString()
//                                    , hrAndGridProductIdList, hrAndGridProductList ) );

//                            {
//                                //  : for horizontal products
//                                horizontalItemViewModelList = new ArrayList <>();
//
//                                long no_of_products = (long) documentSnapshot.get( "no_of_products" );
//                                for (long i = 1; i < no_of_products + 1; i++) {
//                                    // access the banners from database...
//                                    horizontalItemViewModelList.add( new HorizontalItemViewModel( 0,
//                                            documentSnapshot.get( "product_" + i + "_id").toString(),
//                                            documentSnapshot.get( "product_" + i ).toString(),
//                                            documentSnapshot.get( "product_" + i + "_name" ).toString(),
//                                            documentSnapshot.get( "product_" + i + "_price" ).toString(),
//                                            documentSnapshot.get( "product_" + i + "_cut_price" ).toString(),
//                                            (Boolean) documentSnapshot.get( "product_" + i + "_cod" ),
//                                            (long) documentSnapshot.get( "product_" + i + "_stock" ) ) );
//
//                                }
//
//                            }

                            } else
                            if ((long) documentSnapshot.get( "view_type" ) == GRID_ITEM_LAYOUT_CONTAINER) {
                                // TODO : for grid products
//                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                                gridLayoutViewList = new ArrayList <>();
                                List<String> hrAndGridProductIdList = new ArrayList <>();
                                long no_of_products = (long) documentSnapshot.get( "no_of_products" );
                                for (long i = 1; i < no_of_products + 1; i++) {
                                    // access the banners from database...
                                    hrAndGridProductIdList.add( documentSnapshot.get( "product_id_" + i ).toString() );
                                }
                                // add list in home fragment model
                                // TODO : To User
                                if (hrAndGridProductIdList.size()>=4){
                                    homeCategoryList.get( index ).add( new HomeFragmentModel( GRID_ITEM_LAYOUT_CONTAINER, hrAndGridProductIdList,
                                            gridLayoutViewList, documentSnapshot.get( "layout_title" ).toString() ) );
                                }
//                            // TODO : To Admin
//                            commonCatList.get( index ).add( new CommonCatModel( GRID_ITEM_LAYOUT_CONTAINER, layout_id
//                                    , documentSnapshot.get( "layout_title" ).toString()
//                                    , hrAndGridProductIdList, hrAndGridProductList ) );


                            } else
                            if ((long) documentSnapshot.get( "view_type" ) == BANNER_AD_LAYOUT_CONTAINER) {
                                // TODO : for Banner ad
//                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                                homeCategoryList.get( index ).add( new HomeFragmentModel( BANNER_AD_LAYOUT_CONTAINER,
                                    documentSnapshot.get( "banner_ad" ).toString(), documentSnapshot.get( "banner_bg" ).toString() ) );

                            }

//                            if (homeFragmentAdaptor != null){
//                                homeFragmentAdaptor.notifyDataSetChanged();
//                            }
                        }

                        HomeFragmentAdaptor homeFragmentAdaptor = new HomeFragmentAdaptor( homeCategoryList.get( index ) );
                        // $ Changes...
                        if (homeLayoutContainerRecycler != null){
                            homeLayoutContainerRecycler.setAdapter( homeFragmentAdaptor );
                            homeFragmentAdaptor.notifyDataSetChanged();
                        }
                        if (HomeFragment.homeSwipeRefreshLayout != null){
                            HomeFragment.homeSwipeRefreshLayout.setRefreshing( false );
                        }

                    }
                    else {
                        // showToast( task.getException().getMessage() );
                    }
                }
            } );

            // Set Home Data....

        }else{
            showToast( context, "Please select City and.!" );
            if (HomeFragment.homeSwipeRefreshLayout != null){
                HomeFragment.homeSwipeRefreshLayout.setRefreshing( false );
            }
        }

    }

    // Sample For Test...
    public static void getQuerySetFragmentData(final RecyclerView homeLayoutContainerRecycler, final int index, Context context){
    /**
            // Set Home Data.... Loading Category Data...!!
//        String categoryName = catName.toUpperCase();
            String categoryName = "MOBILES";

        // TODO : add banners
        bannerSliderModelList = new ArrayList <>();
        // access the banners from database...
        bannerSliderModelList.add( new BannerSliderModel( String.valueOf( R.drawable.banner_image_placeholder ), "#0ff0ff" ) );
        bannerSliderModelList.add( new BannerSliderModel( String.valueOf( R.drawable.banner_image_placeholder ), "#0ff0ff" ) );
        bannerSliderModelList.add( new BannerSliderModel( String.valueOf( R.drawable.banner_image_placeholder ), "#0ff0ff" ) );

        homeCategoryList.get( index ).add( new HomeFragmentModel( BANNER_SLIDER_LAYOUT_CONTAINER, bannerSliderModelList ) );

        // TODO : add banners
        homeCategoryList.get( index ).add( new HomeFragmentModel( STRIP_AD_LAYOUT_CONTAINER, String.valueOf( R.drawable.stip_add_placeholder ), "#ff00ff" ) );
        homeCategoryList.get( index ).add( new HomeFragmentModel( STRIP_AD_LAYOUT_CONTAINER, String.valueOf( R.drawable.stip_add_placeholder ), "#ff00ff" ) );
        // TODO : for horizontal products
        horizontalItemViewModelList = new ArrayList <>();
        horizontalItemViewModelList.add( new HorizontalItemViewModel( 1, "kgubsdfouhios", String.valueOf( R.drawable.squre_image_placeholder ),
                " Sample name", "9892", "29000", true, 20 ) );
        horizontalItemViewModelList.add( new HorizontalItemViewModel( 1, "iguehdoiohooiho", String.valueOf( R.drawable.squre_image_placeholder ),
                " Sample 2 name", "999", "1000", true, 20 ) );

        List<String> hrAndGridProductIdList = new ArrayList <>();
        hrAndGridProductIdList.add( "kgubsdfouhios" );
        hrAndGridProductIdList.add( "iguehdoiohooiho" );

        homeCategoryList.get( index ).add( new HomeFragmentModel( HORIZONTAL_ITEM_LAYOUT_CONTAINER, hrAndGridProductIdList,
                horizontalItemViewModelList, " Title kjbasd " ) );


        // TODO : for grid products
        gridLayoutViewList = new ArrayList <>();
        gridLayoutViewList.add( new HorizontalItemViewModel( 0, "iguehdoiohooiho", String.valueOf( R.drawable.squre_image_placeholder ),
                " Sample 2 name", "999", "1000", true, 20 ) );
        gridLayoutViewList.add( new HorizontalItemViewModel( 0, "iguehdoiohooiho", String.valueOf( R.drawable.squre_image_placeholder ),
                " Sample 2 name", "999", "1000", true, 20 ) );
        gridLayoutViewList.add( new HorizontalItemViewModel( 1, "iguehdoiohooiho", String.valueOf( R.drawable.squre_image_placeholder ),
                " Sample 2 name", "999", "1000", true, 20 ) );
        gridLayoutViewList.add( new HorizontalItemViewModel( 1, "iguehdoiohooiho", String.valueOf( R.drawable.squre_image_placeholder ),
                " Sample 2 name", "999", "1000", true, 20 ) );

        List<String> hrAndGridProductIdList1 = new ArrayList <>();
        hrAndGridProductIdList.add( "khdkgubsdfouhios"  );
        hrAndGridProductIdList.add( "huoasdoihhiuuih"  );
        homeCategoryList.get( index ).add( new HomeFragmentModel( GRID_ITEM_LAYOUT_CONTAINER, hrAndGridProductIdList,
                gridLayoutViewList,"Grid Tilkjhsagd " ) );


            // Set Home Data....
        HomeFragmentAdaptor homeFragmentAdaptor = new HomeFragmentAdaptor( homeCategoryList.get( index ) );
        // $ Changes...
        if (homeLayoutContainerRecycler != null){
            homeLayoutContainerRecycler.setAdapter( homeFragmentAdaptor );
            homeFragmentAdaptor.notifyDataSetChanged();
        }
        if (HomeFragment.homeSwipeRefreshLayout != null){
            HomeFragment.homeSwipeRefreshLayout.setRefreshing( false );
        }

     */
    }


    // Query to load wish List data....
    public static void wishListQuery(final Context context, final Dialog dialog, final boolean loadProductData) {
        // clear all List..
        myWishList.clear();
        if (loadProductData){
            wishListModelList.clear();
        }
        firebaseFirestore.collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
                .collection( "USER_DATA" ).document( "MY_WISH_LIST" )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    tempIndex = 0;
                    for ( long x = 0; x < (long) task.getResult().get( "wish_list_size" ); x++ ) {
                        myWishList.add( task.getResult().get( "product_ID_" + x ).toString() );
                        if (myWishList.size() != 0){
                            // set color of wishList button based on wishList...
                            if (DBquery.myWishList.contains( ProductDetails.productID )){
                                ProductDetails.ALREADY_ADDED_IN_WISHLIST = true;
                                ProductDetails.addToWishListBtn.setSupportImageTintList( context.getResources().getColorStateList( R.color.colorRed ) );
                            }else{
                                ProductDetails.ALREADY_ADDED_IN_WISHLIST = false;
                            }
                        }
                        // load data to show in wishList..
                        if (loadProductData) {
                            firebaseFirestore.collection( "PRODUCTS" )
                                    .document( task.getResult().get( "product_ID_" + x ).toString() )
                                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().get( "product_qty_type" ) != null){
                                            wishListModelList.add( new MyWishListModel( task.getResult()
                                                    .get( "product_image_1" ).toString()
                                                    , task.getResult().get( "product_full_name" ).toString()
                                                    , task.getResult().get( "product_price" ).toString()
                                                    , task.getResult().get( "product_cut_price" ).toString()
                                                    , myWishList.get( tempIndex )
                                                    , " "
                                                    , true
                                                    , task.getResult().get( "product_qty_type" ).toString() ) );
                                        }else{
                                            wishListModelList.add( new MyWishListModel( task.getResult()
                                                    .get( "product_image_1" ).toString()
                                                    , task.getResult().get( "product_full_name" ).toString()
                                                    , task.getResult().get( "product_price" ).toString()
                                                    , task.getResult().get( "product_cut_price" ).toString()
                                                    , myWishList.get( tempIndex )
                                                    , " "
                                                    , true
                                                    , "" ) );
                                        }

                                        // Update Stock info wishList...
                                        if ((long)task.getResult().get( "product_stocks" ) > 0){
                                            DBquery.wishListModelList.get( DBquery.wishListModelList.size() - 1 ).setWishStockInfo( "IN_STOCK" );
                                        }else{
                                            DBquery.wishListModelList.get( DBquery.wishListModelList.size() - 1 ).setWishStockInfo( "OUT_OF_STOCK" );
                                        }
                                        // Update COD info in wishList...
                                        if ((boolean)task.getResult().get( "product_cod" )){
                                            DBquery.wishListModelList.get( DBquery.wishListModelList.size() - 1 ).setWishCODinfo( true );
                                        }else{
                                            DBquery.wishListModelList.get( DBquery.wishListModelList.size() - 1 ).setWishCODinfo( false );
                                        }
                                        tempIndex = tempIndex + 1;
                                        MyWishList.myWishListAdaptor.notifyDataSetChanged();
                                    } else {
                                        String error = task.getException().getMessage();
                                        showToast( context, error );
                                    }
                                    dialog.dismiss();
                                }
                            } );
                        }
                    }

                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                    String error = task.getException().getMessage();
                    showToast( context, error );
                }
//                dialog.dismiss();
            }
        } );

    }

    // Query to Remove Data from wish List....
    public static void removeItemFromWishList(final int index, final Context context, final Dialog dialog, final int removeType) {
        // Storing list into temp List...
        dbTempListItem = myWishList.get( index );
        // Remove item from local wishlist...
        if (myWishList.size() == 0){
            ProductDetails.addToWishListBtn.setSupportImageTintList( context.getResources().getColorStateList( R.color.colorRed ) );
            ProductDetails.addToWishListBtn.setEnabled( true );
            showToast( context, "wish list is empty!" );
            return;
        }else{
            myWishList.remove( index );
        }
        Map <String, Object> updateWishList = new HashMap <>();

        updateWishList.put( "wish_list_size", (long)myWishList.size() );
        for ( int x = 0; x < myWishList.size(); x++){
            updateWishList.put( "product_ID_"+x, myWishList.get( x ) );
        }

        firebaseFirestore.collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
                .collection( "USER_DATA" ).document( "MY_WISH_LIST" ).
                set( updateWishList ).addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (task.isSuccessful()) {
                    if (wishListModelList.size() != 0) {
                        wishListModelList.remove( index );
                        MyWishList.myWishListAdaptor.notifyDataSetChanged();
                    }

                    if (removeType == PRODUCT_DETAILS_ACTIVITY){
                        ProductDetails.ALREADY_ADDED_IN_WISHLIST = false;
                        ProductDetails.addToWishListBtn.setEnabled( true );
                    }

                    dialog.dismiss();
                    if (removeType != StaticValues.REMOVE_ITEM_AFTER_BUY){
                        showToast( context, "Remove item Successfully..!" );
                    }
                    if (myWishList.size() == 0){
                        MyWishList.myWishListRecycler.setVisibility( View.GONE );
                        MyWishList.noItemInWishListLayout.setVisibility( View.VISIBLE );
                    }
                } else {
                    dialog.dismiss();
                    myWishList.add( dbTempListItem );
                    ProductDetails.addToWishListBtn.setSupportImageTintList( context.getResources().getColorStateList( R.color.colorRed ) );
                    String error = task.getException().getMessage();
                    showToast( context, error );
                    ProductDetails.addToWishListBtn.setEnabled( true );
                }
            }
        } );

    }

    // Query to load Cart List data....
    public static void cartListQuery(final Context context, final boolean loadProductData, final Dialog dialog, final int requestActivity){
        // clear all List..
        myCartCheckList.clear();
        if (loadProductData){
            myCartItemModelList.clear();
        }
        firebaseFirestore.collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
                .collection( "USER_DATA" ).document( "MY_CART" ).get()
                .addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    tempIndex = 0;
                    final long my_cart_size = (long) task.getResult().get( "my_cart_size" );
                    for (long x = 0; x < my_cart_size; x++) {
                        dbTempListItem = task.getResult().get( "product_ID_" + x ).toString(); // Product ID
                        dbTempListItem2 = task.getResult().get( "product_qty_" + x ).toString(); // Product Qty
                        myCartCheckList.add( new MyCartCheckModel( task.getResult().get( "product_ID_" + x ).toString(),
                                task.getResult().get( "product_qty_" + x ).toString() ) );
                        if (myCartCheckList.size() != 0){
                            ProductDetails.ALREADY_ADDED_IN_CART = isContainInMyCartCheckList( ProductDetails.productID );
                            // Set Badge Cart data...
                            if (!loadProductData){
                                setCartCountText( requestActivity, String.valueOf( myCartCheckList.size()) );
                            }
                        }
                        if (loadProductData) {
                            //set visibility of cart view...
                            if ( tempIndex == 0 ){
                                MyCartFragment.isCartEmpty = false;
                                MyCartFragment.dontHaveCartConstLayout.setVisibility( View.GONE );
                                MyCartFragment.myCartConstLayout.setVisibility( View.VISIBLE );
                            }
//                            final String product_ID = task.getResult().get( "product_ID_" + x ).toString();
                            firebaseFirestore.collection( "PRODUCTS" )
                                    .document( task.getResult().get( "product_ID_" + x ).toString() )
                                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
//                                        int cartIndex = 0;
//                                        if (myCartList.size() >= 2){
//                                            cartIndex = myCartList.size() - 2;
//                                        }

                                        if(task.getResult().get( "product_qty_type" ) != null){
                                            myCartItemModelList.add( new MyCartItemModel( 0
                                                    , myCartCheckList.get( tempIndex ).getProductId()
                                                    , Integer.parseInt( myCartCheckList.get( tempIndex ).getProductQuantity() )
                                                    , task.getResult().get( "product_image_1" ).toString()
                                                    , task.getResult().get( "product_full_name" ).toString()
                                                    , task.getResult().get( "product_price" ).toString()
                                                    , task.getResult().get( "product_cut_price" ).toString()
                                                    , task.getResult().get( "product_qty_type" ).toString() ) );
                                        }else{
                                            myCartItemModelList.add( new MyCartItemModel( 0
                                                    , myCartCheckList.get( tempIndex ).getProductId()
                                                    , Integer.parseInt( myCartCheckList.get( tempIndex ).getProductQuantity() )
                                                    , task.getResult().get( "product_image_1" ).toString()
                                                    , task.getResult().get( "product_full_name" ).toString()
                                                    , task.getResult().get( "product_price" ).toString()
                                                    , task.getResult().get( "product_cut_price" ).toString()
                                                    , "" ) );
                                        }

                                        if ( tempIndex == ( my_cart_size - 1 ) ){
                                            myCartItemModelList.add( new MyCartItemModel( 1 ) );
                                        }
                                        if (myCartCheckList.size() == 0){
                                            myCartItemModelList.clear();
                                            MyCartFragment.isCartEmpty = true;
                                            MyCartFragment.myCartConstLayout.setVisibility( View.GONE );
                                            MyCartFragment.dontHaveCartConstLayout.setVisibility( View.VISIBLE );
                                        }
                                        MyCartFragment.myCartAdaptor.notifyDataSetChanged();
                                        tempIndex = tempIndex + 1;
                                        dialog.dismiss();
                                    } else {
                                        dialog.dismiss();
                                        tempIndex = tempIndex + 1;
                                        String error = task.getException().getMessage();
                                        showToast( context, error );
                                    }
                                }
                            } );

                            dialog.dismiss();
                        }else{
                            dialog.dismiss();
                        }
                    }

                    dialog.dismiss();
                }
                else {
                    dialog.dismiss();
                    String error = task.getException().getMessage();
                    showToast( context, error );
                }

            }
        } );

    }

    // Query to Remove Item from Cart List....
    public static void removeItemFromCart(final int index, final Context context, final int removeType, final Dialog dialog ){
        // Storing list into temp List...
        dbTempListItem = myCartCheckList.get( index ).getProductId();
        dbTempListItem2 = myCartCheckList.get( index ).getProductQuantity();
        int sizeOfMyCartList = myCartCheckList.size();
        // Remove item from local Cart List...
        if ( sizeOfMyCartList != 0 ){
            if (removeType == REMOVE_ONE_ITEM){
                myCartCheckList.remove( index );
            }else{
                myCartCheckList.clear();
            }

            Map <String, Object> updateCartList = new HashMap <>();
            sizeOfMyCartList = myCartCheckList.size();

            updateCartList.put( "my_cart_size", (long)sizeOfMyCartList );
            for ( int x = 0; x < sizeOfMyCartList; x++){
                updateCartList.put( "product_ID_"+x, myCartCheckList.get( x ).getProductId() );
                updateCartList.put( "product_qty_"+x, myCartCheckList.get( x ).getProductQuantity() );
            }
            firebaseFirestore.collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
                    .collection( "USER_DATA" ).document( "MY_CART" ).
                    set( updateCartList ).addOnCompleteListener( new OnCompleteListener <Void>() {
                @Override
                public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()) {
                            if (myCartItemModelList.size() != 0 && removeType == REMOVE_ONE_ITEM) {
                                myCartItemModelList.remove( index );
                                MyCartFragment.myCartAdaptor.notifyDataSetChanged();
                                dialog.dismiss();
                                showToast( context, "Remove item Successfully..!" );
                            }
                            if (myCartCheckList.size() == 0){
                                myCartItemModelList.clear();
                                ProductDetails.ALREADY_ADDED_IN_CART = false;
                                MyCartFragment.isCartEmpty = true;
                                MyCartFragment.myCartConstLayout.setVisibility( View.GONE );
                                MyCartFragment.dontHaveCartConstLayout.setVisibility( View.VISIBLE );
                            }
                        }
                        else {
                            dialog.dismiss();
                            myCartCheckList.add( index, new MyCartCheckModel( dbTempListItem, dbTempListItem2 ) );
                            String error = task.getException().getMessage();
                            showToast( context, error );
                        }
                    }
                } );

        }else{
            showToast( context, " You Don't have any cart item..!" );
            return;
        }

    }

    // Query To Remove and update address from database...
    public static void updateAndRemoveAddressQuery(final Context context, final Dialog dialog, final int queryType ){
        //        firebaseFirestore.collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
//                .collection( "USER_DATA" ).document( "MY_ADDRESS" ).
//                get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
//                if (task.isSuccessful()){// Query ... and Map }
//                else{
//                    dialog.dismiss();
//                    String error = task.getException().getMessage();
//                    showToast( context, error );
//                }
//            }
//        } );

        final int totalAddress = myAddressRecyclerModelList.size();

        Map <String, Object> addNewAddress = new HashMap <>();
        addNewAddress.put( "total_address", (long)totalAddress );
        for(int x = 0; x < totalAddress; x++ ){
            addNewAddress.put( "add_user_"+ x, myAddressRecyclerModelList.get( x ).getAddUserName() );
            addNewAddress.put( "add_h_no_"+ x, myAddressRecyclerModelList.get( x ).getAddHNo() );
            addNewAddress.put( "add_colony_"+ x, myAddressRecyclerModelList.get( x ).getAddColony() );
            addNewAddress.put( "add_city_"+ x, myAddressRecyclerModelList.get( x ).getAddCity() );
            addNewAddress.put( "add_state_"+ x, myAddressRecyclerModelList.get( x ).getAddState() );
            addNewAddress.put( "add_area_code_"+ x, myAddressRecyclerModelList.get( x ).getAddAreaCode() );
            addNewAddress.put( "add_landmark_"+ x, myAddressRecyclerModelList.get( x ).getAddLandmark() );
            addNewAddress.put( "add_area_key_code_"+ x, myAddressRecyclerModelList.get( x ).getAreaKeyCode() );
            if (x == 0){
                myAddressRecyclerModelList.get( x ).setSelectedAddress( true );
            }else{
                myAddressRecyclerModelList.get( x ).setSelectedAddress( false );
            }
        }

        firebaseFirestore.collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
                .collection( "USER_DATA" ).document( "MY_ADDRESS" ).
                set( addNewAddress ).addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (task.isSuccessful()){
                    // Start activity and finishing..
                    MyAddressesActivity.myAddressRecyclerAdaptor.notifyDataSetChanged();
                    switch (queryType){
                        case QUERY_TO_ADD_ADDRESS:
                            showToast( context, "Address Added Successfully..!" );
                            AddAddressActivity.addAddressActivity.finish();
                            break;
                        case QUERY_TO_REMOVE_ADDRESS:
                            showToast( context, "Remove Address Successfully..!" );
                            break;
                        case QUERY_TO_UPDATE_ADDRESS:
                            showToast( context, "Update address Successfully..!" );
                            AddAddressActivity.addAddressActivity.finish();
                            break;
                        default:
                            break;
                    }
                    dialog.dismiss();
                }
                else{
                    switch (queryType){
                        case QUERY_TO_ADD_ADDRESS:
                            myAddressRecyclerModelList.remove( totalAddress - 1);
                            AddAddressActivity.addSaveBtn.setEnabled( true );
                            dialog.dismiss();
                            break;
                        case QUERY_TO_REMOVE_ADDRESS:
                            // TODO : request to add address again...
                            getAllAddressQuery(context, dialog, true);
                            showToast( context, "Remove Address Failed..!" );
                            break;
                        case QUERY_TO_UPDATE_ADDRESS:
                            // Run getAllAddressQuery to update offline list again...
                            AddAddressActivity.addSaveBtn.setEnabled( true );
                            getAllAddressQuery(context, dialog, true);
                            break;
                        default:
                            break;
                    }
                    MyAddressesActivity.myAddressRecyclerAdaptor.notifyDataSetChanged();
                    String error = "Failed..! Error : " + task.getException().getMessage();
                    showToast( context, error );
                }
            }

        } );

    }

    // Query to get Data of Address from Database ...
    public static void getAllAddressQuery(final Context context, final Dialog dialog, final boolean isUpdateAdoptor){
        myAddressRecyclerModelList.clear();

        firebaseFirestore.collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
                .collection( "USER_DATA" ).document( "MY_ADDRESS" ).
                get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    long totalAddress = (long)task.getResult().get( "total_address" );
                    int xIndex = 0;
                    for (long x = 0; x < totalAddress; x++, xIndex++){
                        myAddressRecyclerModelList.add( new MyAddressRecyclerModel(
                                  task.getResult().get( "add_user_"+ x).toString()
                                , task.getResult().get( "add_h_no_"+ x ).toString()
                                , task.getResult().get( "add_colony_"+ x ).toString()
                                , task.getResult().get( "add_city_"+ x ).toString()
                                , task.getResult().get( "add_state_"+ x ).toString()
                                , task.getResult().get( "add_area_code_"+ x ).toString()
                                , task.getResult().get( "add_landmark_"+ x ).toString()
                                , task.getResult().get( "add_area_key_code_" + x ).toString()
                        ));
                        if (x == 0){
                            myAddressRecyclerModelList.get( xIndex ).setSelectedAddress( true );
                        }else{
                            myAddressRecyclerModelList.get( xIndex ).setSelectedAddress( false );
                        }
                        if (isUpdateAdoptor){
                            MyAddressesActivity.myAddressRecyclerAdaptor.notifyDataSetChanged();
                        }
                        if (!isUpdateAdoptor && x == 0){
                            MyAccountFragment.setAddress();
                        }
                    }

                    dialog.dismiss();

                }else{
                    dialog.dismiss();
                    String error = "Failed..! Error : " + task.getException().getMessage();
                    showToast( context, error );
                }
            }
        } );

    }

    // Query to load Order List data....
    public static void orderListQuery(final Context context, final Dialog dialog, final boolean loadOrderData) {
        // clear all List..
        myOrderList.clear();
        if (loadOrderData){
            myOrderModelArrayList.clear();
        }
        firebaseFirestore.collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
                .collection( "USER_DATA" ).document( "MY_ORDER" )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    tempIndex = 0;
                    for (long x = (long) task.getResult().get( "my_order_size" )-1; x >= 0 ; x--) {
                        myOrderList.add( task.getResult().get( "order_ID_" + x ).toString() );
                        // load data to show in MyOrder...
                        if (loadOrderData) {

                            firebaseFirestore.collection("COM_ORDERS").document( task.getResult().get( "order_ID_" + x ).toString() )
                                    .get( ).addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        if (task.getResult().get( "no_of_products" ) != null) {
                                            for (long y = 0; y < (long) task.getResult().get( "no_of_products" ); y++) {
                                                myOrderModelArrayList.add( 0
                                                        , new MyOrderModel( myOrderList.get( tempIndex )
                                                        , task.getResult().get( "product_img_" + y ).toString()
                                                        , task.getResult().get( "product_name_" + y ).toString()
                                                        , task.getResult().get( "delivery_status" ).toString() ) );
                                                // Update Adaptor...
                                                MyOrder.myOrderAdaptor.notifyDataSetChanged();
                                            }
                                            tempIndex = tempIndex + 1;
                                            MyOrder.noOrderHistoryLayout.setVisibility( View.GONE );
                                            MyOrder.myOrderRecycler.setVisibility( View.VISIBLE );

                                        }
                                    }else{
                                        // error..
                                        dialog.dismiss();
                                    }
                                }
                            } );

                        }
                    }
                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                    String error = task.getException().getMessage();
                    showToast( context, error );
                }
//                dialog.dismiss();
            }
        } );

    }

    // Query To update Notifications...
    public static void updateNotificationQuery( Context context, boolean remove ){
        if (remove ){
            if ( notificationLR != null )
                notificationLR.remove();
        }
        else{
           /* if (isInternetConnect( context )){
                notificationLR = firebaseFirestore.collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
                        .collection( "USER_DATA" ).document( "MY_NOTIFICATION" )
                        .addSnapshotListener( new EventListener <DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                if (documentSnapshot != null && documentSnapshot.exists()){
                                    notificationModelList.clear();
                                    int notifyCount = 0;
                                    for ( long x = 0; x < (long) documentSnapshot.get( "notification_size" ); x++ ){
                                        notificationModelList.add( 0, new NotificationModel(
                                                documentSnapshot.get( "noti_img_"+x ).toString(),
                                                documentSnapshot.get( "noti_text_"+x ).toString(),
                                                (boolean)documentSnapshot.get( "noti_read_"+x )
                                        ) );
                                        if (!documentSnapshot.getBoolean( "noti_read_"+x )){
                                            notifyCount = notifyCount + 1;
                                        }
                                    }
                                    if (MainActivity.badgeNotifyCount != null){
                                        if (notifyCount > 0){
                                            MainActivity.badgeNotifyCount.setVisibility( View.VISIBLE );
                                            MainActivity.badgeNotifyCount.setText( String.valueOf( notifyCount ) );
                                        }else{
                                            MainActivity.badgeNotifyCount.setVisibility( View.INVISIBLE );
                                        }
                                    }
                                    if (NotificationActivity.notificationAdaptor != null ){
                                        NotificationActivity.notificationAdaptor.notifyDataSetChanged();
                                    }
                                }
                            }
                        } );
            }  */
            if (isInternetConnect( context )) {

                notificationLR = firebaseFirestore.collection( "USER" ).document( currentUser.getUid() )
                        .collection( "USER_NOTIFICATION" ).orderBy( "notify_read" )
                        .addSnapshotListener( new EventListener <QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                if (queryDocumentSnapshots != null ) {
                                    // TODO: Query to update notification...
                                    int notifyCount = 0;
                                    notificationModelList.clear();

                                    for ( int x = 0; x < queryDocumentSnapshots.getDocuments().size(); x++ ){
                                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get( x );
                                        if( !documentSnapshot.getBoolean( "notify_read" ) ){
                                            // New Notification....
//                                            notificationModelList.get( 0 ).getNotifyId()
                                            String notifyType = documentSnapshot.get( "notify_type" ).toString();

                                            if ( notifyType.toUpperCase().equals( "ORDER_UPDATE" )){
                                                notificationModelList.add( 0, new NotificationModel( NOTIFY_ORDER_UPDATE,
                                                        false
                                                        ,documentSnapshot.get( "notify_id" ).toString()
                                                        ,documentSnapshot.get( "notify_order_id" ).toString()
                                                        ,documentSnapshot.get( "notify_heading" ).toString()
                                                        ,documentSnapshot.get( "notify_text" ).toString()
                                                ) );
                                            }else{
                                                // Other Type Notification...

                                            }

                                            notifyCount = notifyCount + 1;

                                        }else{
                                            // Old Notifications......
                                            if ( documentSnapshot.get( "notify_type" ).toString().equals( "ORDER_UPDATE" )){
                                                notificationModelList.add( new NotificationModel( NOTIFY_ORDER_UPDATE
                                                        , true
                                                        ,documentSnapshot.get( "notify_id" ).toString()
                                                        ,documentSnapshot.get( "notify_order_id" ).toString()
                                                        ,documentSnapshot.get( "notify_heading" ).toString()
                                                        ,documentSnapshot.get( "notify_text" ).toString()
                                                ) );
                                            }else{
                                                // Other Type Notification...

                                            }

                                        }
                                    }
                                    if (MainActivity.badgeNotifyCount != null){
                                        if (notifyCount > 0){
                                            MainActivity.badgeNotifyCount.setVisibility( View.VISIBLE );
                                            MainActivity.badgeNotifyCount.setText( String.valueOf( notifyCount ) );
                                        }else{
                                            MainActivity.badgeNotifyCount.setVisibility( View.INVISIBLE );
                                        }
                                    }
                                    if (NotificationActivity.notificationAdaptor != null ){
                                        NotificationActivity.notificationAdaptor.notifyDataSetChanged();
                                    }

                                }

                            }
                        } );
            }

        }
    }

    // Update information on DataBase...
    public static void userInformationQuery(final Context context, final Dialog dialog){
        userInformationList.clear();

        firebaseFirestore.collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String name = task.getResult().get( "user_name" ).toString();
                    String email = task.getResult().get( "user_email" ).toString();
                    String phone = task.getResult().get( "user_phone" ).toString();
                    String image = task.getResult().get( "user_image" ).toString();
                    userInformationList.add( 0, name );
                    userInformationList.add( 1, email );
                    userInformationList.add( 2, phone );
                    userInformationList.add( 3, image );
                    if (MainActivity.drawerImage != null && MainActivity.drawerName != null && MainActivity.drawerEmail != null){
                        Glide.with( context ).load( image ).into( MainActivity.drawerImage );
                        MainActivity.drawerName.setText( name );
                        MainActivity.drawerEmail.setText( email );
                    }
                    StaticValues.PROFILE_IMG_LINK = image;
                    dialog.dismiss();
                }else{
                    // Show Error Message...
                    String error = task.getException().getMessage();
                    showToast(context ,error);
                    dialog.dismiss();
                }
            }
        } );
    }

    // Toast message show method...
    private static void showToast(Context context, String s) {
        Toast.makeText( context, s, Toast.LENGTH_SHORT ).show();
        // or
//        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();

    }

    // Set notification On Cart icon...
     private static void setCartCountText(int requestActivity, String cartSize){

         switch (requestActivity){
             case MAIN_ACTIVITY:
                 if (MainActivity.badgeCartCount != null){
                     MainActivity.badgeCartCount.setVisibility( View.VISIBLE );
                     MainActivity.badgeCartCount.setText( cartSize );
                 }
                 break;
             case PRODUCT_DETAILS_ACTIVITY:
                 if (ProductDetails.badgeCartCount != null){
                     ProductDetails.badgeCartCount.setVisibility( View.VISIBLE );
                     ProductDetails.badgeCartCount.setText( cartSize );
                 }
                 break;
             case VIEW_ALL_ACTIVITY:
                 if (ViewAllActivity.badgeCartCount != null){
                     ViewAllActivity.badgeCartCount.setVisibility( View.VISIBLE );
                     ViewAllActivity.badgeCartCount.setText( cartSize );
                 }
                 break;
             case CATEGORIES_ITEMS_VIEW_ACTIVITY:
                 if (CategoriesItemsViewActivity.badgeCartCount != null){
                     CategoriesItemsViewActivity.badgeCartCount.setVisibility( View.VISIBLE );
                     CategoriesItemsViewActivity.badgeCartCount.setText( cartSize );
                 }
                 break;
//             case BUY_NOW_ACTIVITY:
//                break;
             default:
                 break;
         }

     }

    private static boolean isInternetConnect(Context context) {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        return !checkInternetCON.checkInternet( context );

    }

    // Check isContain...
    private static boolean isContainInMyCartCheckList(String productId){
         for (int i = 0; i < DBquery.myCartCheckList.size(); i++) {
            if( DBquery.myCartCheckList.get( i ).getProductId().equals( productId )){
                return true;
            }
        }
        return false;
    }



}
