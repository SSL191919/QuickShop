package com.example.shailendra.quickshop.homecatframe;

public class HorizontalItemViewModel {

    private String hrProductImage;
    private String hrProductName;
    private String hrProductPrice;
    private String hrProductCutPrice;
    public static int hrViewType;
    private Boolean hrCodInfo;
    private long hrStockInfo;

    private String hrProductId;

    //---------------------------------------------------------


    public HorizontalItemViewModel(int hrViewType, String hrProductId,String hrProductImage, String hrProductName, String hrProductPrice, String hrProductCutPrice) {
        this.hrProductId = hrProductId;
        this.hrProductImage = hrProductImage;
        this.hrProductName = hrProductName;
        this.hrProductPrice = hrProductPrice;
        this.hrProductCutPrice = hrProductCutPrice;
        this.hrViewType = hrViewType;
    }


    public HorizontalItemViewModel(int hrViewType, String hrProductId, String hrProductImage, String hrProductName, String hrProductPrice, String hrProductCutPrice,  Boolean hrCodInfo, long hrStockInfo) {
        this.hrProductId = hrProductId;
        this.hrProductImage = hrProductImage;
        this.hrProductName = hrProductName;
        this.hrProductPrice = hrProductPrice;
        this.hrProductCutPrice = hrProductCutPrice;
        this.hrViewType = hrViewType;
        this.hrCodInfo = hrCodInfo;
        this.hrStockInfo = hrStockInfo;
    }

    //---------------------------------------------------------

    public String getHrProductId() {
        return hrProductId;
    }

    public void setHrProductId(String hrProductId) {
        this.hrProductId = hrProductId;
    }

    public Boolean getHrCodInfo() {
        return hrCodInfo;
    }

    public void setHrCodInfo(Boolean hrCodInfo) {
        this.hrCodInfo = hrCodInfo;
    }

    public long getHrStockInfo() {
        return hrStockInfo;
    }

    public void setHrStockInfo(long hrStockInfo) {
        this.hrStockInfo = hrStockInfo;
    }

    public int getHrViewType() {
        return hrViewType;
    }

    public void setHrViewType(int hrViewType) {
        this.hrViewType = hrViewType;
    }

    public String getHrProductImage() {
        return hrProductImage;
    }

    public void setHrProductImage(String hrProductImage) {
        this.hrProductImage = hrProductImage;
    }

    public String getHrProductName() {
        return hrProductName;
    }

    public void setHrProductName(String hrProductName) {
        this.hrProductName = hrProductName;
    }

    public String getHrProductPrice() {
        return hrProductPrice;
    }

    public void setHrProductPrice(String hrProductPrice) {
        this.hrProductPrice = hrProductPrice;
    }

    public String getHrProductCutPrice() {
        return hrProductCutPrice;
    }

    public void setHrProductCutPrice(String hrProductCutPrice) {
        this.hrProductCutPrice = hrProductCutPrice;
    }
}
