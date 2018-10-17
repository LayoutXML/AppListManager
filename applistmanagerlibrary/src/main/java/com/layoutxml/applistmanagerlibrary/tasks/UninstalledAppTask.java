package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.layoutxml.applistmanagerlibrary.interfaces.UninstalledAppListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 23/08/2018
 */
public class UninstalledAppTask extends AsyncTask<Void,Void,List<AppData>>{

    private final WeakReference<UninstalledAppListener> allUninstalledAppsListener;
    private final WeakReference<Context> contextWeakReference;
    private final List<AppData> receivedAppList;
    private final Integer uniqueIdentifier;
    private final Integer applicationFlags;
    private final Boolean applicationFlagsMatch;
    private final String[] permissions;
    private final Boolean matchPermissions;


    public UninstalledAppTask(WeakReference<Context> context, List<AppData> receivedAppList, Integer applicationFlags, Boolean applicationFlagsMatch, String[] permissions,Boolean matchPermissions,  Integer uniqueIdentifier,  WeakReference<UninstalledAppListener> uninstalledListener) {
        contextWeakReference = context;
        this.allUninstalledAppsListener = uninstalledListener;
        this.receivedAppList = receivedAppList;
        this.uniqueIdentifier = uniqueIdentifier;
        this.applicationFlags = applicationFlags;
        this.applicationFlagsMatch = applicationFlagsMatch;
        this.permissions = permissions;
        this.matchPermissions = matchPermissions;
    }

    @Override
    protected final List<AppData> doInBackground(Void... voids){
        Context context1 = contextWeakReference.get();
        if (context1!=null) {
            PackageManager packageManager = context1.getPackageManager();
            List<ApplicationInfo> applicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
            List<AppData> appDataList = new ArrayList<>();
            List<AppData> installedAppList = new ArrayList<>();
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
                        containsPermission = matchPermissions;
                    }
                    app.setPermissions(requestedPermissions);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    if (permissions==null)
                        containsPermission = matchPermissions;
                }
                if ((containsPermission && matchPermissions) || (!containsPermission && !matchPermissions)) {
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
        final UninstalledAppListener listener = allUninstalledAppsListener.get();
        if (listener!=null) {
            listener.uninstalledAppListener(appDataList, false, applicationFlags, applicationFlagsMatch, uniqueIdentifier);
        }
    }

}
