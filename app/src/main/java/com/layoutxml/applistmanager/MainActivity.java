package com.layoutxml.applistmanager;

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

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private Button getAllButton;
    private Button getNewButton;
    private Button getUninstalledButton;
    private TextView getAllText;
    private TextView getNewText;
    private TextView getUninstalledTxt;
    private List<AppData> AllAppsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllButton = findViewById(R.id.getAllBtn);
        getNewButton = findViewById(R.id.getNewBtn);
        getUninstalledButton = findViewById(R.id.getUninstalledBtn);
        getAllText = findViewById(R.id.getAllTxt);
        getNewText = findViewById(R.id.getNewTxt);
        getUninstalledTxt = findViewById(R.id.getUninstalledTxt);

        final AllListener allListener = new AllListener() {
            @Override
            public void allListener(List<AppData> appDataList) {
                getAllText.setText("There are now "+appDataList.size()+" apps installed.");
                AllAppsList = appDataList;
            }
        };

        final NewListener newListener = new NewListener() {
            @Override
            public void newListener(List<AppData> appDataList) {
                getNewText.setText(appDataList.size()+" new apps installed.");
                if (AllAppsList!=null) {
                    AllAppsList.addAll(appDataList);
                    getAllText.setText(getAllText.getText()+"\n+ "+appDataList.size()+" ("+AllAppsList.size()+" total).");
                }
            }
        };

        final UninstalledListener uninstalledListener = new UninstalledListener() {
            @Override
            public void uninstalledListener(List<AppData> appDataList) {
                getUninstalledTxt.setText(appDataList.size()+" apps uninstaleld.");
                if (AllAppsList!=null) {
                    AllAppsList.removeAll(appDataList);
                    getAllText.setText(getAllText.getText()+"\n- "+appDataList.size()+" ("+AllAppsList.size()+" total).");
                }
            }
        };

        getAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppList.getAll(getApplicationContext(), allListener);
            }
        });

        getNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppList.getNew(getApplicationContext(), newListener, AllAppsList);
            }
        });

        getUninstalledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppList.getUninstalled(getApplicationContext(), uninstalledListener, AllAppsList);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppList.stop();
    }
}
