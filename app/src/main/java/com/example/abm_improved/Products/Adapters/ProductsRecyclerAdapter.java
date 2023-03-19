package com.example.abm_improved.Products.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm_improved.DataClasses.Product;
import com.example.abm_improved.R;

import java.util.ArrayList;

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ProductsViewHolder> {

    private ArrayList<Product> products;
    private ProductsRecyclerAdapter.OnItemClickListener clickListener; //instance of interface below

    //Interface for onclick listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ProductsRecyclerAdapter.OnItemClickListener listener) {
        this.clickListener = listener;
    }

    //This is a static class that is a subclass of RecyclerView.ViewHolder
    public static class ProductsViewHolder extends RecyclerView.ViewHolder {
        // Parameters of the XML item
        public TextView productName;
        public TextView productQuantity;

        public ProductsViewHolder(@NonNull View itemView, ProductsRecyclerAdapter.OnItemClickListener listener) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productQuantity = itemView.findViewById(R.id.productQuantity);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public ProductsRecyclerAdapter(ArrayList<Product> products) {
        this.products = products;
    }


    @NonNull
    @Override
    public ProductsRecyclerAdapter.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_product_item, parent, false);
        return new ProductsRecyclerAdapter.ProductsViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsRecyclerAdapter.ProductsViewHolder holder, int position) {
        //This method is called when the recycler view is created and it binds the view holder to the data
        Product currProduct = this.products.get(position);
        holder.productName.setText(currProduct.getName());
        holder.productQuantity.setText(currProduct.getQuantity());
    }


    @Override
    public int getItemCount() {
        return this.products.size();
    }
}
