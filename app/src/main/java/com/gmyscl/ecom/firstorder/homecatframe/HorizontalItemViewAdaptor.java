package com.gmyscl.ecom.firstorder.homecatframe;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.productdetails.ProductDetails;

import java.util.List;

public class HorizontalItemViewAdaptor extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    private static final int HOME_HORIZONTAL_LAYOUT = 0;
    private static final int VIEW_ALL_HORIZONTAL_LAYOUT = 1;

    private List <HorizontalItemViewModel> horizontalItemViewModelList;

    public HorizontalItemViewAdaptor(List <HorizontalItemViewModel> horizontalItemViewModelList) {
        this.horizontalItemViewModelList = horizontalItemViewModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (horizontalItemViewModelList.get( position ).getHrViewType()) {
            case 0:
                // TODO : horizontal home list ...
                return HOME_HORIZONTAL_LAYOUT;
            case 1:
                // TODO : View all horizontal...
                return VIEW_ALL_HORIZONTAL_LAYOUT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case HOME_HORIZONTAL_LAYOUT:
                View hrView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.horizontal_itemview_item, parent, false );
                return new HomeHorizontalViewHolder( hrView );
            case VIEW_ALL_HORIZONTAL_LAYOUT:
                View viewAllView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.horizontal_products_view_all_layout, parent, false );
                return new ViewAllHorizontalViewHolder( viewAllView );
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String productId = horizontalItemViewModelList.get( position ).getHrProductId();
        String imgLink = horizontalItemViewModelList.get( position ).getHrProductImage();
        String name = horizontalItemViewModelList.get( position ).getHrProductName();
        String price = horizontalItemViewModelList.get( position ).getHrProductPrice();
        String cutPrice = horizontalItemViewModelList.get( position ).getHrProductCutPrice();
        switch (horizontalItemViewModelList.get( position ).getHrViewType()) {
            case HOME_HORIZONTAL_LAYOUT:
                ((HomeHorizontalViewHolder) holder).setHomeHrProduct( productId, imgLink, name, price, cutPrice );
                break;
            case VIEW_ALL_HORIZONTAL_LAYOUT:
                Boolean codInfo = horizontalItemViewModelList.get( position ).getHrCodInfo();
                long stockInfo = horizontalItemViewModelList.get( position ).getHrStockInfo();
                ((ViewAllHorizontalViewHolder) holder).setHorizontalProducts( productId, imgLink, name, price, cutPrice, codInfo, stockInfo );
                break;
            default:
                return;
        }


//        holder.setHrProductImage( imgLink, name, price, cutPrice );
    }

    @Override
    public int getItemCount() {

        if (horizontalItemViewModelList.size() > 8) {
            return 8;
        } else {
            return horizontalItemViewModelList.size();
        }
    }

    public class HomeHorizontalViewHolder extends RecyclerView.ViewHolder {

        ImageView hrProductImage;
        TextView hrProductName;
        TextView hrProductPrice;
        TextView hrProductCutPrice;
        TextView hrProductOffPercentage;

        public HomeHorizontalViewHolder(@NonNull View itemView) {
            super( itemView );

            hrProductImage = itemView.findViewById( R.id.hr_product_image );
            hrProductName = itemView.findViewById( R.id.hr_product_name );
            hrProductPrice = itemView.findViewById( R.id.hr_product_price );
            hrProductCutPrice = itemView.findViewById( R.id.hr_product_cut_price );
            hrProductOffPercentage = itemView.findViewById( R.id.hr_off_percentage );

        }

        private void setHomeHrProduct(final String productId, String imgLink, String name, String price, String cutPrice) {

            hrProductName.setText( name );
            hrProductPrice.setText( "Rs." + price + "/-" );
            hrProductCutPrice.setText( "Rs." + cutPrice + "/-" );

            Glide.with( itemView.getContext() ).load( imgLink ).apply( new RequestOptions()
                    .placeholder( R.drawable.squre_image_placeholder ) ).into( hrProductImage );


            int perOff = ((Integer.parseInt( cutPrice ) - Integer.parseInt( price )) * 100) / Integer.parseInt( cutPrice );
            hrProductOffPercentage.setText( perOff + "% Off" );


            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailIntent = new Intent( itemView.getContext(), ProductDetails.class );
                    productDetailIntent.putExtra( "PRODUCT_ID", productId );
                    itemView.getContext().startActivity( productDetailIntent );
                }
            } );

        }

    }

    public class ViewAllHorizontalViewHolder extends RecyclerView.ViewHolder {
        ImageView hrProductImage;
        TextView hrProductName;
        TextView hrProductPrice;
        TextView hrProductCutPrice;
        TextView hrProductOffPercentage;
        TextView hrProductCodText;
        TextView hrProductStockInfo;

        public ViewAllHorizontalViewHolder(@NonNull View itemView) {
            super( itemView );
            hrProductImage = itemView.findViewById( R.id.hr_viewAll_product_image );
            hrProductName = itemView.findViewById( R.id.hr_viewAll_product_name );
            hrProductPrice = itemView.findViewById( R.id.hr_viewAll_product_price );
            hrProductCutPrice = itemView.findViewById( R.id.hr_viewAll_product_cut_price );
            hrProductOffPercentage = itemView.findViewById( R.id.hr_viewAll_product_off_per );
            hrProductCodText = itemView.findViewById( R.id.hr_viewAll_product_cod_text );
            hrProductStockInfo = itemView.findViewById( R.id.hr_viewAll_product_stock_info );
        }

        private void setHorizontalProducts(final String productId, String imgLink, String name, String price, String cutPrice, Boolean cod, long stockInfo) {
            hrProductName.setText( name );
            hrProductPrice.setText( "Rs." + price + "/-" );
            hrProductCutPrice.setText( "Rs." + cutPrice + "/-" );
            if (cod) {
                hrProductCodText.setText( "Cash on Delivery Available" );
                hrProductCodText.setVisibility( View.VISIBLE );
            } else {
                hrProductCodText.setVisibility( View.INVISIBLE );
            }
            if (stockInfo > 0) {
                hrProductStockInfo.setText( "in Stock" );
            } else {
                hrProductStockInfo.setText( "Out of Stock" );
            }

            Glide.with( itemView.getContext() ).load( imgLink ).apply( new RequestOptions()
                    .placeholder( R.drawable.squre_image_placeholder ) ).into( hrProductImage );

            int perOff = ((Integer.parseInt( cutPrice ) - Integer.parseInt( price )) * 100) / Integer.parseInt( cutPrice );
            hrProductOffPercentage.setText( perOff + "% Off" );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailIntent = new Intent( itemView.getContext(), ProductDetails.class );
                    productDetailIntent.putExtra( "PRODUCT_ID", productId );
                    itemView.getContext().startActivity( productDetailIntent );
                }
            } );

        }
    }

}
