package com.example.shopappn.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;


import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.shopappn.R;
import com.example.shopappn.adapter.LoaiSpAdapter;
import com.example.shopappn.adapter.SanPhamMoiAdapter;
import com.example.shopappn.model.LoaiSp;
import com.example.shopappn.model.LoaiSpModel;
import com.example.shopappn.model.SanPhamMoi;
import com.example.shopappn.model.SanPhamMoiModel;
import com.example.shopappn.retrofit.ApiBanHang;
import com.example.shopappn.retrofit.RetrofitClient;
import com.example.shopappn.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        AnhXa();
        ActionBar();
        ActionViewFlipper();
        getSpMoi();
        if(isConnected(this)){

            getLoaiSanPham();
            getEventClick();
        } else{
            Toast.makeText(this, "Không có internet,vui lòng kết nối", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(),DienThoatActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(),LapTopActivity.class);
                        startActivity(laptop);
                        break;


                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                             if(sanPhamMoiModel.isSuccess()){
                                 mangSpMoi = sanPhamMoiModel.getResult();
                                 spAdapter = new SanPhamMoiAdapter(getApplicationContext(),mangSpMoi);
                                 recyclerViewManHinhChinh.setAdapter(spAdapter);
                             }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getLoaiSanPham() {
      compositeDisposable.add(apiBanHang.getLoaiSp()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(
                    loaiSpModel -> {
                        if (loaiSpModel.isSuccess()){
                            mangloaisp = loaiSpModel.getResult();
                            loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangloaisp);
                            listViewManHinhChinh.setAdapter(loaiSpAdapter);
                        }
                    }
              ));
    }

    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for(int i =0;i<mangquangcao.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in  = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out  = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_left);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setInAnimation(slide_out);

    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewlipper);
        recyclerViewManHinhChinh = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
        recyclerViewManHinhChinh.setHasFixedSize(true);
        listViewManHinhChinh = findViewById(R.id.listiviewmanhinhchinh);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);
        // khởi tạo adapter
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();



    }
    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }
    private  boolean isConnected(Context context){
        ConnectivityManager connectivityManager  =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi !=null && wifi.isConnected()) ||(mobile.isConnected())){
            return true;
        }else{
            return false;
        }

    }
    protected  void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}