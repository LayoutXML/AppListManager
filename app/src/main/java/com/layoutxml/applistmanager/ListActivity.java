package com.layoutxml.applistmanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.layoutxml.applistmanagerlibrary.AppList;
import com.layoutxml.applistmanagerlibrary.interfaces.ActivitiesListener;
import com.layoutxml.applistmanagerlibrary.interfaces.AppListener;
import com.layoutxml.applistmanagerlibrary.interfaces.SortListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements ActivitiesListener, AppListener, SortListener {

    private static final String TAG = "ListActivity";

    private List<AppData> appDataList = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.listView);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        String data = intent.getStringExtra("appDataList");

        AppList.registerListeners(ListActivity.this,ListActivity.this,null,null,null,null,ListActivity.this);

        switch (data){
            default:
                progressBar.setVisibility(View.VISIBLE);
                AppList.getAllApps(getApplicationContext(),0);
                break;
            case "apps":
                progressBar.setVisibility(View.VISIBLE);
                AppList.getAllApps(getApplicationContext(),0);
                break;
            case "activities":
                progressBar.setVisibility(View.VISIBLE);
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                AppList.getAllActivities(getApplicationContext(),mainIntent,1);
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        AppList.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppList.registerListeners(ListActivity.this,ListActivity.this,null,null,null,null,ListActivity.this);
    }

    @Override
    public void activitiesListener(List<AppData> appDataList, Intent intent, Integer activitiesFlags, Integer applicationFlags, Boolean applicationFlagsMatch, Integer uniqueIdentifier) {
        AppList.sort(appDataList,AppList.BY_APPNAME,AppList.IN_ASCENDING,uniqueIdentifier);
    }

    @Override
    public void appListener(List<AppData> appDataList, Integer applicationFlags, Boolean applicationFlagsMatch, Integer uniqueIdentifier) {
        AppList.sort(appDataList,AppList.BY_APPNAME,AppList.IN_ASCENDING,uniqueIdentifier);
    }

    @Override
    public void sortListener(List<AppData> appDataList, Integer sortBy, Integer inOrder, Integer uniqueIdentifier) {
        this.appDataList = appDataList;
        progressBar.setVisibility(View.GONE);

        adapter = new MyAdapter(appDataList);
        recyclerView.setAdapter(adapter);
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<AppData> appDataList0;

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView packageName;
            ImageView logo;

            MyViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.itemName);
                packageName = v.findViewById(R.id.itemPackageName);
                logo = v.findViewById(R.id.itemLogo);
            }
        }

        MyAdapter(List<AppData> appDataList) {
            appDataList0 = appDataList;
        }

        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            AppData app = appDataList0.get(position);
            holder.name.setText(app.getName());
            holder.packageName.setText(app.getPackageName());
            holder.logo.setImageDrawable(app.getIcon());
        }

        @Override
        public int getItemCount() {
            return appDataList0.size();
        }
    }
}
