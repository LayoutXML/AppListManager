package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;

import com.layoutxml.applistmanagerlibrary.interfaces.UninstalledActivityListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class UninstalledActivityTask extends AsyncTask<Void,Void,List<AppData>> {

    private final WeakReference<UninstalledActivityListener> uninstalledActivitiesTaskWeakReference;
    private final WeakReference<Context> contextWeakReference;
    private List<AppData> receivedAppList;
    private Integer uniqueIdentifier;
    private Intent intent;


    public UninstalledActivityTask(WeakReference<Context> context, List<AppData> receivedAppList, Intent intent, Integer uniqueIdentifier, WeakReference<UninstalledActivityListener> uninstalledListener) {
        contextWeakReference = context;
        this.uninstalledActivitiesTaskWeakReference = uninstalledListener;
        this.receivedAppList = receivedAppList;
        this.uniqueIdentifier = uniqueIdentifier;
        this.intent = intent;
    }

    @Override
    protected final List<AppData> doInBackground(Void... voids){
        Context context1 = contextWeakReference.get();
        if (context1!=null) {
            PackageManager packageManager = context1.getPackageManager();
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent,0);
            List<AppData> appDataList = new ArrayList<>();
            List<AppData> installedAppList = new ArrayList<>();
            for (ResolveInfo resolveInfo : resolveInfoList) {
                AppData app = new AppData();
                app.setName(resolveInfo.loadLabel(packageManager).toString());
                app.setPackageName(resolveInfo.activityInfo.packageName);
                app.setIcon(resolveInfo.activityInfo.loadIcon(packageManager));
                app.setFlags(resolveInfo.activityInfo.flags);
                installedAppList.add(app);
                if (isCancelled())
                    break;
            }
            if (receivedAppList != null) {
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
        return null;
    }

    @Override
    protected void onPostExecute(List<AppData> appDataList){
        final UninstalledActivityListener listener = uninstalledActivitiesTaskWeakReference.get();
        if (listener!=null) {
            listener.uninstalledActivityListener(appDataList, intent, false, uniqueIdentifier);
        }
    }

}
