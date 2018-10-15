package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.layoutxml.applistmanagerlibrary.interfaces.NewAppListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 23/08/2018
 */
public class NewAppTask extends AsyncTask<Void,Void,List<AppData>>{

    private final WeakReference<NewAppListener> allNewAppsListener;
    private final WeakReference<Context> contextWeakReference;
    private final List<AppData> receivedAppList;
    private final Integer flags;
    private final Boolean match;
    private final Integer uniqueIdentifier;
    private final String[] permissions;


    public NewAppTask(WeakReference<Context> context, List<AppData> receivedAppList, Integer flags, Boolean match, String[] permissions, Integer uniqueIdentifier, WeakReference<NewAppListener> newListener) {
        contextWeakReference = context;
        this.allNewAppsListener = newListener;
        this.receivedAppList = receivedAppList;
        this.flags = flags;
        this.match = match;
        this.uniqueIdentifier = uniqueIdentifier;
        this.permissions = permissions;
    }

    @Override
    protected final List<AppData> doInBackground(Void... voids){
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
                Boolean containsPermission = false;
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
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
                    if (receivedAppList != null) {
                        if (match) {
                            if ((flags == null) || ((applicationInfo.flags & flags) != 0)) {
                                if (!receivedAppList.contains(app))
                                    appDataList.add(app);
                            }
                        } else {
                            if ((flags == null) || ((applicationInfo.flags & flags) == 0)) {
                                if (!receivedAppList.contains(app))
                                    appDataList.add(app);
                            }
                        }
                    } else {
                        if (match) {
                            if ((flags == null) || ((applicationInfo.flags & flags) != 0)) {
                                appDataList.add(app);
                            }
                        } else {
                            if ((flags == null) || ((applicationInfo.flags & flags) == 0)) {
                                appDataList.add(app);
                            }
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
        final NewAppListener listener = allNewAppsListener.get();
        if (listener!=null) {
            listener.newAppListener(appDataList, flags, match, false, uniqueIdentifier);
        }
    }

}
