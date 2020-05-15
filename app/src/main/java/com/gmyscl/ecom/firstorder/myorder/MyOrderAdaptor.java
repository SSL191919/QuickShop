package com.gmyscl.ecom.firstorder.myorder;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
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

import java.util.List;

public class MyOrderAdaptor extends RecyclerView.Adapter<MyOrderAdaptor.ViewHolder> {

    private List<MyOrderModel> myOrderModelList;

    public MyOrderAdaptor(List <MyOrderModel> myOrderModelList) {
        this.myOrderModelList = myOrderModelList;
    }

    @NonNull
    @Override
    public MyOrderAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.my_order_recycler_item, parent, false );
        return new MyOrderAdaptor.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdaptor.ViewHolder holder, int position) {
        String myOrderImageLink = myOrderModelList.get( position ).getMyOrderImageLink();
        String myOrderItemName = myOrderModelList.get( position ).getMyOrderItemName();
        String myOrderDeliveryTime = myOrderModelList.get( position ).getMyOrderDeliveryStatus();
        holder.setData( myOrderImageLink, myOrderItemName, myOrderDeliveryTime, position );
    }

    @Override
    public int getItemCount() {
        return myOrderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView myOrderImage;
        private TextView myOrderIndicator;
        private TextView myOrderName;
        private TextView myOrderDeliveryTime;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            myOrderImage = itemView.findViewById( R.id.my_order_item_img );
            myOrderIndicator = itemView.findViewById( R.id.my_order_indicater );
            myOrderName = itemView.findViewById( R.id.my_order_item_name );
            myOrderDeliveryTime = itemView.findViewById( R.id.my_order_item_deliverydate );
        }

        private void setData(String img, String name, String orderDate, final int index){

            Glide.with( itemView.getContext() ).load( img )
                    .apply( new RequestOptions().placeholder( R.drawable.squre_image_placeholder) ).into( myOrderImage );

            myOrderName.setText( name );
            if (orderDate.equals( "Cancelled" )){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    myOrderIndicator.setBackgroundTintList(
                            ColorStateList.valueOf( itemView.getContext().getResources().getColor(  R.color.colorRed ) ) );
                }
            }
            myOrderDeliveryTime.setText( orderDate );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent orderDetailIntent = new Intent( itemView.getContext(), OrderDetailsActivity.class);
                    // Get Order Id when User Click on Order...
                    orderDetailIntent.putExtra( "ORDER_ID", myOrderModelList.get( index ).getMyOrderId());
                    itemView.getContext().startActivity( orderDetailIntent );
                }
            } );

        }

    }
}
