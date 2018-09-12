package com.layoutxml.applistmanager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.layoutxml.applistmanagerlibrary.AppList;
import com.layoutxml.applistmanagerlibrary.interfaces.ActivitiesListener;
import com.layoutxml.applistmanagerlibrary.interfaces.AppListener;
import com.layoutxml.applistmanagerlibrary.interfaces.NewActivitiesListener;
import com.layoutxml.applistmanagerlibrary.interfaces.NewAppListener;
import com.layoutxml.applistmanagerlibrary.interfaces.SortListener;
import com.layoutxml.applistmanagerlibrary.interfaces.UninstalledActivitiesListener;
import com.layoutxml.applistmanagerlibrary.interfaces.UninstalledAppListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AppListener, NewAppListener, UninstalledAppListener, SortListener, ActivitiesListener, NewActivitiesListener, UninstalledActivitiesListener {

    @Override
    protected void onPause() {
        super.onPause();
        AppList.destroy();
    }

    private static final String TAG = "MainActivity";
    private Button getAllButton;
    private Button getNewButton;
    private Button getUninstalledButton;
    private Button getAllSystemButton;
    private Button getActivitiesButton;
    private Button getNewActivitiesButton;
    private Button getUninstalledActivitiesButton;
    private TextView getAllText;
    private TextView getNewText;
    private TextView getUninstalledText;
    private TextView getAllSystemText;
    private TextView getActivitiesText;
    private TextView getNewActivitiesText;
    private TextView getUninstalledActivitiesText;
    private List<AppData> AllAppsList;
    private List<AppData> AllSystemAppsList;
    private List<AppData> AllActivitiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllButton = findViewById(R.id.getAllBtn);
        getNewButton = findViewById(R.id.getNewBtn);
        getUninstalledButton = findViewById(R.id.getUninstalledBtn);
        getAllSystemButton = findViewById(R.id.getAllSystemBtn);
        getActivitiesButton = findViewById(R.id.getActivitiesBtn);
        getNewActivitiesButton = findViewById(R.id.getNewActivitiesBtn);
        getUninstalledActivitiesButton = findViewById(R.id.getUninstalledActivitiesBtn);
        getAllText = findViewById(R.id.getAllTxt);
        getNewText = findViewById(R.id.getNewTxt);
        getUninstalledText = findViewById(R.id.getUninstalledTxt);
        getAllSystemText = findViewById(R.id.getAllSystemTxt);
        getActivitiesText = findViewById(R.id.getActivitiesTxt);
        getNewActivitiesText = findViewById(R.id.getNewActivitiesTxt);
        getUninstalledActivitiesText = findViewById(R.id.getUninstalledActivitiesTxt);

        AppList.registerListeners(MainActivity.this,MainActivity.this,MainActivity.this,MainActivity.this,MainActivity.this,MainActivity.this,MainActivity.this);
        registerReceiver(new AppList(),AppList.intentFilter);

        getAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppList.getAllApps(getApplicationContext(),0);
            }
        });

        getNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppList.getAllNewApps(getApplicationContext(), AllAppsList, 1);
            }
        });

        getUninstalledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppList.getAllUninstalledApps(getApplicationContext(), AllAppsList,2);
            }
        });

        getAllSystemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppList.getSomeApps(getApplicationContext(), ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP, true, 3);
            }
        });

        getActivitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                AppList.getAllActivities(getApplicationContext(),mainIntent,4);
            }
        });

        getNewActivitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                AppList.getAllNewActivities(getApplicationContext(),AllActivitiesList,mainIntent,5);
            }
        });

        getUninstalledActivitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                AppList.getAllUninstalledActivities(getApplicationContext(),AllActivitiesList,mainIntent,6);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void appListener(List<AppData> appDataList, Integer filterFlags, Boolean match, Integer uniqueIdentifier) {
        if (uniqueIdentifier==0) { //Could also be: if (filterFlags == null)
            getAllText.setText("There are now " + appDataList.size() + " apps installed.");
            AppList.sort(appDataList,AppList.BY_APPNAME,AppList.IN_ASCENDING,0);
        }
        else if (uniqueIdentifier==3) { //Could also be: else if (filterFlags == (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP))
            getAllSystemText.setText("There are now " + appDataList.size() + " apps installed.");
            AllSystemAppsList = appDataList;
        }
    }

    @Override
    public void newAppListener(List<AppData> appDataList, Integer filterFlags, Boolean match, Boolean fromReceiver, Integer uniqueIdentifier) {
        getNewText.setText(appDataList.size()+" new apps installed.");
        if (AllAppsList != null) {
            AllAppsList.addAll(appDataList);
            getAllText.setText(getAllText.getText() + "\n+ " + appDataList.size() + " (" + AllAppsList.size() + " total).");
        }
    }

    @Override
    public void uninstalledAppListener(List<AppData> appDataList, Boolean fromReceiver, Integer uniqueIdentifier) {
        getUninstalledText.setText(appDataList.size()+" apps uninstaleld.");
        if (AllAppsList!=null) {
            AllAppsList.removeAll(appDataList);
            getAllText.setText(getAllText.getText()+"\n- "+appDataList.size()+" ("+AllAppsList.size()+" total).");
        }
    }

    @Override
    public void sortListener(List<AppData> appDataList, Integer sortBy, Integer inOrder, Integer uniqueIdentifier) {
        AllAppsList = appDataList;
    }

    @Override
    public void activitiesListener(List<AppData> appDataList, Intent intent, Integer activitiesFlags, Integer filterFlags, Boolean match, Integer uniqueIdentifier) {
        if (uniqueIdentifier==4) {
            getActivitiesText.setText("There are now " + appDataList.size() + " activities");
            AllActivitiesList = appDataList;
        }
    }

    @Override
    public void newActivitiesListener(List<AppData> appDataList, Intent intent, Integer filterFlags, Boolean match, Boolean fromReceiver, Integer uniqueIdentifier) {

    }

    @Override
    public void uninstalledActivitiesListener(List<AppData> appDataList, Intent intent, Boolean fromReceiver, Integer uniqueIdentifier) {

    }
}
