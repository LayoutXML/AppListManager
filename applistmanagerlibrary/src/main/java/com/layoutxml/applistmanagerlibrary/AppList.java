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

    public static void start(AllListener allListener, NewListener newListener, UninstalledListener uninstalledListener) {
        AppList.allListener = new WeakReference<>(allListener);
        AppList.newListener = new WeakReference<>(newListener);
        AppList.uninstalledListener = new WeakReference<>(uninstalledListener);

        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        }

    public static void getAll(Context context){
        //Returns a list of all installed packages
        allTask = new AllTask(context.getPackageManager(),context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA),allListener);
        allTask.execute();
    }

    public static void getNew(Context context, List<AppData> appDataList) {
        //Returns a list of installed packages that are not in the given list
        newTask = new NewTask(context.getPackageManager(),context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA), appDataList, newListener);
        newTask.execute();
    }

    public static void getUninstalled(Context context, List<AppData> appDataList) {
        //Returns a list of all uninstalled packages that are in a given list
        uninstalledTask = new UninstalledTask(context.getPackageManager(),context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA), appDataList, uninstalledListener);
        uninstalledTask.execute();
    }

    public static void stop() {
        //Stops all AsyncTasks to not create a memory leak
        //Calls all AsyncTasks that are in this library
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
        //Returns a list with information about one app that has just been installed/uninstalled on Android Versions <=7.1.1
        //Can only return data about new installed apps if getNew was called at least once
        //Can only return data about uninstalled apps if getUninstalled was called at least once
        //Returns empty (not null) app name and transparent icon if uninstalled
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
                            newListener.get().newListener(newApp);
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
                        app.setAppIcon(new ColorDrawable(Color.TRANSPARENT));
                        app.setAppName("");
                        newApp.add(app);
                        uninstalledListener.get().uninstalledListener(newApp);
                    }
                }
            }
        }
    }
}
