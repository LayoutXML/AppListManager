package com.layoutxml.applistmanagerlibrary.interfaces;

import android.content.pm.PackageManager;

import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public interface AllAppsListener {
    public void allAppsListener(List<AppData> appDataList);
}
