package com.example.shailendra.quickshop.retailerdata;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shailendra.quickshop.R;

import java.util.ArrayList;
import java.util.List;

public class X_OfferRecyclerViewAdaptor extends RecyclerView.Adapter <X_OfferRecyclerViewAdaptor.ViewHolder> {


    private List<X_OfferRecyclerViewModel> offerRecyclerViewModelList;

    public X_OfferRecyclerViewAdaptor(List <X_OfferRecyclerViewModel> offerRecyclerViewModelList) {
        this.offerRecyclerViewModelList = offerRecyclerViewModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.x_offer_recycler_view_item, viewGroup, false );
        ViewHolder vH = new ViewHolder( view );
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        int resource = offerRecyclerViewModelList.get( position ).getxOfferItemImage();
        String price = offerRecyclerViewModelList.get( position ).getxOfferItemPrice();
        String name = offerRecyclerViewModelList.get( position ).getxOfferItemName();

        viewHolder.setxOfferItemImage( resource );
        viewHolder.setxOfferItemPrice( price );
        viewHolder.setxOfferItemName( name );

//        X_OfferRecyclerViewModel currItem = offerRecyclerViewModelList.get( position );
//        viewHolder.xOfferItemImage.setImageResource( resource );

    }


    @Override
    public int getItemCount() {
        if(offerRecyclerViewModelList.size()>4){
            return 4;
        }else {
            return offerRecyclerViewModelList.size();
        }
    }

// ==========================================================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView xOfferItemImage;
        private TextView xOfferItemPrice, xOfferItemName;

        public ViewHolder(View itemView) {
            super( itemView );
            xOfferItemImage = itemView.findViewById( R.id.x_offer_item_image );
            xOfferItemPrice = itemView.findViewById( R.id.x_offer_item_price );
            xOfferItemName = itemView.findViewById( R.id.x_offer_item_name );
        }

        private void setxOfferItemImage(int resource) {
            xOfferItemImage.setImageResource( resource );
        }

        private void setxOfferItemPrice(String price) {
            xOfferItemPrice.setText( price );
        }

        private void setxOfferItemName(String name) {
            xOfferItemName.setText( name );
        }
    }
}
