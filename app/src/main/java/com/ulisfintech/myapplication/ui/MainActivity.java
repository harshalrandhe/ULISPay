package com.ulisfintech.myapplication.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ulisfintech.myapplication.databinding.ActivityMainBinding;
import com.ulisfintech.myapplication.ui.products.ProductRecyclerAdapter;
import com.ulisfintech.myapplication.ui.products.ProductViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ProductViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);



        ProductRecyclerAdapter adapter = new ProductRecyclerAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        viewModel.getProducts().observe(this, productBeans -> {

            if (productBeans != null) {
                adapter.setProducts(productBeans);
                adapter.notifyDataSetChanged();
            }

        });


        viewModel.getAllProducts(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}