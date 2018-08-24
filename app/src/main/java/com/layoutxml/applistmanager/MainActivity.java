package com.layoutxml.applistmanager;

import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.layoutxml.applistmanagerlibrary.AppList;
import com.layoutxml.applistmanagerlibrary.interfaces.AllListener;
import com.layoutxml.applistmanagerlibrary.interfaces.NewListener;
import com.layoutxml.applistmanagerlibrary.interfaces.UninstalledListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AllListener, NewListener, UninstalledListener{

    @Override
    protected void onPause() {
        super.onPause();
        AppList.stop();
    }

    private static final String TAG = "MainActivity";
    private Button getAllButton;
    private Button getNewButton;
    private Button getUninstalledButton;
    private Button getAllSystemButton;
    private TextView getAllText;
    private TextView getNewText;
    private TextView getUninstalledTxt;
    private TextView getAllSystemText;
    private List<AppData> AllAppsList;
    private List<AppData> AllSystemAppsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllButton = findViewById(R.id.getAllBtn);
        getNewButton = findViewById(R.id.getNewBtn);
        getUninstalledButton = findViewById(R.id.getUninstalledBtn);
        getAllSystemButton = findViewById(R.id.getAllSystemBtn);
        getAllText = findViewById(R.id.getAllTxt);
        getNewText = findViewById(R.id.getNewTxt);
        getUninstalledTxt = findViewById(R.id.getUninstalledTxt);
        getAllSystemText = findViewById(R.id.getAllSystemTxt);

        AppList.start(MainActivity.this,MainActivity.this,MainActivity.this);
        registerReceiver(new AppList(),AppList.intentFilter);

        getAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppList.getAll(getApplicationContext());
            }
        });

        getNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppList.getNew(getApplicationContext(), AllAppsList);
            }
        });

        getUninstalledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppList.getUninstalled(getApplicationContext(), AllAppsList);
            }
        });

        getAllSystemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppList.getAll(getApplicationContext(), ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP,true);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void allListener(List<AppData> appDataList, Integer filterFlags) {
        //TODO: receive Boolean
        if (filterFlags == null) {
            getAllText.setText("There are now " + appDataList.size() + " apps installed.");
            AllAppsList = appDataList;
        }
        else if (filterFlags == (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) {
            getAllSystemText.setText("There are now " + appDataList.size() + " apps installed.");
            AllSystemAppsList = appDataList;
        }
    }

    @Override
    public void newListener(List<AppData> appDataList, Integer filterFlags, Boolean fromReceiver) {
        //TODO: receive Boolean
        getNewText.setText(appDataList.size()+" new apps installed.");
        if (filterFlags==null) {
            if (AllAppsList != null) {
                AllAppsList.addAll(appDataList);
                getAllText.setText(getAllText.getText() + "\n+ " + appDataList.size() + " (" + AllAppsList.size() + " total).");
            }
        } else if (filterFlags == (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) {
            if (AllAppsList != null) {
                AllAppsList.addAll(appDataList);
                getAllText.setText(getAllText.getText() + "\n+ " + appDataList.size() + " (" + AllAppsList.size() + " total).");
            }
            if (AllSystemAppsList!=null) {
                AllSystemAppsList.addAll(appDataList);
                getAllSystemText.setText(getAllSystemText.getText() + "\n+ " + appDataList.size() + " (" + AllSystemAppsList.size() + " total).");
            }
        }
    }

    @Override
    public void uninstalledListener(List<AppData> appDataList, Boolean fromReceiver) {
        //TODO: receive Boolean
        getUninstalledTxt.setText(appDataList.size()+" apps uninstaleld.");
        if (AllAppsList!=null) {
            AllAppsList.removeAll(appDataList);
            getAllText.setText(getAllText.getText()+"\n- "+appDataList.size()+" ("+AllAppsList.size()+" total).");
        }
    }
}
