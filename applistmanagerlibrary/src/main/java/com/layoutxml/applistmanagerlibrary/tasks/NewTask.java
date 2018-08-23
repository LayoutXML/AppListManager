package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.layoutxml.applistmanagerlibrary.interfaces.NewListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 23/08/2018
 */
public class NewTask extends AsyncTask<Void,Void,List<AppData>>{

    private static final String TAG = "NewTask";

    private final WeakReference<NewListener> allNewAppsListener;
    private PackageManager packageManager;
    private List<ApplicationInfo> applicationInfoList;
    private List<AppData> receivedAppList;

    public NewTask(PackageManager packageManager, List<ApplicationInfo> applicationInfoList, List<AppData> receivedAppList, WeakReference<NewListener> newListener) {
        this.packageManager = packageManager;
        this.applicationInfoList = applicationInfoList;
        this.allNewAppsListener = newListener;
        this.receivedAppList = receivedAppList;
    }

    @Override
    protected final List<AppData> doInBackground(Void... voids){
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
        final NewListener listener = allNewAppsListener.get();
        if (listener!=null) {
            listener.newListener(appDataList);
        }
    }

}
