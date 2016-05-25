package com.example.hitao.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hitao.R;
import com.example.hitao.model.Order;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/5/10.
 */
public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.OrderViewHolder>{
    private Context mContext;
    private List<Order> orderList;
    private DeleteOrderListener deleteOrderListener;

    public OrderRecyclerViewAdapter(Context mContext, List<Order> orderList,DeleteOrderListener deleteOrderListener) {
        this.mContext = mContext;
        this.orderList = orderList;
        this.deleteOrderListener=deleteOrderListener;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.order_item_layout,parent,false);
        OrderViewHolder orderViewHolder=new OrderViewHolder(view);
        return orderViewHolder;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, final int position) {
        holder.productNameText.setText(orderList.get(position).getOrderProductName());
//        final String filePath=findProductimage(position,holder);
        holder.productNameText.setText(orderList.get(position).getOrderProductName());
        holder.productPriceText.setText(String.valueOf(orderList.get(position).getOrderProductPrice()));
        holder.sellerNameText.setText(orderList.get(position).getOrderSellerName());
        holder.productNumText.setText(String.valueOf(orderList.get(position).getProductNum()));
        holder.orderTotleNumText.setText(String.valueOf(orderList.get(position).getTotlePrice()));
        if(orderList.get(position).getOrderState()==0){
            holder.orderStateText.setText("未发货");
            holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"点击了取消订单",Toast.LENGTH_SHORT).show();
                    deleteOrderListener.deleteOrder(position);
                }
            });
        }else {
            holder.orderStateText.setText("发货");
            holder.cancelBtn.setVisibility(View.GONE);
        }
        holder.orderId.setText(orderList.get(position).getObjectId());




    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    //ViewHolder类,继承RecyclerView.ViewHolder。自定义ViewHolder类仅仅对控件进行初始化绑定
    class OrderViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView productNameText;
        TextView productPriceText;
        TextView sellerNameText;
        TextView productNumText;
        TextView orderTotleNumText;
        TextView orderStateText;
        TextView orderId;
        Button cancelBtn;
        public OrderViewHolder(View itemView) {
            super(itemView);
            orderId= (TextView) itemView.findViewById(R.id.order_id);
            circleImageView= (CircleImageView) itemView.findViewById(R.id.order_product_image);
            productNameText= (TextView) itemView.findViewById(R.id.order_product_name);
            productPriceText= (TextView) itemView.findViewById(R.id.order_product_one_price);
            sellerNameText= (TextView) itemView.findViewById(R.id.order_seller_name);
            productNumText= (TextView) itemView.findViewById(R.id.order_product_num);
            orderTotleNumText= (TextView) itemView.findViewById(R.id.order_product_totleprice);
            orderStateText= (TextView) itemView.findViewById(R.id.order_state);
            cancelBtn= (Button) itemView.findViewById(R.id.cancelorder_btn);

        }
    }
//    public String findProductimage(final int position, final OrderViewHolder holder){
//        final BmobFile bmobFile=orderList.get(position).getProductObject().getProductPic();
//        File appDir = new File(Environment.getExternalStorageDirectory(), "Hitao");
//        if (!appDir.exists()) {
//            appDir.mkdir();
//        }
//        String fileName =bmobFile.getFilename();
//        final File file = new File(appDir, fileName);
//        Bitmap bitmap=getBitmapFromMemCache(file.getPath());
//        if(bitmap!=null){
//            holder.circleImageView.setImageBitmap(bitmap);
//        }else{
//            bmobFile.download(mContext, file,new DownloadFileListener() {
//                @Override
//                public void onSuccess(String s) {
//                    //性能需要优化
////                    Toast.makeText(mContext,"图片下载完成"+file.getPath(),Toast.LENGTH_SHORT).show();
//                    Bitmap bitmap=RecyclerViewAdapter.decodeSampledBitmapfromFielPath(file.getPath(),100,100);
//                    addBitmapToMemoryCache(file.getPath(),bitmap);
//
//                    if(bitmap!=null){
//                        holder.circleImageView.setImageBitmap(bitmap);
//                    }else{
//                        holder.circleImageView.setImageResource(R.drawable.welcome);
//                    }
//                }
//                @Override
//                public void onFailure(int i, String s) {
//                    Toast.makeText(mContext,"图片下载失败"+s,Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//        return file.getPath();
//    }
    public void addBitmapToMemoryCache(String key,Bitmap bitmap){
        if(getBitmapFromMemCache(key)==null){
            RecyclerViewAdapter.mMemoryCache.put(key,bitmap);
        }
    }
    public Bitmap getBitmapFromMemCache(String key){
        return  RecyclerViewAdapter.mMemoryCache.get(key);
    }
    public interface DeleteOrderListener{
        public void deleteOrder(int position);
    }


}
