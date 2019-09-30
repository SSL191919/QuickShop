package com.example.shailendra.quickshop.retailerdata;

import java.util.ArrayList;
import java.util.List;

public class Ret_HomePageModel {

    public static final int RET_OFFER_TYPE = 0;
    public static final int RET_GRID_TYPE = 1;
    public static final int RET_RECYCLER_LIST_TYPE = 2;
    private int type;

    public int getType() {
        return type;
    }
    public void setType(int type){
        this.type = type;
    }

    //---------- offer recycler View in horizontal -------------------------------------------------

    private String offerTitle;
    private List<X_OfferRecyclerViewModel> x_offerRecyclerViewModelList;

    public Ret_HomePageModel(int type, String offerTitle, List <X_OfferRecyclerViewModel> x_offerRecyclerViewModelList) {
        this.type = type;
        this.offerTitle = offerTitle;
        this.x_offerRecyclerViewModelList = x_offerRecyclerViewModelList;
    }
    public String getOfferTitle() {
        return offerTitle;
    }
    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }
    public List <X_OfferRecyclerViewModel> getX_offerRecyclerViewModelList() {
        return x_offerRecyclerViewModelList;
    }
    public void setX_offerRecyclerViewModelList(List <X_OfferRecyclerViewModel> x_offerRecyclerViewModelList) {
        this.x_offerRecyclerViewModelList = x_offerRecyclerViewModelList;
    }

    //---------- offer recycler View in horizontal -------------------------------------------------
    //---------- offer Grid View in Recycler View --------------------------------------------------

    private String gridTitle;
    private List<X_OfferRecyclerViewModel> x_offerRecyclerViewModelGridList;

    public Ret_HomePageModel(String gridTitle, List <X_OfferRecyclerViewModel> x_offerRecyclerViewModelGridList) {
        this.gridTitle = gridTitle;
        this.x_offerRecyclerViewModelGridList = x_offerRecyclerViewModelGridList;
    }
    public String getGridTitle() {
        return gridTitle;
    }
    public void setGridTitle(String gridTitle) {
        this.gridTitle = gridTitle;
    }
    public List <X_OfferRecyclerViewModel> getX_offerRecyclerViewModelGridList() {
        return x_offerRecyclerViewModelGridList;
    }
    public void setX_offerRecyclerViewModelGridList(List <X_OfferRecyclerViewModel> x_offerRecyclerViewModelGridList) {
        this.x_offerRecyclerViewModelGridList = x_offerRecyclerViewModelGridList;
    }

    //---------- offer Grid View in Recycler View --------------------------------------------------
    //---------- offer Recycler List View in Recycler View -----------------------------------------

    private String recyclerListTitle;
    private List<RetRecyclerListModel> retRecyclerListModelList;

    public Ret_HomePageModel(int type, List <RetRecyclerListModel> retRecyclerListModelList, String recyclerListTitle) {
        this.type = type;
        this.recyclerListTitle = recyclerListTitle;
        this.retRecyclerListModelList = retRecyclerListModelList;
    }
    public static int getRetRecyclerListType() {
        return RET_RECYCLER_LIST_TYPE;
    }
    public String getRecyclerListTitle() {
        return recyclerListTitle;
    }
    public void setRecyclerListTitle(String recyclerListTitle) {
        this.recyclerListTitle = recyclerListTitle;
    }
    public List <RetRecyclerListModel> getRetRecyclerListModelList() {
        return retRecyclerListModelList;
    }
    public void setRetRecyclerListModelList(List <RetRecyclerListModel> retRecyclerListModelList) {
        this.retRecyclerListModelList = retRecyclerListModelList;
    }

    //---------- offer Recycler List View in Recycler View -----------------------------------------

}
