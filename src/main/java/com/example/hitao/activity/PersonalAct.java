package com.example.hitao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hitao.R;
import com.example.hitao.model.Buyer;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/5/5.
 */
public class PersonalAct extends Activity implements View.OnClickListener{
    private Button changeBtn;
    private MaterialEditText idEdit;
    private MaterialEditText nameEdit;
    private MaterialEditText passEdit;
    private TextView moneyText;
    private MaterialEditText addMoneyEdit;
    private Button addBtn;
    private String buyerId;
    private String name;
    private String pass;
    private Double money;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    moneyText.setText(money+"");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_layout);
        init();
        buyerId= getIntent().getStringExtra("buyerObjectId");
        money=getIntent().getDoubleExtra("buyerMoney",0);
        moneyText.setText(money+"");
        idEdit.setText(buyerId);
        idEdit.setEnabled(false);
        changeBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);

    }
    public void init(){
        changeBtn= (Button) findViewById(R.id.person_btn);
        idEdit= (MaterialEditText) findViewById(R.id.person_id);
        nameEdit= (MaterialEditText) findViewById(R.id.person_name);
        passEdit= (MaterialEditText) findViewById(R.id.person_pass);
        moneyText= (TextView) findViewById(R.id.person_money);
        addMoneyEdit= (MaterialEditText) findViewById(R.id.person_addmoney);
        addBtn= (Button) findViewById(R.id.person_recharge);
    }

    @Override
    public void onClick(View view) {
        Buyer buyer=new Buyer();
        switch (view.getId()){

            case R.id.person_btn:
                name=nameEdit.getText().toString();
                pass=passEdit.getText().toString();
                buyer.setBuyerName(name);
                buyer.setPassword(pass);
                buyer.update(this, buyerId, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(PersonalAct.this,"用户名密码修改完成",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(PersonalAct.this,"用户名密码修改失败"+s,Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.person_recharge:
                money+=Double.valueOf(addMoneyEdit.getText().toString());
                buyer.setMoney(money);
                buyer.update(this, buyerId, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Message message=new Message();
                        message.what=1;

                        handler.sendMessage(message);
                        Toast.makeText(PersonalAct.this,"账户充值成功,余额为："+money,Toast.LENGTH_SHORT).show();
                        addMoneyEdit.setText("");
                        BuyerAct.money=money;


                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(PersonalAct.this,"账户充值失败,请稍后再试",Toast.LENGTH_SHORT).show();
                    }
                });

                break;
        }
    }
}
