package com.layoutxml.applistmanagerlibrary.objects;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class AppData {

    private static final String TAG = "AppData";

    private String name;
    private Drawable icon;
    private Integer flags;
    @NonNull
    private String packageName = "";

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(@NonNull String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof AppData){
            AppData p = (AppData) o;
            return this.getPackageName().equals(p.getPackageName());
        } else
            return false;
    }
}
