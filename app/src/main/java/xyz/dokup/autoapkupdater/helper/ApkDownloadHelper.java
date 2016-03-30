package xyz.dokup.autoapkupdater.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import xyz.dokup.autoapkupdater.R;
import xyz.dokup.autoapkupdater.service.ApkDownloadService;

/**
 * Created by e10dokup on 2016/03/30
 **/
public class ApkDownloadHelper {
    private static final String TAG = ApkDownloadHelper.class.getSimpleName();
    private final ApkDownloadHelper self = this;

    private Context mContext;

    public ApkDownloadHelper(Context context) {
        mContext = context;
    }

    public void getNewApk(String apkName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mContext.getString(R.string.url_apk))
                .build();

        ApkDownloadService service = retrofit.create(ApkDownloadService.class);

        Call<ResponseBody> call = service.getApk();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    saveApk(response);
                } else {
                    Log.d(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void saveApk(final Response<ResponseBody> response) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String fileName = "app.apk";
                    byte[] responseBytes = response.body().bytes();
                    response.body().close();
                    //File file = new File(mContext.getExternalFilesDir(null) + "/" + fileName);
                    File file = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    bufferedOutputStream.write(responseBytes);
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();

                    install(file);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void install(final File file) {
        ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, file.getPath(), Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}