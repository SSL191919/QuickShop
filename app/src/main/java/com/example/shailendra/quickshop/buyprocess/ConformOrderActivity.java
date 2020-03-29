package com.example.shailendra.quickshop.buyprocess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shailendra.quickshop.DialogsClass;
import com.example.shailendra.quickshop.MainActivity;
import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.database.DBquery;
import com.example.shailendra.quickshop.database.OrderQuery;
import com.example.shailendra.quickshop.database.StaticValues;
import com.example.shailendra.quickshop.userprofile.MyAddressRecyclerModel;
import com.example.shailendra.quickshop.userprofile.MyAddressesActivity;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.shailendra.quickshop.database.StaticValues.BUY_FROM_CART;
import static com.example.shailendra.quickshop.database.StaticValues.BUY_FROM_HOME;
import static com.example.shailendra.quickshop.database.StaticValues.BUY_FROM_VALUE;
import static com.example.shailendra.quickshop.database.StaticValues.BUY_FROM_WISH_LIST;
import static com.example.shailendra.quickshop.database.StaticValues.SELECT_ADDRESS;
import static com.example.shailendra.quickshop.database.StaticValues.productDetailTempList;

public class ConformOrderActivity extends AppCompatActivity {
    public static AppCompatActivity conFormOrderActivity;
    DialogsClass dialogsClass =  new DialogsClass(  );

    private int index;
    private TextView addUserName;
    private TextView addUserFullAddress;
    private TextView addUserPincode;
    private Button changeOrAddNewAddressBtn;
    private TextView totalBillAmount;
    private Button  conformOrderBtn;

    private ConstraintLayout orderConfirmConstLayout;
    private LinearLayout orderSuccessLinearLayout;
    private Button continueShoppingBtn;

    // Pay mode...
    private RadioGroup payModeRadioGroup;
    private RadioButton payCODRadioBtn;
    private RadioButton payPaytmRadioBtn;
    // create Variable of addressRecyclerModel
    private MyAddressRecyclerModel addressRecyclerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_conform_order );
        conFormOrderActivity  = this;

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        try{
            getSupportActionBar().setTitle( "Conform Order" );
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){ }

        // get intent value...
        index = getIntent().getIntExtra( "INDEX", -1 );
        // Assign Variable...
        orderConfirmConstLayout = findViewById( R.id.orderConfirmConstLayout );
        orderSuccessLinearLayout = findViewById( R.id.successOrderTextLinearLayout );
        changeOrAddNewAddressBtn = findViewById( R.id.add_nw_address_btn );
        addUserName = findViewById( R.id.my_add_full_name );
        addUserFullAddress = findViewById( R.id.my_add_full_address );
        addUserPincode = findViewById( R.id.my_add_pin_text );

        totalBillAmount = findViewById( R.id.total_bill_amount );
        conformOrderBtn = findViewById( R.id.conformOrderBtn );
        continueShoppingBtn = findViewById( R.id.continueShoppingBtn );

        // Pay Mode...
        payModeRadioGroup = (RadioGroup)findViewById( R.id.payModeRadioGroup );
        payCODRadioBtn = (RadioButton)findViewById( R.id.payCODRadioBtn );
        payPaytmRadioBtn = (RadioButton)findViewById( R.id.payPaytmRadioBtn );

        changeOrAddNewAddressBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myAddressIntent = new Intent( ConformOrderActivity.this, MyAddressesActivity.class );
                myAddressIntent.putExtra( "MODE", SELECT_ADDRESS );
                startActivity( myAddressIntent );
                MyAddressesActivity.isRequestForChangeAddress = true;
            }
        } );

        if (DBquery.myAddressRecyclerModelList.size() > 0){
            addressRecyclerModel = DBquery.myAddressRecyclerModelList.get( index );
            setDeliveryAddress();
        }

        // Set Bill Text...
        totalBillAmount.setText( "Rs." + StaticValues.TOTAL_BILL_AMOUNT + "/-" );

        conformOrderBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedMode = payModeRadioGroup.getCheckedRadioButtonId();
                if (selectedMode == payCODRadioBtn.getId()){
                    callOrderQuery( StaticValues.COD_MODE );
//                    setOrderSuccessLinearLayout();

                }else if (selectedMode == payPaytmRadioBtn.getId()){
                    showPaytmDialog();
//                    callOrderQuery( StaticValues.ONLINE_MODE );

                }else{
                    Toast.makeText( ConformOrderActivity.this,"Please select Pay Mode..!!", Toast.LENGTH_SHORT ).show();
                }
            }
        } );

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
                finishAffinity();
                startActivity( new Intent( ConformOrderActivity.this, MainActivity.class ) );
                finish();
            }
        } );

        // Paytm...
//        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //initOrderId();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    @Override
    public void onBackPressed() {
        if (orderSuccessLinearLayout.getVisibility() == View.VISIBLE){
            // Jump To MainActivity...
            if (MainActivity.isFragmentIsMyCart){
                MainActivity.isFragmentIsMyCart = false;
                MainActivity.mainActivityForCart.finish();
            }
            MainActivity.mainActivity.finish();
            finishAffinity();
            startActivity( new Intent( ConformOrderActivity.this, MainActivity.class ) );
            finish();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ((item.getItemId() == android.R.id.home)){
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    public void setDeliveryAddress(){
        addUserName.setText( getDeliveredFullName() );
        addUserFullAddress.setText( getDeliveryAddress() );
        addUserPincode.setText( getDeliveryPin() );
    }

    private String getDeliveredFullName(){
        String fullName = addressRecyclerModel.getAddUserName();
        return fullName;
    }
    private String getDeliveryAddress(){
        String landmark = "";
        if ( !TextUtils.isEmpty( addressRecyclerModel.getAddLandmark()) ){
            landmark = ", ( Landmark : " + addressRecyclerModel.getAddLandmark() + " )";
        }
        String fullAddress = addressRecyclerModel.getAddHNo() + ", "
                + addressRecyclerModel.getAddColony() + ", "
                + addressRecyclerModel.getAddCity() + ", \n"
                + addressRecyclerModel.getAddState() + landmark;
        return fullAddress;
    }
    private String getDeliveryPin(){
        String pinCode = addressRecyclerModel.getAddAreaCode();
        return pinCode;
    }

    public void setOrderSuccessLinearLayout(){
        // SetVisibility default...After success order...
        orderConfirmConstLayout.setVisibility( View.GONE );
        orderSuccessLinearLayout.setVisibility( View.VISIBLE );
        try{
            getSupportActionBar().setTitle( "Continue Shopping..." );
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( false );
        }catch (NullPointerException e){ }
    }

    // Order Query call...
    private void callOrderQuery( int payMode ){
        Dialog dialog = dialogsClass.progressDialog( ConformOrderActivity.this );
        dialog.show();

        // User Information...
         String orderByUserId = FirebaseAuth.getInstance().getUid();
         String orderDeliveredName = getDeliveredFullName();
         String orderDeliveryAddress = getDeliveryAddress();
         String orderDeliveryPin = getDeliveryPin();
         String orderDateDay = StaticValues.getCurrentDateDay();
         String orderTime = StaticValues.getCurrentTime();
         String billAmount = String.valueOf( StaticValues.TOTAL_BILL_AMOUNT );
         String deliveryCharge = String.valueOf( StaticValues.DELIVERY_AMOUNT );

        if (BUY_FROM_VALUE == BUY_FROM_CART){
            // Means We have to handle Multiple Product Data...
            OrderQuery.buyNowModelList.clear();
            for ( int x = 0; x < DBquery.myCartItemModelList.size() - 1; x++){
                OrderQuery.buyNowModelList.add( new BuyNowModel(
                        DBquery.myCartItemModelList.get( x ).getProductId(),
                        DBquery.myCartItemModelList.get( x ).getProductName(),
                        DBquery.myCartItemModelList.get( x ).getProductImage(),
                        DBquery.myCartItemModelList.get( x ).getProductPrice(),
                        String.valueOf( DBquery.myCartItemModelList.get( x ).getProductQuantity() )
                ) );
            }
            // Order Query...
            OrderQuery.createOrderIdAndDocument( ConformOrderActivity.this, dialog,
                    BUY_FROM_CART, deliveryCharge, payMode, "", billAmount , orderByUserId,
                    orderDeliveredName, orderDeliveryAddress, orderDeliveryPin, orderDateDay, orderTime);

//            dialog.dismiss();

        }else
        if (BUY_FROM_VALUE == BUY_FROM_WISH_LIST){
            // Buy From Wish List..We Can Call OrderQuery Class method.!
            OrderQuery.buyNowModelList.clear();
            OrderQuery.buyNowModelList.add( new BuyNowModel( productDetailTempList.get( 0 ),
                    productDetailTempList.get( 2 ), productDetailTempList.get( 1 ), productDetailTempList.get( 3 ), String.valueOf( BuyNowActivity.buyNowQty )) );
            // Order Query...
            OrderQuery.createOrderIdAndDocument( ConformOrderActivity.this, dialog,
                    BUY_FROM_WISH_LIST, deliveryCharge, payMode, "", billAmount, orderByUserId,
                    orderDeliveredName, orderDeliveryAddress, orderDeliveryPin, orderDateDay, orderTime);
        }else
        if (BUY_FROM_VALUE == BUY_FROM_HOME){
            // Buy From Home... We Can Call OrderQuery Class method.!
            OrderQuery.buyNowModelList.clear();
            OrderQuery.buyNowModelList.add( new BuyNowModel( productDetailTempList.get( 0 ),
                    productDetailTempList.get( 2 ), productDetailTempList.get( 1 ), productDetailTempList.get( 3 ), String.valueOf( BuyNowActivity.buyNowQty )) );
            // Order Query...
            OrderQuery.createOrderIdAndDocument( ConformOrderActivity.this, dialog,
                    BUY_FROM_HOME, deliveryCharge, payMode, "", billAmount, orderByUserId,
                    orderDeliveredName, orderDeliveryAddress, orderDeliveryPin, orderDateDay, orderTime);

        }else{
            showToast( "Something Went Wrong...!!" );
        }
        setOrderSuccessLinearLayout();
    }



    private void showToast(String s){
        Toast.makeText( ConformOrderActivity.this, s , Toast.LENGTH_SHORT ).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // Paytm accessing dialog...
    private void showPaytmDialog(){
        /// Sample Button click...
        final Dialog permissionDialog = new Dialog( this );
        permissionDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        permissionDialog.setContentView( R.layout.dialog_permission );
        permissionDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        permissionDialog.setCancelable( false );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            permissionDialog.getWindow().setBackgroundDrawable( this.getDrawable( R.drawable.x_ractangle_layout ) );
        }
        Button okBtn = permissionDialog.findViewById( R.id.per_ok_button );
        TextView msgTxt = permissionDialog.findViewById( R.id.per_text_des );

        msgTxt.setText( "Please select COD for Testing Purpose..! Paytm is Locked for Testing. For further Details Contact App Founder..!" );
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionDialog.dismiss();
            }
        } );
        permissionDialog.show();
    }


    /// Update Data on Database...



}
