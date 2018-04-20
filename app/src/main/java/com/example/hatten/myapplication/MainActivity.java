package com.example.hatten.myapplication;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myapplication.MESSAGE";

    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] INSTANCE_PROJECTION = new String[] {
            /*CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.OWNER_ACCOUNT,                 // 3
            CalendarContract.Calendars.ACCOUNT_TYPE,                  // 4
            */
            CalendarContract.Instances.EVENT_ID,  // 0
            CalendarContract.Instances.BEGIN,  // 1
            CalendarContract.Instances.END,   // 2
            CalendarContract.Events.TITLE,  // 3
            CalendarContract.Instances._ID, //4
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME         // 5
    };

    // The indices for the projection array above.
    /*private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private static final int PROJECTION_ACCOUNT_TYPE_INDEX = 4;*/

    private static final int PROJECTION_INSTANCE_EVENT_INDEX = 0;
    private static final int PROJECTION_INSTANCE_BEGIN_INDEX = 1;
    private static final int PROJECTION_INSTANCE_END_INDEX = 2;
    private static final int PROJECTION_EVENT_TITLE_INDEX = 3;
    private static final int PROJECTION_INSTANCE_ID_INDEX = 4;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 5;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    private static final String TAG = "MyApplication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, Alarm.class);
        intent.setAction("com.example.hatten.myapplication.action.refresh");
        sendBroadcast(intent);
        //setContentView(R.layout.activity_main);

        //Pair<Long, String> res = stuff();
        /*Intent mServiceIntent = new Intent(this, event_notifier.class);
        mServiceIntent.setAction(event_notifier.ACTION_START);
        startService(mServiceIntent);*/
        //setAlarm(res.second, res.first);


    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
