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
import com.layoutxml.applistmanagerlibrary.interfaces.UninstalledListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;
import com.layoutxml.applistmanagerlibrary.tasks.AllTask;
import com.layoutxml.applistmanagerlibrary.tasks.NewTask;
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
    private static WeakReference<AllListener> allListener;
    private static WeakReference<NewListener> newListener;
    private static WeakReference<UninstalledListener> uninstalledListener;
    public static IntentFilter intentFilter = new IntentFilter();
    public static final Integer BY_APPNAME = 0;
    public static final Integer BY_PACKAGENAME = 1;
    public static final Integer IN_ASCENDING = 0;
    public static final Integer IN_DESCENDING = 1;

    public static void start(AllListener allListener, NewListener newListener, UninstalledListener uninstalledListener) {
        AppList.allListener = new WeakReference<>(allListener);
        AppList.newListener = new WeakReference<>(newListener);
        AppList.uninstalledListener = new WeakReference<>(uninstalledListener);

        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
    }

    public static void getAll(Context context){
        allTask = new AllTask(context.getPackageManager(), context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA),null, true, allListener);
        allTask.execute();
    }

    public static void getAll(Context context, Integer flags, Boolean match){
        allTask = new AllTask(context.getPackageManager(), context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA), flags, match, allListener);
        allTask.execute();
    }

    public static void getNew(Context context, List<AppData> appDataList) {
        newTask = new NewTask(context.getPackageManager(),context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA), appDataList, null, true, newListener);
        newTask.execute();
    }

    public static void getNew(Context context, List<AppData> appDataList, Integer flags, Boolean match) {
        newTask = new NewTask(context.getPackageManager(),context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA), appDataList, flags, match, newListener);
        newTask.execute();
    }

    public static void getUninstalled(Context context, List<AppData> appDataList) {
        uninstalledTask = new UninstalledTask(context.getPackageManager(),context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA), appDataList, uninstalledListener);
        uninstalledTask.execute();
    }

    public static Boolean checkFlags(AppData appData, Integer flags, Boolean match) {
        if (match)
            return ((flags==null) || ((appData.getFlags() & flags) != 0));
        else
            return ((flags==null) || ((appData.getFlags() & flags) == 0));
    }

    public static List<AppData> sort(List<AppData> appDataList, Integer sortBy, Integer inOrder) {
        //TODO: AsynctAsk
        //TODO: Check if null
        if (sortBy.equals(BY_APPNAME) && inOrder.equals(IN_ASCENDING)) {
            Collections.sort(appDataList, new Comparator<AppData>() {
                @Override
                public int compare(AppData t0, AppData t1) {
                    return t0.getAppName().compareTo(t1.getAppName());
                }
            });
        } else if (sortBy.equals(BY_APPNAME) && inOrder.equals(IN_DESCENDING)) {
            Collections.sort(appDataList, new Comparator<AppData>() {
                @Override
                public int compare(AppData t0, AppData t1) {
                    return t1.getAppName().compareTo(t0.getAppName());
                }
            });
        } else if (sortBy.equals(BY_PACKAGENAME) && inOrder.equals(IN_ASCENDING)) {
            Collections.sort(appDataList, new Comparator<AppData>() {
                @Override
                public int compare(AppData t0, AppData t1) {
                    return t0.getAppPackageName().compareTo(t1.getAppPackageName());
                }
            });
        } else if (sortBy.equals(BY_PACKAGENAME) && inOrder.equals(IN_DESCENDING)) {
            Collections.sort(appDataList, new Comparator<AppData>() {
                @Override
                public int compare(AppData t0, AppData t1) {
                    return t1.getAppPackageName().compareTo(t0.getAppPackageName());
                }
            });
        }
        return appDataList;
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
                            newListener.get().newListener(newApp, null, true);
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
                        uninstalledListener.get().uninstalledListener(newApp, true);
                    }
                }
            }
        }
    }
}
