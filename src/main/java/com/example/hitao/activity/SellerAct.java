package com.example.hitao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hitao.R;
import com.example.hitao.model.Product;
import com.example.hitao.model.Seller;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by fengsiyuan on 16/5/6.
 */
public class SellerAct extends Activity {
    private RecyclerView recyclerView;
    private List<Product> productList=new ArrayList<>();
    private SRecyclerViewAdapter sellerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton orderFloatingBtn;
    private FloatingActionButton personalFloatingBtn;
    private FloatingActionButton addproductBtn;
    private final int REFRESHVIEW=1;
    private int FIRST_REFRESH=0;
    private Integer Mysellerid;
    private String sellername;
    private Seller seller;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESHVIEW:
                    swipeRefreshLayout.setRefreshing(false);
                    sellerAdapter= new SRecyclerViewAdapter(productList, SellerAct.this);
                    recyclerView.setAdapter(sellerAdapter);
                    Toast.makeText(SellerAct.this, "Handler ProductList Size :" + productList.size(), Toast.LENGTH_SHORT).show();
                    break;


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main_layout);
        Intent intent = getIntent();
        Mysellerid = intent.getIntExtra("Mysellerid", 0);
        sellername = intent.getStringExtra("sellername");
        orderFloatingBtn= (FloatingActionButton) findViewById(R.id.seller_action_a);
        personalFloatingBtn= (FloatingActionButton) findViewById(R.id.seller_action_b);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_seller_main);
        addproductBtn = (FloatingActionButton)findViewById(R.id.seller_action_c);
        seller= (Seller) getIntent().getSerializableExtra("sellerObject");

        //1、实例化recyclerView
        recyclerView= (RecyclerView) findViewById(R.id.seller_recyclerview);
        //2、实例化布局管理
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        //3、为RecyclerView设置布局
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置刷新时动画的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                FIRST_REFRESH = 1;
                findProduct();
                Message message = new Message();
                message.what = REFRESHVIEW;
                handler.sendMessage(message);
            }
        });

        findProduct();
        FloatingActionButtoninfo();
        Toast.makeText(SellerAct.this,"onCreate ProductList Size :"+productList.size(),Toast.LENGTH_SHORT).show();


    }
    public void findProduct(){
        BmobQuery<Product> productBmobQuery=new BmobQuery<Product>();
        Toast.makeText(SellerAct.this,"sellid is"+Mysellerid,Toast.LENGTH_SHORT).show();
        productBmobQuery.addWhereEqualTo("SellerId",Mysellerid);
        Toast.makeText(SellerAct.this,"sellerid:"+Mysellerid,Toast.LENGTH_SHORT).show();


        productBmobQuery.findObjects(this, new FindListener<Product>() {
            @Override
            public void onSuccess(List<Product> list) {

                productList=list;
                Toast.makeText(SellerAct.this,"findProduct ProductList Size :"+list.size(),Toast.LENGTH_SHORT).show();
                if(FIRST_REFRESH==0){
                    Message message=new Message();
                    message.what=REFRESHVIEW;
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(SellerAct.this,s,Toast.LENGTH_SHORT).show();


            }
        });

    }
    public void FloatingActionButtoninfo(){
        addproductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerAct.this,AddProducctActivity.class);
                intent.putExtra("Mysellerid",Mysellerid);
                intent.putExtra("sellername",sellername);
                startActivity(intent);

            }
        });
        orderFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerAct.this,SOrderAct.class);
                intent.putExtra("Mysellerid",Mysellerid);
                intent.putExtra("sellername",sellername);
                startActivity(intent);


            }
        });
        personalFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerAct.this,SPersonalActivity.class);
                intent.putExtra("SellerObjectId",seller.getObjectId());
                Log.d("SellerObjectId",seller.getObjectId());
                startActivity(intent);



            }
        });
    }
}
