package com.example.hitao.model;

import java.io.Serializable;
import java.sql.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/4/28.
 */
public class Product extends BmobObject implements Serializable{
    private Integer ProductId;//产品Id
    private Integer Category;//产品分类     //1、女装  2、男装  3、鞋包 4、饰品  5、运动
    // 6、美妆  7、童装  8、食品  9、母婴  10、百货  11、家电  12、数码  13、家装
    private Integer Number;//数量
    private Double Price;//价格
    private Integer SellerId;//卖家Id
    private String ProductName;//产品名称
    private BmobFile ProductPic;//产品图片
    private String ProductDesc;//产品详情
    private String SellerName;

    public Integer getProductId() {
        return ProductId;
    }

    public void setProductId(Integer productId) {
        ProductId = productId;
    }

    public Integer getCategory() {
        return Category;
    }

    public void setCategory(Integer category) {
        Category = category;
    }

    public Integer getNumber() {
        return Number;
    }

    public void setNumber(Integer number) {
        Number = number;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Integer getSellerId() {
        return SellerId;
    }

    public void setSellerId(Integer sellerId) {
        SellerId = sellerId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public BmobFile getProductPic() {
        return ProductPic;
    }

    public void setProductPic(BmobFile productPic) {
        ProductPic = productPic;
    }

    public String getProductDesc() {
        return ProductDesc;
    }

    public void setProductDesc(String productDesc) {
        ProductDesc = productDesc;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }
}
