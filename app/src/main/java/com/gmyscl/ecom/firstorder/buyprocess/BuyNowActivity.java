package com.gmyscl.ecom.firstorder.buyprocess;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gmyscl.ecom.firstorder.DialogsClass;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.database.StaticValues;
import com.gmyscl.ecom.firstorder.userprofile.MyAddressesActivity;

import static com.gmyscl.ecom.firstorder.database.DBquery.currentUser;
import static com.gmyscl.ecom.firstorder.database.StaticValues.BUY_NOW_ACTIVITY;
import static com.gmyscl.ecom.firstorder.database.StaticValues.SELECT_ADDRESS;
import static com.gmyscl.ecom.firstorder.database.StaticValues.productDetailTempList;

public class BuyNowActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;
    private TextView productCutPrice;
    private TextView productQuantity;
    private TextView productCOD;
    private TextView productStock;
    public static int buyNowQty = 1;
    private TextView cartTotalItems;
    private TextView cartTotalItemsPrice;
    private TextView cartDeliveryCharge;
    private TextView cartSavedAmount;
    private TextView cartTotalAmount1;
    private TextView cartTotalAmount2;

    DialogsClass dialogsClass = new DialogsClass( BuyNowActivity.this );
    private Dialog dialog;

    private Button continueProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_buy_now );
        dialog = dialogsClass.progressDialog( this );
        buyNowQty = 1;
        // Assign Variable... Item..
        productImage = findViewById( R.id.product_image );
        productName = findViewById( R.id.product_name );
        productPrice = findViewById( R.id.product_price );
        productCutPrice = findViewById( R.id.product_cut_price );
        productQuantity = findViewById( R.id.item_quntity );
        productCOD = findViewById( R.id.COD_text );
        productStock = findViewById( R.id.Stock_txt );
        // Assign Variable... Amount..
        cartTotalItems = findViewById( R.id.total_items );
        cartTotalItemsPrice = findViewById( R.id.total_items_amount );
        cartDeliveryCharge = findViewById( R.id.delivery_amount );
        cartSavedAmount = findViewById( R.id.saving_amounts );
        cartTotalAmount1 = findViewById( R.id.my_cart_total_amounts1 );
        cartTotalAmount2 = findViewById( R.id.my_cart_total_amounts2 );
        continueProcess = findViewById( R.id.my_cart_continue_btn );
        // ToolBar...
        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( false );
            getSupportActionBar().setTitle( R.string.app_name );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){ }

        setProductData();

        // Product Quantity...
        productQuantity.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuantityWithDialog();
            }
        } );

        // continue....
        continueProcess.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( productDetailTempList.get( 6 ).equals( "IN_STOCK" )){
                    if (currentUser == null){
                        dialogsClass.signInUpDialog( BUY_NOW_ACTIVITY );
                    }else{
                        showConditionDialog();
                    }
                }else{
                    // Out Of Stock
                    Toast.makeText( BuyNowActivity.this, "Sorry .! Product is not in stocks.!", Toast.LENGTH_SHORT ).show();
                }
            }
        } );

    }

    private void setProductData(){
        dialog.show();
//        productDetailTempList.add( 7, "1" );

        if (productDetailTempList.size() != 0 ){
            // Image Set...
            Glide.with( this ).load( productDetailTempList.get( 1 ) )
                    .apply( new RequestOptions().placeholder( R.drawable.squre_image_placeholder) ).into( productImage );
            // other...
            productName.setText( productDetailTempList.get( 2 ) );
            productPrice.setText( "Rs." + productDetailTempList.get( 3 ) + "/-" );
            productCutPrice.setText( "Rs." + productDetailTempList.get( 4 ) + "/-" );
            if ( productDetailTempList.get( 5 ).equals( "COD" ) ){
                // COD Available
                productCOD.setText( "COD available" );
            }else{
                productCOD.setText( "COD not available" );
                productCOD.setTextColor( getResources().getColor( R.color.colorRed ) );
            }
            if ( productDetailTempList.get( 6 ).equals( "IN_STOCK" )){
                // in stock
                productStock.setText( "In Stock" );
            }else{
                // Out Of Stock
                productStock.setText( "Out of Stock" );
//                continueProcess.setEnabled( false );
            }

            setAmountData( buyNowQty );
            dialog.dismiss();
        }else {
            dialog.dismiss();
        }
    }

    private void setAmountData(int Qty){
        if (productDetailTempList.size() != 0 ){
            productQuantity.setText( "QTY: " + Qty + " " + productDetailTempList.get( 7 ) );

            cartTotalItems.setText( "(" + Qty + ")" );
            // Set Price according to quantity...
            productPrice.setText( "Rs."+ getPriceValue( Qty,  productDetailTempList.get( 3 ) ) +"/-" );
            cartTotalItemsPrice.setText( "Rs."+ getPriceValue( Qty,  productDetailTempList.get( 3 ) ) +"/-" );
            productCutPrice.setText( "Rs." + getPriceValue( Qty, productDetailTempList.get( 4 ) )  + "/-" );

            cartTotalAmount1.setText( "Rs." + getTotalAmount( Qty ) +"/-" );
            cartTotalAmount2.setText( "Rs." + getTotalAmount( Qty ) +"/-" );

            cartSavedAmount.setText( "You have save Rs." +savingData( Qty )+ "/- on this shopping..." );

//            if ( getCartDeliveryCharge( getPriceValue( Qty,  productDetailTempList.get( 3 ) )  ) == 0){
//                cartDeliveryCharge.setText( "As per Company" );
////                cartDeliveryCharge.setText( "Free" );
//            }else{
////                cartDeliveryCharge.setText( "Rs."+ getCartDeliveryCharge( getPriceValue( Qty,  productDetailTempList.get( 3 ) )  ) + "/-" );
//            }
            cartDeliveryCharge.setText( "As per Company" );

        }
    }

    private int getCartDeliveryCharge( String itemsTotalAmount){
        int deliveryAmt = 0;
//        if ( Integer.parseInt( itemsTotalAmount ) < 500 ){
//            deliveryAmt = 50;
//        }
        return deliveryAmt;
    }
    private int getTotalAmount(int Qty){
        String tempamt = getPriceValue( Qty, productDetailTempList.get( 3 ) );
        int totalAmt = Integer.parseInt( tempamt ) + getCartDeliveryCharge( tempamt );
        StaticValues.TOTAL_BILL_AMOUNT = totalAmt;
        StaticValues.DELIVERY_AMOUNT = getCartDeliveryCharge( tempamt );
        return totalAmt;
    }
    private String getPriceValue(int qty, String price){
        if (qty == 1){
            return price;
        }else if (qty > 1){
            int tempP = Integer.parseInt( price ) * qty;
            return Integer.toString( tempP );
        }
        return null;
    }
    private String savingData(int Qty){
        int saveAmt = Integer.parseInt( productDetailTempList.get( 4 ) ) - Integer.parseInt( productDetailTempList.get( 3 ) );
        return String.valueOf( saveAmt * Qty );
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ((item.getItemId() == android.R.id.home)){
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private void setQuantityWithDialog() {

        /// Sample Button click...
        final Dialog quantityDialog = new Dialog( this );
        quantityDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        quantityDialog.setContentView( R.layout.dialog_quantity );
        quantityDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        quantityDialog.setCancelable( false );
        Button okBtn = quantityDialog.findViewById( R.id.quantity_ok_btn );
        final Button CancelBtn = quantityDialog.findViewById( R.id.quantity_cancel_btn );
        final EditText quantityText = quantityDialog.findViewById( R.id.quantity_editText );

        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( TextUtils.isEmpty(quantityText.getText().toString().trim())) {
                    quantityText.setError( "Please Enter Quantity..!" );
                } else
                    if ( Integer.parseInt( quantityText.getText().toString().trim() ) < 1 ) {
                    quantityText.setError( "Quantity can not be less than 1" );
                } else
                    if ( Integer.parseInt( quantityText.getText().toString().trim() ) == buyNowQty ) {
                    quantityDialog.dismiss();
                } else
                    {
                        quantityDialog.dismiss();
                        buyNowQty = Integer.parseInt( quantityText.getText().toString().trim() );
                        setAmountData( buyNowQty );
                        productQuantity.setText( "QTY: " + buyNowQty + " " + productDetailTempList.get( 7 ) );
                    }
            }
        } );

        CancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityDialog.dismiss();

            }
        } );
        quantityDialog.show();

    }

    private void showConditionDialog(){

        final Dialog deliveryDialog = new Dialog( this );
        deliveryDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        deliveryDialog.setContentView( R.layout.dialog_delivery_charge );
        deliveryDialog.setCancelable( false );

        ImageButton closeBtn = deliveryDialog.findViewById( R.id.close_btn );
        Button dialogContinueBtn = deliveryDialog.findViewById( R.id.accept_and_continue_btn );

        closeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryDialog.dismiss();
            }
        } );

        dialogContinueBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressIntent = new Intent( BuyNowActivity.this, MyAddressesActivity.class );
                myAddressIntent.putExtra( "MODE", SELECT_ADDRESS );
                startActivity( myAddressIntent );
                deliveryDialog.dismiss();
            }
        } );

        deliveryDialog.show();

    }


}
