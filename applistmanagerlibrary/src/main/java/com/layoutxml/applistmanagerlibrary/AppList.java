package com.layoutxml.applistmanagerlibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;

import com.layoutxml.applistmanagerlibrary.interfaces.ActivityListener;
import com.layoutxml.applistmanagerlibrary.interfaces.AppListener;
import com.layoutxml.applistmanagerlibrary.interfaces.NewActivityListener;
import com.layoutxml.applistmanagerlibrary.interfaces.NewAppListener;
import com.layoutxml.applistmanagerlibrary.interfaces.SortListener;
import com.layoutxml.applistmanagerlibrary.interfaces.UninstalledActivityListener;
import com.layoutxml.applistmanagerlibrary.interfaces.UninstalledAppListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;
import com.layoutxml.applistmanagerlibrary.tasks.ActivityTask;
import com.layoutxml.applistmanagerlibrary.tasks.AppTask;
import com.layoutxml.applistmanagerlibrary.tasks.NewActivityTask;
import com.layoutxml.applistmanagerlibrary.tasks.NewAppTask;
import com.layoutxml.applistmanagerlibrary.tasks.SortTask;
import com.layoutxml.applistmanagerlibrary.tasks.UninstalledActivityTask;
import com.layoutxml.applistmanagerlibrary.tasks.UninstalledAppTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class AppList extends BroadcastReceiver{

    public static final IntentFilter intentFilter = new IntentFilter();

    //Tasks
    private static AppTask appTask;
    private static ActivityTask activityTask;
    private static NewAppTask newAppTask;
    private static NewActivityTask newActivityTask;
    private static UninstalledAppTask uninstalledAppTask;
    private static UninstalledActivityTask uninstalledActivityTask;
    private static SortTask sortTask;

    //Listeners - weakreferences
    private static WeakReference<AppListener> appListener;
    private static WeakReference<ActivityListener> activitiesListener;
    private static WeakReference<NewAppListener> newAppListener;
    private static WeakReference<NewActivityListener> newActivitiesListener;
    private static WeakReference<UninstalledAppListener> uninstalledAppListener;
    private static WeakReference<UninstalledActivityListener> uninstalledActivitiesListener;
    private static WeakReference<SortListener> sortListener;

    //Values
    public static final Integer BY_APPNAME = 0;
    public static final Integer BY_PACKAGENAME = 1;
    public static final Integer BY_APPNAME_IGNORE_CASE = 2;
    public static final Integer IN_ASCENDING = 0;
    public static final Integer IN_DESCENDING = 1;

    public static void registerListeners(AppListener appListener, ActivityListener activityListener, NewAppListener newAppListener, NewActivityListener newActivityListener, UninstalledAppListener uninstalledAppListener, UninstalledActivityListener uninstalledActivityListener, SortListener sortListener) {
        AppList.appListener = new WeakReference<>(appListener);
        AppList.activitiesListener = new WeakReference<>(activityListener);
        AppList.newAppListener = new WeakReference<>(newAppListener);
        AppList.newActivitiesListener = new WeakReference<>(newActivityListener);
        AppList.uninstalledAppListener = new WeakReference<>(uninstalledAppListener);
        AppList.uninstalledActivitiesListener = new WeakReference<>(uninstalledActivityListener);
        AppList.sortListener = new WeakReference<>(sortListener);

        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
    }

    public static void getAllApps(Context context, Integer uniqueIdentifier){
        WeakReference<Context> context1 = new WeakReference<>(context);
        appTask = new AppTask(context1,null, true,null, false, uniqueIdentifier, appListener);
        appTask.execute();
    }

    public static void getSomeApps(Context context, Integer applicationFlags, Boolean applicationFlagsMatch, String[] permissions, Boolean matchPermissions, Integer uniqueIdentifier){
        WeakReference<Context> context1 = new WeakReference<>(context);
        appTask = new AppTask(context1, applicationFlags, applicationFlagsMatch, permissions, matchPermissions, uniqueIdentifier, appListener);
        appTask.execute();
    }

    public static void getAllActivities(Context context, Intent intent, Integer uniqueIdentifier){
        WeakReference<Context> context1 = new WeakReference<>(context);
        activityTask = new ActivityTask(context1, intent, 0, null, false,null, false, uniqueIdentifier, activitiesListener);
        activityTask.execute();
    }

    public static void getSomeActivities(Context context, Intent intent, Integer activityFlags, Integer applicationFlags, Boolean applicationFlagsMatch, String[] permissions, Boolean matchPermissions, Integer uniqueIdentifier){
        WeakReference<Context> context1 = new WeakReference<>(context);
        activityTask = new ActivityTask(context1, intent, activityFlags, applicationFlags, applicationFlagsMatch,permissions, matchPermissions, uniqueIdentifier, activitiesListener);
        activityTask.execute();
    }

    public static void getAllNewApps(Context context, List<AppData> appDataList, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        newAppTask = new NewAppTask(context1, appDataList, null, false, null, false, uniqueIdentifier, newAppListener);
        newAppTask.execute();
    }

    public static void getSomeNewApps(Context context, List<AppData> appDataList, Integer applicationFlags, Boolean applicationFlagsMatch, String[] permissions, Boolean matchPermissions, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        newAppTask = new NewAppTask(context1, appDataList, applicationFlags, applicationFlagsMatch, permissions, matchPermissions, uniqueIdentifier, newAppListener);
        newAppTask.execute();
    }

    public static void getAllNewActivities(Context context, List<AppData> appDataList, Intent intent, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        newActivityTask = new NewActivityTask(context1,appDataList,intent,0,null,false,null,false, uniqueIdentifier,newActivitiesListener);
        newActivityTask.execute();
    }

    public static void getSomeNewActivities(Context context, List<AppData> appDataList, Intent intent, Integer activityFlags, Integer applicationFlags, Boolean applicationFlagsMatch, String[] permissions, Boolean matchPermissions, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        newActivityTask = new NewActivityTask(context1,appDataList,intent,activityFlags,applicationFlags,applicationFlagsMatch,permissions,matchPermissions, uniqueIdentifier,newActivitiesListener);
        newActivityTask.execute();
    }

    public static void getAllUninstalledApps(Context context, List<AppData> appDataList, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        uninstalledAppTask = new UninstalledAppTask(context1, appDataList, null, false, null,false, uniqueIdentifier, uninstalledAppListener);
        uninstalledAppTask.execute();
    }

    public static void getSomeUninstalledApps(Context context, List<AppData> appDataList, Integer applicationFlags, Boolean applicationFlagsMatch, String[] permissions, Boolean matchPermissions, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        uninstalledAppTask = new UninstalledAppTask(context1, appDataList, applicationFlags, applicationFlagsMatch, permissions, matchPermissions, uniqueIdentifier, uninstalledAppListener);
        uninstalledAppTask.execute();
    }

    public static void getAllUninstalledActivities(Context context, List<AppData> appDataList, Intent intent, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        uninstalledActivityTask = new UninstalledActivityTask(context1, appDataList, intent, 0, null, false,null, false, uniqueIdentifier, uninstalledActivitiesListener);
        uninstalledActivityTask.execute();
    }

    public static void getSomeUninstalledActivities(Context context, List<AppData> appDataList, Intent intent, Integer activityFlags, Integer applicationFlags, Boolean applicationFlagsMatch, String[] permissions, Boolean matchPermissions, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        uninstalledActivityTask = new UninstalledActivityTask(context1, appDataList, intent, activityFlags, applicationFlags, applicationFlagsMatch,permissions,matchPermissions,  uniqueIdentifier, uninstalledActivitiesListener);
        uninstalledActivityTask.execute();
    }

    public static Boolean checkApplicationFlags(AppData appData, Integer applicationFlags, Boolean applicationFlagsMatch) {
        if (applicationFlagsMatch)
            return ((applicationFlags==null) || ((appData.getFlags() & applicationFlags) != 0));
        else
            return ((applicationFlags==null) || ((appData.getFlags() & applicationFlags) == 0));
    }

    public static Boolean checkApplicationPermissions(AppData appData, String[] permissions, Boolean matchPermissions) {
        Boolean containsPermission = false;
        for (String permission : permissions) {
            for (String permissionReal : appData.getPermissions()) {
                if (permission.equals(permissionReal)) {
                    containsPermission = true;
                    break;
                }
            }
            if (containsPermission)
                break;
        }
        if (matchPermissions)
            return containsPermission;
        else
            return !containsPermission;
    }

    public static void sort(List<AppData> appDataList, Integer sortBy, Integer inOrder, Integer uniqueIdentifier) {
        sortTask = new SortTask(appDataList,sortBy,inOrder,uniqueIdentifier,sortListener);
        sortTask.execute();
    }

    public static void destroy() {
        if (appTask !=null) {
            if (appTask.getStatus()!=AsyncTask.Status.FINISHED) {
                appTask.cancel(true);
            }
        }
        if (activityTask !=null) {
            if (activityTask.getStatus()!=AsyncTask.Status.FINISHED) {
                activityTask.cancel(true);
            }
        }
        if (newAppTask !=null) {
            if (newAppTask.getStatus()!=AsyncTask.Status.FINISHED) {
                newAppTask.cancel(true);
            }
        }
        if (newActivityTask !=null) {
            if (newActivityTask.getStatus()!=AsyncTask.Status.FINISHED) {
                newActivityTask.cancel(true);
            }
        }
        if (uninstalledAppTask !=null) {
            if (uninstalledAppTask.getStatus()!=AsyncTask.Status.FINISHED) {
                uninstalledAppTask.cancel(true);
            }
        }
        if (uninstalledActivityTask !=null) {
            if (uninstalledActivityTask.getStatus()!=AsyncTask.Status.FINISHED) {
                uninstalledActivityTask.cancel(true);
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
            if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                if (newAppListener!=null) {
                    if (newAppListener.get() != null) {
                        Uri data = intent.getData();
                        List<AppData> newApp = new ArrayList<>();
                        AppData app = new AppData();
                        if (data != null) {
                            app.setPackageName(data.getEncodedSchemeSpecificPart());
                            try {
                                final PackageManager packageManager = context.getPackageManager();
                                final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(app.getPackageName(), 0);
                                app.setIcon(applicationInfo.loadIcon(packageManager));
                                app.setName(applicationInfo.loadLabel(packageManager).toString());
                                app.setFlags(applicationInfo.flags);
                                PackageInfo packageInfo = packageManager.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
                                app.setPermissions(packageInfo.requestedPermissions);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            newApp.add(app);
                            newAppListener.get().newAppListener(newApp, null, false, true,null,false, -1);
                        }
                    }
                }
                if (newActivitiesListener!=null) {
                    if (newActivitiesListener.get() != null) {
                        Uri data = intent.getData();
                        List<AppData> newApps = new ArrayList<>();
                        if (data!=null) {
                            Intent intent1 = new Intent();
                            intent1.setPackage(data.getEncodedSchemeSpecificPart());
                            final PackageManager packageManager = context.getPackageManager();
                            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent1,0);
                            for (ResolveInfo resolveInfo : resolveInfoList) {
                                AppData app = new AppData();
                                app.setName(resolveInfo.loadLabel(packageManager).toString());
                                app.setPackageName(resolveInfo.activityInfo.packageName);
                                try {
                                    ApplicationInfo appInfo = packageManager.getApplicationInfo(app.getPackageName(),0);
                                    app.setFlags(appInfo.flags);
                                    PackageInfo packageInfo = packageManager.getPackageInfo(app.getPackageName(), PackageManager.GET_PERMISSIONS);
                                    app.setPermissions(packageInfo.requestedPermissions);
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }
                                app.setIcon(resolveInfo.activityInfo.loadIcon(packageManager));
                                app.setActivityName(resolveInfo.activityInfo.name);
                                newApps.add(app);
                            }
                            newActivitiesListener.get().newActivityListener(newApps,null,0,null,false,true,null,false,-1);
                        }
                    }
                }
            }
            else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                if (uninstalledAppListener !=null) {
                    if (uninstalledAppListener.get() != null) {
                        Uri data = intent.getData();
                        List<AppData> newApp = new ArrayList<>();
                        AppData app = new AppData();
                        if (data != null) {
                            app.setPackageName(data.getEncodedSchemeSpecificPart());
                            newApp.add(app);
                            uninstalledAppListener.get().uninstalledAppListener(newApp, true,null,false, null,false,-1);
                        }
                    }
                }
                if (uninstalledActivitiesListener != null) {
                    if (uninstalledActivitiesListener.get() != null) {
                        Uri data = intent.getData();
                        List<AppData> newApp = new ArrayList<>();
                        AppData app = new AppData();
                        if (data != null) {
                            app.setPackageName(data.getEncodedSchemeSpecificPart());
                            newApp.add(app);
                            uninstalledActivitiesListener.get().uninstalledActivityListener(newApp, null, 0, null, false, true,null,false, -1);
                        }
                    }
                }
            }
        }
    }
}
