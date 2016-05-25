package com.example.hitao.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/4/28.
 */
public class Seller extends BmobObject{
    private Integer SellerId;
    private String SellerName;
    private String Password;


    public Integer getSellerId() {
        return SellerId;
    }

    public void setSellerId(Integer sellerId) {
        SellerId = sellerId;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

