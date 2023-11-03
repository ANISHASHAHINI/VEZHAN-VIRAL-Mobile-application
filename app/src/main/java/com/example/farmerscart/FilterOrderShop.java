package com.example.farmerscart;


import android.widget.Filter;

import com.example.farmerscart.adapters.AdapterOrderShop;
import com.example.farmerscart.adapters.AdapterProductSeller;
import com.example.farmerscart.models.ModelOrderShop;
import com.example.farmerscart.models.ModelProduct;

import java.util.ArrayList;

public class FilterOrderShop extends Filter {

    private AdapterOrderShop adapter;
    private ArrayList<ModelOrderShop> filterList;

    public FilterOrderShop(AdapterOrderShop adapter, ArrayList<ModelOrderShop> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();

        if (charSequence != null && charSequence.length() >0) {
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<ModelOrderShop> filteredModels = new ArrayList<>();
            for (int i=0;i<filterList.size();i++){
                if (filterList.get(i).getOrderStatus().toUpperCase().contains(charSequence)){

                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
        }

        else {
            results.count = filterList.size();
            results.values = filterList;

        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.orderShopArrayList = (ArrayList<ModelOrderShop>) filterResults.values;
        adapter.notifyDataSetChanged();



    }
}
