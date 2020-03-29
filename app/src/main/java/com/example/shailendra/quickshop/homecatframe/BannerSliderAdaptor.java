package com.example.shailendra.quickshop.homecatframe;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.quickshop.R;

import java.util.List;

public class BannerSliderAdaptor extends PagerAdapter {

    List<BannerSliderModel> bannerSliderModelList;

    public BannerSliderAdaptor(List <BannerSliderModel> bannerSliderModelList) {
        this.bannerSliderModelList = bannerSliderModelList;
    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from( container.getContext() ).inflate( R.layout.banner_slider_item, container, false );
        ImageView bannerImage = view.findViewById( R.id.banner_slider_Images );
        ConstraintLayout bannerSliderContainer = view.findViewById( R.id.banner_slider_container );
        // Set Backgrgound color...
        bannerSliderContainer.setBackgroundTintList(
                ColorStateList.valueOf( Color.parseColor( bannerSliderModelList.get( position ).getBackgroundColor() ) ) );
        // Set Image Resource...
        Glide.with( view.getContext() ).load( bannerSliderModelList.get( position ).getBannerImage() )
                .apply( new RequestOptions().placeholder( R.drawable.banner_image_placeholder ) ).into( bannerImage );

        container.addView( view, 0 );
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (View)object );
    }

    @Override
    public int getCount() {
        return bannerSliderModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
