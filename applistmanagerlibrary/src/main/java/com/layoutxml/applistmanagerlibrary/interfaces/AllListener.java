package com.layoutxml.applistmanagerlibrary.interfaces;

import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public interface AllListener {
    void allListener(List<AppData> appDataList, Integer filterFlags, Boolean match);
}
