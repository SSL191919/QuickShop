package com.example.shailendra.quickshop.retailerdata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shailendra.quickshop.R;

import java.util.List;

public class X_ret_grid_adaptor extends BaseAdapter {

    List<X_OfferRecyclerViewModel> x_ret_grid_List;

    public X_ret_grid_adaptor(List <X_OfferRecyclerViewModel> x_ret_grid_List) {
        this.x_ret_grid_List = x_ret_grid_List;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if(convertView == null){
            view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.x_offer_recycler_view_item, null );
            ImageView gridImageView = view.findViewById( R.id.x_offer_item_image );
            TextView gridProductPrice = view.findViewById( R.id.x_offer_item_price );
            TextView gridProductName = view.findViewById( R.id.x_offer_item_name );

            gridImageView.setImageResource( x_ret_grid_List.get( position ).getxOfferItemImage() );
            gridProductPrice.setText( x_ret_grid_List.get( position ).getxOfferItemPrice() );
            gridProductName.setText( x_ret_grid_List.get( position ).getxOfferItemName() );

        }else {
            view = convertView;
        }
        return  view;
    }
}
