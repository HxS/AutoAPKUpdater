package xyz.dokup.autoapkupdater.service;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by e10dokup on 2016/03/30
 **/
public interface ApkDownloadService {
    @GET("/HxS/UzukiSampleForAndroid/blob/release/1.0.0/konashi-uzuki-sample.apk?raw=true")
    Call<okhttp3.ResponseBody> getApk();

}