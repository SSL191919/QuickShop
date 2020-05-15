package com.gmyscl.ecom.firstorder.buyprocess;


import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gmyscl.ecom.firstorder.DialogsClass;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.database.DBquery;
import com.gmyscl.ecom.firstorder.database.OrderQuery;
import com.gmyscl.ecom.firstorder.database.StaticValues;
import com.gmyscl.ecom.firstorder.userprofile.MyAddressRecyclerModel;
import com.gmyscl.ecom.firstorder.userprofile.MyAddressesActivity;
import com.google.firebase.auth.FirebaseAuth;

import static com.gmyscl.ecom.firstorder.database.StaticValues.BUY_FROM_CART;
import static com.gmyscl.ecom.firstorder.database.StaticValues.BUY_FROM_HOME;
import static com.gmyscl.ecom.firstorder.database.StaticValues.BUY_FROM_VALUE;
import static com.gmyscl.ecom.firstorder.database.StaticValues.BUY_FROM_WISH_LIST;
import static com.gmyscl.ecom.firstorder.database.StaticValues.CONTINUE_SHOPPING_FRAGMENT;
import static com.gmyscl.ecom.firstorder.database.StaticValues.SELECT_ADDRESS;
import static com.gmyscl.ecom.firstorder.database.StaticValues.productDetailTempList;

public class ConformOrderFragment extends Fragment {

    public ConformOrderFragment() {
        // Required empty public constructor
    }

    DialogsClass dialogsClass =  new DialogsClass(  );

    private TextView addUserName;
    private TextView addUserFullAddress;
    private TextView addUserPincode;
    private Button changeOrAddNewAddressBtn;
    private TextView totalBillAmount;
    private Button  conformOrderBtn;

    private ConstraintLayout orderConfirmConstLayout;

    private FrameLayout confirmOrderFragmentFrameLayout;
    // Pay mode...
    private RadioGroup payModeRadioGroup;
    private RadioButton payCODRadioBtn;
    private RadioButton payPaytmRadioBtn;
    // create Variable of addressRecyclerModel
    private MyAddressRecyclerModel addressRecyclerModel;

    private String userPhone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_conform_order, container, false );


        // Assign Variable...
        orderConfirmConstLayout = view.findViewById( R.id.orderConfirmConstLayout );
        changeOrAddNewAddressBtn = view.findViewById( R.id.add_nw_address_btn );
        addUserName = view.findViewById( R.id.my_add_full_name );
        addUserFullAddress = view.findViewById( R.id.my_add_full_address );
        addUserPincode = view.findViewById( R.id.my_add_pin_text );

        totalBillAmount = view.findViewById( R.id.total_bill_amount );
        conformOrderBtn = view.findViewById( R.id.conformOrderBtn );
        confirmOrderFragmentFrameLayout = view.findViewById( R.id.confirm_order_fragmentFrame );

        // Pay Mode...
        payModeRadioGroup = view.findViewById( R.id.payModeRadioGroup );
        payCODRadioBtn = view.findViewById( R.id.payCODRadioBtn );
        payPaytmRadioBtn = view.findViewById( R.id.payPaytmRadioBtn );

        changeOrAddNewAddressBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myAddressIntent = new Intent( getActivity(), MyAddressesActivity.class );
                myAddressIntent.putExtra( "MODE", SELECT_ADDRESS );
                startActivity( myAddressIntent );
                MyAddressesActivity.isRequestForChangeAddress = true;
            }
        } );

        if (DBquery.myAddressRecyclerModelList.size() > 0){
            addressRecyclerModel = DBquery.myAddressRecyclerModelList.get( ConformOrderActivity.index );
            setDeliveryAddress();
        }

        if (DBquery.userInformationList.size() > 0){
//            orderDetailMap.put( "user_phone",  );/
            userPhone = DBquery.userInformationList.get( 2 );
        }else{
            Dialog dialog = dialogsClass.progressDialog( getContext() );
            dialog.show();
            DBquery.userInformationQuery( getContext(), dialog );
        }

        // Set Bill Text...
        totalBillAmount.setText( "Rs." + StaticValues.TOTAL_BILL_AMOUNT + "/-" );

        conformOrderBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedMode = payModeRadioGroup.getCheckedRadioButtonId();
                if (selectedMode == payCODRadioBtn.getId()){

                    if (userPhone != null){
                        StaticValues.isVerifiedMobile = false;
                        Intent otpIntent = new Intent( getActivity(), OTPVerifyActivity.class );
                        otpIntent.putExtra( "USER_PHONE", userPhone );
                        startActivity( otpIntent );
                    }else{
                        Toast.makeText( getContext(), "Please Update Your Phone number..!", Toast.LENGTH_SHORT ).show();
                    }
//                    callOrderQuery( StaticValues.COD_MODE );
//                    setOrderSuccessLinearLayout();
                }
                else if (selectedMode == payPaytmRadioBtn.getId()){
                    showPaytmDialog();
//                    callOrderQuery( StaticValues.ONLINE_MODE );

                }else{
                    Toast.makeText( getContext(),"Please select Pay Mode..!!", Toast.LENGTH_SHORT ).show();
                }
            }
        } );



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (StaticValues.isVerifiedMobile){
            StaticValues.isVerifiedMobile = false;
            // GOTO :
            callOrderQuery( StaticValues.COD_MODE );
        }

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

    // Order Query call...
    private void callOrderQuery( int payMode ){
        Dialog dialog = dialogsClass.progressDialog( getActivity() );
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
            OrderQuery.createOrderIdAndDocument( getActivity(), dialog,
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
            OrderQuery.createOrderIdAndDocument( getActivity(), dialog,
                    BUY_FROM_WISH_LIST, deliveryCharge, payMode, "", billAmount, orderByUserId,
                    orderDeliveredName, orderDeliveryAddress, orderDeliveryPin, orderDateDay, orderTime);
        }else
        if (BUY_FROM_VALUE == BUY_FROM_HOME){
            // Buy From Home... We Can Call OrderQuery Class method.!
            OrderQuery.buyNowModelList.clear();
            OrderQuery.buyNowModelList.add( new BuyNowModel( productDetailTempList.get( 0 ),
                    productDetailTempList.get( 2 ), productDetailTempList.get( 1 ), productDetailTempList.get( 3 ), String.valueOf( BuyNowActivity.buyNowQty )) );
            // Order Query...
            OrderQuery.createOrderIdAndDocument( getActivity(), dialog,
                    BUY_FROM_HOME, deliveryCharge, payMode, "", billAmount, orderByUserId,
                    orderDeliveredName, orderDeliveryAddress, orderDeliveryPin, orderDateDay, orderTime);

        }else{
            showToast( "Something Went Wrong...!!" );
        }
        setFragment( new ContinueShopping() );
    }

    // Paytm accessing dialog...
    private void showPaytmDialog(){
        /// Sample Button click...
        final Dialog permissionDialog = new Dialog( getActivity() );
        permissionDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        permissionDialog.setContentView( R.layout.dialog_permission );
        permissionDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        permissionDialog.setCancelable( false );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            permissionDialog.getWindow().setBackgroundDrawable( getActivity().getDrawable( R.drawable.x_ractangle_layout ) );
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


    @Override
    public void onDestroyView() {
//        ViewGroup mContainer = (ViewGroup) getActivity().findViewById(R.id.sign_in_frameLayout);
//        mContainer.removeAllViews();
        confirmOrderFragmentFrameLayout.removeAllViews();
        super.onDestroyView();
    }

    // Fragment Transaction...
    public void setFragment( Fragment fragment){
        try{
            getActivity().getParent().getActionBar().setTitle( "Continue Shopping..." );
            getActivity().getParent().getActionBar().setDisplayShowTitleEnabled( true );
            getActivity().getParent().getActionBar().setDisplayHomeAsUpEnabled( false );
        }catch (NullPointerException e){ }
        ConformOrderActivity.currentFrameLayout = CONTINUE_SHOPPING_FRAGMENT;
        orderConfirmConstLayout.setVisibility( View.GONE );
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
//        onDestroyView();
        fragmentTransaction.replace( confirmOrderFragmentFrameLayout.getId(),fragment );
        fragmentTransaction.commit();
    }

    private void showToast(String s){
        Toast.makeText( getActivity(), s , Toast.LENGTH_SHORT ).show();
    }


}
