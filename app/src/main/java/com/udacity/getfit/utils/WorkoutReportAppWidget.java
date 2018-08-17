package com.udacity.getfit.utils;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.WorkoutData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class WorkoutReportAppWidget extends AppWidgetProvider {


    private static final String ACTION_SCHEDULED_UPDATE = "com.udacity.getfit.SCHEDULED_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_SCHEDULED_UPDATE)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] ids = manager.getAppWidgetIds(new ComponentName(context, WorkoutReportAppWidget.class));
            onUpdate(context, manager, ids);
        }
        super.onReceive(context, intent);
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, ArrayList<WorkoutData> workoutDataList) {

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.workout_report_app_widget);
        Intent intent = new Intent(context.getApplicationContext(), WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        Gson gson = new Gson();
        String workoutsList = gson.toJson(workoutDataList);
        intent.putExtra(AppConstants.INTENT_WORKOUT_LIST,""+workoutsList);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.lvReports, intent);
        remoteViews.setEmptyView(R.id.lvReports,R.id.empty);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        scheduleNextUpdate(context);


        final ArrayList<WorkoutData> workoutDataList = new ArrayList();
        final Date todaysDate = Calendar.getInstance().getTime();
        final SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy");
        final DatabaseReference workoutReferece = FirebaseDatabase.getInstance().getReference("workouts");
        workoutReferece.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    if(dataSnapshot1.getKey().equalsIgnoreCase(Utils.getCurrentUserForDB(""+ FirebaseAuth.getInstance().getCurrentUser().getEmail()))){
                        workoutDataList.clear();
                        for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                            WorkoutData workoutData = dataSnapshot2.getValue(WorkoutData.class);
                            if(workoutData.getDate().equalsIgnoreCase(sf.format(todaysDate)))
                                workoutDataList.add(workoutData);
                        }

                       /* for(int i=0; i< workoutDataList.size(); i++){
                            Log.d("Widget","------workoutName "+workoutDataList.get(i).getWorkoutName());
                        }*/

                        for (int appWidgetId : appWidgetIds) {
                            updateAppWidget(context, appWidgetManager, appWidgetId, workoutDataList);
                        }
                        workoutReferece.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    private static void scheduleNextUpdate(Context context) {
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WorkoutReportAppWidget.class);
        intent.setAction(ACTION_SCHEDULED_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 1);
        midnight.set(Calendar.MILLISECOND, 0);
        midnight.add(Calendar.DAY_OF_YEAR, 1);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pendingIntent);
        }
    }
}

