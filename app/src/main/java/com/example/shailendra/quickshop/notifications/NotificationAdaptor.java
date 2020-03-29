package com.example.shailendra.quickshop.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.quickshop.R;

import java.util.List;

public class NotificationAdaptor extends RecyclerView.Adapter<NotificationAdaptor.ViewHolder> {

    private List<NotificationModel> notificationModelList;

    public NotificationAdaptor(List <NotificationModel> notificationModelList) {
        this.notificationModelList = notificationModelList;
    }

    @NonNull
    @Override
    public NotificationAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.notifications_layout, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdaptor.ViewHolder holder, int position) {
        String imgLink = notificationModelList.get( position ).getNotificationImg();
        String notification = notificationModelList.get( position ).getNotificationText();
        boolean isRead = notificationModelList.get( position ).isReaded();
        holder.setData( imgLink, notification, isRead );
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView notificationImage;
        private TextView niotificationText;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            notificationImage = itemView.findViewById( R.id.notification_img );
            niotificationText = itemView.findViewById( R.id.notification_text );
        }

        private void setData(String imgLink, String notification, boolean isReaded){

            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.first_logo) ).into( notificationImage );

            niotificationText.setText( notification );
            if (isReaded){
                niotificationText.setAlpha( 0.5f );
            }else{
                niotificationText.setAlpha( 1f );
            }
        }

    }


}
