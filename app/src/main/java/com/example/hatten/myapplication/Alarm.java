package com.example.hatten.myapplication;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;

/**
 * Created by hatten on 2018-04-17.
 */

public class Alarm extends BroadcastReceiver{
    private static final String TAG = "MyApplication";


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
    private static final int PROJECTION_INSTANCE_EVENT_INDEX = 0;
    private static final int PROJECTION_INSTANCE_BEGIN_INDEX = 1;
    private static final int PROJECTION_INSTANCE_END_INDEX = 2;
    private static final int PROJECTION_EVENT_TITLE_INDEX = 3;
    private static final int PROJECTION_INSTANCE_ID_INDEX = 4;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 5;

    PendingIntent alarmIntent = null;



    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("com.example.hatten.myapplication.action.refresh")) {
            Log.v(TAG, "received intent from tasker, refreshing alarm.");

            //cancel current alarm
            if (alarmIntent != null) {
                AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmMgr.cancel(alarmIntent);
            }


            //query calendar
            Pair<Long, String> res = stuff(context);
            //and set new alarm
            setAlarm(context, res.second, res.first);
        }
        else if (intent.getAction().equals("com.example.hatten.myapplication.action.alarm")) {
            Log.v(TAG, "received intent, alarm triggered with " + intent.getStringExtra("id"));

            //Send intent to tasker to handle routine event
            notifyTasker(context, intent.getStringExtra("id"));

            //query calendar
            Pair<Long, String> res = stuff(context);
            //and set new alarm
            setAlarm(context, res.second, res.first);
        }
    }


    public void setAlarm(Context context, String id, long time) {
        //Intent intent = new Intent("com.example.MyApplication.event_start");
        //Intent intent = new Intent("com.example.hatten.myapplication.action.START");
        //intent.putExtra("message", message);
        Intent intent = new Intent(context, Alarm.class);
        intent.setAction("com.example.hatten.myapplication.action.alarm");
        intent.putExtra("id", id);
        //sendBroadcast(intent);



        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        long delta = System.currentTimeMillis() - SystemClock.elapsedRealtime();

        Log.v(TAG, "alarm in=" + ((time-delta-SystemClock.elapsedRealtime())/1000));
        //alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        //        SystemClock.elapsedRealtime() + 10 * 1000, alarmIntent);
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                time - delta, alarmIntent);
    }

    public void notifyTasker(Context context, String id) {
        Intent intent = new Intent("com.example.MyApplication.event_start");
        //intent.putExtra("title", title);
        intent.putExtra("id", id);
        context.sendBroadcast(intent);

    }

    public Pair<Long, String> stuff(Context context) {
        // Run query
        long currentTime = System.currentTimeMillis();
        long endTime = currentTime + 12 * 60 * 60 * 1000;
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Instances.CONTENT_URI;
        //Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?) AND ("
                + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ?) AND ("
                + CalendarContract.Events.DTSTART + " > ?))";
        //String selection = "((" + CalendarContract.Calendars.+ " = ?))";
        String[] selectionArgs = new String[] {"h6+fruux@protonmail.com", "bitfire.at.davdroid",
                "h6+fruux@protonmail.com", "routine", String.valueOf(currentTime)};

        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_CALENDAR);
        // Submit the query and get a Cursor object back.
        //cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        //cur = cr.query(uri, EVENT_PROJECTION, "", new String[] {}, null);
        //cur = cr.query
        cur = CalendarContract.Instances.query(cr,
                INSTANCE_PROJECTION, currentTime, endTime);

        // Use the cursor to step through the returned records
        boolean found_event = false;

        String eventTitle = null;
        long event_start = 0;
        String event_id = null;
        String instance_id = null;

        while (cur.moveToNext() && !found_event) {
            long calID = 0;
            long event_end = 0;
            /*String displayName = null;
            String accountName = null;
            String ownerName = null;
            */

            // Get the field values
            //calID = cur.getLong(PROJECTION_ID_INDEX);
            event_id = cur.getString(PROJECTION_INSTANCE_EVENT_INDEX);
            instance_id = cur.getString(PROJECTION_INSTANCE_ID_INDEX);
            event_start = cur.getLong(PROJECTION_INSTANCE_BEGIN_INDEX);
            //event_end = cur.getLong(PROJECTION_INSTANCE_END_INDEX);
            String displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            /*accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);*/
            eventTitle = cur.getString(PROJECTION_EVENT_TITLE_INDEX);

            if (displayName.equals("routine") && event_start >= currentTime)
            {
                found_event = true;
            }
        }
        cur.close();

        if (! found_event) {
            throw new UnsupportedOperationException("didn't find any routine instances");
        }

        Log.v(TAG, "eventTitle=" + eventTitle);
        cur.close();
        return new Pair<>(event_start,
                event_id + "#" + instance_id);
    }
}
