package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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
    private final List<AppData> receivedAppList;
    private final Integer uniqueIdentifier;
    private final Intent intent;
    private final Integer activityFlags;
    private final Integer applicationFlags;
    private final Boolean applicationFlagsMatch;
    private final String[] permissions;

    public UninstalledActivityTask(WeakReference<Context> context, List<AppData> receivedAppList, Intent intent, Integer activityFlags, Integer applicationFlags, Boolean applicationFlagsMatch, String[] permissions, Integer uniqueIdentifier, WeakReference<UninstalledActivityListener> uninstalledListener) {
        contextWeakReference = context;
        this.uninstalledActivitiesTaskWeakReference = uninstalledListener;
        this.receivedAppList = receivedAppList;
        this.uniqueIdentifier = uniqueIdentifier;
        this.intent = intent;
        this.activityFlags = activityFlags;
        this.applicationFlags = applicationFlags;
        this.applicationFlagsMatch = applicationFlagsMatch;
        this.permissions = permissions;
    }

    @Override
    protected final List<AppData> doInBackground(Void... voids){
        Context context1 = contextWeakReference.get();
        if (context1!=null) {
            PackageManager packageManager = context1.getPackageManager();
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent,activityFlags);
            List<AppData> appDataList = new ArrayList<>();
            List<AppData> installedAppList = new ArrayList<>();
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
                        containsPermission = true;
                    }
                    app.setPermissions(requestedPermissions);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    if (permissions==null)
                        containsPermission=true;
                }
                if (containsPermission) {
                    if (applicationFlagsMatch) {
                        if ((applicationFlags == null) || ((app.getFlags() & applicationFlags) != 0)) {
                            installedAppList.add(app);
                        }
                    } else {
                        if ((applicationFlags == null) || ((app.getFlags() & applicationFlags) == 0)) {
                            installedAppList.add(app);
                        }
                    }
                }
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
            listener.uninstalledActivityListener(appDataList, intent, activityFlags, applicationFlags, applicationFlagsMatch, false, uniqueIdentifier);
        }
    }

}
