package com.ulisfintech.arthavendor.ui.products;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ulisfintech.arthavendor.R;

import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder> {

    private Context context;
    private List<ProductBean> products;

    public ProductRecyclerAdapter(Context context) {
        this.context = context;
    }

    public ProductRecyclerAdapter(Context context, List<ProductBean> products) {
        this.context = context;
        this.products = products;
    }

    public List<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBean> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_adapter_item,
                parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        ProductBean productBean = products.get(position);
        holder.tvProductName.setText(productBean.getName());
        holder.tvProductDesc.setText(productBean.getDesc());
        holder.tvProductCategory.setText("Cat: "+productBean.getCategory());
        holder.tvProductPrice.setText("Price ₹ "+productBean.getPrice());


        String url = productBean.getImg();

        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivProductPoster);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("product", productBean);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName;
        TextView tvProductDesc;
        TextView tvProductPrice;
        TextView tvProductCategory;
        ImageView ivProductPoster;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductDesc = itemView.findViewById(R.id.tvProductDesc);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            ivProductPoster = itemView.findViewById(R.id.ivProductPoster);
            tvProductCategory = itemView.findViewById(R.id.tvProductCategory);
        }
    }
}
