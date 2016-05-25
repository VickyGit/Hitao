package com.example.hitao.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hitao.R;
import com.example.hitao.model.Product;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by Administrator on 2016/4/29.
 */
public class SRecyclerViewAdapter extends RecyclerView.Adapter<SRecyclerViewAdapter.ProductViewHolder>{

    private List<Product> productList;
    private Context mContext;
    private int width;
    private int height;


    @Override
    public void onViewDetachedFromWindow(final ProductViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.productPic.post(new Runnable() {
            @Override
            public void run() {
                width=holder.productPic.getWidth();
                height=holder.productPic.getHeight();
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(final ProductViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.productPic.post(new Runnable() {
            @Override
            public void run() {
                width=holder.productPic.getWidth();
                height=holder.productPic.getHeight();
            }
        });
    }

    public SRecyclerViewAdapter(List<Product> productList, Context mContext) {
        this.productList = productList;
        this.mContext = mContext;


    }
    /*1、动态加载布局。
    * 2、实例化ViewHolder
    * 3、返回实例化ViewHolder
    */
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.buyer_item_layout,parent,false);
        ProductViewHolder productViewHolder=new ProductViewHolder(view);

        return productViewHolder;
    }


    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        holder.productPic.post(new Runnable() {
            @Override
            public void run() {
                width=holder.productPic.getMeasuredWidth();
                height=holder.productPic.getMeasuredHeight();
            }
        });


        final String filePath=findProductimage(position,holder);
        holder.productTitle.setText(productList.get(position).getProductName());
        holder.productNum.setText(productList.get(position).getNumber().toString());
        holder.productPrice.setText(productList.get(position).getPrice().toString());
        holder.itemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,SDetailedAct.class);
                intent.putExtra("picPath",filePath);
                intent.putExtra("name",productList.get(position).getProductName());
                intent.putExtra("desc",productList.get(position).getProductDesc());
                intent.putExtra("price",productList.get(position).getPrice().toString());
                intent.putExtra("num",productList.get(position).getNumber().toString());
                intent.putExtra("sellerId",productList.get(position).getSellerId().toString());
                intent.putExtra("category",productList.get(position).getCategory().toString());
                intent.putExtra("objectId",productList.get(position).getObjectId().toString());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    //ViewHolder类,继承RecyclerView.ViewHolder。自定义ViewHolder类仅仅对控件进行初始化绑定
    class ProductViewHolder extends RecyclerView.ViewHolder{
        CardView itemCardView;
        ImageView productPic;
        TextView productTitle;
        TextView productPrice;
        TextView productNum;
        public ProductViewHolder(View itemView) {
            super(itemView);
            itemCardView= (CardView) itemView.findViewById(R.id.card_item_view);
            productPic= (ImageView) itemView.findViewById(R.id.product_item_image);
            productTitle= (TextView) itemView.findViewById(R.id.produce_item_name);
            productPrice= (TextView) itemView.findViewById(R.id.product_price_item);
            productNum= (TextView) itemView.findViewById(R.id.product_number_item);
        }
    }
    public String findProductimage(final int position, final ProductViewHolder holder){
        final BmobFile bmobFile=productList.get(position).getProductPic();
        File appDir = new File(Environment.getExternalStorageDirectory(), "Hitao");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName =bmobFile.getFilename();
        final File file = new File(appDir, fileName);
        bmobFile.download(mContext, file,new DownloadFileListener() {
            @Override
            public void onSuccess(String s) {
                //性能需要优化
                //Toast.makeText(mContext,"图片下载完成"+file.getPath(),Toast.LENGTH_SHORT).show();
                Bitmap bitmap=decodeSampledBitmapfromFielPath(file.getPath(),width,height);
                if(bitmap!=null){
                    holder.productPic.setImageBitmap(bitmap);
                }else{
                    holder.productPic.setImageResource(R.drawable.welcome);
                }
            }
            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(mContext,"图片下载失败"+s,Toast.LENGTH_SHORT).show();
            }
        });
        return file.getPath();
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        //原图片的高和宽
        final int height=options.outHeight;
        final int width=options.outWidth;
        int inSampleSize=1;
        if(height>reqHeight||width>reqHeight){
            final int heightRatio=Math.round((float)height/(float)reqHeight);
            final int widthRatio=Math.round((float)width/(float)reqWidth);
            inSampleSize=heightRatio<widthRatio? heightRatio:widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapfromFielPath(String filePath,int reqWidth,int reqHeight){
        //第一次解析将InJustDecodeBounds设置为true，获取图片大小
        final BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(filePath,options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(filePath,options);
    }

}
