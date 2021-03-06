package com.layoutxml.applistmanagerlibrary.interfaces;

import android.content.Intent;

import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.util.List;

public interface NewActivityListener {
    void newActivityListener(List<AppData> appDataList, Intent intent, Integer activityFlags, Integer applicationFlags, Boolean applicationFlagsMatch, Boolean fromReceiver, String[] permissions, Boolean matchPermissions, Integer uniqueIdentifier);
}
