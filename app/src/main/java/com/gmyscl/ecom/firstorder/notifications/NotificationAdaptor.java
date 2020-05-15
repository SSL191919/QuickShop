package com.gmyscl.ecom.firstorder.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmyscl.ecom.firstorder.R;

import java.util.List;

import static com.gmyscl.ecom.firstorder.database.StaticValues.NOTIFY_ORDER_CANCEL;
import static com.gmyscl.ecom.firstorder.database.StaticValues.NOTIFY_ORDER_UPDATE;

public class NotificationAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NotificationModel> notificationModelList;

    public NotificationAdaptor(List <NotificationModel> notificationModelList) {
        this.notificationModelList = notificationModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (notificationModelList.get( position ).getNotifyType()){
            case NOTIFY_ORDER_UPDATE:
                return NOTIFY_ORDER_UPDATE;
            case NOTIFY_ORDER_CANCEL:
                return NOTIFY_ORDER_CANCEL;
            default:
                return -1;
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case NOTIFY_ORDER_UPDATE:
                View orderNotify = LayoutInflater.from( parent.getContext() ).inflate( R.layout.notifications_layout, parent, false );
                return new OrderUpdateNotification( orderNotify );
            case NOTIFY_ORDER_CANCEL:
            default:
                return null;
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (notificationModelList.get( position ).getNotifyType()){
            case NOTIFY_ORDER_UPDATE:
                boolean notifyIsRead = notificationModelList.get( position ).isNotifyIsRead();
                String notifyOrderId = notificationModelList.get( position ).getNotifyOrderId();
                String notifyOrderHeading = notificationModelList.get( position ).getNotifyOrderHeading();
                String notifyOrderMessage = notificationModelList.get( position ).getNotifyOrderMessage();
                (( NotificationAdaptor.OrderUpdateNotification ) holder ).setData( notifyIsRead, notifyOrderId, notifyOrderHeading, notifyOrderMessage  );
                break;
            case NOTIFY_ORDER_CANCEL:
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }


    public class OrderUpdateNotification extends RecyclerView.ViewHolder {
        private LinearLayout layout;
        private ImageView notificationImage;
        private TextView notificationMessage;
        private TextView notificationHeading;

        public OrderUpdateNotification(@NonNull View itemView) {
            super( itemView );
            layout = itemView.findViewById( R.id.layout );
            notificationImage = itemView.findViewById( R.id.notification_img );
            notificationMessage = itemView.findViewById( R.id.notification_text );
            notificationHeading = itemView.findViewById( R.id.notify_heading );
        }

        private void setData(boolean notifyIsRead, String notifyOrderId, String notifyOrderHeading, String notifyOrderMessage){

//            Glide.with( itemView.getContext() ).load( imgLink )
//                    .apply( new RequestOptions().placeholder( R.drawable.first_logo) ).into( notificationImage );
            notificationImage.setImageResource( R.mipmap.logo );

            notificationHeading.setText( notifyOrderHeading );
            notificationMessage.setText( notifyOrderMessage );
            if (notifyIsRead){
                layout.setAlpha( 0.5f );
            }else{
                layout.setAlpha( 1f );
            }
        }

    }



}
