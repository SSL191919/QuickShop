package com.example.shailendra.quickshop.retailerdata;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.shailendra.quickshop.R;

import java.util.List;

public class RetRecyclerListAdaptor extends RecyclerView.Adapter <RetRecyclerListAdaptor.RetRecyclerListViewHolder> {

    List<RetRecyclerListModel> retRecyclerListModelList;

    public RetRecyclerListAdaptor(List <RetRecyclerListModel> retRecyclerListModelList) {
        this.retRecyclerListModelList = retRecyclerListModelList;
    }


    @NonNull
    @Override
    public RetRecyclerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.x_item_show_in_list,null);

        return new RetRecyclerListViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull RetRecyclerListViewHolder holder, int position) {
        int imageResource = retRecyclerListModelList.get( position ).getImageResource();
        String itemNameStr = retRecyclerListModelList.get( position ).getItemName();
        String itemDiscrStr = retRecyclerListModelList.get( position ).getItemDiscrption();

        holder.setRecyclerListContent(imageResource,itemNameStr,itemDiscrStr);
    }

    @Override
    public int getItemCount() {
        return retRecyclerListModelList.size();
    }

    public class RetRecyclerListViewHolder extends RecyclerView.ViewHolder{

        private ImageView itemImage;
        private TextView itemName, itemDiscription;

        public RetRecyclerListViewHolder(View itemView) {
            super( itemView );
            itemImage = itemView.findViewById( R.id.x_item_list_Img );
            itemName = itemView.findViewById( R.id.x_item_list_Name );
            itemDiscription = itemView.findViewById( R.id.x_item_list_discr );
        }

        public void setRecyclerListContent(int imageResource,String itemNameStr,String itemDiscrStr){
            itemImage.setImageResource( imageResource );
            itemName.setText( itemNameStr );
            itemDiscription.setText( itemDiscrStr );
        }
    }

}
