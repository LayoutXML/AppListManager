package com.layoutxml.applistmanagerlibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.layoutxml.applistmanagerlibrary.interfaces.AllAppsListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;
import com.layoutxml.applistmanagerlibrary.tasks.AllAppsTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class AppList {

    private static final String TAG = "AppList";
    private static AllAppsTask allAppsTask;

    public static void getAllApps(Context context, AllAppsListener allAppsListener){
        //Returns a list of all installed packages
        allAppsTask = new AllAppsTask(context.getPackageManager(),context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA), allAppsListener);
        allAppsTask.execute();
    }

    public static void stop(Context context) {
        //Stops all AsyncTasks to not create a memory leak
        //Supposed to call all AsyncTasks that are in this library
        if (!allAppsTask.isCancelled()){
            allAppsTask.cancel(true);
        }
    }
}
