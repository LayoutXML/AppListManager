package com.layoutxml.applistmanagerlibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;

import com.layoutxml.applistmanagerlibrary.interfaces.AllListener;
import com.layoutxml.applistmanagerlibrary.interfaces.NewListener;
import com.layoutxml.applistmanagerlibrary.interfaces.SortListener;
import com.layoutxml.applistmanagerlibrary.interfaces.UninstalledListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;
import com.layoutxml.applistmanagerlibrary.tasks.AllTask;
import com.layoutxml.applistmanagerlibrary.tasks.NewTask;
import com.layoutxml.applistmanagerlibrary.tasks.SortTask;
import com.layoutxml.applistmanagerlibrary.tasks.UninstalledTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class AppList extends BroadcastReceiver{

    private static final String TAG = "AppList";
    private static AllTask allTask;
    private static NewTask newTask;
    private static UninstalledTask uninstalledTask;
    private static SortTask sortTask;
    private static WeakReference<AllListener> allListener;
    private static WeakReference<NewListener> newListener;
    private static WeakReference<UninstalledListener> uninstalledListener;
    private static WeakReference<SortListener> sortListener;
    public static IntentFilter intentFilter = new IntentFilter();
    public static final Integer BY_APPNAME = 0;
    public static final Integer BY_PACKAGENAME = 1;
    public static final Integer IN_ASCENDING = 0;
    public static final Integer IN_DESCENDING = 1;

    public static void start(AllListener allListener, NewListener newListener, UninstalledListener uninstalledListener, SortListener sortListener) {
        AppList.allListener = new WeakReference<>(allListener);
        AppList.newListener = new WeakReference<>(newListener);
        AppList.uninstalledListener = new WeakReference<>(uninstalledListener);
        AppList.sortListener = new WeakReference<>(sortListener);

        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
    }

    public static void getAll(Context context, Integer uniqueIdentifier){
        WeakReference<Context> context1 = new WeakReference<>(context);
        allTask = new AllTask(context1,null, true, uniqueIdentifier, allListener);
        allTask.execute();
    }

    public static void getAll(Context context,  Integer flags, Boolean match, Integer uniqueIdentifier){
        WeakReference<Context> context1 = new WeakReference<>(context);
        allTask = new AllTask(context1, flags, match, uniqueIdentifier, allListener);
        allTask.execute();
    }

    public static void getNew(Context context, List<AppData> appDataList, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        newTask = new NewTask(context1, appDataList, null, true, uniqueIdentifier, newListener);
        newTask.execute();
    }

    public static void getNew(Context context, List<AppData> appDataList, Integer flags, Boolean match, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        newTask = new NewTask(context1, appDataList, flags, match, uniqueIdentifier, newListener);
        newTask.execute();
    }

    public static void getUninstalled(Context context, List<AppData> appDataList, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        uninstalledTask = new UninstalledTask(context1, appDataList, uniqueIdentifier, uninstalledListener);
        uninstalledTask.execute();
    }

    public static Boolean checkFlags(AppData appData, Integer flags, Boolean match) {
        if (match)
            return ((flags==null) || ((appData.getFlags() & flags) != 0));
        else
            return ((flags==null) || ((appData.getFlags() & flags) == 0));
    }

    public static void sort(List<AppData> appDataList, Integer sortBy, Integer inOrder, Integer uniqueIdentifier) {
        sortTask = new SortTask(appDataList,sortBy,inOrder,uniqueIdentifier,sortListener);
        sortTask.execute();
    }

    public static void stop() {
        if (allTask !=null) {
            if (allTask.getStatus()!=AsyncTask.Status.FINISHED) {
                allTask.cancel(true);
            }
        }
        if (newTask !=null) {
            if (newTask.getStatus()!=AsyncTask.Status.FINISHED) {
                newTask.cancel(true);
            }
        }
        if (uninstalledTask !=null) {
            if (uninstalledTask.getStatus()!=AsyncTask.Status.FINISHED) {
                uninstalledTask.cancel(true);
            }
        }
        if (sortTask != null) {
            if (sortTask.getStatus()!=AsyncTask.Status.FINISHED) {
                sortTask.cancel(true);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action!=null && context!=null) {
            if (action.equals(Intent.ACTION_PACKAGE_ADDED) && newListener!=null) {
                if (newListener.get()!=null) {
                    Uri data = intent.getData();
                    List<AppData> newApp = new ArrayList<>();
                    AppData app = new AppData();
                    if (data != null) {
                        app.setAppPackageName(data.getEncodedSchemeSpecificPart());
                        try {
                            final PackageManager packageManager = context.getPackageManager();
                            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(app.getAppPackageName(), 0);
                            app.setAppIcon(applicationInfo.loadIcon(packageManager));
                            app.setAppName(applicationInfo.loadLabel(packageManager).toString());
                            newApp.add(app);
                            newListener.get().newListener(newApp, null, false, true, -1);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else if (action.equals(Intent.ACTION_PACKAGE_REMOVED) && uninstalledListener!=null) {
                if (uninstalledListener.get()!=null) {
                    Uri data = intent.getData();
                    List<AppData> newApp = new ArrayList<>();
                    AppData app = new AppData();
                    if (data != null) {
                        app.setAppPackageName(data.getEncodedSchemeSpecificPart());
                        newApp.add(app);
                        uninstalledListener.get().uninstalledListener(newApp, true, -1);
                    }
                }
            }
        }
    }
}
