package com.example.hitao.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hitao.R;
import com.example.hitao.model.Buyer;
import com.example.hitao.model.Order;
import com.example.hitao.model.Product;
import com.getbase.floatingactionbutton.FloatingActionButton;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/5/4.
 */
public class DetailedAct extends Activity{
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
    private String sellerName;
    private String buyerName;
    private TextView productNumText;
    private String buyerId;
    private static Integer surplus;
    private String productObjectId;
    private int position;
    private Product product;
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
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //第一步：允许Transition，并设置Transition
       // getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //getWindow().setEnterTransition(new Explode().setDuration(1000));
        setContentView(R.layout.detialed_layout);
        init();
        filePath=getIntent().getStringExtra("picPath");
        name=getIntent().getStringExtra("name");
        desc=getIntent().getStringExtra("desc");
        price=getIntent().getStringExtra("price");
        num=getIntent().getStringExtra("num");
        surplus=Integer.valueOf(num);
        sellerName=getIntent().getStringExtra("sellerName");
        buyerName=getIntent().getStringExtra("buyerName");
        buyerId= getIntent().getStringExtra("buyerObjectId");
        productObjectId=getIntent().getStringExtra("productObjectId");
        position=getIntent().getIntExtra("position",0);
        product= (Product) getIntent().getSerializableExtra("productObject");

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
        productSellerName.setText(sellerName);

        //商品购买
        buyFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Animator animator= ViewAnimationUtils.createCircularReveal(buyFloatingBtn,buyFloatingBtn.getWidth()/2,buyFloatingBtn.getHeight()/2,0,buyFloatingBtn.getWidth());
                //animator.setInterpolator(new AccelerateDecelerateInterpolator());
                //animator.setDuration(1000);
                //animator.start();

                final Dialog dialog=new Dialog(DetailedAct.this);
                dialog.setContentView(R.layout.buy_dialog_layout);

                //绑定dialog控件
                productNumText = (TextView) dialog.findViewById(R.id.dialog_num);
                final ImageButton addimageBtn= (ImageButton) dialog.findViewById(R.id.addBtn);
                final ImageButton removeimageBtn= (ImageButton) dialog.findViewById(R.id.removeBtn);
                Button buyBtn= (Button) dialog.findViewById(R.id.dialog_btn);
                final int[] buynum = {1};
                addimageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buynum[0] +=1;
                        productNumText.setText(buynum[0]+"");
                    }
                });
                removeimageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(buynum[0]<=1){
                            buynum[0]=1;
                        }else{
                            buynum[0]-=1;
                            productNumText.setText(buynum[0]+"");
                        }
                    }
                });
                buyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(surplus>=0){
                            if(surplus>=buynum[0]){

                                addOrder();

                                dialog.dismiss();

                            }else {
                                Toast.makeText(DetailedAct.this,"库存不足",Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(DetailedAct.this,"产品没有库存啦",Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                //dialog.create();
                dialog.show();
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
        productSellerName= (TextView) findViewById(R.id.product_desc_sellerName);
    }
    /*
    当界面不可见时释放内存
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level){
            case TRIM_MEMORY_UI_HIDDEN:
                productImage=null;
                buyFloatingBtn=null;
                productName=null;
                productDescText=null;
                productPrice=null;
                productNum=null;
                productSellerName=null;

                break;
        }
    }


    public void addOrder(){
        Order order=new Order();

        order.setOrderBuyerName(buyerName);
        order.setOrderProductName(name);
        order.setOrderSellerName(sellerName);
        order.setOrderState(0);
        order.setBuyerObjectId(buyerId);
        order.setOrderProductPrice(Double.valueOf(price));
        final Integer buynum=Integer.valueOf(productNumText.getText().toString());
        order.setProductNum(buynum);
        final Double totlePrice= (Double.valueOf(price)*buynum);
        order.setTotlePrice(totlePrice);

        order.save(DetailedAct.this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(DetailedAct.this,"购买成功,订单已自动生成",Toast.LENGTH_SHORT).show();
                BuyerAct.money-=totlePrice;
                Buyer buyer=new Buyer();
                buyer.setMoney(BuyerAct.money);
                surplus-=buynum;

                productNum.setText(surplus+"");
                Product product=new Product();

                product.setNumber(surplus);
                BuyerAct.productList.get(position).setNumber(surplus);
                product.update(DetailedAct.this, productObjectId, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DetailedAct.this,"产品更新完毕",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

                buyer.update(DetailedAct.this, buyerId, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DetailedAct.this,"金额消费成功，余额为"+BuyerAct.money,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(DetailedAct.this,"购买失败"+s,Toast.LENGTH_SHORT).show();

            }

        });



    }

}
