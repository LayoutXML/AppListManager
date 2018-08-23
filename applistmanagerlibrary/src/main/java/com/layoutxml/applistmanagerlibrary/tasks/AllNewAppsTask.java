package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.layoutxml.applistmanagerlibrary.interfaces.AllNewAppsListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 23/08/2018
 */
public class AllNewAppsTask extends AsyncTask<List,Void,List<AppData>>{

    private static final String TAG = "AllNewAppsTask";

    private AllNewAppsListener allNewAppsListener;
    private PackageManager packageManager;
    private List<ApplicationInfo> applicationInfoList;

    public AllNewAppsTask(PackageManager packageManager, List<ApplicationInfo> applicationInfoList, AllNewAppsListener allNewAppsListener) {
        this.packageManager = packageManager;
        this.applicationInfoList = applicationInfoList;
        this.allNewAppsListener = allNewAppsListener;
    }

    @SafeVarargs
    @Override
    protected final List<AppData> doInBackground(List... appDataLists){
        List<AppData> receivedAppList = appDataLists[0];
        List<AppData> appDataList = new ArrayList<>();
        for (ApplicationInfo applicationInfo:applicationInfoList) {
            AppData app = new AppData();
            app.setAppName(applicationInfo.loadLabel(packageManager).toString());
            app.setAppPackageName(applicationInfo.packageName);
            app.setAppIcon(applicationInfo.loadIcon(packageManager));
            if (receivedAppList!=null) {
                if (!receivedAppList.contains(app))
                    appDataList.add(app);
            }
            if (isCancelled())
                break;
        }
        return appDataList;
    }

    @Override
    protected void onPostExecute(List<AppData> appDataList) {
        if (allNewAppsListener!=null) {
            allNewAppsListener.allNewAppsListener(appDataList);
        }
    }

}
