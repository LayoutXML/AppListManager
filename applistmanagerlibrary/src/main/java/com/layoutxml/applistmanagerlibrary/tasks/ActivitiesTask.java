package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.layoutxml.applistmanagerlibrary.interfaces.ActivitiesListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ActivitiesTask extends AsyncTask<Void,Void,List<AppData>> {

    private static final String TAG="ActivitiesTask";

    private final WeakReference<ActivitiesListener> activitiesListenerWeakReference;
    private final WeakReference<Context> contextWeakReference;
    private Intent intent;
    private Integer uniqueIdentifier;
    private Integer flags;

    public ActivitiesTask(WeakReference<Context> context, Intent intent, Integer flags, Integer uniqueIdentifier, WeakReference<ActivitiesListener> activitiesListenerWeakReference) {
        this.contextWeakReference = context;
        this.intent = intent;
        this.uniqueIdentifier = uniqueIdentifier;
        this.activitiesListenerWeakReference = activitiesListenerWeakReference;
        this.flags = flags;
    }

    @Override
    protected List<AppData> doInBackground(Void... voids){
        Context context1 = contextWeakReference.get();
        if (context1!=null) {
            PackageManager packageManager = context1.getPackageManager();
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent,flags);
            List<AppData> appDataList = new ArrayList<>();
            for (ResolveInfo resolveInfo : resolveInfoList) {
                AppData app = new AppData();
                app.setName(resolveInfo.loadLabel(packageManager).toString());
                app.setPackageName(resolveInfo.activityInfo.packageName);
                app.setIcon(resolveInfo.activityInfo.loadIcon(packageManager));
                app.setFlags(resolveInfo.activityInfo.flags);
                if (isCancelled())
                    break;
            }
            return appDataList;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<AppData> appDataList) {
        final ActivitiesListener listener = activitiesListenerWeakReference.get();
        if (listener!=null) {
            listener.activitiesListener(appDataList, intent, flags, uniqueIdentifier);
        }
    }
}
