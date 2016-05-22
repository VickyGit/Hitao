package com.example.hitao.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.hitao.R;

import com.example.hitao.model.Buyer;
import com.example.hitao.model.Product;
import com.getbase.floatingactionbutton.FloatingActionButton;


import java.io.File;
import java.util.ArrayList;
import java.util.List;



import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2016/5/1.
 */
public class BuyerAct extends Activity implements View.OnClickListener{
    private RecyclerView recyclerView;
    public static  List<Product> productList=new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton orderFloatingBtn;
    private FloatingActionButton personalFloatingBtn;
    private final int REFRESHVIEW=1;
    private int FIRST_REFRESH=0;
    private Buyer buyer;
    public static Double money;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESHVIEW:
                    swipeRefreshLayout.setRefreshing(false);
                    recyclerViewAdapter=new RecyclerViewAdapter(productList, BuyerAct.this);
                    recyclerView.setAdapter(recyclerViewAdapter);
//                    Toast.makeText(BuyerAct.this,"Handler ProductList Size :"+productList.size(),Toast.LENGTH_SHORT).show();

                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_main_layout);
        orderFloatingBtn= (FloatingActionButton) findViewById(R.id.action_a);
        personalFloatingBtn= (FloatingActionButton) findViewById(R.id.action_b);

        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        //设置刷新时动画的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                FIRST_REFRESH=1;
                findProduct();
                Message message=new Message();
                message.what=REFRESHVIEW;
                handler.sendMessage(message);
            }
        });
        //1、实例化recyclerView
        recyclerView= (RecyclerView) findViewById(R.id.recyclerViewMain);
        //2、实例化布局管理
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        //3、为RecyclerView设置布局
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        buyer= (Buyer) getIntent().getSerializableExtra("buyerObject");
        money=buyer.getMoney();
        findProduct();
//        Toast.makeText(BuyerAct.this,"onCreate ProductList Size :"+productList.size(),Toast.LENGTH_SHORT).show();
        personalFloatingBtn.setOnClickListener(this);
        orderFloatingBtn.setOnClickListener(this);
    }

    public void findProduct(){
        BmobQuery<Product> productBmobQuery=new BmobQuery<Product>();

        productBmobQuery.findObjects(this, new FindListener<Product>() {
            @Override
            public void onSuccess(List<Product> list) {
                productList=list;
//                Toast.makeText(BuyerAct.this,"findProduct ProductList Size :"+list.size(),Toast.LENGTH_SHORT).show();
                if(FIRST_REFRESH==0){
                    Message message=new Message();
                    message.what=REFRESHVIEW;
                    handler.sendMessage(message);
                }
                
//                BmobFile bmobFile=list.get(0).getProductPic();
//                File appDir = new File(Environment.getExternalStorageDirectory(), "Hitao");
//                if (!appDir.exists()) {
//                    appDir.mkdir();
//                }
//                String fileName =bmobFile.getFilename();
//                final File file = new File(appDir, fileName);
//
//                bmobFile.download(BuyerAct.this, file,new DownloadFileListener() {
//                    @Override
//                    public void onSuccess(String s) {
//                        Toast.makeText(BuyerAct.this,"图片下载完成"+file.getPath(),Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(int i, String s) {
//                        Toast.makeText(BuyerAct.this,"图片下载失败"+s,Toast.LENGTH_SHORT).show();
//                    }
//                });


            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(BuyerAct.this,s,Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void startAnimationActivity(final View view,int position,List<Product> productList,String filePath){
        View card=view.findViewById(R.id.card_item_view);
        View btn=this.findViewById(R.id.floatingMenu);
        Transition ts=new ChangeTransform();
        ts.setDuration(3000);
        getWindow().setExitTransition(ts);
        Bundle bundle= ActivityOptions.makeSceneTransitionAnimation((Activity)this, Pair.create(card,"card"),Pair.create(btn,"btn")).toBundle();
        Intent intent=new Intent(BuyerAct.this,DetailedAct.class);
        intent.putExtra("picPath",filePath);
        intent.putExtra("name",productList.get(position).getProductName());
        intent.putExtra("desc",productList.get(position).getProductDesc());
        intent.putExtra("price",productList.get(position).getPrice().toString());
        intent.putExtra("num",productList.get(position).getNumber().toString());
        intent.putExtra("sellerName",productList.get(position).getSellerName().toString());
        intent.putExtra("buyerName",buyer.getBuyerName().toString());
        intent.putExtra("buyerObjectId",buyer.getObjectId());
        intent.putExtra("productObjectId",productList.get(position).getObjectId().toString());
        intent.putExtra("productObject",productList.get(position));
        intent.putExtra("position",position);
        startActivity(intent,bundle);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.action_b:
                Intent intent=new Intent(BuyerAct.this,PersonalAct.class);
                intent.putExtra("buyerObjectId",buyer.getObjectId());
                intent.putExtra("buyerMoney",money);
                startActivity(intent);
                break;
            case R.id.action_a:
                Intent intent1=new Intent(BuyerAct.this,OrderAct.class);
                intent1.putExtra("buyerObjectId",buyer.getObjectId());
                startActivity(intent1);
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level){
            case TRIM_MEMORY_UI_HIDDEN :
                recyclerView=null;
                personalFloatingBtn=null;
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recyclerViewAdapter.notifyDataSetChanged();
    }

}
