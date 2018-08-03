package com.zh.zrz.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.zh.zrz.tinyviewmodelutil.TinyViewModelUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    File dexPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "app-debug.apk");// 外部路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        }
        ((LinearLayout) findViewById(R.id.container)).addView(TinyViewModelUtil.load(dexPath.getAbsolutePath(), "com.example.cdzhangruize3.appdextest2.Test1", MainActivity.this));
    }
}
