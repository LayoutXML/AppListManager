package com.layoutxml.applistmanagerlibrary.tasks;

import android.os.AsyncTask;

import com.layoutxml.applistmanagerlibrary.interfaces.SortListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by LayoutXML on 24/08/2018
 */
public class SortTask extends AsyncTask<Void,Void,List<AppData>> {

    private static final String TAG = "SortTask";

    private final WeakReference<SortListener> sortListener;
    private List<AppData> appDataList;
    private Integer sortBy;
    private Integer inOrder;
    private Integer uniqueIdentifier;

    public SortTask(List<AppData> appDataList, Integer sortBy, Integer inOrder, Integer uniqueIdentifier, WeakReference<SortListener> sortListener){
        this.sortListener = sortListener;
        this.appDataList = appDataList;
        this.sortBy = sortBy;
        this.inOrder = inOrder;
        this.uniqueIdentifier = uniqueIdentifier;
    }


    @Override
    protected List<AppData> doInBackground(Void... voids) {
        if (sortBy.equals(0) && inOrder.equals(0)) {
            Collections.sort(appDataList, new Comparator<AppData>() {
                @Override
                public int compare(AppData t0, AppData t1) {
                    return t0.getName().compareTo(t1.getName());
                }
            });
        } else if (sortBy.equals(0) && inOrder.equals(1)) {
            Collections.sort(appDataList, new Comparator<AppData>() {
                @Override
                public int compare(AppData t0, AppData t1) {
                    return t1.getName().compareTo(t0.getName());
                }
            });
        } else if (sortBy.equals(1) && inOrder.equals(0)) {
            Collections.sort(appDataList, new Comparator<AppData>() {
                @Override
                public int compare(AppData t0, AppData t1) {
                    return t0.getPackageName().compareTo(t1.getPackageName());
                }
            });
        } else if (sortBy.equals(1) && inOrder.equals(1)) {
            Collections.sort(appDataList, new Comparator<AppData>() {
                @Override
                public int compare(AppData t0, AppData t1) {
                    return t1.getPackageName().compareTo(t0.getPackageName());
                }
            });
        }
        return appDataList;
    }

    @Override
    protected void onPostExecute(List<AppData> appDataList) {
        final SortListener listener = sortListener.get();
        if (listener!=null){
            listener.sortListener(appDataList,sortBy,inOrder,uniqueIdentifier);
        }
    }
}
