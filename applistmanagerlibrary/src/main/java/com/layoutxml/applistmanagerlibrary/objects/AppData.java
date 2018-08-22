package com.layoutxml.applistmanagerlibrary.objects;

import android.graphics.drawable.Drawable;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class AppData {
    private String appName;
    private String appPackageName;
    private Drawable appIcon;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
