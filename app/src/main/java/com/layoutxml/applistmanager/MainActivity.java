package com.layoutxml.applistmanager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.layoutxml.applistmanagerlibrary.AppData;
import com.layoutxml.applistmanagerlibrary.AppList;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    List<AppData> appList0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppList.getAll callback = new AppList.getAll() {
            @Override
            public void getAllApps(List<AppData> appList, Context context) {
                appList0 = appList;
            }
        };
        AppList appList = new AppList();
        appList.getAllApps(callback, this);

        Log.d(TAG, appList0.get(0).getAppName());

    }
}
