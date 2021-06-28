package com.backblaze.b2.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.backblaze.b2.R;
import com.backblaze.b2.viewmodels.AppKeyViewModel;
import com.backblaze.b2.viewmodels.CFileViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private final Context context = this;
    private EditText appKeyId;
    private EditText appKey;
    private AppKeyViewModel mAppKeyViewModel;
    private CFileViewModel mCFileViewModel;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    private static final String BACK_STACK_ROOT_TAG = "file_list_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appKeyId = (EditText) findViewById(R.id.app_key_id);
        appKey = (EditText) findViewById(R.id.app_key);
        Button butCheck = (Button) findViewById(R.id.process_files);
        mAppKeyViewModel = new ViewModelProvider(this).get(AppKeyViewModel.class);
        mCFileViewModel = new ViewModelProvider(this).get(CFileViewModel.class);
        manager = getSupportFragmentManager();
        manager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        butCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPressed();
            }
        });
    }

    private void buttonPressed () {
        try {
            String app_key_id = appKeyId.getText().toString();
            String app_key = appKey.getText().toString();
            if (app_key_id == null || app_key_id.isEmpty()) {
                appKeyId.requestFocus();
                Toast.makeText(context, R.string.app_key_id_required, Toast.LENGTH_LONG).show();
            } else if (app_key == null || app_key.isEmpty()) {
                appKey.requestFocus();
                Toast.makeText(context, R.string.app_key_required, Toast.LENGTH_LONG).show();
            } else {
                if ( mAppKeyViewModel.setAppKeys(app_key_id, app_key) )  {
                    mCFileViewModel.reloadAllCFiles();
                }
                invokeFragment();
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private void invokeFragment() {
        FileListFragment fragment = new FileListFragment();
        transaction = manager.beginTransaction().addToBackStack(BACK_STACK_ROOT_TAG)
                .setCustomAnimations(R.anim.right_in, R.anim.left_out).add(R.id.fragment, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if ( manager.getBackStackEntryCount() != 0){
            super.onBackPressed();
        }else{
            System.exit(0);
        }
    }

    private BroadcastReceiver mReceiver;

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg_for_me = intent.getStringExtra("some_msg");
                Toast.makeText(context, msg_for_me, Toast.LENGTH_LONG).show();
                if ( manager.getBackStackEntryCount() != 0){
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById( R.id.fragment )).commit();
                }
            }
        };
        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //unregister our receiver
        this.unregisterReceiver(this.mReceiver);
    }
}
