package com.layoutxml.applistmanagerlibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;

import com.layoutxml.applistmanagerlibrary.interfaces.ActivitiesListener;
import com.layoutxml.applistmanagerlibrary.interfaces.AppListener;
import com.layoutxml.applistmanagerlibrary.interfaces.NewActivitiesListener;
import com.layoutxml.applistmanagerlibrary.interfaces.NewAppListener;
import com.layoutxml.applistmanagerlibrary.interfaces.SortListener;
import com.layoutxml.applistmanagerlibrary.interfaces.UninstalledActivitiesListener;
import com.layoutxml.applistmanagerlibrary.interfaces.UninstalledAppListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;
import com.layoutxml.applistmanagerlibrary.tasks.ActivitiesTask;
import com.layoutxml.applistmanagerlibrary.tasks.AppTask;
import com.layoutxml.applistmanagerlibrary.tasks.NewActivitiesTask;
import com.layoutxml.applistmanagerlibrary.tasks.NewAppTask;
import com.layoutxml.applistmanagerlibrary.tasks.SortTask;
import com.layoutxml.applistmanagerlibrary.tasks.UninstalledActivitiesTask;
import com.layoutxml.applistmanagerlibrary.tasks.UninstalledAppTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class AppList extends BroadcastReceiver{

    public static IntentFilter intentFilter = new IntentFilter();

    //Tasks
    private static AppTask appTask;
    private static ActivitiesTask activitiesTask;
    private static NewAppTask newAppTask;
    private static NewActivitiesTask newActivitiesTask;
    private static UninstalledAppTask uninstalledAppTask;
    private static UninstalledActivitiesTask uninstalledActivitiesTask;
    private static SortTask sortTask;

    //Listeners - weakreferences
    private static WeakReference<AppListener> appListener;
    private static WeakReference<ActivitiesListener> activitiesListener;
    private static WeakReference<NewAppListener> newAppListener;
    private static WeakReference<NewActivitiesListener> newActivitiesListener;
    private static WeakReference<UninstalledAppListener> uninstalledAppListener;
    private static WeakReference<UninstalledActivitiesListener> uninstalledActivitiesListener;
    private static WeakReference<SortListener> sortListener;

    //Values
    public static final Integer BY_APPNAME = 0;
    public static final Integer BY_PACKAGENAME = 1;
    public static final Integer IN_ASCENDING = 0;
    public static final Integer IN_DESCENDING = 1;

    public static void registerListeners(AppListener appListener, ActivitiesListener activitiesListener, NewAppListener newAppListener, NewActivitiesListener newActivitiesListener, UninstalledAppListener uninstalledAppListener, UninstalledActivitiesListener uninstalledActivitiesListener, SortListener sortListener) {
        AppList.appListener = new WeakReference<>(appListener);
        AppList.activitiesListener = new WeakReference<>(activitiesListener);
        AppList.newAppListener = new WeakReference<>(newAppListener);
        AppList.newActivitiesListener = new WeakReference<>(newActivitiesListener);
        AppList.uninstalledAppListener = new WeakReference<>(uninstalledAppListener);
        AppList.uninstalledActivitiesListener = new WeakReference<>(uninstalledActivitiesListener);
        AppList.sortListener = new WeakReference<>(sortListener);

        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
    }

    public static void getAllApps(Context context, Integer uniqueIdentifier){
        WeakReference<Context> context1 = new WeakReference<>(context);
        appTask = new AppTask(context1,null, true, uniqueIdentifier, appListener);
        appTask.execute();
    }

    public static void getSomeApps(Context context, Integer flags, Boolean match, Integer uniqueIdentifier){
        WeakReference<Context> context1 = new WeakReference<>(context);
        appTask = new AppTask(context1, flags, match, uniqueIdentifier, appListener);
        appTask.execute();
    }

    public static void getAllActivities(Context context, Intent intent, Integer uniqueIdentifier){
        WeakReference<Context> context1 = new WeakReference<>(context);
        activitiesTask = new ActivitiesTask(context1, intent, 0, null, false, uniqueIdentifier, activitiesListener);
        activitiesTask.execute();
    }

    public static void getSomeActivities(Context context, Intent intent, Integer activitiesFlags, Integer appFlags, Boolean appMatch, Integer uniqueIdentifier){
        WeakReference<Context> context1 = new WeakReference<>(context);
        activitiesTask = new ActivitiesTask(context1, intent, activitiesFlags, appFlags, appMatch, uniqueIdentifier, activitiesListener);
        activitiesTask.execute();
    }

    public static void getAllNewApps(Context context, List<AppData> appDataList, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        newAppTask = new NewAppTask(context1, appDataList, null, false, uniqueIdentifier, newAppListener);
        newAppTask.execute();
    }

    public static void getSomeNewApps(Context context, List<AppData> appDataList, Integer flags, Boolean match, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        newAppTask = new NewAppTask(context1, appDataList, flags, match, uniqueIdentifier, newAppListener);
        newAppTask.execute();
    }

    public static void getAllNewActivities(Context context, List<AppData> appDataList, Intent intent, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        newActivitiesTask = new NewActivitiesTask(context1,appDataList,intent,0,null,false,uniqueIdentifier,newActivitiesListener);
        newActivitiesTask.execute();
    }

    public static void getSomeNewActivities(Context context, List<AppData> appDataList, Intent intent, Integer activitiesFlags, Integer appFlags, Boolean appMatch, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        newActivitiesTask = new NewActivitiesTask(context1,appDataList,intent,activitiesFlags,appFlags,appMatch,uniqueIdentifier,newActivitiesListener);
        newActivitiesTask.execute();
    }

    public static void getAllUninstalledApps(Context context, List<AppData> appDataList, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        uninstalledAppTask = new UninstalledAppTask(context1, appDataList, uniqueIdentifier, uninstalledAppListener);
        uninstalledAppTask.execute();
    }

    public static void getAllUninstalledActivities(Context context, List<AppData> appDataList, Intent intent, Integer uniqueIdentifier) {
        WeakReference<Context> context1 = new WeakReference<>(context);
        uninstalledActivitiesTask = new UninstalledActivitiesTask(context1, appDataList, intent, uniqueIdentifier, uninstalledActivitiesListener);
        uninstalledActivitiesTask.execute();
    }

    public static Boolean checkApplicationFlags(AppData appData, Integer flags, Boolean match) {
        if (match)
            return ((flags==null) || ((appData.getFlags() & flags) != 0));
        else
            return ((flags==null) || ((appData.getFlags() & flags) == 0));
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
        if (activitiesTask !=null) {
            if (activitiesTask.getStatus()!=AsyncTask.Status.FINISHED) {
                activitiesTask.cancel(true);
            }
        }
        if (newAppTask !=null) {
            if (newAppTask.getStatus()!=AsyncTask.Status.FINISHED) {
                newAppTask.cancel(true);
            }
        }
        if (newActivitiesTask !=null) {
            if (newActivitiesTask.getStatus()!=AsyncTask.Status.FINISHED) {
                newActivitiesTask.cancel(true);
            }
        }
        if (uninstalledAppTask !=null) {
            if (uninstalledAppTask.getStatus()!=AsyncTask.Status.FINISHED) {
                uninstalledAppTask.cancel(true);
            }
        }
        if (uninstalledActivitiesTask !=null) {
            if (uninstalledActivitiesTask.getStatus()!=AsyncTask.Status.FINISHED) {
                uninstalledActivitiesTask.cancel(true);
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
                                newApp.add(app);
                                newAppListener.get().newAppListener(newApp, null, false, true, -1);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
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
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    app.setIcon(packageManager.getApplicationIcon(app.getPackageName()));
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }
                                newApps.add(app);
                            }
                            newActivitiesListener.get().newActivitiesListener(newApps,null,0,null,false,true,-1);
                        }
                    }
                }
            }
            else if (action.equals(Intent.ACTION_PACKAGE_REMOVED) && uninstalledAppListener !=null) {
                if (uninstalledAppListener.get()!=null) {
                    Uri data = intent.getData();
                    List<AppData> newApp = new ArrayList<>();
                    AppData app = new AppData();
                    if (data != null) {
                        app.setPackageName(data.getEncodedSchemeSpecificPart());
                        newApp.add(app);
                        uninstalledAppListener.get().uninstalledAppListener(newApp, true, -1);
                        uninstalledActivitiesListener.get().uninstalledActivitiesListener(newApp,null,true,-1);
                    }
                }
            }
        }
    }
}
