package com.example.shopappn.activity;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopappn.R;
import com.example.shopappn.adapter.DienThoaiAdapter;
import com.example.shopappn.adapter.SanPhamMoiAdapter;
import com.example.shopappn.model.SanPhamMoi;
import com.example.shopappn.retrofit.ApiBanHang;
import com.example.shopappn.retrofit.RetrofitClient;
import com.example.shopappn.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DienThoatActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page =1;
    int loai;
    DienThoaiAdapter adapterDt;
    List<SanPhamMoi> sanPhamMoiList;
    LinearLayoutManager linearLayoutManager;
    Handler  handler = new Handler();
    boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoat);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai",1);
        AnhXa();
        ActionToolBars();
        getData(page);
        addEvenLoad();
    }

    private void addEvenLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override

            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition()== sanPhamMoiList.size()-1){
                        isLoading = true;
                        loadmore();
                    }
                }
            }

        });
    }

    private void loadmore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                sanPhamMoiList.add(null);
                adapterDt.notifyItemInserted(sanPhamMoiList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sanPhamMoiList.remove(sanPhamMoiList.size()-1);
                adapterDt.notifyItemRemoved(sanPhamMoiList.size());
                page= page +1;
                getData(page);
                adapterDt.notifyDataSetChanged();
                isLoading = false;
            }
        },2000 );
    }

    private void getData(int page) {
            compositeDisposable.add(apiBanHang.getSanPham(page,loai)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            sanPhamMoiModel -> {
                              if(sanPhamMoiModel.isSuccess()){
                                  if(adapterDt ==null){
                                      sanPhamMoiList = sanPhamMoiModel.getResult();
                                      adapterDt = new DienThoaiAdapter(getApplicationContext(),sanPhamMoiList);
                                      recyclerView.setAdapter(adapterDt);
                                  }else{
                                      int vitri = sanPhamMoiList.size()-1;
                                      int soluongadd = sanPhamMoiModel.getResult().size();
                                      for(int i=0;i<soluongadd;i++){
                                          sanPhamMoiList.add(sanPhamMoiModel.getResult().get(i));
                                      }
                                      adapterDt.notifyItemRangeInserted(vitri,soluongadd);
                                  }
                              }else{
                                  Toast.makeText(getApplicationContext(),"Hết dữ liệu rồi",Toast.LENGTH_LONG).show();
                              }

                            },

                            throwable -> {
                                Toast.makeText(getApplicationContext(),"Khong ket noi server",Toast.LENGTH_LONG).show();
                            }
                    ));
    }

    private void ActionToolBars() {
       setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview_dt);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList = new ArrayList<>();

    }
}