package com.example.farmerscart.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmerscart.R;
import com.example.farmerscart.activities.ShopDetailsActivity;
import com.example.farmerscart.models.ModelCartItem;
import com.example.farmerscart.models.ModelProduct;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class AdapterCartItem extends RecyclerView.Adapter<AdapterCartItem.HolderCartItem> {

    private Context context;
    private ArrayList<ModelCartItem> cartItems;

    public AdapterCartItem(Context context, ArrayList<ModelCartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public HolderCartItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_cartitem, parent, false);
        return new HolderCartItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCartItem holder,  int position) {
        ModelCartItem modelCartItem = cartItems.get(position);
        String id = modelCartItem.getId();
        String getpId  = modelCartItem.getpId();
        String title  = modelCartItem.getName();
        final String cost  = modelCartItem.getCost();
        String price  = modelCartItem.getPrice();
        String quantity  = modelCartItem.getQuantity();

        holder.itemTitleTv.setText(""+title);
        holder.itemPriceTv.setText(""+cost);
        holder.itemQuantityTv.setText("["+quantity+"]");
        holder.itemPriceEachTv.setText(""+price);

        holder.itemRemoveTv.setOnClickListener(view -> {

            EasyDB easyDB = EasyDB.init(context, "ITEMS_DB")
                    .setTableName("ITEMS_TABLE")
                    .addColumn(new Column("Item_Id", new String[]{"text", "unique"}))
                    .addColumn(new Column("Item_PID", new String[]{"text", "not null"}))
                    .addColumn(new Column("Item_Name", new String[]{"text", "not null"}))
                    .addColumn(new Column("Item_Price_Each", new String[]{"text", "not null"}))
                    .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
                    .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                    .doneTableColumn();

            easyDB.deleteRow(1, id);
            Toast.makeText(context, "Removed from Cart...", Toast.LENGTH_SHORT).show();

            cartItems.remove(position);
            AdapterCartItem.this.notifyItemChanged(position);
            AdapterCartItem.this.notifyDataSetChanged();

            double tx = Double.parseDouble((((ShopDetailsActivity) context).allTotalPriceTv.getText().toString().trim().replace("Rs", "")));
            double totalPrice = tx - Double.parseDouble(cost.replace("Rs", ""));
            double deliveryFee = Double.parseDouble((((ShopDetailsActivity) context).deliveryFee.replace("Rs", "")));
            double sTotalPrice = Double.parseDouble(String.format("%.2f", totalPrice)) - Double.parseDouble(String.format("%.2f", deliveryFee));
            ((ShopDetailsActivity) context).allTotalPrice = 0.00;
            ((ShopDetailsActivity) context).sTotalTv.setText("Rs" + String.format("%.2f", sTotalPrice));
            ((ShopDetailsActivity) context).allTotalPriceTv.setText("Rs" + String.format("%.2f", Double.parseDouble(String.format("%.2f", totalPrice))));

            ((ShopDetailsActivity)context).cartCount();

        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class HolderCartItem extends RecyclerView.ViewHolder{

        private TextView itemTitleTv,itemPriceTv,itemPriceEachTv,itemQuantityTv,itemRemoveTv;


        public HolderCartItem(@NonNull View itemView) {
            super(itemView);

            itemTitleTv = itemView.findViewById(R.id.itemTitleTv);
            itemPriceTv = itemView.findViewById(R.id.itemPriceTv);
            itemPriceEachTv = itemView.findViewById(R.id.itemPriceEachTv);
            itemQuantityTv = itemView.findViewById(R.id.itemQuantityTv);
            itemRemoveTv = itemView.findViewById(R.id.itemRemoveTv);
        }
    }
}
