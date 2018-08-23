package com.layoutxml.applistmanagerlibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;

import com.layoutxml.applistmanagerlibrary.interfaces.AllAppsListener;
import com.layoutxml.applistmanagerlibrary.interfaces.AllNewAppsListener;
import com.layoutxml.applistmanagerlibrary.interfaces.AllUninstalledAppsListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;
import com.layoutxml.applistmanagerlibrary.tasks.AllAppsTask;
import com.layoutxml.applistmanagerlibrary.tasks.AllNewAppsTask;
import com.layoutxml.applistmanagerlibrary.tasks.AllUninstalledAppsTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class AppList extends BroadcastReceiver{

    private static final String TAG = "AppList";
    private static AllAppsTask allAppsTask;
    private static AllNewAppsTask allNewAppsTask;
    private static AllUninstalledAppsTask allUninstalledAppsTask;
    private static AllNewAppsListener allNewAppsListenerCopy;
    private static AllUninstalledAppsListener allUninstalledAppsListenerCopy;

    public static void getAllApps(Context context, AllAppsListener allAppsListener){
        //Returns a list of all installed packages
        allAppsTask = new AllAppsTask(context.getPackageManager(),context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA), allAppsListener);
        allAppsTask.execute();
    }

    public static void getAllNewApps(Context context, AllNewAppsListener allNewAppsListener, List<AppData> appDataList) {
        //Returns a list of installed packages that are not in the given list
        allNewAppsListenerCopy = allNewAppsListener;
        allNewAppsTask = new AllNewAppsTask(context.getPackageManager(),context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA), allNewAppsListener);
        allNewAppsTask.execute(appDataList);
    }

    public static void getAllUninstalledApps(Context context, AllUninstalledAppsListener allUninstalledAppsListener, List<AppData> appDataList) {
        //Returns a list of all uninstalled packages that are in a given list
        allUninstalledAppsListenerCopy = allUninstalledAppsListener;
        allUninstalledAppsTask = new AllUninstalledAppsTask(context.getPackageManager(),context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA),allUninstalledAppsListener, appDataList);
        allUninstalledAppsTask.execute();
    }

    public static void stop() {
        //Stops all AsyncTasks to not create a memory leak
        //Calls all AsyncTasks that are in this library
        if (allAppsTask!=null) {
            if (allAppsTask.getStatus()!=AsyncTask.Status.FINISHED) {
                allAppsTask.cancel(true);
            }
        }
        if (allNewAppsTask!=null) {
            if (allNewAppsTask.getStatus()!=AsyncTask.Status.FINISHED) {
                allNewAppsTask.cancel(true);
            }
        }
        if (allUninstalledAppsTask!=null) {
            if (allUninstalledAppsTask.getStatus()!=AsyncTask.Status.FINISHED) {
                allUninstalledAppsTask.cancel(true);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Returns a list with information about one app that has just been installed/uninstalled on Android Versions <=7.1.1
        //Can only return data about new installed apps if getAllNewApps was called at least once
        //Can only return data about uninstalled apps if getAllUninstalledApps was called at least once
        //
        final String action = intent.getAction();
        if (action!=null && context!=null) {
            if (action.equals(Intent.ACTION_PACKAGE_ADDED) && allNewAppsListenerCopy!=null) {
                Uri data = intent.getData();
                List<AppData> newApp = new ArrayList<>();
                AppData app = new AppData();
                if (data!=null) {
                    app.setAppPackageName(data.getEncodedSchemeSpecificPart());
                    try {
                        final PackageManager packageManager = context.getPackageManager();
                        final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(app.getAppPackageName(), 0);
                        app.setAppIcon(applicationInfo.loadIcon(packageManager));
                        app.setAppName(applicationInfo.loadLabel(packageManager).toString());
                        newApp.add(app);
                        allNewAppsListenerCopy.allNewAppsListener(newApp);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (action.equals(Intent.ACTION_PACKAGE_REMOVED) && allUninstalledAppsListenerCopy!=null) {
                Uri data = intent.getData();
                List<AppData> newApp = new ArrayList<>();
                AppData app = new AppData();
                if (data!=null) {
                    app.setAppPackageName(data.getEncodedSchemeSpecificPart());
                    app.setAppIcon(new ColorDrawable(Color.TRANSPARENT));
                    app.setAppPackageName("");
                    newApp.add(app);
                    allUninstalledAppsListenerCopy.allUninstalledAppsListener(newApp);
                }
            }
        }
    }
}
