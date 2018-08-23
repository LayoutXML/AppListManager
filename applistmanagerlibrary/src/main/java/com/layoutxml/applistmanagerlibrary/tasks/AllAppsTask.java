package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import com.layoutxml.applistmanagerlibrary.interfaces.AllAppsListener;
import com.layoutxml.applistmanagerlibrary.interfaces.AllNewAppsListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class AllAppsTask extends AsyncTask<Void,Void,List<AppData>> {

    private static final String TAG = "AllAppsTask";

    private final WeakReference<AllAppsListener> allAppsListener;
    private PackageManager packageManager;
    private List<ApplicationInfo> applicationInfoList;

    public AllAppsTask(PackageManager packageManager, List<ApplicationInfo> applicationInfoList, AllAppsListener allAppsListener) {
        this.packageManager = packageManager;
        this.applicationInfoList = applicationInfoList;
        this.allAppsListener = new WeakReference<>(allAppsListener);
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
        final AllAppsListener listener = allAppsListener.get();
        if (listener!=null) {
            listener.allAppsListener(appDataList);
        }
    }

}
