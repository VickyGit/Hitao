package com.example.hitao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.example.hitao.R;
import com.example.hitao.model.Order;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/5/10.
 */
public class OrderAct extends Activity implements OrderRecyclerViewAdapter.DeleteOrderListener{
    private RecyclerView recyclerView;
    private List<Order> orderList;
    private OrderRecyclerViewAdapter orderRecyclerViewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String orderBuyerId;
    private final int REFRESHVIEW=1;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESHVIEW:
                    swipeRefreshLayout.setRefreshing(false);
                    orderRecyclerViewAdapter=new OrderRecyclerViewAdapter(OrderAct.this,orderList,OrderAct.this);
                    recyclerView.setAdapter(orderRecyclerViewAdapter);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main_layout);
        orderBuyerId=getIntent().getStringExtra("buyerObjectId");
        Toast.makeText(OrderAct.this,"Id is "+orderBuyerId,Toast.LENGTH_SHORT).show();


        recyclerView= (RecyclerView) findViewById(R.id.order_recyclerview);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.order_swipe_container);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(OrderAct.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        findOrder();
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                findOrder();
            }
        });


    }
    /*
    查找订单
     */

    public void findOrder(){
        BmobQuery<Order> orderBmobQuery=new BmobQuery<Order>();
        orderBmobQuery.addWhereEqualTo("buyerObjectId",orderBuyerId);
        orderBmobQuery.findObjects(OrderAct.this, new FindListener<Order>() {
            @Override
            public void onSuccess(List<Order> list) {
                orderList=list;
                Message message=new Message();
                message.what=REFRESHVIEW;
                handler.sendMessage(message);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(OrderAct.this,"查询失败"+i,Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void deleteOrder(int position) {
//        Order order=new Order();
//        order.setObjectId(orderList.get(position).getObjectId());
//        order.delete(OrderAct.this, new DeleteListener() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(OrderAct.this,"目前位置"+position,Toast.LENGTH_LONG).show();
//                orderList.remove(position);
//
//                Message message=new Message();
//                message.what=REFRESHVIEW;
//                handler.sendMessage(message);
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                Toast.makeText(OrderAct.this,"订单删除失败",Toast.LENGTH_SHORT).show();
//            }
//        });
        Order order=new Order();
        order.setObjectId(orderList.get(position).getObjectId());
        order.delete(OrderAct.this, new DeleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(OrderAct.this,"订单删除成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
        orderList.remove(position);
        Message message=new Message();
        message.what=REFRESHVIEW;
        handler.sendMessage(message);


    }

}
