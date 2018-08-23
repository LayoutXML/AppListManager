package com.layoutxml.applistmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.layoutxml.applistmanagerlibrary.AppList;
import com.layoutxml.applistmanagerlibrary.interfaces.AllAppsListener;
import com.layoutxml.applistmanagerlibrary.interfaces.AllNewAppsListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private Button getAllButton;
    private Button getNewButton;
    private TextView getAllText;
    private TextView getNewText;
    private List<AppData> AllAppsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllButton = findViewById(R.id.getAllBtn);
        getNewButton = findViewById(R.id.getNewBtn);
        getAllText = findViewById(R.id.getAllTxt);
        getNewText = findViewById(R.id.getNewTxt);

        final AllAppsListener allAppsListener = new AllAppsListener() {
            @Override
            public void allAppsListener(List<AppData> appDataList) {
                getAllText.setText("There are now "+appDataList.size()+" apps installed.");
                AllAppsList = appDataList;
            }
        };

        final AllNewAppsListener allNewAppsListener = new AllNewAppsListener() {
            @Override
            public void allNewAppsListener(List<AppData> appDataList) {
                getNewText.setText("There were "+appDataList.size()+" new apps installed.");
                if (AllAppsList!=null) {
                    AllAppsList.addAll(appDataList);
                    getAllText.setText(getAllText.getText() + "\n+" + appDataList.size() + " (" + AllAppsList.size() + " total)");
                }
            }
        };

        getAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppList.getAllApps(getApplicationContext(),allAppsListener);
            }
        });

        getNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppList.getAllNewApps(getApplicationContext(), allNewAppsListener, AllAppsList);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppList.stop();
    }
}
