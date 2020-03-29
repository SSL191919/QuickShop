package com.example.shailendra.quickshop.productdetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shailendra.quickshop.buyprocess.BuyNowActivity;
import com.example.shailendra.quickshop.MainActivity;
import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.DialogsClass;
import com.example.shailendra.quickshop.database.DBquery;
import com.example.shailendra.quickshop.database.StaticValues;
import com.example.shailendra.quickshop.mycart.MyCartCheckModel;
import com.example.shailendra.quickshop.mycart.MyCartFragment;
import com.example.shailendra.quickshop.mycart.MyCartItemModel;
import com.example.shailendra.quickshop.mywishlist.MyWishListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shailendra.quickshop.database.DBquery.currentUser;
import static com.example.shailendra.quickshop.database.StaticValues.PRODUCT_DETAILS_ACTIVITY;
import static com.example.shailendra.quickshop.database.StaticValues.productDetailTempList;

public class ProductDetails extends AppCompatActivity {

    // --- Product Details Image Layout...
    private ViewPager productImagesViewPager;
    private TabLayout productImagesIndicator;
    public static FloatingActionButton addToWishListBtn;
    private ConstraintLayout productDescriptionLayout;
    private TextView productName;
    private TextView productPrice;
    private TextView productCutPrice;
    private TextView productCODText;
    private TextView productCODSubText;
    private TextView productStock;

    // --- Product Details Image Layout...
    private TextView productDetailsText;

    private ViewPager productDescriptionViewPager;
    private TabLayout productDescriptionIndicator;

    private Button addToCartBtn;
    private Button buyNowBtn;
    public static String productID;
    public static TextView badgeCartCount;

    private ConstraintLayout activityProductDetailLayout;
    // Dialogs...
    private Dialog dialog;

    // FirebaseFireStore...
    private FirebaseFirestore firebaseFirestore;
    private DocumentSnapshot documentSnapshot;
    private DialogsClass dialogsClass = new DialogsClass( this );

    // Product Specification ...
    private List<ProductDetailsSpecificationModel> productDetailsSpecificationModelList = new ArrayList <>();
    private String productDescription;

    public static boolean ALREADY_ADDED_IN_WISHLIST = false;
    public static boolean ALREADY_ADDED_IN_CART = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_product_details );
        activityProductDetailLayout = findViewById( R.id.activity_product_detail_constLayout );

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // TODO : get product ID through Intent ...
        productID = getIntent().getStringExtra( "PRODUCT_ID" );


        // Set Title on Action Menu
        try{
            // To test We assign a default PRODUCT_ID ...
            if (productID.isEmpty()){
                productID = "k2SGQbneH477j6X18l6a";
            }
            getSupportActionBar().setDisplayShowTitleEnabled( false );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }


        // Clear the ptoductDetailTempList...
        productDetailTempList.clear();

        addToWishListBtn = findViewById( R.id.add_to_wishList_btn );
        addToCartBtn = findViewById( R.id.add_to_cart_btn );
        buyNowBtn = findViewById( R.id.buy_now_btn );

        // --- Product Details Image Layout...
        productName = findViewById( R.id.product_item_name );
        productPrice = findViewById( R.id.product_item_price );
        productCutPrice = findViewById( R.id.product_item_cut_price );
        productCODText = findViewById( R.id.product_item_cod_text );
        productCODSubText = findViewById( R.id.product_item_cod_sub_text );
        productStock = findViewById( R.id.product_item_stocks );

        // --- Product Details Image Layout...
        productDescriptionLayout = findViewById( R.id.product_details_description_ConstLayout );
        productDetailsText = findViewById( R.id.product_details_text );

        //----------- Product Images ---
        productImagesViewPager = findViewById( R.id.product_images_viewpager );
        productImagesIndicator = findViewById( R.id.product_images_viewpager_indicator );

        // create a list for testing...
        final List<String> productImageList = new ArrayList <>();

        dialog = dialogsClass.progressDialog( ProductDetails.this );
        dialog.show();
        // ---- Progress Dialog...

        firebaseFirestore = FirebaseFirestore.getInstance();

        // ---------- Product Description code----
        productDescriptionViewPager = findViewById( R.id.product_detail_viewpager );
        productDescriptionIndicator = findViewById( R.id.product_details_indicator );

        // TODO: Retrieve details from database...----------------
        firebaseFirestore.collection( "PRODUCTS" ).document( productID )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    documentSnapshot = task.getResult();
                    // --- add Product Images...
                    for (long x = 1; x < (long)documentSnapshot.get( "product_image_num" )+1; x++){
                        productImageList.add( documentSnapshot.get( "product_image_"+x ).toString() );
                    }
                    // set adapter with viewpager...
                    ProductDetailsImagesAdapter productDetailsImagesAdapter = new ProductDetailsImagesAdapter( productImageList );
                    productImagesViewPager.setAdapter( productDetailsImagesAdapter );
                    // Add Product ID  in productDetailTempList
                    productDetailTempList.add( 0, productID );
                    //add Main Image in productDetailTempList...
                    productDetailTempList.add( 1, productImageList.get( 0 ) );
                    // Set other Details of  Product Details Image Layout.
                    // Product Name...
                    productName.setText( documentSnapshot.get( "product_full_name" ).toString() );
                    //add name in Temp List
                    productDetailTempList.add( 2, documentSnapshot.get( "product_full_name" ).toString() );
                    // Product Price...
                    productPrice.setText( "Rs. " + documentSnapshot.get( "product_price" ).toString() +"/-" );
                    // add price in  Temp List
                    productDetailTempList.add( 3, documentSnapshot.get( "product_price" ).toString() );
                    // Product Cut Price...
                    productCutPrice.setText( "Rs. " + documentSnapshot.get( "product_cut_price" ).toString() +"/-" );
                    //add cut price in  Temp List
                    productDetailTempList.add(4, documentSnapshot.get( "product_cut_price" ).toString() );

                    if ((boolean)documentSnapshot.get( "product_cod" )){
                        productCODText.setVisibility( View.VISIBLE );
                        productCODSubText.setVisibility( View.VISIBLE );
                        //add COD in  Temp List
                        productDetailTempList.add( 5, "COD" );
                    }else{
                        productCODText.setVisibility( View.INVISIBLE );
                        productCODSubText.setVisibility( View.INVISIBLE );
                        //add COD in  Temp List
                        productDetailTempList.add( 5, "NO_COD" );
                    }
                    if ((long)documentSnapshot.get( "product_stocks" ) > 0){
                        productStock.setText( "In Stock" );
                        //add COD in  Temp List
                        productDetailTempList.add( 6, "IN_STOCK" );
                    }else{
                        productStock.setText( "Out of Stock" );
                        //add COD in  Temp List
                        productDetailTempList.add( 6, "OUT_OF_STOCK" );
                    }

                    // Set other Details of  Product Details Image Layout.
                    if ((boolean)documentSnapshot.get( "use_tab_layout" )){
                        // use tab layout...
                        productDescriptionLayout.setVisibility( View.VISIBLE );
                        // TODO : set Description data..
                        productDescription = documentSnapshot.get( "product_description" ).toString() ;
                        // TODO : set Specification data...
                        for (long x = 1; x < (long) documentSnapshot.get( "pro_sp_head_num" )+1; x++){
                            productDetailsSpecificationModelList.add( new ProductDetailsSpecificationModel( 0,
                                    documentSnapshot.get( "pro_sp_head_" + x ).toString() ) );
                            for (long i = 1; i < (long)documentSnapshot.get( "pro_sp_sub_head_"+x+"_num" )+1; i++){
                                productDetailsSpecificationModelList.add( new ProductDetailsSpecificationModel( 1,
                                        documentSnapshot.get( "pro_sp_sub_head_" + x + i ).toString(), documentSnapshot.get( "pro_sp_sub_head_d_" + x + i ).toString() ) );
                            }
                        }

                        ProductDetailsDescriptionAdaptor productDetailsDescriptionAdaptor = new ProductDetailsDescriptionAdaptor( getSupportFragmentManager()
                                , productDescriptionIndicator.getTabCount()
                                , productDescription
                                , productDetailsSpecificationModelList  );
                        productDescriptionViewPager.setAdapter( productDetailsDescriptionAdaptor );
                        productDetailsDescriptionAdaptor.notifyDataSetChanged();

                    }
                    else{
                        // don't use tabLayout...
                        productDescriptionLayout.setVisibility( View.GONE );
                    }
                    productDetailsText.setText( documentSnapshot.get( "product_details" ).toString() );
                    // check offline wishList Size.. and run DB query to update wishList if size is 0
                    if (currentUser != null){

                        // for Wish list...
                        if (DBquery.myWishList.size() == 0){
                            DBquery.wishListQuery(ProductDetails.this, dialog, false);
                        }
                        // set color of wishList button based on wishList...
                        if (DBquery.myWishList.size() != 0){
                            if (DBquery.myWishList.contains( productID )){
                                ALREADY_ADDED_IN_WISHLIST = true;
                                addToWishListBtn.setSupportImageTintList( getResources().getColorStateList( R.color.colorRed ) );
                            }else{
                                ALREADY_ADDED_IN_WISHLIST = false;
                            }
                            dialog.dismiss();
                        }

                        // for cart list...
                        if (DBquery.myCartCheckList.size() == 0){
                            DBquery.cartListQuery(ProductDetails.this, false, dialog, PRODUCT_DETAILS_ACTIVITY);
                        }
                        if (DBquery.myCartCheckList.size() != 0){
                            for (int i = 0; i < DBquery.myCartCheckList.size(); i++) {
                                if( DBquery.myCartCheckList.get( i ).getProductId().equals( productID )){
                                    ALREADY_ADDED_IN_CART = true;
                                    break;
                                }else{
                                    ALREADY_ADDED_IN_CART = false;
                                }
                            }
                            dialog.dismiss();
                        }

                    }else {
                        dialog.dismiss();
                    }

                }
                else{
                    String error = task.getException().getMessage();
                    showToast(error);
                    dialog.dismiss();
                }
            }
        } );

        // Retrieve details from database...----------------

        // connect TabLayout with viewPager...
        productImagesIndicator.setupWithViewPager( productImagesViewPager, true );
        //----------- Product Images ---

        productDescriptionViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener( productDescriptionIndicator ) );
        productDescriptionIndicator.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDescriptionViewPager.setCurrentItem( tab.getPosition() );
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        } );

        // ---------- Product Description code----
        // TODO: WishList Code
        // WishList Button click...
        addToWishListBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null){
                    dialogsClass.signInUpDialog( PRODUCT_DETAILS_ACTIVITY );
                }
                else{
                    dialog.show();
                    addToWishListBtn.setEnabled( false );
                    if (ALREADY_ADDED_IN_WISHLIST){
                        // if ALREADY_ADDED_IN_WISH_LIST then, Code for remove from wishList
                        int listIndex = DBquery.myWishList.indexOf( productID );
                        DBquery.removeItemFromWishList( listIndex, ProductDetails.this, dialog, -1 );
                        addToWishListBtn.setSupportImageTintList( ColorStateList.valueOf( Color.parseColor( "#4e4e4e" ) ) );
                    }
                    else {
                        // if not add item in wishList then, Code To Add in wishList
                        Map<String, Object> addProductIntoWishList = new HashMap <>();
                        for (int temX = 0; temX < DBquery.myWishList.size(); temX++){
                            addProductIntoWishList.put( "product_ID_"+temX, DBquery.myWishList.get( temX ) );
                        }
                        addProductIntoWishList.put( "wish_list_size", (long)DBquery.myWishList.size()+1 );
                        addProductIntoWishList.put( "product_ID_"+ String.valueOf( DBquery.myWishList.size() ), productID );
                        //Since we have use set Method so we have to add all the previous fields in Map also to update the List...
                        firebaseFirestore.collection( "USER" ).document( currentUser.getUid() )
                                .collection( "USER_DATA" ).document( "MY_WISH_LIST" )
                                .set( addProductIntoWishList ).addOnCompleteListener( new OnCompleteListener <Void>() {
                            @Override
                            public void onComplete(@NonNull Task <Void> task) {
                                if (task.isSuccessful()){

                                    DBquery.myWishList.add( productID.trim().toString() );
                                    if (DBquery.wishListModelList.size() != 0){

                                        DBquery.wishListModelList.add( new MyWishListModel( documentSnapshot.get( "product_image_1" ).toString()
                                                , documentSnapshot.get( "product_full_name" ).toString()
                                                , documentSnapshot.get( "product_price" ).toString()
                                                , documentSnapshot.get( "product_cut_price" ).toString()
                                                , productID
                                                , " "
                                                , true ) );
                                        // Update Stock info wishList...
                                        if ((long)documentSnapshot.get( "product_stocks" ) > 0){
                                            DBquery.wishListModelList.get( DBquery.wishListModelList.size() - 1 ).setWishStockInfo( "IN_STOCK" );
//                                                        myWishListModelList.get( myWishListModelList.size() - 1 ).setWishStockInfo( "IN_STOCK" );
                                        }else{
                                            DBquery.wishListModelList.get( DBquery.wishListModelList.size() - 1 ).setWishStockInfo( "OUT_OF_STOCK" );
//                                                        myWishListModelList.get( myWishListModelList.size() - 1 ).setWishStockInfo( "OUT_OF_STOCK" );
                                        }
                                        // Update COD info in wishList...
                                        if ((boolean)documentSnapshot.get( "product_cod" )){
                                            DBquery.wishListModelList.get( DBquery.wishListModelList.size() - 1 ).setWishCODinfo( true );
                                        }else{
                                            DBquery.wishListModelList.get( DBquery.wishListModelList.size() - 1 ).setWishCODinfo( false );
                                        }

                                    }

                                    addToWishListBtn.setSupportImageTintList( getResources().getColorStateList( R.color.colorRed ) );
                                    dialog.dismiss(); // Dismiss the Dialog First Then Show Toast Msg...
                                    if (DBquery.myWishList.contains( productID )){
                                        showToast( "Added to WishList SuccessFully..!" );
                                    }else{
                                        showToast( "Added to WishList but not Add in Local List." );
                                    }
                                    ALREADY_ADDED_IN_WISHLIST = true;
                                    addToWishListBtn.setEnabled( true );
                                }
                                else{
                                    dialog.dismiss();
                                    addToWishListBtn.setSupportImageTintList( ColorStateList.valueOf( Color.parseColor( "#4e4e4e" ) ) );
                                    String error = task.getException().getMessage();
                                    showToast( error );
                                    addToWishListBtn.setEnabled( true );
                                }
                            }
                        } );

                    }
                }
            }
        } );
        // WishList Button click...
        // TODO: MyCart Code
        // Add to Cart Click...
        addToCartBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartBtn.setEnabled( false );
                if (currentUser == null){
                    dialogsClass.signInUpDialog( PRODUCT_DETAILS_ACTIVITY );
                    addToCartBtn.setEnabled( true );
                }
                else{
                    dialog.show();
                    if (ALREADY_ADDED_IN_CART){
                        dialog.dismiss();
                        // Code if already add in cart...
                        showToast( "Already Added in cart..!" );
                        addToCartBtn.setEnabled( true );
                    }
                    else{
                        // Code if item not add in cart...
                        final int tempCartSize = DBquery.myCartCheckList.size();
                        Map<String, Object> addProductIntoCart = new HashMap <>();
                        for (int temX = 0; temX < tempCartSize; temX++ ){
                            addProductIntoCart.put( "product_ID_"+ temX, DBquery.myCartCheckList.get( temX ).getProductId() );
                            addProductIntoCart.put( "product_qty_"+ temX, DBquery.myCartCheckList.get( temX ).getProductQuantity() );
                        }
                        addProductIntoCart.put( "product_ID_"+ tempCartSize, productID );
                        addProductIntoCart.put( "product_qty_"+ tempCartSize, "1" );
                        addProductIntoCart.put( "my_cart_size", (long)DBquery.myCartCheckList.size()+1  );
                        //Since we have use set Method so we have to add all the previous fields in Map also to update the List...
                        firebaseFirestore.collection( "USER" ).document( currentUser.getUid() )
                                .collection( "USER_DATA" ).document( "MY_CART" )
                                .set( addProductIntoCart ).addOnCompleteListener( new OnCompleteListener <Void>() {
                            @Override
                            public void onComplete(@NonNull Task <Void> task) {
                                if (task.isSuccessful()){

                                    DBquery.myCartCheckList.add( new MyCartCheckModel( productID, "1" ) );

                                    if (DBquery.myCartItemModelList.size() != 0){
                                        DBquery.myCartItemModelList.add( tempCartSize, new MyCartItemModel( 0, productID
                                                , Integer.parseInt( documentSnapshot.get( "product_quantity" ).toString() )
                                                , documentSnapshot.get( "product_image_1" ).toString()
                                                , documentSnapshot.get( "product_full_name" ).toString()
                                                , documentSnapshot.get( "product_price" ).toString()
                                                , documentSnapshot.get( "product_cut_price" ).toString() ) );

                                        if (MyCartFragment.myCartAdaptor != null){
                                            MyCartFragment.myCartAdaptor.notifyDataSetChanged();
                                        }
                                    }
                                    ALREADY_ADDED_IN_CART = true;
                                    dialog.dismiss();
                                    showToast( "Added to Cart SuccessFully..!" );
                                    // To Refresh Menu...
                                    invalidateOptionsMenu();
                                    addToCartBtn.setEnabled( true );

                                    // Query To update List Size on firebase and offline list...
                                }
                                else{
                                    dialog.dismiss();
                                    String error = task.getException().getMessage();
                                    showToast( error );
                                    addToCartBtn.setEnabled( true );
                                }
                            }
                        } );

                        // Code if item not add in cart...
                    }
                }
            }
        } );
        // Add to Cart Click...
        // TODO: Buy Now Code
        // Buy Now Click...
        buyNowBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent buyNowIntent = new Intent( ProductDetails.this, BuyNowActivity.class );
//                myAddressIntent.putExtra( "MODE", SELECT_ADDRESS );
                startActivity( buyNowIntent );
                // Set BUY_FROM value...
                StaticValues.BUY_FROM_VALUE = StaticValues.BUY_FROM_HOME;
                // Run this query to get Size of OrderList... So if user Buy this item then New order would be added..!
                if (currentUser != null){
                    DBquery.orderListQuery( ProductDetails.this, dialog, false );
                }
            }
        } );
        // Buy Now Click...

    }

    // onCreate method End.. ----------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        // To Refresh Menu...
        invalidateOptionsMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // check offline wishList Size.. and run DB query to update wishList if size is 0
        if (currentUser != null){

            if (DBquery.myWishList.size() == 0){
                DBquery.wishListQuery(ProductDetails.this, dialog, false);
            }
            // set color of wishList button based on wishList...
            if (DBquery.myWishList.size() != 0){
                if (DBquery.myWishList.contains( productID )){
                    ALREADY_ADDED_IN_WISHLIST = true;
                    addToWishListBtn.setSupportImageTintList( getResources().getColorStateList( R.color.colorRed ) );
                }else{
                    ALREADY_ADDED_IN_WISHLIST = false;
                }
                dialog.dismiss();
            }

            // for cart list...
            if (DBquery.myCartCheckList.size() == 0){
                DBquery.cartListQuery(ProductDetails.this, false, dialog, PRODUCT_DETAILS_ACTIVITY);
            }else {
                for (int i = 0; i < DBquery.myCartCheckList.size(); i++) {
                    if( DBquery.myCartCheckList.get( i ).getProductId().equals( productID )){
                        ALREADY_ADDED_IN_CART = true;
                        break;
                    }else{
                        ALREADY_ADDED_IN_CART = false;
                    }
                }
                dialog.dismiss();
            }

        }else {
            dialog.dismiss();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_show_in_product_details,menu);
        MenuItem cartItem = menu.findItem( R.id.menu_cart );
        // Check First whether any item in cart or not...
            // if any item has in cart...
            cartItem.setActionView( R.layout.badge_cart_layout );
//            ImageView badgeCartIcon = cartItem.getActionView().findViewById( R.id.badge_cart_icon );
            badgeCartCount = cartItem.getActionView().findViewById( R.id.badge_count_text );
            if (DBquery.myCartCheckList.size() > 0){
                badgeCartCount.setVisibility( View.VISIBLE );
                badgeCartCount.setText( String.valueOf( DBquery.myCartCheckList.size() ) );
            }
            cartItem.getActionView().setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // GOTO : Goto Cart
                    if (currentUser == null){
                        dialogsClass.signInUpDialog( PRODUCT_DETAILS_ACTIVITY );
                    }else{
                        startActivity( new Intent(ProductDetails.this, MainActivity.class) );
                        MainActivity.isFragmentIsMyCart = true;
                    }
                }
            } );
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if ((item.getItemId() == android.R.id.home)){
            finish();
            return true;
        }else
        if (id == R.id.menu_cart){
            // GOTO : Goto Cart
            if (currentUser == null){
                dialogsClass.signInUpDialog( PRODUCT_DETAILS_ACTIVITY );
            }else{
                startActivity( new Intent(this, MainActivity.class) );
                MainActivity.isFragmentIsMyCart = true;
            }
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    private void showToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
