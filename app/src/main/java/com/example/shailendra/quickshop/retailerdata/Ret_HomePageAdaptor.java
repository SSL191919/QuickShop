package com.example.shailendra.quickshop.retailerdata;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.shailendra.quickshop.R;

import java.util.List;

public class Ret_HomePageAdaptor extends RecyclerView.Adapter {

    private List <Ret_HomePageModel> ret_homePageModelList;

    public Ret_HomePageAdaptor(List <Ret_HomePageModel> ret_homePageModelList) {
        this.ret_homePageModelList = ret_homePageModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (ret_homePageModelList.get( position ).getType()) {
            case 0:
                return Ret_HomePageModel.RET_OFFER_TYPE;
            case 1:
                return Ret_HomePageModel.RET_GRID_TYPE;
            case 2:
                return Ret_HomePageModel.RET_RECYCLER_LIST_TYPE;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case Ret_HomePageModel.RET_OFFER_TYPE:
                View Ret_Offer_View = LayoutInflater.from( parent.getContext() )
                        .inflate( R.layout.x_offer_recycler_view, parent, false );
                return new Ret_OfferViewHolder( Ret_Offer_View );
            case Ret_HomePageModel.RET_GRID_TYPE:
                View Ret_Grid_View = LayoutInflater.from( parent.getContext() )
                        .inflate( R.layout.ret_grid_layout, parent, false );
                return new Ret_GridViewHolder( Ret_Grid_View );
            case Ret_HomePageModel.RET_RECYCLER_LIST_TYPE:
                View Ret_Recycler_List_View = LayoutInflater.from( parent.getContext() )
                        .inflate( R.layout.ret_recycler_list_layout, parent, false );
                return new Ret_RecyclerListViewHolder( Ret_Recycler_List_View );
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (ret_homePageModelList.get( position ).getType()) {
            case Ret_HomePageModel.RET_OFFER_TYPE:
                String offerTitle = ret_homePageModelList.get( position ).getOfferTitle();
                List <X_OfferRecyclerViewModel> x_offerRecyclerViewModelList = ret_homePageModelList.get( position )
                        .getX_offerRecyclerViewModelList();
                ((Ret_OfferViewHolder) viewHolder).setRetOfferProductLayout( x_offerRecyclerViewModelList, offerTitle );
                break;
            case Ret_HomePageModel.RET_GRID_TYPE:
                String gridTitle = ret_homePageModelList.get( position ).getOfferTitle();
                List <X_OfferRecyclerViewModel> x_offerRecyclerViewModeGridList = ret_homePageModelList.get( position )
                        .getX_offerRecyclerViewModelList();
                ((Ret_GridViewHolder) viewHolder).setRetGridLayoutContent( x_offerRecyclerViewModeGridList, gridTitle );
                break;
            case Ret_HomePageModel.RET_RECYCLER_LIST_TYPE:
                String listHeading = ret_homePageModelList.get( position ).getRecyclerListTitle();
                List <RetRecyclerListModel> retRecyclerListModelList = ret_homePageModelList.get( position ).getRetRecyclerListModelList();
                ((Ret_RecyclerListViewHolder) viewHolder).setRetRecyclerListContent( listHeading, retRecyclerListModelList );
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return ret_homePageModelList.size();
    }

//    ==============================================================================================
//________________________ Class For Offer Layout ___________________________________________________

    public class Ret_OfferViewHolder extends RecyclerView.ViewHolder {

        private TextView offerTitle;
        private Button offerViewAllBtn;
        private RecyclerView offerRecyclerView;

        public Ret_OfferViewHolder(View itemView) {

            super( itemView );

            offerTitle = itemView.findViewById( R.id.offer_title );
            offerViewAllBtn = itemView.findViewById( R.id.offer_view_all_btn );
            offerRecyclerView = itemView.findViewById( R.id.offer_recycler_view );

        }

        private void setRetOfferProductLayout(List <X_OfferRecyclerViewModel> x_offerRecyclerViewModelList, String title) {
            if (x_offerRecyclerViewModelList.size() > 4) {
                offerViewAllBtn.setVisibility( View.VISIBLE );
            } else {
                offerViewAllBtn.setVisibility( View.INVISIBLE );
            }

            offerTitle.setText( title );
            X_OfferRecyclerViewAdaptor x_offerRecyclerViewAdaptor = new X_OfferRecyclerViewAdaptor( x_offerRecyclerViewModelList );

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext() );
            linearLayoutManager.setOrientation( LinearLayoutManager.HORIZONTAL );
            offerRecyclerView.setLayoutManager( linearLayoutManager );
            offerRecyclerView.setAdapter( x_offerRecyclerViewAdaptor );

            x_offerRecyclerViewAdaptor.notifyDataSetChanged();
        }
    }

//________________________ Class For Offer Layout ___________________________________________________
//________________________ Class For Grid Layout ___________________________________________________

    public class Ret_GridViewHolder extends RecyclerView.ViewHolder {

        private TextView retGridTitle, retGridViewAllTxt;
        private GridView retGridLayoutContent;

        public Ret_GridViewHolder(View itemView) {
            super( itemView );
            retGridTitle = itemView.findViewById( R.id.ret_grid_title );
            retGridLayoutContent = itemView.findViewById( R.id.ret_grid_layout_content );
            retGridViewAllTxt = itemView.findViewById( R.id.ret_grid_view_all_txt );
        }

        private void setRetGridLayoutContent(List <X_OfferRecyclerViewModel> retGridList, String gridTitle) {


            retGridTitle.setText( gridTitle );
            retGridLayoutContent.setAdapter( new X_ret_grid_adaptor( retGridList ) );

        }

    }
//________________________ Class For Grid Layout ___________________________________________________
//________________________ Class For Recycler List Layout __________________________________________

    public class Ret_RecyclerListViewHolder extends RecyclerView.ViewHolder {

        private TextView retRecyclerListHeading;
        private RecyclerView retRecyclerListContent;
        private TextView retRecyclerListViewAllTxt;

        public Ret_RecyclerListViewHolder(View itemView) {
            super( itemView );
            retRecyclerListHeading = itemView.findViewById( R.id.ret_recycler_list_heading );
            retRecyclerListContent = itemView.findViewById( R.id.ret_recycler_list_content );
            retRecyclerListViewAllTxt = itemView.findViewById( R.id.ret_recycler_list_view_all_txt );
        }

        private void setRetRecyclerListContent(String listHeading, List <RetRecyclerListModel> retRecyclerListModelList) {

            retRecyclerListHeading.setText( listHeading );

            RetRecyclerListAdaptor retRecyclerListAdaptor = new RetRecyclerListAdaptor( retRecyclerListModelList );
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext() );
            retRecyclerListContent.setLayoutManager( linearLayoutManager );
            retRecyclerListContent.setAdapter( retRecyclerListAdaptor );
            retRecyclerListAdaptor.notifyDataSetChanged();
        }
    }
//________________________ Class For Recycler List Layout __________________________________________


}
