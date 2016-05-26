package com.example.hitao.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.hitao.R;
import com.example.hitao.model.Buyer;
import com.example.hitao.model.Seller;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends Activity implements View.OnClickListener{
    private MaterialEditText nameEdit;
    private MaterialEditText passwordEdit;
    private RadioButton buyerBtn;
    private RadioButton sellerBtn;
    private Button lognBtn;
    private Button regBtn;
    private String userName;
    private String userPass;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //第一步：允许Transition，并设置Transition
        //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //getWindow().setEnterTransition(new Explode().setDuration(1000));
        setContentView(R.layout.activity_main);
        init();
        //初始化Bmob
        Bmob.initialize(this,"2ea5f5c1d687a5d8ad9e8d657cfb13ac");
        lognBtn.setOnClickListener(this);
        regBtn.setOnClickListener(this);
        //接受注册返回的用户名
        String newUserName=getIntent().getStringExtra("userName");
        if(newUserName!=null){
            nameEdit.setText(newUserName);
        }
    }
    public void init(){
        nameEdit= (MaterialEditText) findViewById(R.id.name);
        passwordEdit= (MaterialEditText) findViewById(R.id.password);
        buyerBtn= (RadioButton) findViewById(R.id.buyerbtn);
        sellerBtn= (RadioButton) findViewById(R.id.sellerbtn);
        lognBtn= (Button) findViewById(R.id.logn);
        regBtn= (Button) findViewById(R.id.reg);
    }
    /*
    查询数据
     */
    public void queryData(){
        BmobQuery query=new BmobQuery("Buyer");
        BmobQuery query1 = new BmobQuery("Seller");
        query.findObjects(this, new FindCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                //Toast.makeText(MainActivity.this,"查询成功"+jsonArray.toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                //Toast.makeText(MainActivity.this,"查询失败:"+s,Toast.LENGTH_SHORT).show();
            }
        });
        query1.findObjects(this, new FindCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                //Toast.makeText(MainActivity.this,"查询成功"+jsonArray.toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                //Toast.makeText(MainActivity.this,"查询失败:"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }
    //注册和登录按钮动作事件
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logn:{
//                File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
//                File file=new File(appDir,"1460262981809.jpeg");
//                final BmobFile bmobFile=new BmobFile(file);
//                bmobFile.uploadblock(MainActivity.this, new UploadFileListener() {
//                    @Override
//                    public void onSuccess() {
//                        Product product=new Product();
//                        product.setProductPic(bmobFile);
//                        product.setProductName("好丽友");
//                        product.setPrice(44.33);
//                        product.setCategory(1);
//                        product.setNumber(3);
//                        product.setSellerId(3);
//                        product.save(MainActivity.this, new SaveListener() {
//                            @Override
//                            public void onSuccess() {
//                                Toast.makeText(MainActivity.this,"图片保存成功",Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onFailure(int i, String s) {
//                                Toast.makeText(MainActivity.this,"图片保存失败"+s,Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(int i, String s) {
//                        Toast.makeText(MainActivity.this,"图片保存失败"+s,Toast.LENGTH_SHORT).show();
//                    }
//                });
                userName=nameEdit.getText().toString();
                userPass=passwordEdit.getText().toString();
                //监测是否联网
                ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if (networkInfo!=null&&networkInfo.isAvailable()){


                    if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(userPass)){
                        Toast.makeText(MainActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                    }else{
                        if(buyerBtn.isChecked()){
                            BmobQuery<Buyer> buyerBmobQuery=new BmobQuery<Buyer>();
                            buyerBmobQuery.addWhereEqualTo("BuyerName",userName);
                            buyerBmobQuery.findObjects(MainActivity.this, new FindListener<Buyer>() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onSuccess(List<Buyer> list) {
                                    if(list.isEmpty()){
                                        Toast.makeText(MainActivity.this,"用户名不存在",Toast.LENGTH_SHORT).show();
                                    }else{
                                        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                                        progressDialog.setTitle("登录成功");
                                        progressDialog.setMessage("Loading...");
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                        Intent intent=new Intent(MainActivity.this,BuyerAct.class);
                                        intent.putExtra("buyerObject",list.get(0));

                                        //启动普通的Activity动画
                                        startActivity(intent);
                                        MainActivity.this.finish();
                                    }
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Toast.makeText(MainActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                                }
                            });


                        }else if (sellerBtn.isChecked()){
                            BmobQuery<Seller> sellerBmobQuery=new BmobQuery<Seller>();
                            sellerBmobQuery.addWhereEqualTo("SellerName",userName);
                            sellerBmobQuery.findObjects(MainActivity.this, new FindListener<Seller>() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onSuccess(List<Seller> list) {
                                    if(list.isEmpty()){
                                        Toast.makeText(MainActivity.this,"用户名不存在",Toast.LENGTH_SHORT).show();
                                    }else{
                                        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                                        progressDialog.setTitle("登录成功");
                                        progressDialog.setMessage("Loading...");
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                        Intent intent=new Intent(MainActivity.this,SellerAct.class);
                                        intent.putExtra("Mysellerid",list.get(0).getSellerId());
                                        intent.putExtra("sellername",list.get(0).getSellerName());
                                        intent.putExtra("sellerObject",list.get(0));

                                        //启动普通的Activity动画
                                        startActivity(intent);
                                        MainActivity.this.finish();
                                    }
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Toast.makeText(MainActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }
                }else{
                    Toast.makeText(MainActivity.this,"哎呀，网络走丢啦",Toast.LENGTH_SHORT).show();
                }


                break;
            }
            case R.id.reg:{

                Intent intent=new Intent(MainActivity.this,RegAct.class);
                //启动普通的Activity动画
                startActivity(intent);
                break;
            }

        }
    }


}
