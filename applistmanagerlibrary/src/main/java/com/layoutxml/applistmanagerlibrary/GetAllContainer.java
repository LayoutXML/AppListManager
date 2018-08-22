package com.layoutxml.applistmanagerlibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class GetAllContainer {
    public PackageManager packageManager;
    public List<ApplicationInfo> ApplicationInfoList;
    public Context context;

    GetAllContainer(PackageManager packageManager, List<ApplicationInfo> ApplicationInfoList, Context context) {
        this.packageManager = packageManager;
        this.ApplicationInfoList = ApplicationInfoList;
        this.context = context;
    }
}
