package com.udacity.getfit.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.WorkoutData;
import java.util.ArrayList;

class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private ArrayList<WorkoutData> workoutDataList = new ArrayList();

    public WidgetRemoteViewFactory(final Context context, Intent intent) {
        this.context = context;
        String name = intent.getStringExtra(AppConstants.INTENT_WORKOUT_LIST);
        Gson gson = new Gson();
        workoutDataList = gson.fromJson(name, new TypeToken<ArrayList<WorkoutData>>() {}.getType());
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return workoutDataList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),R.layout.reports_list_item);
        remoteView.setTextViewText(R.id.tvWorkoutName, workoutDataList.get(position).getWorkoutName());
        remoteView.setTextViewText(R.id.tvWorkoutTime, workoutDataList.get(position).getTime());
        remoteView.setTextViewText(R.id.tvWorkoutReps, context.getString(R.string.reps)+""+ workoutDataList.get(position).getWorkoutReps());
        remoteView.setTextViewText(R.id.tvWorkoutDate, workoutDataList.get(position).getDate());

        remoteView.setContentDescription(R.id.tvWorkoutName, workoutDataList.get(position).getWorkoutName()+" "+context.getString(R.string.cd_completed_in));
        remoteView.setContentDescription(R.id.tvWorkoutTime,  workoutDataList.get(position).getTime());
        remoteView.setContentDescription(R.id.tvWorkoutReps, context.getString(R.string.cd_reps)+" "+ workoutDataList.get(position).getWorkoutReps() + " "+context.getString(R.string.cd_on));
        remoteView.setContentDescription(R.id.tvWorkoutDate,  workoutDataList.get(position).getDate());
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
