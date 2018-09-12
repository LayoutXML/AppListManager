package com.layoutxml.applistmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.layoutxml.applistmanagerlibrary.AppList;
import com.layoutxml.applistmanagerlibrary.interfaces.ActivitiesListener;
import com.layoutxml.applistmanagerlibrary.interfaces.AppListener;
import com.layoutxml.applistmanagerlibrary.interfaces.SortListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.util.List;

public class ListActivity extends AppCompatActivity implements ActivitiesListener, AppListener, SortListener {

    private static final String TAG = "ListActivity";

    private List<AppData> appDataList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        String data = intent.getStringExtra("appDataList");

        AppList.registerListeners(ListActivity.this,ListActivity.this,null,null,null,null,ListActivity.this);

        switch (data){
            default:
                progressBar.setVisibility(View.VISIBLE);
                AppList.getAllApps(getApplicationContext(),0);
                break;
            case "apps":
                progressBar.setVisibility(View.VISIBLE);
                AppList.getAllApps(getApplicationContext(),0);
                break;
            case "activities":
                progressBar.setVisibility(View.VISIBLE);
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                AppList.getAllActivities(getApplicationContext(),mainIntent,1);
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        AppList.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppList.registerListeners(ListActivity.this,ListActivity.this,null,null,null,null,ListActivity.this);
    }

    @Override
    public void activitiesListener(List<AppData> appDataList, Intent intent, Integer activitiesFlags, Integer applicationFlags, Boolean applicationFlagsMatch, Integer uniqueIdentifier) {
        AppList.sort(appDataList,AppList.BY_APPNAME,AppList.IN_ASCENDING,uniqueIdentifier);
    }

    @Override
    public void appListener(List<AppData> appDataList, Integer applicationFlags, Boolean applicationFlagsMatch, Integer uniqueIdentifier) {
        AppList.sort(appDataList,AppList.BY_APPNAME,AppList.IN_ASCENDING,uniqueIdentifier);
    }

    @Override
    public void sortListener(List<AppData> appDataList, Integer sortBy, Integer inOrder, Integer uniqueIdentifier) {
        this.appDataList = appDataList;
        progressBar.setVisibility(View.GONE);
    }
}
