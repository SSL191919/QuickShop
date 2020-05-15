package com.gmyscl.ecom.firstorder.mywishlist;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gmyscl.ecom.firstorder.DialogsClass;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.buyprocess.BuyNowActivity;
import com.gmyscl.ecom.firstorder.database.DBquery;
import com.gmyscl.ecom.firstorder.database.StaticValues;

import java.util.List;

import static com.gmyscl.ecom.firstorder.database.StaticValues.productDetailTempList;

public class MyWishListAdaptor extends RecyclerView.Adapter<MyWishListAdaptor.ViewHolder>{

    private List<MyWishListModel> myWishListModelList;
    private Boolean isWishList;
    private int lastPosition = -1;

    public MyWishListAdaptor(List <MyWishListModel> myWishListModelList, Boolean isWishList) {
        this.myWishListModelList = myWishListModelList;
        this.isWishList = isWishList;
    }

    @NonNull
    @Override
    public MyWishListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.my_wish_list_recycler_item, parent, false );
        return new MyWishListAdaptor.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MyWishListAdaptor.ViewHolder holder, int position) {
         String wishImage = myWishListModelList.get( position ).getWishImage();
         String wishName = myWishListModelList.get( position ).getWishName();
         String wishPrice = myWishListModelList.get( position ).getWishPrice();
         String wishCutPrice = myWishListModelList.get( position ).getWishCutPrice();
         holder.setData(wishImage, wishName, wishPrice, wishCutPrice, position);

         if (lastPosition < position){
             Animation animation = AnimationUtils.loadAnimation( holder.itemView.getContext(), R.anim.nav_default_exit_anim );
             holder.itemView.setAnimation( animation );
             lastPosition = position;
         }

    }

    @Override
    public int getItemCount() {
        return myWishListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView wishImage;
        private TextView wishName;
        private TextView wishPrice;
        private TextView wishCutPrice;
        private TextView wishPerOff;
        private Button wishBuyNowBtn;
        private Button wishRemoveBtn;
        private LinearLayout buyRemoveLayout;
        private TextView codTextShoe;

        Dialog dialog;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            wishImage = itemView.findViewById( R.id.wish_item_image );
            wishName = itemView.findViewById( R.id.wish_item_name );
            wishPrice = itemView.findViewById( R.id.wish_item_price );
            wishCutPrice = itemView.findViewById( R.id.wish_item_cut_price );
            wishPerOff = itemView.findViewById( R.id.wish_item_off_percentage );
            wishBuyNowBtn = itemView.findViewById( R.id.wish_item_buy );
            wishRemoveBtn = itemView.findViewById( R.id.wish_item_delete );
            buyRemoveLayout = itemView.findViewById(R.id.buy_delete_layout);
            codTextShoe = itemView.findViewById(R.id.cod_text_show);

            dialog = new DialogsClass().progressDialog( itemView.getContext() );
        }

        private void setData(String image, String name, String price, String cutPrice, final int index){
            Glide.with( itemView.getContext() ).load( image )
                    .apply( new RequestOptions().placeholder( R.drawable.squre_image_placeholder) ).into( wishImage );
//            wishImage.setImageResource( Integer.parseInt( image ) );
            wishName.setText( name );
            wishPrice.setText( "Rs." + price + "/-");
            wishCutPrice.setText( "Rs." + cutPrice + "/-");

            if (isWishList){
                buyRemoveLayout.setVisibility( View.VISIBLE );
                codTextShoe.setVisibility( View.GONE );
            }else {
                buyRemoveLayout.setVisibility( View.GONE );
                codTextShoe.setVisibility( View.VISIBLE );
            }

            // Remove item from WishList...
            wishRemoveBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    wishRemoveBtn.setEnabled( false );
                    DBquery.removeItemFromWishList( index, itemView.getContext(), dialog, StaticValues.BUY_FROM_WISH_LIST );
                }
            } );

            // Buy Now Item...
            wishBuyNowBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    productDetailTempList.clear();

                    // We have to get index where user clicked...
                    int buyIndex = index;
                    // Set All data of Products in tempList...
                    // Add Product ID  in productDetailTempList
                    productDetailTempList.add( 0, myWishListModelList.get( buyIndex ).getWishListProductId() );
                    // Product Image
                    productDetailTempList.add( 1, myWishListModelList.get( buyIndex ).getWishImage() );
                    // Product Name or Full Name
                    productDetailTempList.add( 2, myWishListModelList.get( buyIndex ).getWishName() );
                    //  Product Price
                    productDetailTempList.add( 3, myWishListModelList.get( buyIndex ).getWishPrice() );
                    // Product Cut Price
                    productDetailTempList.add( 4, myWishListModelList.get( buyIndex ).getWishCutPrice() );
                    // Product COD or NO_COD info
                    if ( myWishListModelList.get( buyIndex ).getWishCODinfo() ){
                        productDetailTempList.add( 5, "COD");
                    }else{
                        productDetailTempList.add( 5, "NO_COD");
                    }
                    // Product IN_STOCK or OUT_OF_STOCK info
                    productDetailTempList.add( 6, myWishListModelList.get( buyIndex ).getWishStockInfo());

//                    if(documentSnapshot.get( "product_qty_type" ) != null){
//                        productDetailTempList.add( 7, documentSnapshot.get( "product_qty_type" ).toString()  );
//                    }else{
//                        productDetailTempList.add( 7, ""  );
//                    }

                    // Now we Can Start the BuyNow Activity...
                    Intent buyNowIntent = new Intent( itemView.getContext(), BuyNowActivity.class );
                    itemView.getContext().startActivity( buyNowIntent );
                    // Set value BUY_FROM...
                    StaticValues.BUY_FROM_VALUE = StaticValues.BUY_FROM_WISH_LIST;
                    // Query if DBquery.myOrderList is empty.. so we have to run first this query...
                    if (DBquery.myOrderList.size() == 0){
                        DBquery.orderListQuery( itemView.getContext(), new Dialog( itemView.getContext() ), false );
                    }

                }
            } );


        }

    }



}