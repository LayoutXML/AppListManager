package com.layoutxml.applistmanagerlibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class AppList {

    public interface getAll {
        void getAllApps(List<AppData> appList, Context context);
    }

    public void getAllApps(AppList.getAll callback, Context context){
        //Returns a list of all installed packages

        final PackageManager packageManager = context.getPackageManager();
        final List<ApplicationInfo> ApplicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        GetAllContainer container = new GetAllContainer(packageManager, ApplicationInfoList, context);

        getAllTask getAllTask = new getAllTask(callback);
        getAllTask.execute(container);

    }

    public static class getAllTask extends AsyncTask<GetAllContainer,Void,ListContextContainer> {

        private AppList.getAll callback;

        public getAllTask(AppList.getAll callback) {
            this.callback = callback;
        }

        @Override
        protected ListContextContainer doInBackground(GetAllContainer... getAllContainers) {
            List<AppData> AppDataList = new ArrayList<>();
            for (ApplicationInfo applicationInfo:getAllContainers[0].ApplicationInfoList) {
                AppData app = new AppData();
                app.setAppName(applicationInfo.loadLabel(getAllContainers[0].packageManager).toString());
                app.setAppPackageName(applicationInfo.packageName);
                app.setAppIcon(applicationInfo.loadIcon(getAllContainers[0].packageManager));
                AppDataList.add(app);
            }

            return new ListContextContainer(AppDataList, getAllContainers[0].context);
        }

        @Override
        protected void onPostExecute(ListContextContainer listContextContainer) {
            super.onPostExecute(listContextContainer);

            callback.getAllApps(listContextContainer.appDataList, listContextContainer.context);

        }

    }

    public static void getLaunchable(){
        //Returns all
    }

}
