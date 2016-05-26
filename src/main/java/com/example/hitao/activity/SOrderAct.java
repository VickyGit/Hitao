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

import com.example.hitao.R;
import com.example.hitao.model.Order;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/5/10.
 */
public class SOrderAct extends Activity{
    private RecyclerView recyclerView;
    private List<Order> orderList;
    private SOrderRecyclerViewAdapter orderRecyclerViewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String orderSellerName;
    private final int REFRESHVIEW=1;
    private int FIRST_REFRESH=0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESHVIEW:
                    swipeRefreshLayout.setRefreshing(false);
                    orderRecyclerViewAdapter= new SOrderRecyclerViewAdapter(SOrderAct.this,orderList);
                    recyclerView.setAdapter(orderRecyclerViewAdapter);
                    //Toast.makeText(SOrderAct.this, "Handler ProductList Size :" + orderList.size(), Toast.LENGTH_SHORT).show();
                    break;


            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_order_main_layout);
        init();
        findOrder();

    }
    public void init(){
        recyclerView= (RecyclerView) findViewById(R.id.s_order_recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.s_order_swipe_container);


        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                FIRST_REFRESH = 1;
                findOrder();
                Message message = new Message();
                message.what = REFRESHVIEW;
                handler.sendMessage(message);
            }
        });
        Intent intent = getIntent();
        orderSellerName = intent.getStringExtra("sellername");

    }
    public void findOrder(){
        BmobQuery<Order> orderBmobQuery=new BmobQuery<Order>();
        orderBmobQuery.addWhereEqualTo("OrderSellerName",orderSellerName);
        orderBmobQuery.findObjects(SOrderAct.this, new FindListener<Order>() {
            @Override
            public void onSuccess(List<Order> list) {
                orderList = list;
                //Toast.makeText(SOrderAct.this,"findorder OrderList Size :"+list.size(),Toast.LENGTH_SHORT).show();
                if(FIRST_REFRESH==0){
                    Message message=new Message();
                    message.what=REFRESHVIEW;
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onError(int i, String s) {
                //Toast.makeText(SOrderAct.this,"失败",Toast.LENGTH_SHORT).show();


            }
        });
    }
}
