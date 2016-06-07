package com.example.hitao.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/4/30.
 */
public class RegAct extends Activity implements View.OnClickListener{
    private MaterialEditText userName;
    private MaterialEditText userPass;
    private RadioButton buyerBtn;
    private RadioButton sellerBtn;
    private Button regBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //getWindow().setEnterTransition(new Explode().setDuration(1000));
        setContentView(R.layout.reg_layout);
        init();
        regBtn.setOnClickListener(this);}
    public void init(){
        userName= (MaterialEditText) findViewById(R.id.name_reg);
        userPass= (MaterialEditText) findViewById(R.id.password_reg);
        buyerBtn= (RadioButton) findViewById(R.id.buyerbtn_reg);
        sellerBtn= (RadioButton) findViewById(R.id.sellerbtn_reg);
        regBtn= (Button) findViewById(R.id.reg_btn);
    }

    public void addUser(){
        String name=userName.getText().toString();
        String password=userPass.getText().toString();
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isAvailable()){
            final Intent intent=new Intent(RegAct.this,MainActivity.class);
            intent.putExtra("userName",name);
            if(sellerBtn.isChecked()){
                Seller seller=new Seller();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(password)){
                    Toast.makeText(RegAct.this,"用户名密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    seller.setSellerName(name);
                    seller.setPassword(password);


                    seller.save(RegAct.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(RegAct.this,"恭喜你注册成功",Toast.LENGTH_SHORT).show();
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(RegAct.this).toBundle());
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(RegAct.this,"注册失败，请重新尝试",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }else if(buyerBtn.isChecked()){
                Buyer buyer=new Buyer();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(password)){
                    Toast.makeText(RegAct.this,"用户名密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    buyer.setBuyerName(name);
                    buyer.setPassword(password);
                    buyer.setMoney(1000.0);
                    buyer.save(RegAct.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(RegAct.this,"恭喜你注册成功",Toast.LENGTH_SHORT).show();
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(RegAct.this).toBundle());
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(RegAct.this,"注册失败，请重新尝试",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }else {
            Toast.makeText(RegAct.this,"哎呀，网络走丢啦",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        addUser();
    }
}
