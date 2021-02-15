package com.example.hardwaremall;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwaremall.api.OrderService;
import com.example.hardwaremall.bean.Order;
import com.example.hardwaremall.bean.OrderItems;
import com.example.hardwaremall.bean.Reorder;
import com.example.hardwaremall.databinding.BuyScreenBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReOrderActivity extends AppCompatActivity {
    BuyScreenBinding binding;
    Order order;
    List<OrderItems> itemsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BuyScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        Intent in = getIntent();
        order = (Order) in.getSerializableExtra("order");

        Log.e("order","o---->"+order.getOrderId());

        //itemsList = order.getOrderItems();

        OrderService.OrderApi api = OrderService.getOrderApiInstance();
        Call<ArrayList<Reorder>> call = api.getQuantityOfOrderItems(order);
        call.enqueue(new Callback<ArrayList<Reorder>>() {
            @Override
            public void onResponse(Call<ArrayList<Reorder>> call, Response<ArrayList<Reorder>> response) {
                if (response.code() == 200){
                    ArrayList<Reorder> reorder = response.body();
                    Log.e("response code","===>"+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Reorder>> call, Throwable t) {

            }
        });

    }
}
