package com.layoutxml.applistmanager;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.layoutxml.applistmanagerlibrary.interfaces.AllAppsListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;
import com.layoutxml.applistmanagerlibrary.tasks.AllAppsTask;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AllAppsTask allAppsTask = new AllAppsTask(getApplicationContext().getPackageManager(),getApplicationContext().getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA), new AllAppsListener() {

            @Override
            public void allAppsListener(List<AppData> appDataList) {
                Log.d(TAG,"ApplicationList: "+appDataList.size());
            }
        });

        allAppsTask.execute();

    }
}
