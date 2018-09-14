package com.layoutxml.applistmanagerlibrary.interfaces;

import android.content.Intent;

import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.util.List;

public interface UninstalledActivityListener {
    void uninstalledActivityListener(List<AppData> appDataList, Intent intent, Integer activityFlags, Integer applicationFlags, Boolean applicationFlagsMatch, Boolean fromReceiver, Integer uniqueIdentifier);
}
