package com.gmyscl.ecom.firstorder.homecatframe;

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
import com.gmyscl.ecom.firstorder.categoryItemClass.CategoriesItemsViewActivity;

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
        String icon = homeCategoryIconModelList.get( position+1 ).getCategoryIcon();
        String name = homeCategoryIconModelList.get( position+1 ).getCategoryName();
        holder.setData( icon, name, position+1 );
    }
    @Override
    public int getItemCount() {
        return homeCategoryIconModelList.size() - 1;
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
            catName.setVisibility( View.GONE );
            if (name.toUpperCase().equals( "HOME" )){
                itemView.setVisibility( View.GONE );
                catIcon.setImageResource( R.drawable.ic_home_black_24dp );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    catIcon.setImageTintList( ColorStateList.valueOf( itemView.getResources().getColor( R.color.colorPrimary ) ) );
                }
            }else{
                itemView.setVisibility( View.VISIBLE );
                Glide.with( itemView.getContext() ).load( icon ).apply( new RequestOptions()
                        .placeholder( R.drawable.squre_image_placeholder ) ).into( catIcon );
            }

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // We are using ViewAllActivity to show products based on categories...
                    if ( ! name.toUpperCase().equals( "HOME" )){
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
