package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.layoutxml.applistmanagerlibrary.interfaces.AllListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AllActivitiesTask extends AsyncTask<Void,Void,List<AppData>> {

    private static final String TAG="AllActivitiesTask";

    private final WeakReference<AllListener> allAppsListener;
    private final WeakReference<Context> contextWeakReference;
    private Intent intent;
    private Integer uniqueIdentifier;

    public AllActivitiesTask(WeakReference<Context> context, Intent intent, Integer uniqueIdentifier, WeakReference<AllListener> allListener) {
        this.contextWeakReference = context;
        this.intent = intent;
        this.uniqueIdentifier = uniqueIdentifier;
        this.allAppsListener = allListener;
    }

    @Override
    protected List<AppData> doInBackground(Void... voids){
        Context context1 = contextWeakReference.get();
        if (context1!=null) {
            PackageManager packageManager = context1.getPackageManager();
            List<ApplicationInfo> applicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
            List<AppData> appDataList = new ArrayList<>();
            for (ApplicationInfo applicationInfo : applicationInfoList) {
                AppData app = new AppData();
                app.setAppName(applicationInfo.loadLabel(packageManager).toString());
                app.setAppPackageName(applicationInfo.packageName);
                app.setAppIcon(applicationInfo.loadIcon(packageManager));
                app.setFlags(applicationInfo.flags);
                if (match) {
                    if ((flags == null) || ((applicationInfo.flags & flags) != 0))
                        appDataList.add(app);
                } else {
                    if ((flags == null) || ((applicationInfo.flags & flags) == 0))
                        appDataList.add(app);
                }
                if (isCancelled())
                    break;
            }
            return appDataList;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<AppData> appDataList) {
        final AllListener listener = allAppsListener.get();
        if (listener!=null) {
            listener.allListener(appDataList, flags, match, uniqueIdentifier);
        }
    }
}
