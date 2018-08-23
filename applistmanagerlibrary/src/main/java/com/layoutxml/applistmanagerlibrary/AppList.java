package com.layoutxml.applistmanagerlibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.layoutxml.applistmanagerlibrary.interfaces.AllAppsListener;
import com.layoutxml.applistmanagerlibrary.interfaces.AllNewAppsListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;
import com.layoutxml.applistmanagerlibrary.tasks.AllAppsTask;
import com.layoutxml.applistmanagerlibrary.tasks.AllNewAppsTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class AppList {

    private static final String TAG = "AppList";
    private static AllAppsTask allAppsTask;
    private static AllNewAppsTask allNewAppsTask;

    public static void getAllApps(Context context, AllAppsListener allAppsListener){
        //Returns a list of all installed packages
        allAppsTask = new AllAppsTask(context.getPackageManager(),context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA), allAppsListener);
        allAppsTask.execute();
    }

    public static void getAllNewApps(Context context, AllNewAppsListener allNewAppsListener, List<AppData> appDataList) {
        //Returns a list of installed packages that are not in the given list
        allNewAppsTask = new AllNewAppsTask(context.getPackageManager(),context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA), allNewAppsListener);
        allNewAppsTask.execute(appDataList);
    }

    public static void stop() {
        //Stops all AsyncTasks to not create a memory leak
        //Supposed to call all AsyncTasks that are in this library
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
    }
}
