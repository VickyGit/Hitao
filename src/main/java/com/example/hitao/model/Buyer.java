package com.example.hitao.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/4/28.
 */
public class Buyer extends BmobObject implements Serializable{
    private Integer BuyerId;
    private String BuyerName;
    private String Password;
    private Double Money;

    public Integer getBuyerId() {
        return BuyerId;
    }

    public void setBuyerId(Integer buyerId) {
        BuyerId = buyerId;
    }

    public String getBuyerName() {
        return BuyerName;
    }

    public void setBuyerName(String buyerName) {
        BuyerName = buyerName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Double getMoney() {
        return Money;
    }

    public void setMoney(Double money) {
        Money = money;
    }
}
