package com.example.hitao.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hitao.R;
import com.example.hitao.model.Order;

import java.util.List;

import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by fengsiyuan on 16/5/22.
 */

public class SOrderRecyclerViewAdapter extends RecyclerView.Adapter<SOrderRecyclerViewAdapter.SOrderViewHolder>{
    private Context mContext;
    private List<Order> orderList;
    private SendoutOrderListener sendoutOrderListener;
    public SOrderRecyclerViewAdapter(Context mContext, List<Order> orderList) {
        this.mContext = mContext;
        this.orderList = orderList;
    }


    @Override
    public SOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.s_order_item_layout,parent,false);
        SOrderViewHolder sOrderViewHolder = new SOrderViewHolder(view);
        return sOrderViewHolder;
    }


    public void onBindViewHolder(final SOrderViewHolder holder, final int position) {

        holder.productNameText.setText(orderList.get(position).getOrderProductName());
//        final String filePath=findProductimage(position,holder);
        holder.productNameText.setText(orderList.get(position).getOrderProductName());
        holder.productPriceText.setText(String.valueOf(orderList.get(position).getOrderProductPrice()));
        holder.buyerNameText.setText(orderList.get(position).getOrderBuyerName());
        holder.productNumText.setText(String.valueOf(orderList.get(position).getProductNum()));
        holder.orderTotleNumText.setText(String.valueOf(orderList.get(position).getTotlePrice()));
        if(orderList.get(position).getOrderState()==0){
            holder.orderStateText.setText("未发货");
            holder.sendoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"点击了发货",Toast.LENGTH_SHORT).show();

                    String objectid = orderList.get(position).getObjectId();
                    Order order = new Order();
                    order.setOrderState(1);
                    order.update(mContext, objectid, new UpdateListener() {
                        @Override
                        public void onSuccess() {

                            Log.d("V","发货成功");
                            holder.orderStateText.setText("发货");
                            holder.sendoutButton.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.d("F","发货失败");

                        }
                    });




                }
            });
        }else {
            holder.orderStateText.setText("发货");
            holder.sendoutButton.setVisibility(View.GONE);
        }
        holder.orderId.setText(orderList.get(position).getObjectId());



    }



    @Override
    public int getItemCount() {
        return orderList.size();
    }
    class SOrderViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView productNameText;
        TextView productPriceText;
        TextView buyerNameText;
        TextView productNumText;
        TextView orderTotleNumText;
        TextView orderStateText;
        TextView orderId;
        Button sendoutButton;
        public SOrderViewHolder(View itemView) {
            super(itemView);
            orderId= (TextView) itemView.findViewById(R.id.s_order_id);
            circleImageView= (CircleImageView) itemView.findViewById(R.id.s_order_product_image);
            productNameText= (TextView) itemView.findViewById(R.id.s_order_product_name);
            productPriceText= (TextView) itemView.findViewById(R.id.s_order_product_one_price);
            buyerNameText= (TextView) itemView.findViewById(R.id.s_order_buyer_name);
            productNumText= (TextView) itemView.findViewById(R.id.s_order_product_num);
            orderTotleNumText= (TextView) itemView.findViewById(R.id.s_order_product_totleprice);
            orderStateText= (TextView) itemView.findViewById(R.id.s_order_state);
            sendoutButton= (Button) itemView.findViewById(R.id.s_send_out_btn);

        }
    }
    public interface SendoutOrderListener{
        public void SendoutOrder(int position);
    }

}
