package com.layoutxml.applistmanagerlibrary.tasks;

import android.content.Context;
import android.content.pm.ApplicationInfo;
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
    private List<AppData> receivedAppList;
    private Integer uniqueIdentifier;
    private Integer applicationFlags;
    private Boolean applicationFlagsMatch;


    public UninstalledAppTask(WeakReference<Context> context, List<AppData> receivedAppList, Integer applicationFlags, Boolean applicationFlagsMatch, Integer uniqueIdentifier,  WeakReference<UninstalledAppListener> uninstalledListener) {
        contextWeakReference = context;
        this.allUninstalledAppsListener = uninstalledListener;
        this.receivedAppList = receivedAppList;
        this.uniqueIdentifier = uniqueIdentifier;
        this.applicationFlags = applicationFlags;
        this.applicationFlagsMatch = applicationFlagsMatch;
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
                if (applicationFlagsMatch) {
                    if ((applicationFlags == null) || ((app.getFlags() & applicationFlags) != 0)) {
                        installedAppList.add(app);
                    }
                } else {
                    if ((applicationFlags == null) || ((app.getFlags() & applicationFlags) == 0)) {
                        installedAppList.add(app);
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
