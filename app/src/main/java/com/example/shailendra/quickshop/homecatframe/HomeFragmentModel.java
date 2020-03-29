package com.example.shailendra.quickshop.homecatframe;


import java.util.List;

public class HomeFragmentModel {

    private int layoutType;
    private List <BannerSliderModel> bannerSliderModelList;

    public int getLayoutType() {
        return layoutType;
    }
    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    //------ Layout for Banner Slider...
    public HomeFragmentModel(int layoutType, List <BannerSliderModel> bannerSliderModelList) {
        this.layoutType = layoutType;
        this.bannerSliderModelList = bannerSliderModelList;
    }
    public List <BannerSliderModel> getBannerSliderModelList() {
        return bannerSliderModelList;
    }
    public void setBannerSliderModelList(List <BannerSliderModel> bannerSliderModelList) {
        this.bannerSliderModelList = bannerSliderModelList;
    }
    //------ Layout for Banner Slider...

    //------ Layout for Strip ad....
    private String stripAdImage;
    private String stripAdBackground;

    public HomeFragmentModel(int layoutType, String stripAdImage, String stripAdBackground) {
        this.layoutType = layoutType;
        this.stripAdImage = stripAdImage;
        this.stripAdBackground = stripAdBackground;
    }
    public String getStripAdImage() {
        return stripAdImage;
    }
    public void setStripAdImage(String stripAdImage) {
        this.stripAdImage = stripAdImage;
    }
    public String getStripAdBackground() {
        return stripAdBackground;
    }
    public void setStripAdBackground(String stripAdBackground) {
        this.stripAdBackground = stripAdBackground;
    }
    //------ Layout for Strip ad....

    // ------- Horizontal Item View ..----------------
    private  List<HorizontalItemViewModel> horizontalItemViewModelList;
    private List<String> HrAndGridProductIdList;
    private String horizontalLayoutTitle;

    public HomeFragmentModel(int layoutType, List<String> HrAndGridProductIdList, List <HorizontalItemViewModel> horizontalItemViewModelList, String horizontalLayoutTitle) {
        this.layoutType = layoutType;
        this.horizontalItemViewModelList = horizontalItemViewModelList;
        this.horizontalLayoutTitle = horizontalLayoutTitle;
        this.HrAndGridProductIdList = HrAndGridProductIdList;
    }
    public List <HorizontalItemViewModel> getHorizontalItemViewModelList() {
        return horizontalItemViewModelList;
    }
    public void setHorizontalItemViewModelList(List <HorizontalItemViewModel> horizontalItemViewModelList) {
        this.horizontalItemViewModelList = horizontalItemViewModelList;
    }
    public String getHorizontalLayoutTitle() {
        return horizontalLayoutTitle;
    }
    public void setHorizontalLayoutTitle(String horizontalLayoutTitle) {
        this.horizontalLayoutTitle = horizontalLayoutTitle;
    }

    public List <String> getHrAndGridProductIdList() {
        return HrAndGridProductIdList;
    }

    public void setHrAndGridProductIdList(List <String> hrAndGridProductIdList) {
        HrAndGridProductIdList = hrAndGridProductIdList;
    }

    // ------- Horizontal Item View ..----------------

}
