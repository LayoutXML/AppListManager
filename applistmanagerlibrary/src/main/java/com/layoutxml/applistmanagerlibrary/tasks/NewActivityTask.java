package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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
    private final List<AppData> receivedAppList;
    private final Integer flags;
    private final Boolean match;
    private final Integer uniqueIdentifier;
    private final Intent intent;
    private final Integer activitiesFlags;
    private final String[] permissions;
    private final Boolean matchPermissions;


    public NewActivityTask(WeakReference<Context> context, List<AppData> receivedAppList, Intent intent, Integer activitiesFlags, Integer flags, Boolean match, String[] permissions, Boolean matchPermissions, Integer uniqueIdentifier, WeakReference<NewActivityListener> newActivitiesListener) {
        contextWeakReference = context;
        this.newActivitiesListener = newActivitiesListener;
        this.receivedAppList = receivedAppList;
        this.flags = flags;
        this.match = match;
        this.uniqueIdentifier = uniqueIdentifier;
        this.intent = intent;
        this.activitiesFlags = activitiesFlags;
        this.permissions = permissions;
        this.matchPermissions = matchPermissions;
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
                Boolean containsPermission = false;
                try {
                    ApplicationInfo appInfo = packageManager.getApplicationInfo(app.getPackageName(),0);
                    app.setFlags(appInfo.flags);
                    PackageInfo packageInfo = packageManager.getPackageInfo(app.getPackageName(), PackageManager.GET_PERMISSIONS);
                    String[] requestedPermissions = packageInfo.requestedPermissions;
                    if (permissions!=null) {
                        if (requestedPermissions != null) {
                            for (String requestedPermission : requestedPermissions) {
                                for (String permission : permissions) {
                                    if (requestedPermission.equals(permission)) {
                                        containsPermission = true;
                                        break;
                                    }
                                }
                                if (containsPermission)
                                    break;
                            }
                        }
                    } else {
                        containsPermission = matchPermissions;
                    }
                    app.setPermissions(requestedPermissions);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    if (permissions==null)
                        containsPermission = matchPermissions;
                }
                if ((containsPermission && matchPermissions) || (!containsPermission && !matchPermissions)) {
                    if (receivedAppList != null) {
                        if (match) {
                            if ((flags == null) || ((app.getFlags() & flags) != 0))
                                if (!receivedAppList.contains(app))
                                    appDataList.add(app);
                        } else {
                            if ((flags == null) || ((app.getFlags() & flags) == 0))
                                if (!receivedAppList.contains(app))
                                    appDataList.add(app);
                        }
                    } else {
                        if (match) {
                            if ((flags == null) || ((app.getFlags() & flags) != 0))
                                appDataList.add(app);
                        } else {
                            if ((flags == null) || ((app.getFlags() & flags) == 0))
                                appDataList.add(app);
                        }
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
            listener.newActivityListener(appDataList, intent, activitiesFlags, flags, match, false, permissions, matchPermissions, uniqueIdentifier);
        }
    }

}
