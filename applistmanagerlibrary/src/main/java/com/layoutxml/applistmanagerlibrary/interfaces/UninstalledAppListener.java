package com.layoutxml.applistmanagerlibrary.interfaces;

import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.util.List;

/**
 * Created by LayoutXML on 23/08/2018
 */
public interface UninstalledAppListener {
    void uninstalledAppListener(List<AppData> appDataList, Boolean fromReceiver, Integer applicationFlags, Boolean applicationFlagsMatch, String[] permissions, Boolean matchPermissions, Integer uniqueIdentifier);
}
