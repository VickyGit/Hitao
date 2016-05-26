package com.example.hitao.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hitao.R;
import com.getbase.floatingactionbutton.FloatingActionButton;

/**
 * Created by Administrator on 2016/5/4.
 */
public class SDetailedAct extends Activity{
    static SDetailedAct sDetailedAct;
    private ImageView productImage;
    private FloatingActionButton buyFloatingBtn;
    private TextView productName;
    private TextView productDescText;
    private TextView productPrice;
    private TextView productNum;
    private TextView productSellerName;
    private int imageWidth;
    private String name;
    private String desc;
    private String filePath;
    private String price;
    private String num;
    private String category;
    private String objectID;
    private int SellerID;
    private String sellername;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Bitmap bitmap=RecyclerViewAdapter.decodeSampledBitmapfromFielPath(filePath,imageWidth,200);
                    productImage.setImageBitmap(bitmap);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detialed_layout);
        sDetailedAct = this;
        init();
        filePath=getIntent().getStringExtra("picPath");
        name=getIntent().getStringExtra("name");
        desc=getIntent().getStringExtra("desc");
        price=getIntent().getStringExtra("price");
        num=getIntent().getStringExtra("num");
        sellername=getIntent().getStringExtra("sellername");

        category = getIntent().getStringExtra("category");
        objectID = getIntent().getStringExtra("objectId");
        SellerID = Integer.parseInt(getIntent().getStringExtra("sellerId"));

        productImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                productImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                imageWidth=productImage.getMeasuredWidth();

                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
        });
        productName.setText(name);
        productDescText.setText(desc);
        productPrice.setText(price);
        productNum.setText(num);
        productSellerName.setText(sellername);
        buyFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SDetailedAct.this,ChProoductActivity.class);
                intent.putExtra("choose_act",1);
                intent.putExtra("Pname",name);
                intent.putExtra("Pdesc",desc);
                intent.putExtra("Pprice",price);
                intent.putExtra("Pnum",num);
                intent.putExtra("Pcategory",category);
                intent.putExtra("Pfilename",filePath);
                intent.putExtra("objectId",objectID);
                intent.putExtra("Mysellerid",SellerID);


                startActivity(intent);

            }
        });
    }
    public void init(){
        productImage= (ImageView) findViewById(R.id.product_desc_image);
        buyFloatingBtn= (FloatingActionButton) findViewById(R.id.buy_desc_floatbtn);
        productName= (TextView) findViewById(R.id.product_desc_name);
        productDescText= (TextView) findViewById(R.id.product_desc_text);
        productPrice= (TextView) findViewById(R.id.product_desc_price);
        productNum= (TextView) findViewById(R.id.product_desc_number);
        productSellerName = (TextView)findViewById(R.id.product_desc_sellerName);
    }
}
