package xyz.dokup.autoapkupdater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import xyz.dokup.autoapkupdater.helper.ApkDownloadHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ApkDownloadHelper mApkDownloadHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_update).setOnClickListener(this);

        mApkDownloadHelper = new ApkDownloadHelper(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_update:
                mApkDownloadHelper.getNewApk("PxViewer_3_5_5.apk");
                break;
        }
    }
}
