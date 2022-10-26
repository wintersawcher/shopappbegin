package com.example.shopappn.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toolbar;

import com.example.shopappn.R;
import com.example.shopappn.retrofit.ApiBanHang;
import com.example.shopappn.retrofit.RetrofitClient;
import com.example.shopappn.utils.Utils;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class LapTopActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lap_top);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        AnhXa();
        ActionToolBars();
        getData();
    }

    private void getData() {
        //compositeDisposable.add(apiBanHang.getSanPham())
    }

    private void ActionToolBars() {

    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview_dt);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }
}