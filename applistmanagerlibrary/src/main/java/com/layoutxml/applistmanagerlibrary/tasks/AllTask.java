package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import com.layoutxml.applistmanagerlibrary.interfaces.AllListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class AllTask extends AsyncTask<Void,Void,List<AppData>> {

    private static final String TAG = "AllTask";

    private final WeakReference<AllListener> allAppsListener;
    private PackageManager packageManager;
    private List<ApplicationInfo> applicationInfoList;

    public AllTask(PackageManager packageManager, List<ApplicationInfo> applicationInfoList, AllListener allListener) {
        this.packageManager = packageManager;
        this.applicationInfoList = applicationInfoList;
        this.allAppsListener = new WeakReference<>(allListener);
    }

    @Override
    protected List<AppData> doInBackground(Void... voids){
        List<AppData> appDataList = new ArrayList<>();
        for (ApplicationInfo applicationInfo:applicationInfoList) {
            AppData app = new AppData();
            app.setAppName(applicationInfo.loadLabel(packageManager).toString());
            app.setAppPackageName(applicationInfo.packageName);
            app.setAppIcon(applicationInfo.loadIcon(packageManager));
            appDataList.add(app);
            if (isCancelled())
                break;
        }
        return appDataList;
    }

    @Override
    protected void onPostExecute(List<AppData> appDataList) {
        final AllListener listener = allAppsListener.get();
        if (listener!=null) {
            listener.allListener(appDataList);
        }
    }

}
