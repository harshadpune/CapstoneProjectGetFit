package com.udacity.getfit.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.getfit.R;
import com.udacity.getfit.dao.WorkoutData;
import com.udacity.getfit.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;

class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private final AppDatabase mDb;
    //    private WorkoutList workoutDataList;
    private ArrayList<WorkoutData> workoutDataList;

    public WidgetRemoteViewFactory(final Context context, Intent intent) {
        this.context = context;
        mDb = AppDatabase.getInstance(context);
//        workoutDataList = new ArrayList();
//        workoutDataList.setWorkoutDataList((ArrayList<WorkoutData>) intent.getSerializableExtra("Array"));

//        WorkoutData workoutData = new WorkoutData("asd","12","00:15","12-Aug-2018");
//        workoutDataList = new ArrayList();
//        workoutDataList.add(workoutData);

//        workoutDataList = intent.getExtras().getParcelableArrayList("Array");
//        workoutDataList = intent.getParcelableArrayListExtra("Array");
//        String yes = intent.getStringExtra("Array");

        Log.d("Factory", "############Yo "+workoutDataList.get(0).getWorkoutName());
//        onUpdateWidget();
    }

    /*public void onUpdateWidget(){
        workoutDataList = new ArrayList();
        final DatabaseReference workoutReferece = FirebaseDatabase.getInstance().getReference("workouts");
        workoutReferece.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    if(dataSnapshot1.getKey().equalsIgnoreCase(Utils.getCurrentUserForDB(""+ FirebaseAuth.getInstance().getCurrentUser().getEmail()))){
                        for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                            WorkoutData workoutData = dataSnapshot2.getValue(WorkoutData.class);
                            workoutDataList.add(workoutData);
                        }

                        for(int i=0; i< workoutDataList.size(); i++){
                            Log.d("Fragment","------workoutName "+workoutDataList.get(i).getWorkoutName());
                        }
                    }
                }
                workoutReferece.removeEventListener(this);

//                    AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    @Override
    public void onCreate() {
//            onUpdateWidget();
    }

    @Override
    public void onDataSetChanged() {
//            onUpdateWidget();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {

//        LiveData<List<WorkoutData>> workoutData = mDb.reportsDao().loadAllReports();

        Log.d("WidgetCreatingView", "---- Count "+workoutDataList.size());
        return workoutDataList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Log.d("WidgetCreatingView", "WidgetCreatingView");
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.reports_list_item);

        Log.d("Loading", workoutDataList.get(position).workoutName);
        remoteView.setTextViewText(R.id.tvWorkoutName, workoutDataList.get(position).getWorkoutName());
        remoteView.setTextViewText(R.id.tvWorkoutTime, workoutDataList.get(position).getWorkoutReps());
        remoteView.setTextViewText(R.id.tvWorkoutTime, workoutDataList.get(position).getTime());
        remoteView.setTextViewText(R.id.tvWorkoutDate, workoutDataList.get(position).getDate());


       /* remoteView.setTextViewText(R.id.tvWorkoutName, "Crunches");
        remoteView.setTextViewText(R.id.tvWorkoutReps,"x20");
        remoteView.setTextViewText(R.id.tvWorkoutTime, "02:30");
        remoteView.setTextViewText(R.id.tvWorkoutDate, "14-Aug-2018");
*/
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
