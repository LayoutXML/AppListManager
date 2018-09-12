package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.layoutxml.applistmanagerlibrary.interfaces.AppListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class AppTask extends AsyncTask<Void,Void,List<AppData>> {

    private static final String TAG = "AppTask";

    private final WeakReference<AppListener> allAppsListener;
    private final WeakReference<Context> contextWeakReference;
    private Integer flags;
    private Boolean match;
    private Integer uniqueIdentifier;

    public AppTask(WeakReference<Context> context, Integer flags, Boolean match, Integer uniqueIdentifier, WeakReference<AppListener> allListener) {
        this.contextWeakReference = context;
        this.allAppsListener = allListener;
        this.flags = flags;
        this.match = match;
        this.uniqueIdentifier = uniqueIdentifier;
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
                app.setName(applicationInfo.loadLabel(packageManager).toString());
                app.setPackageName(applicationInfo.packageName);
                app.setIcon(applicationInfo.loadIcon(packageManager));
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
        final AppListener listener = allAppsListener.get();
        if (listener!=null) {
            listener.appListener(appDataList,  flags, match, uniqueIdentifier);
        }
    }

}
