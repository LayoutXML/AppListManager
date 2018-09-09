package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;

import com.layoutxml.applistmanagerlibrary.interfaces.NewActivitiesListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NewActivitiesTask extends AsyncTask<Void,Void,List<AppData>> {

    private static final String TAG = "NewAppTask";

    private final WeakReference<NewActivitiesListener> newActivitiesListener;
    private final WeakReference<Context> contextWeakReference;
    private List<AppData> receivedAppList;
    private Integer flags;
    private Boolean match;
    private Integer uniqueIdentifier;
    private Intent intent;


    public NewActivitiesTask(WeakReference<Context> context, List<AppData> receivedAppList, Intent intent, Integer flags, Boolean match, Integer uniqueIdentifier, WeakReference<NewActivitiesListener> newActivitiesListener) {
        contextWeakReference = context;
        this.newActivitiesListener = newActivitiesListener;
        this.receivedAppList = receivedAppList;
        this.flags = flags;
        this.match = match;
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
            for (ResolveInfo resolveInfo : resolveInfoList) {
                AppData app = new AppData();
                app.setName(resolveInfo.loadLabel(packageManager).toString());
                app.setPackageName(resolveInfo.activityInfo.packageName);
                app.setIcon(resolveInfo.activityInfo.loadIcon(packageManager));
                app.setFlags(resolveInfo.activityInfo.flags);
                if (match) {
                    if ((flags == null) || ((resolveInfo.activityInfo.flags & flags) != 0))
                        if (!receivedAppList.contains(app))
                            appDataList.add(app);
                } else {
                    if ((flags == null) || ((resolveInfo.activityInfo.flags & flags) == 0))
                        if (!receivedAppList.contains(app))
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
        final NewActivitiesListener listener = newActivitiesListener.get();
        if (listener!=null) {
            listener.newActivitiesListener(appDataList, intent, flags, match, false, uniqueIdentifier);
        }
    }

}
