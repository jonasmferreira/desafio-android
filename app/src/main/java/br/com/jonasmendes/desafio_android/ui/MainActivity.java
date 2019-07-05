package br.com.jonasmendes.desafio_android.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.jonasmendes.desafio_android.R;
import br.com.jonasmendes.desafio_android.adapter.RepositoryAdapter;
import br.com.jonasmendes.desafio_android.controller.SearchController;
import br.com.jonasmendes.desafio_android.domain.PullRequest;
import br.com.jonasmendes.desafio_android.domain.Repository;
import br.com.jonasmendes.desafio_android.utils.EndlessRecyclerViewScrollListener;
import br.com.jonasmendes.desafio_android.utils.RecyclerItemClickListener;
import br.com.jonasmendes.desafio_android.utils.SimpleDividerItemDecoration;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class MainActivity extends ActivityManagePermission {
    public static String TAG = MainActivity.class.getSimpleName();
    protected ProgressDialog progressDialog;
    protected BroadcastReceiver mMainReceiver;
    private Context context;
    private android.support.v7.widget.RecyclerView rvRepositories;
    private EndlessRecyclerViewScrollListener scrollListener;
    LinearLayoutManager linearLayoutManager;
    List<Repository> repositories = new ArrayList<>();
    RepositoryAdapter adapter = new RepositoryAdapter(repositories);
    private static final String WAKELOCK_KEY = "DESAFIO_JONAS:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);

        askPermission();

        startWakelock();
        loadComponents();
        loadActions();
        loadData();

    }


    protected void askPermission(){
        askCompactPermissions(new String[]{
                PermissionUtils.Manifest_READ_EXTERNAL_STORAGE,
                PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE

        }, new PermissionResult() {
            @Override
            public void permissionGranted() {
                //permission granted
                //replace with your action
            }

            @Override
            public void permissionDenied() {
                //permission denied
                //replace with your action
            }
            @Override
            public void permissionForeverDenied() {
                // user has check 'never ask again'
                // you need to open setting manually
                //  Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                //  Uri uri = Uri.fromParts("package", getPackageName(), null);
                //   intent.setData(uri);
                //  startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
            }
        });
    }

    protected void loadComponents() {
        this.rvRepositories = (RecyclerView) findViewById(R.id.rvRepositories);

        linearLayoutManager = new LinearLayoutManager(this);
        rvRepositories.setLayoutManager(linearLayoutManager);
        rvRepositories.addItemDecoration(new SimpleDividerItemDecoration(this));
    }

    protected void loadActions(){
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                SearchController.getRepository(String.valueOf(page));
            }
        };
        rvRepositories.addOnScrollListener(scrollListener);
        rvRepositories.addOnItemTouchListener(
                new RecyclerItemClickListener(context, rvRepositories ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Repository r = repositories.get(position);
                        Intent intent = new Intent(context, PullRequestsActivity.class);
                        intent.putExtra("criador",r.getOwner().getLogin());
                        intent.putExtra("repositorio",r.getName());
                        context.startActivity(intent);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    protected void loadData() {
        rvRepositories.setAdapter(adapter);
        showProgressDialog("Buscando mais itens");
        SearchController.getRepository();
    }

    protected void loadBroadcasts() {
        mMainReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                try {
                    switch (action) {
                        case "desafio-android-get-repository":
                            closeProgressBar();
                            if(!intent.getExtras().getBoolean("success")){
                                return;
                            }
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Repository>>() {}.getType();
                            List<Repository> repositoryList = gson.fromJson(intent.getExtras().getString("repository"), type);
                            repositories.addAll(repositoryList);
                            final int curSize = adapter.getItemCount();
                            Log.d(TAG, repositoryList.toString());
                            rvRepositories.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyItemRangeInserted(curSize, repositories.size() - 1);
                                }
                            });
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("desafio-android-get-repository");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMainReceiver, filter);
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();
        loadBroadcasts();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMainReceiver);
    }

    protected void showProgressDialog(String msg) {
        showProgressDialog("Aguarde", msg);
    }

    protected void showProgressDialog(String title, String msg) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    protected void closeProgressBar() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.cancel();
    }

    protected void startWakelock(){
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,WAKELOCK_KEY);
        wakeLock.acquire();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
