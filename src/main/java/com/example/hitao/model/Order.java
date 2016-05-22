package com.example.hitao.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/4/28.
 */
public class Order extends BmobObject{
    private Integer OrderState;    //0未发货，1发货
    private String OrderBuyerName;
    private String OrderSellerName;
    private String OrderProductName;
    private Double TotlePrice;
    private Integer ProductNum;
    private Double OrderProductPrice;

    private String buyerObjectId;


    public Integer getOrderState() {
        return OrderState;
    }

    public void setOrderState(Integer orderState) {
        OrderState = orderState;
    }

    public String getOrderBuyerName() {
        return OrderBuyerName;
    }

    public void setOrderBuyerName(String orderBuyerName) {
        OrderBuyerName = orderBuyerName;
    }

    public String getOrderSellerName() {
        return OrderSellerName;
    }

    public void setOrderSellerName(String orderSellerName) {
        OrderSellerName = orderSellerName;
    }

    public String getOrderProductName() {
        return OrderProductName;
    }

    public void setOrderProductName(String orderProductName) {
        OrderProductName = orderProductName;
    }


    public Integer getProductNum() {
        return ProductNum;
    }

    public void setProductNum(Integer productNum) {
        ProductNum = productNum;
    }

    public Double getTotlePrice() {
        return TotlePrice;
    }

    public void setTotlePrice(Double totlePrice) {
        TotlePrice = totlePrice;
    }

    public Double getOrderProductPrice() {
        return OrderProductPrice;
    }

    public void setOrderProductPrice(Double orderProductPrice) {
        OrderProductPrice = orderProductPrice;
    }

    public String getBuyerObjectId() {
        return buyerObjectId;
    }

    public void setBuyerObjectId(String buyerObjectId) {
        this.buyerObjectId = buyerObjectId;
    }
}
