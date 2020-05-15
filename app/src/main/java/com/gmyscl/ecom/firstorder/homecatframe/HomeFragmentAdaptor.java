package com.gmyscl.ecom.firstorder.homecatframe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gmyscl.ecom.firstorder.R;
import com.gmyscl.ecom.firstorder.activityforitems.ViewAllActivity;
import com.gmyscl.ecom.firstorder.productdetails.ProductDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.gmyscl.ecom.firstorder.activityforitems.ViewAllActivity.gridViewModelListViewAll;
import static com.gmyscl.ecom.firstorder.activityforitems.ViewAllActivity.horizontalItemViewModelListViewAll;
import static com.gmyscl.ecom.firstorder.activityforitems.ViewAllActivity.totalProductsIdViewAll;
import static com.gmyscl.ecom.firstorder.database.StaticValues.BANNER_AD_LAYOUT_CONTAINER;
import static com.gmyscl.ecom.firstorder.database.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static com.gmyscl.ecom.firstorder.database.StaticValues.GRID_ITEM_LAYOUT_CONTAINER;
import static com.gmyscl.ecom.firstorder.database.StaticValues.GRID_PRODUCT_LAYOUT;
import static com.gmyscl.ecom.firstorder.database.StaticValues.HORIZONTAL_ITEM_LAYOUT_CONTAINER;
import static com.gmyscl.ecom.firstorder.database.StaticValues.RECYCLER_PRODUCT_LAYOUT;
import static com.gmyscl.ecom.firstorder.database.StaticValues.STRIP_AD_LAYOUT_CONTAINER;
import static com.gmyscl.ecom.firstorder.homecatframe.HorizontalItemViewModel.hrViewType;

public class HomeFragmentAdaptor extends RecyclerView.Adapter {

    private List <HomeFragmentModel> homeFragmentModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public HomeFragmentAdaptor(List <HomeFragmentModel> homeFragmentModelList) {
        this.homeFragmentModelList = homeFragmentModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homeFragmentModelList.get( position ).getLayoutType()) {
            case BANNER_SLIDER_LAYOUT_CONTAINER:
                return BANNER_SLIDER_LAYOUT_CONTAINER;
            case STRIP_AD_LAYOUT_CONTAINER:
                return STRIP_AD_LAYOUT_CONTAINER;
            case BANNER_AD_LAYOUT_CONTAINER:
                return BANNER_AD_LAYOUT_CONTAINER;
            case HORIZONTAL_ITEM_LAYOUT_CONTAINER:
                return HORIZONTAL_ITEM_LAYOUT_CONTAINER;
            case GRID_ITEM_LAYOUT_CONTAINER:
                return GRID_ITEM_LAYOUT_CONTAINER;
            default:
                return -1;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANNER_SLIDER_LAYOUT_CONTAINER:
                // TODO : banner Slider
                View bannerView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.banner_slider_layout, parent, false );
                return new BannerSliderViewHolder( bannerView );
            case STRIP_AD_LAYOUT_CONTAINER:
                // TODO : Strip ad Slider
                View stripAdView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.strip_ad_layout, parent, false );
                return new StripAdViewHolder( stripAdView );
            case BANNER_AD_LAYOUT_CONTAINER:
                // TODO : Banner ad Slider
                View bannerAdView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.banner_ad_layout, parent, false );
                return new BannerAdViewHolder( bannerAdView );
            case HORIZONTAL_ITEM_LAYOUT_CONTAINER:
                // TODO : Horizontal viewHolder
                View horizontalLayoutView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.horizontal_itemview_layout, parent, false );
                return new HorizontalLayoutViewHolder( horizontalLayoutView );
            case GRID_ITEM_LAYOUT_CONTAINER:
                // TODO : GridLayout viewHolder
                View gridLayoutView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.home_itemview_grid_layout, parent, false );
                return new GridLayoutViewHolder( gridLayoutView );
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homeFragmentModelList.get( position ).getLayoutType()) {
            case BANNER_SLIDER_LAYOUT_CONTAINER:
                List <BannerSliderModel> bannerSliderModelList =
                        homeFragmentModelList.get( position ).getBannerSliderModelList();
                ((BannerSliderViewHolder) holder).setBannerSliderViewPager( bannerSliderModelList );
                break;
            case STRIP_AD_LAYOUT_CONTAINER:
                String imgLink = homeFragmentModelList.get( position ).getStripAdImage();
                String colorCode = homeFragmentModelList.get( position ).getStripAdBackground();
                ((StripAdViewHolder)holder).setStripAd( imgLink, colorCode );
                break;
            case BANNER_AD_LAYOUT_CONTAINER:
                String bImgLink = homeFragmentModelList.get( position ).getStripAdImage();
                String bBgColor = homeFragmentModelList.get( position ).getStripAdBackground();
                ((BannerAdViewHolder)holder).setBannerAd( bImgLink, bBgColor );
                break;
            case HORIZONTAL_ITEM_LAYOUT_CONTAINER:
               List<HorizontalItemViewModel> horizontalItemViewModelList =
                       homeFragmentModelList.get( position ).getHorizontalItemViewModelList();
               List<String> hrProductIdList = homeFragmentModelList.get( position ).getHrAndGridProductIdList();
               String layoutTitle = homeFragmentModelList.get( position ).getHorizontalLayoutTitle();
                ((HorizontalLayoutViewHolder)holder).setHorizontalLayout( hrProductIdList ,horizontalItemViewModelList, layoutTitle, position );
                break;
            case GRID_ITEM_LAYOUT_CONTAINER:
                List<HorizontalItemViewModel> gridItemViewModelList =
                        homeFragmentModelList.get( position ).getHorizontalItemViewModelList();
                List<String> gridProductIdList = homeFragmentModelList.get( position ).getHrAndGridProductIdList();
                String gridLayoutTitle = homeFragmentModelList.get( position ).getHorizontalLayoutTitle();
                ((GridLayoutViewHolder)holder).setDataGridLayout(gridProductIdList, gridItemViewModelList, gridLayoutTitle, position );
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return homeFragmentModelList.size();
    }

    //____________________________ View Holder Class _______________________________________________
    //============  Banner Slider View Holder ============
    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {
        //------ View Pager for Banner Slider...
        private ViewPager bannerSliderViewPager;
        private int BANNER_CURRENT_PAGE;
        private final long DELAY_TIME = 3000;
        private final long PERIOD_TIME = 3000;
        private Timer timer;
        private List<BannerSliderModel> arrangedList;
        //------ View Paager for Banner Slidder...

        public BannerSliderViewHolder(@NonNull View itemView) {
            super( itemView );
            bannerSliderViewPager = itemView.findViewById( R.id.banner_slider_viewPager );
        }

        // BannerSlidder ---
        private void setBannerSliderViewPager(final List <BannerSliderModel> bannerSliderModelList) {
            BANNER_CURRENT_PAGE = 2;
            if (timer != null){
                timer.cancel();
            }

            arrangedList = new ArrayList <>();
            for(int i =0; i < bannerSliderModelList.size(); i++){
                arrangedList.add( i, bannerSliderModelList.get( i ) );
            }
            arrangedList.add( 2, bannerSliderModelList.get( bannerSliderModelList.size() - 2 ) );
            arrangedList.add( 1, bannerSliderModelList.get( bannerSliderModelList.size() - 1 ) );
            arrangedList.add( bannerSliderModelList.get( 0 ) );
            arrangedList.add( bannerSliderModelList.get( 1 ) );

            BannerSliderAdaptor bannerSliderAdaptor = new BannerSliderAdaptor( arrangedList );
            bannerSliderViewPager.setAdapter( bannerSliderAdaptor );
            bannerSliderViewPager.setClipToPadding( false );
            bannerSliderViewPager.setPageMargin( 20 );
            bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE );

            ViewPager.OnPageChangeListener viewPagerOnPageChange = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    BANNER_CURRENT_PAGE = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        bannerSlideLooper( arrangedList );
                    }
                }
            };
            bannerSliderViewPager.addOnPageChangeListener( viewPagerOnPageChange );
            startBannerSliderAnim( arrangedList );

            bannerSliderViewPager.setOnTouchListener( new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    bannerSlideLooper( arrangedList );
                    stopBannerSliderAnim();
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        startBannerSliderAnim( arrangedList );
                    }
                    return false;
                }
            } );

            bannerSliderViewPager.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : OnClick Banner...
                }
            } );

            //------ View Paager for Banner Slidder...
        }

        private void bannerSlideLooper(List <BannerSliderModel> bannerSliderModelList) {
            if (BANNER_CURRENT_PAGE == bannerSliderModelList.size() - 2) {
                BANNER_CURRENT_PAGE = 2;
                bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE, false );
            }
            if (BANNER_CURRENT_PAGE == 1) {
                BANNER_CURRENT_PAGE = bannerSliderModelList.size() - 3;
                bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE, false );
            }
        }

        private void startBannerSliderAnim(final List <BannerSliderModel> bannerSliderModelList) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (BANNER_CURRENT_PAGE > bannerSliderModelList.size()) {
                        BANNER_CURRENT_PAGE = 1;
                    }
                    bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE++, true );
                }
            };
            timer = new Timer();
            timer.schedule( new TimerTask() {
                @Override
                public void run() {
                    handler.post( update );
                }
            }, DELAY_TIME, PERIOD_TIME );
        }

        private void stopBannerSliderAnim() {
            timer.cancel();
        }
        // BannerSlidder ---

    }
    //============  Banner Slider View Holder ============

    //============  Strip ad  View Holder ============
    public class StripAdViewHolder extends RecyclerView.ViewHolder{
        private ImageView stripAdImage;
        private ConstraintLayout stripAdContainer;

        public StripAdViewHolder(@NonNull View itemView) {
            super( itemView );
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);
            stripAdImage = itemView.findViewById( R.id.strip_ad_image );

        }
        private void setStripAd(String imgLink, String colorCode){
            stripAdContainer.setBackgroundColor( Color.parseColor( colorCode ) );
            // set Image Resouice from database..
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.stip_add_placeholder ) ).into( stripAdImage );
        }
    }
    //============  Strip ad  View Holder ============

    //============  Banner ad  View Holder ============
    public class BannerAdViewHolder extends RecyclerView.ViewHolder{
        private ImageView bannerAdImage;
        private ConstraintLayout bannerAdContainer;

        public BannerAdViewHolder(@NonNull View itemView) {
            super( itemView );
            bannerAdContainer = itemView.findViewById(R.id.banner_ad_container);
            bannerAdImage = itemView.findViewById( R.id.banner_ad_image );
        }
        private void setBannerAd(String imgLink, String colorCode){
            bannerAdContainer.setBackgroundColor( Color.parseColor( colorCode ) );
            // set Image Resource from database..
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.stip_add_placeholder ) ).into( bannerAdImage );
        }
    }
    //============  Banner ad  View Holder ============

    //============  Horizontal Layout View Holder ============
    public class HorizontalLayoutViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView horizontalItemViewRecycler;
        private Button horizontalItemViewAllBtn;
        private TextView horizontalLayoutTitle;
        private List<HorizontalItemViewModel> tempHrGridList;
        private HorizontalItemViewAdaptor horizontalItemViewAdaptor;

        public HorizontalLayoutViewHolder(@NonNull View itemView) {
            super( itemView );
            horizontalItemViewRecycler = itemView.findViewById( R.id.horizontal_itemView_recycler );
            horizontalLayoutTitle = itemView.findViewById( R.id.horizontal_itemView_title );
            horizontalItemViewAllBtn = itemView.findViewById( R.id.horizontal_item_viewAll_btn );
            // TODO: $ Changes...
//            horizontalItemViewRecycler.setRecycledViewPool( recycledViewPool );
            tempHrGridList = new ArrayList <>();
        }
        private void setHorizontalLayout(final List<String> hrLayoutProductIdList, final List<HorizontalItemViewModel> hrProductDetailsList,
                                         final String layoutTitle, int index){
            horizontalLayoutTitle.setText( layoutTitle );
            hrViewType = 0;

            if (hrLayoutProductIdList.size()>6){
                horizontalItemViewAllBtn.setVisibility( View.VISIBLE );
            }else {
                horizontalItemViewAllBtn.setVisibility( View.INVISIBLE );
            }

            horizontalItemViewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    hrViewType = 1;
                    horizontalItemViewModelListViewAll = new ArrayList <>();
                    totalProductsIdViewAll = new ArrayList <>();
                    if (hrProductDetailsList.size() == 0){
                        horizontalItemViewModelListViewAll = tempHrGridList;
                    }else{
                        horizontalItemViewModelListViewAll = hrProductDetailsList;
                    }
                    totalProductsIdViewAll = hrLayoutProductIdList;

                    Intent viewAllIntent = new Intent( itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra( "LAYOUT_CODE", RECYCLER_PRODUCT_LAYOUT );
                    viewAllIntent.putExtra( "TITLE", layoutTitle );
                    itemView.getContext().startActivity( viewAllIntent );
                }
            } );

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext() );
            linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
            horizontalItemViewRecycler.setLayoutManager( linearLayoutManager );
            // $ Changes...================
            if (hrProductDetailsList.size() == 0){
                if (hrLayoutProductIdList.size() > 6){
                    for (int id_no = 0; id_no < 6; id_no++){
                        loadProductDetailsData(index, horizontalItemViewRecycler, hrLayoutProductIdList.get( id_no ));
                    }
                }else{
                    for (int id_no = 0; id_no < hrLayoutProductIdList.size(); id_no++){
                        loadProductDetailsData(index, horizontalItemViewRecycler, hrLayoutProductIdList.get( id_no ));
                    }
                }
            }
            else{
                horizontalItemViewAdaptor = new HorizontalItemViewAdaptor( hrProductDetailsList );
                horizontalItemViewRecycler.setAdapter( horizontalItemViewAdaptor );
                horizontalItemViewAdaptor.notifyDataSetChanged();
            }

        }
        private void loadProductDetailsData(final int index, final RecyclerView recyclerView, final String productId ){
            FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document( productId )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        // access the banners from database...
                        tempHrGridList.add( new HorizontalItemViewModel( 0, productId
                                , task.getResult().get( "product_image_1").toString()
                                , task.getResult().get( "product_full_name" ).toString()
                                , task.getResult().get( "product_price" ).toString()
                                , task.getResult().get( "product_cut_price" ).toString()
                                , (Boolean) task.getResult().get( "product_cod" )
                                , (long) task.getResult().get( "product_stocks" ) ) );

                        horizontalItemViewAdaptor = new HorizontalItemViewAdaptor( tempHrGridList );
                        homeFragmentModelList.get( index ).setHorizontalItemViewModelList( tempHrGridList );
                        if (recyclerView != null){
                            recyclerView.setAdapter( horizontalItemViewAdaptor );
                            horizontalItemViewAdaptor.notifyDataSetChanged();
                        }
                    }
                    else{
                        String error = task.getException().getMessage();
//                                    showToast(error);
//                                    dialog.dismiss();
                    }
                }
            } );
        }


    }
    //============  Horizontal Layout View Holder ============

    //==============  GridProduct Grid Layout View Holder =================
    public class GridLayoutViewHolder extends  RecyclerView.ViewHolder{
        private GridLayout gridLayout;
        private TextView gridLayoutTitle;
        private Button gridLayoutViewAllBtn;
        private int temp = 0;
        private List<HorizontalItemViewModel> tempHrGridList;
        public GridLayoutViewHolder(@NonNull View itemView) {
            super( itemView );
            gridLayout = itemView.findViewById( R.id.product_grid_layout );
            gridLayoutTitle = itemView.findViewById( R.id.product_grid__layout_title );
            gridLayoutViewAllBtn = itemView.findViewById( R.id.grid_view_all_btn );
        }
        private void setDataGridLayout(final List<String> gridProductIdList, final List<HorizontalItemViewModel> gridLayoutList, final String gridTitle, int index){
            gridLayoutTitle.setText( gridTitle );
            if (gridLayoutList.size() == 0){
                tempHrGridList = new ArrayList <>();
                for (int i = 0; i < 4; i++ ){
                    setProductData( i, index,gridProductIdList.get( i ));
                }
            }
            for (int i = 0; i < 4; i++ ){

                if (gridLayoutList.size() != 0){
                    ImageView img = gridLayout.getChildAt( i ).findViewById( R.id.hr_product_image );
                    TextView name = gridLayout.getChildAt( i ).findViewById( R.id.hr_product_name );
                    TextView price = gridLayout.getChildAt( i ).findViewById( R.id.hr_product_price );
                    TextView cutPrice = gridLayout.getChildAt( i ).findViewById( R.id.hr_product_cut_price );
                    TextView perOffText = gridLayout.getChildAt( i ).findViewById( R.id.hr_off_percentage );

                    name.setText( gridLayoutList.get( i ).getHrProductName() );
                    price.setText("Rs." + gridLayoutList.get( i ).getHrProductPrice() +"/-" );
                    cutPrice.setText( "Rs."+ gridLayoutList.get( i ).getHrProductCutPrice() +"/-" );
                    // Set img resource
                    Glide.with( itemView.getContext() ).load( gridLayoutList.get( i ).getHrProductImage()  )
                            .apply( new RequestOptions().placeholder( R.drawable.squre_image_placeholder ) ).into( img );

                    int mrp =  Integer.parseInt( gridLayoutList.get( i ).getHrProductCutPrice());
                    int showPrice = Integer.parseInt(  gridLayoutList.get( i ).getHrProductPrice());
                    int perOff = (( mrp - showPrice )*100)/showPrice;
                    perOffText.setText( perOff +"% Off" );
                }

                gridLayout.getChildAt( i ).setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent gotoProductDetailIntent = new Intent(itemView.getContext(), ProductDetails.class);
                        if (view == gridLayout.getChildAt( 0 )){
                            if (gridLayoutList.size() != 0){
                                gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridLayoutList.get( 0 ).getHrProductId() );
                            }else{
                                gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridProductIdList.get( 0 ) );
                            }
                            itemView.getContext().startActivity( gotoProductDetailIntent );
                        }else
                            if (view == gridLayout.getChildAt( 1 )){
                                if (gridLayoutList.size() != 0){
                                    gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridLayoutList.get( 1 ).getHrProductId() );
                                }else{
                                    gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridProductIdList.get( 1 ) );
                                }

                            itemView.getContext().startActivity( gotoProductDetailIntent );
                        }else
                            if (view == gridLayout.getChildAt( 2 )){
                                if (gridLayoutList.size() != 0){
                                    gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridLayoutList.get( 2 ).getHrProductId() );
                                }else{
                                    gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridProductIdList.get( 2 ) );
                                }

                            itemView.getContext().startActivity( gotoProductDetailIntent );
                        }else
                            if (view == gridLayout.getChildAt( 3 )){
                                if (gridLayoutList.size() != 0){
                                    gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridLayoutList.get( 3 ).getHrProductId() );
                                }else{
                                    gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridProductIdList.get( 3 ) );
                                }

                            itemView.getContext().startActivity( gotoProductDetailIntent );
                        }
                    }
                } );
            }
            if (gridProductIdList.size()>4){
                gridLayoutViewAllBtn.setVisibility( View.VISIBLE );
            }else{
                gridLayoutViewAllBtn.setVisibility( View.INVISIBLE );
            }

            gridLayoutViewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gridViewModelListViewAll = new ArrayList <>();
                    totalProductsIdViewAll = new ArrayList <>();
                    if (gridLayoutList.size() == 0){
                        gridViewModelListViewAll = tempHrGridList;
                    }else{
                        gridViewModelListViewAll = gridLayoutList;
                    }

                    totalProductsIdViewAll.addAll( gridProductIdList );
//                    totalProductsIdViewAll = gridProductIdList;

                    Intent viewAllIntent = new Intent( itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra( "LAYOUT_CODE",GRID_PRODUCT_LAYOUT );
                    viewAllIntent.putExtra( "TITLE", gridTitle );
                    itemView.getContext().startActivity( viewAllIntent );
                }
            } );

        }
        private void setProductData(final int i, final int index, final String productId){
            FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document( productId )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        // access the banners from database...
                        String pImgLink = task.getResult().get( "product_image_1").toString();
                        String pName = task.getResult().get( "product_full_name" ).toString();
                        String pPrice = task.getResult().get( "product_price" ).toString();
                        String pMrpPrice = task.getResult().get( "product_cut_price" ).toString();
                        long pStock = (long) task.getResult().get( "product_stocks" );
                        Boolean pCod = (Boolean) task.getResult().get( "product_cod" );

                        tempHrGridList.add(new HorizontalItemViewModel( 0, productId
                                , pImgLink
                                , pName
                                , pPrice
                                , pMrpPrice
                                , pCod
                                , pStock ) );

                        homeFragmentModelList.get( index ).setHorizontalItemViewModelList( tempHrGridList );
                        temp = i;

                        ImageView img = gridLayout.getChildAt( temp ).findViewById( R.id.hr_product_image );
                        TextView name = gridLayout.getChildAt( temp ).findViewById( R.id.hr_product_name );
                        TextView price = gridLayout.getChildAt( temp ).findViewById( R.id.hr_product_price );
                        TextView cutPrice = gridLayout.getChildAt( temp ).findViewById( R.id.hr_product_cut_price );
                        TextView perOffText = gridLayout.getChildAt( temp ).findViewById( R.id.hr_off_percentage );
                        TextView stockText = gridLayout.getChildAt( temp ).findViewById( R.id.stock_text );

                        name.setText( pName );
                        price.setText("Rs." + pPrice +"/-" );
                        cutPrice.setText( "Rs."+ pMrpPrice +"/-" );
                        // Set img resource
                        Glide.with( itemView.getContext() ).load( pImgLink )
                                .apply( new RequestOptions().placeholder( R.drawable.squre_image_placeholder ) ).into( img );

                        int mrp =  Integer.parseInt( pMrpPrice );
                        int showPrice = Integer.parseInt( pPrice );
                        int perOff = (( mrp - showPrice )*100)/showPrice;
                        perOffText.setText( perOff +"% Off" );
                        if (pStock > 0){
                            stockText.setText("In Stock");
                        }else{
                            stockText.setText( "Out Of Stock" );
                        }
                    }
                    else{
                        String error = task.getException().getMessage();
//                                    showToast(error);
//                                    dialog.dismiss();
                    }
                }
            } );


        }

    }
    //==============  GridProduct Grid Layout View Holder =================


}
