package com.layoutxml.applistmanagerlibrary;

import android.content.Context;

import java.util.List;

/**
 * Created by LayoutXML on 22/08/2018
 */
public class ListContextContainer {
    List<AppData> appDataList;
    Context context;

    ListContextContainer(List<AppData> appDataList, Context context) {
        this.appDataList = appDataList;
        this.context = context;
    }
}
