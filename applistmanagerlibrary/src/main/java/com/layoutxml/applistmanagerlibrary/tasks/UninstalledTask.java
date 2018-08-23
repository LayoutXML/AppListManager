package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.layoutxml.applistmanagerlibrary.interfaces.UninstalledListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 23/08/2018
 */
public class UninstalledTask extends AsyncTask<Void,Void,List<AppData>>{

    private static final String TAG = "UninstalledTask";

    private final WeakReference<UninstalledListener> allUninstalledAppsListener;
    private PackageManager packageManager;
    private List<ApplicationInfo> applicationInfoList;
    private List<AppData> receivedAppList;

    public UninstalledTask(PackageManager packageManager, List<ApplicationInfo> applicationInfoList, List<AppData> receivedAppList, WeakReference<UninstalledListener> uninstalledListener) {
        this.packageManager = packageManager;
        this.applicationInfoList = applicationInfoList;
        this.allUninstalledAppsListener = uninstalledListener;
        this.receivedAppList = receivedAppList;
    }

    @Override
    protected final List<AppData> doInBackground(Void... voids){
        List<AppData> appDataList = new ArrayList<>();
        List<AppData> installedAppList = new ArrayList<>();
        for (ApplicationInfo applicationInfo:applicationInfoList) {
            AppData app = new AppData();
            app.setAppName(applicationInfo.loadLabel(packageManager).toString());
            app.setAppPackageName(applicationInfo.packageName);
            app.setAppIcon(applicationInfo.loadIcon(packageManager));
            installedAppList.add(app);
            if (isCancelled())
                break;
        }
        if (receivedAppList!=null) {
            for (AppData app : receivedAppList) {
                if (!installedAppList.contains(app)) {
                    appDataList.add(app);
                }
                if (isCancelled())
                    break;
            }
        }
        return appDataList;
    }

    @Override
    protected void onPostExecute(List<AppData> appDataList){
        final UninstalledListener listener = allUninstalledAppsListener.get();
        if (listener!=null) {
            listener.uninstalledListener(appDataList);
        }
    }

}
