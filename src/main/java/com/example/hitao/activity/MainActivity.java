package com.example.hitao.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.hitao.R;
import com.example.hitao.model.Buyer;
import com.example.hitao.model.Product;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;

import java.io.File;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MainActivity extends Activity implements View.OnClickListener{
    private MaterialEditText nameEdit;
    private MaterialEditText passwordEdit;
    private RadioButton buyerBtn;
    private RadioButton sellerBtn;
    private Button lognBtn;
    private Button regBtn;
    private String userName;
    private String userPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //第一步：允许Transition，并设置Transition
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode().setDuration(1000));
        getWindow().setExitTransition(new Explode().setDuration(1000));
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
        query.findObjects(this, new FindCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                Toast.makeText(MainActivity.this,"查询成功"+jsonArray.toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(MainActivity.this,"查询失败:"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }
    //注册和登录按钮动作事件
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
                if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(userPass)){
                    Toast.makeText(MainActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    if(buyerBtn.isChecked()){
                        BmobQuery<Buyer> buyerBmobQuery=new BmobQuery<Buyer>();
                        buyerBmobQuery.addWhereEqualTo("BuyerName",userName);
                        buyerBmobQuery.addWhereEqualTo("Password",userPass);
                        buyerBmobQuery.findObjects(MainActivity.this, new FindListener<Buyer>() {
                            @Override
                            public void onSuccess(List<Buyer> list) {
                               if(list.isEmpty()){
                                   Toast.makeText(MainActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                               }else{
                                   Intent intent=new Intent(MainActivity.this,BuyerAct.class);
                                   intent.putExtra("buyerObject",list.get(0));
                                   //启动普通的Activity动画
                                   startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                               }
                            }

                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(MainActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }

                break;
            }
            case R.id.reg:{

                Intent intent=new Intent(MainActivity.this,RegAct.class);
                //启动普通的Activity动画
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            }

        }
    }


}
