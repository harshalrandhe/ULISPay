package com.ulisfintech.arthavendor.ui.products;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.ulisfintech.arthavendor.models.ProductResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {

    private MutableLiveData<List<ProductBean>> products;

    public ProductViewModel() {
        this.products = new MutableLiveData<>();
    }

    public MutableLiveData<List<ProductBean>> getProducts() {
        return products;
    }

    public void getAllProducts(Context context){
        List<ProductBean> productBeans = new ArrayList<>();


        String jsonStr = loadJSONFromAsset(context);

        ProductResponse response = new Gson().fromJson(jsonStr, ProductResponse.class);

        products.setValue(response.getProducts());
    }

    public String loadJSONFromAsset(Context context) {
        String json = "";
        try {
            InputStream is = context.getAssets().open("jsonviewer.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
