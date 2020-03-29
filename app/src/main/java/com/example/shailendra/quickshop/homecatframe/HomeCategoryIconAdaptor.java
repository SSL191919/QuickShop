package com.example.shailendra.quickshop.homecatframe;

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
import com.example.shailendra.quickshop.R;
import com.example.shailendra.quickshop.categoryItemClass.CategoriesItemsViewActivity;

import java.util.ArrayList;
import java.util.List;


public class HomeCategoryIconAdaptor extends RecyclerView.Adapter <HomeCategoryIconAdaptor.ViewHolder> {

    private List <HomeCategoryIconModel> homeCategoryIconModelList;

    public HomeCategoryIconAdaptor(List <HomeCategoryIconModel> homeCategoryIconModelList) {
        this.homeCategoryIconModelList = homeCategoryIconModelList;
    }
    @NonNull
    @Override
    public HomeCategoryIconAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.home_category_icon_recycler_item, parent, false );
        return new HomeCategoryIconAdaptor.ViewHolder( view );
    }
    @Override
    public void onBindViewHolder(@NonNull HomeCategoryIconAdaptor.ViewHolder holder, int position) {
        String icon = homeCategoryIconModelList.get( position ).getCategoryIcon();
        String name = homeCategoryIconModelList.get( position ).getCategoryName();
        holder.setData( icon, name, position );
    }
    @Override
    public int getItemCount() {
        return homeCategoryIconModelList.size();
    }

    // Class View Holder...-------------------
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView catIcon;
        private TextView catName;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            catIcon = itemView.findViewById( R.id.category_icon );
            catName = itemView.findViewById( R.id.category_name );
        }

        private void setData(String icon, final String name, final int position) {
            catName.setText( name );
            Glide.with( itemView.getContext() ).load( icon ).apply( new RequestOptions()
                    .placeholder( R.drawable.squre_image_placeholder ) ).into( catIcon );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // We are using ViewAllActivity to show products based on categories...
                    if (name.toUpperCase() != "HOME"){
                        Intent viewAllIntent = new Intent( itemView.getContext(), CategoriesItemsViewActivity.class );
                        viewAllIntent.putExtra( "TITLE", name );
                        viewAllIntent.putExtra( "CAT_INDEX", position );
                        itemView.getContext().startActivity( viewAllIntent );
                    }
                }
            } );
        }

    }

}
