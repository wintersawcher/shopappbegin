package com.example.shopappn.retrofit;

import com.example.shopappn.model.LoaiSpModel;
import com.example.shopappn.model.SanPhamMoiModel;


import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();

    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();

}
