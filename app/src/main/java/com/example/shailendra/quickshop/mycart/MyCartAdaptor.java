package com.example.shailendra.quickshop.mycart;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.quickshop.DialogsClass;
import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.database.DBquery;
import com.example.shailendra.quickshop.database.StaticValues;
import com.example.shailendra.quickshop.productdetails.ProductDetails;

import java.util.List;

public class MyCartAdaptor extends RecyclerView.Adapter {

    private List<MyCartItemModel> myCartItemModelList;

    public MyCartAdaptor(List <MyCartItemModel> myCartItemModelList) {
        this.myCartItemModelList = myCartItemModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (myCartItemModelList.get( position ).getType()){
            case 0:
                return MyCartItemModel.CART_TYPE_ITEMS;
            case 1:
                return MyCartItemModel.CART_TYPE_TOTAL_PRICE;
            default:
                    return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case MyCartItemModel.CART_TYPE_ITEMS:
                View cartItemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.my_cart_item, parent, false );
                return new MyCartViewHolder( cartItemView );

            case MyCartItemModel.CART_TYPE_TOTAL_PRICE:
                View cartTotalAmountView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.my_cart_total_price, parent, false );
                return new MyCartTotalAmountHolder( cartTotalAmountView );
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (myCartItemModelList.get( position ).getType()){
            case MyCartItemModel.CART_TYPE_ITEMS:
                MyCartItemModel myCartItemModel1 = myCartItemModelList.get( position );

                String productID = myCartItemModel1.getProductId();
                String imgLink = myCartItemModel1.getProductImage();
                String name = myCartItemModel1.getProductName();
                String price = myCartItemModel1.getProductPrice();
                String cutPrice = myCartItemModel1.getProductCutPrice();
                int productQuantity = myCartItemModel1.getProductQuantity();

                ((MyCartViewHolder) holder).setCartData( productID, imgLink, name, price, cutPrice, productQuantity );
               break;
            case MyCartItemModel.CART_TYPE_TOTAL_PRICE:
                int totalItems = 0;
                int totalItemsPrice = 0;
                int deliveryCharge = 0;
                int totalCutPrice = 0;
                int productQty = 0;
                for (int x = 0; x < myCartItemModelList.size(); x++){
                    if (myCartItemModelList.get( x ).getType() == MyCartItemModel.CART_TYPE_ITEMS){
                        totalItems++;
                        productQty = myCartItemModelList.get( x ).getProductQuantity();
                        totalItemsPrice = totalItemsPrice + productQty * Integer.parseInt(myCartItemModelList.get( x ).getProductPrice());
                        totalCutPrice = totalCutPrice + productQty * Integer.parseInt( myCartItemModelList.get( x ).getProductCutPrice() );
                    }
                }
                if (totalItemsPrice < 500 ){
                    deliveryCharge = 40;
                }
                int savedAmount = totalCutPrice - totalItemsPrice;

                ((MyCartTotalAmountHolder) holder).setCartTotalAmountData( totalItems, totalItemsPrice, deliveryCharge, savedAmount);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return myCartItemModelList.size();
    }

    public class MyCartViewHolder extends RecyclerView.ViewHolder {
        private DialogsClass dialogsClass;
        private ImageView productImage;
        private TextView productName;
        private TextView productPrice;
        private TextView productCutPrice;
        private TextView removeItemBtn;
        private TextView productQuantity;

        public MyCartViewHolder(@NonNull final View itemView) {
            super( itemView );

            productImage = itemView.findViewById( R.id.product_image );
            productName = itemView.findViewById( R.id.product_name );
            productPrice = itemView.findViewById( R.id.product_price );
            productCutPrice = itemView.findViewById( R.id.product_cut_price );
            removeItemBtn = itemView.findViewById( R.id.remove_item_from_cart );
            productQuantity = itemView.findViewById( R.id.item_quntity );
            dialogsClass = new DialogsClass( itemView.getContext() );

        }

        private void setCartData(final String productId, String imgLink, String name, final String price, final String cutPrice, final int quantity ){

            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.squre_image_placeholder) ).into( productImage );

            productQuantity.setText( "Qty: " + Integer.toString( quantity ) );
            productName.setText( name );
            // Set Price according to quantity...
            productPrice.setText( "Rs."+ getPriceValue( quantity, price ) +"/-" );
            productCutPrice.setText( "Rs." + getPriceValue( quantity, cutPrice )  + "/-" );

            removeItemBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : call method...
                    Dialog dialog = dialogsClass.progressDialog( itemView.getContext() );
                    dialog.show();
                    removeItemBtn.setEnabled( false );
                    // if item in cart then, Code for remove from cart
                    DBquery.removeItemFromCart( getIndexOf( productId ), itemView.getContext(), StaticValues.REMOVE_ONE_ITEM, dialog );
                    removeItemBtn.setEnabled( true );
                }
            } );

            productQuantity.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : Create method...
                    dialogsClass.getQuantityDialog( getIndexOf( productId ), itemView.getContext() );
                }
            } );

        }

        private int getIndexOf(String proId){
            int listIndex = -1;
            for ( int i = 0; i < DBquery.myCartCheckList.size(); i++) {
                if (DBquery.myCartCheckList.get( i ).getProductId().equals( proId )){
                    listIndex = i;
                }
            }
            return listIndex;
        }

        private String  getPriceValue(int qty, String price){
            if (qty == 1){
                return price;
            }else if (qty > 1){
                int tempP = Integer.parseInt( price ) * qty;
                return Integer.toString( tempP );
            }
            return null;
        }
    }

    public class MyCartTotalAmountHolder extends RecyclerView.ViewHolder{

        private TextView cartTotalItems;
        private TextView cartTotalItemsPrice;
        private TextView cartDeliveryCharge;
        private TextView cartSavedAmount;
        private TextView cartTotalAmount;

        public MyCartTotalAmountHolder(@NonNull View itemView) {
            super( itemView );
            cartTotalItems = itemView.findViewById( R.id.total_items );
            cartTotalItemsPrice = itemView.findViewById( R.id.total_items_amount );
            cartDeliveryCharge = itemView.findViewById( R.id.delivery_amount );
            cartSavedAmount = itemView.findViewById( R.id.my_cart_sving_amounts );
            cartTotalAmount = itemView.findViewById( R.id.my_cart_total_amounts1 );
        }

        private void setCartTotalAmountData( int totalItems, int totalItemsPrice, int deliveryCharge, int savedAmount){
            cartTotalItems.setText( "(" + Integer.toString( totalItems ) + ")" );
            cartTotalItemsPrice.setText( "Rs." + totalItemsPrice + "/-");
            if (deliveryCharge > 0){
                cartDeliveryCharge.setText( "Rs." + deliveryCharge + "/-" );
            }else {
                cartDeliveryCharge.setText( "Free" );
            }
            if(savedAmount > 0){
               cartSavedAmount.setVisibility( View.VISIBLE );
               cartSavedAmount.setText( "You have saved Rs."+ savedAmount + "/- on this shopping..!" );
           }else {
               cartSavedAmount.setVisibility( View.INVISIBLE );
           }
            cartTotalAmount.setText( "Rs." + Integer.toString( totalItemsPrice + deliveryCharge ) +"/-" );

            MyCartFragment.myCartTotalAmounts2.setText( "Rs." + Integer.toString( totalItemsPrice + deliveryCharge ) +"/-" );

            // Update Bill amount and Delivery Charge...
            StaticValues.TOTAL_BILL_AMOUNT = totalItemsPrice + deliveryCharge;
            StaticValues.DELIVERY_AMOUNT = deliveryCharge;

        }
    }



}
