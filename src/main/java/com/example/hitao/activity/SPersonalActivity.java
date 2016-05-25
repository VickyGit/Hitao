package com.example.hitao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hitao.R;
import com.example.hitao.model.Seller;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/5/5.
 */
public class SPersonalActivity extends Activity {
    private Button changeBtn;
    private MaterialEditText idEdit;
    private MaterialEditText nameEdit;
    private MaterialEditText passEdit;
    private String SellerID;
    private String name;
    private String pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_personal_layout);
        init();
        SellerID= getIntent().getStringExtra("SellerObjectId");
        idEdit.setText(SellerID);
        idEdit.setEnabled(false);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Seller seller=new Seller();
                name=nameEdit.getText().toString();
                pass=passEdit.getText().toString();
                seller.setSellerName(name);
                seller.setPassword(pass);
                seller.update(SPersonalActivity.this, SellerID, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(SPersonalActivity.this,"用户名密码修改完成",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(SPersonalActivity.this,"用户名密码修改失败"+s,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }
    public void init(){
        changeBtn= (Button) findViewById(R.id.seller_person_btn);
        idEdit= (MaterialEditText) findViewById(R.id.seller_person_id);
        nameEdit= (MaterialEditText) findViewById(R.id.seller_person_name);
        passEdit= (MaterialEditText) findViewById(R.id.seller_person_pass);

    }







}