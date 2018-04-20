package com.example.hatten.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;



/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class event_notifier extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_START = "com.example.hatten.myapplication.action.START";
    //public static final String ACTION_BAZ = "com.example.hatten.myapplication.action.BAZ";

    // TODO: Rename parameters
    /*public static final String EXTRA_PARAM1 = "com.example.hatten.myapplication.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.example.hatten.myapplication.extra.PARAM2";
*/
    public event_notifier() {
        super("event_notifier");
    }


    private static final String TAG = "MyApplication";

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(TAG, "got any intent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                /*final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);*/
                //handleActionStart();
                Log.v(TAG, "got intent start");
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionStart() {
        // TODO: Handle action Foo
        while (true) {

        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
