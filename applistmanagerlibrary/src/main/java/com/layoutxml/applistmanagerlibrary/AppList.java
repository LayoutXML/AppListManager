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

    public static void getAllApps(Context context, AllAppsListener allAppsListener){
        //Returns a list of all installed packages

        AllAppsTask allAppsTask = new AllAppsTask(context.getPackageManager(),context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA), allAppsListener);

        allAppsTask.execute();

    }
}
