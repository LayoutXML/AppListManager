package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;

import com.layoutxml.applistmanagerlibrary.interfaces.NewActivityListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NewActivityTask extends AsyncTask<Void,Void,List<AppData>> {

    private final WeakReference<NewActivityListener> newActivitiesListener;
    private final WeakReference<Context> contextWeakReference;
    private List<AppData> receivedAppList;
    private Integer flags;
    private Boolean match;
    private Integer uniqueIdentifier;
    private Intent intent;
    private Integer activitiesFlags;


    public NewActivityTask(WeakReference<Context> context, List<AppData> receivedAppList, Intent intent, Integer activitiesFlags, Integer flags, Boolean match, Integer uniqueIdentifier, WeakReference<NewActivityListener> newActivitiesListener) {
        contextWeakReference = context;
        this.newActivitiesListener = newActivitiesListener;
        this.receivedAppList = receivedAppList;
        this.flags = flags;
        this.match = match;
        this.uniqueIdentifier = uniqueIdentifier;
        this.intent = intent;
        this.activitiesFlags = activitiesFlags;
    }

    @Override
    protected final List<AppData> doInBackground(Void... voids){
        Context context1 = contextWeakReference.get();
        if (context1!=null) {
            PackageManager packageManager = context1.getPackageManager();
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent,activitiesFlags);
            List<AppData> appDataList = new ArrayList<>();
            for (ResolveInfo resolveInfo : resolveInfoList) {
                AppData app = new AppData();
                app.setName(resolveInfo.loadLabel(packageManager).toString());
                app.setPackageName(resolveInfo.activityInfo.packageName);
                app.setIcon(resolveInfo.activityInfo.loadIcon(packageManager));
                app.setActivityName(resolveInfo.activityInfo.name);
                try {
                    ApplicationInfo appInfo = packageManager.getApplicationInfo(app.getPackageName(),0);
                    app.setFlags(appInfo.flags);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (receivedAppList!=null) {
                    if (match) {
                        if ((flags == null) || ((app.getFlags() & flags) != 0))
                            if (!receivedAppList.contains(app))
                                appDataList.add(app);
                    } else {
                        if ((flags == null) || ((app.getFlags() & flags) == 0))
                            if (!receivedAppList.contains(app))
                                appDataList.add(app);
                    }
                } else
                {
                    if (match) {
                        if ((flags == null) || ((app.getFlags() & flags) != 0))
                            appDataList.add(app);
                    } else {
                        if ((flags == null) || ((app.getFlags() & flags) == 0))
                            appDataList.add(app);
                    }
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
        final NewActivityListener listener = newActivitiesListener.get();
        if (listener!=null) {
            listener.newActivityListener(appDataList, intent, activitiesFlags, flags, match, false, uniqueIdentifier);
        }
    }

}
