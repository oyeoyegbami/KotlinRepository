package com.backblaze.b2.viewmodels;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.AndroidViewModel;
import com.backblaze.b2.R;

public class AppKeyViewModel  extends AndroidViewModel {

    /**
     * ViewModel to persist the App key and App key id
     */
    private static String APP_KEY_ID;
    private static String APP_KEY;
    Context context;

    public static String getAppKeyId() {
        return APP_KEY_ID;
    }

    public static String getAppKey() {
        return APP_KEY;
    }

    public AppKeyViewModel(Application application) {
        super(application);
        context = application;
        APP_KEY_ID = application.getString(R.string.app_key_id);
        APP_KEY = application.getString(R.string.app_key);
    }

    public boolean setAppKeys ( String appKeyId, String appKey ){
        boolean changed = false;
        if ( !appKeyId.equals(APP_KEY_ID) || !appKey.equals(APP_KEY)) {
            changed = true;
        }
        this.APP_KEY_ID = appKeyId;
        this.APP_KEY= appKey;
        return changed;
    }
}
