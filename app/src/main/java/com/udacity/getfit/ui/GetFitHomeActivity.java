package com.udacity.getfit.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.FitnessData;
import com.udacity.getfit.dao.WorkoutMaster;
import com.udacity.getfit.database.AppDatabase;
import com.udacity.getfit.database.AppExcecutors;
import com.udacity.getfit.network.RetrofitAPIInterface;
import com.udacity.getfit.network.RetrofitApiClient;
import com.udacity.getfit.utils.AppConstants;
import com.udacity.getfit.utils.FitnessCardsRecyclerAdapter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetFitHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivDailyVideo, ivPlay;
    private ProgressBar pbLoading;
    private RecyclerView rvFitnessCards;
    private TextView tvUserName;
    private CardView cvFitness;
    private String videoId = "";
    private TextView tvDailyVideo;
    private String videoName = "";
    private AppDatabase mDb;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_fit_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.workoutCategories));
        initComponents();
        setListeners();
        loadFitnessData();
    }

    private void setListeners() {
        cvFitness.setOnClickListener(this);
    }

    private void initComponents() {
        gson = new Gson();
        ivDailyVideo = findViewById(R.id.ivDailyVideo);
        ivPlay = findViewById(R.id.ivPlay);
        pbLoading = findViewById(R.id.pbLoading);
        rvFitnessCards = findViewById(R.id.rvFitnessCards);
        tvUserName = findViewById(R.id.tvUserName);
        tvDailyVideo = findViewById(R.id.tvDailyVideo);
        cvFitness = findViewById(R.id.cvFitness);
        tvUserName.setText(getString(R.string.logged_in)+" "+FirebaseAuth.getInstance().getCurrentUser().getEmail());
        mDb = AppDatabase.getInstance(this);
    }

    private void loadFitnessData() {
        RetrofitAPIInterface retrofitAPIInterface = RetrofitApiClient.getClient().create(RetrofitAPIInterface.class);
        //Get video list
        final Call<List<FitnessData>> fitnessData = retrofitAPIInterface.getFitnessData();

        fitnessData.enqueue(new Callback<List<FitnessData>>() {
            @Override
            public void onResponse(Call<List<FitnessData>> call, Response<List<FitnessData>> response) {
                List<FitnessData> fitnessData = response.body();
                loadVideoThumbnail(fitnessData.get(0).dailyVideo);
                rvFitnessCards.setAdapter(new FitnessCardsRecyclerAdapter(GetFitHomeActivity.this, fitnessData.get(0)));
                videoId = fitnessData.get(0).dailyVideo;
                videoName = ""+fitnessData.get(0).dailyVideoName;
                tvDailyVideo.setText(videoName);
                cvFitness.setContentDescription(videoName+" "+getString(R.string.cd_video_card));
                setFitnessDataInDb(fitnessData.get(0));
            }

            @Override
            public void onFailure(Call<List<FitnessData>> call, Throwable t) {
                fitnessData.cancel();
                loadFitnessDataFromDb();
            }
        });
    }

    /**
     * Refresh Database details everytime application is launched. This data will be used when device is launched when no Internet connection is present.
     * @param fitnessData
     */
    private void setFitnessDataInDb(FitnessData fitnessData) {
        String workoutsList = gson.toJson(fitnessData);
        final WorkoutMaster workoutMasterNew = new WorkoutMaster();
        workoutMasterNew.setWorkoutMasterData(workoutsList);
        AppExcecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.workoutsDao().deleteAllData();
            }
        });


        final LiveData<WorkoutMaster> workoutData = mDb.workoutsDao().loadAllData();
        workoutData.observe(this, new Observer<WorkoutMaster>() {
            @Override
            public void onChanged(@Nullable final WorkoutMaster workoutMaster) {
                workoutData.removeObserver(this);
                if(workoutMaster !=null){
                   deleteAllData();
                }
                insertMasterData(workoutMasterNew);
            }
        });

    }

    /**
     * Delete previous records of workout details before adding the newly received data
     */
    private void deleteAllData(){
        AppExcecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.d("GetFitHomeActivity", "----------Deleting All Data");
                mDb.workoutsDao().deleteAllData();
            }
        });
    }

    /**
     * Add master data of workout in database.
     * @param workoutMasterNew
     */
    private void insertMasterData(final WorkoutMaster workoutMasterNew){
        AppExcecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.d("GetFitHomeActivity", "----------Adding master data First time");
                mDb.workoutsDao().insertWorkoutMaster(workoutMasterNew);
            }
        });
    }

    /**
     * If no internet connection is there then load the data from database.
     */
    private void loadFitnessDataFromDb() {
        final LiveData<WorkoutMaster> workoutData = mDb.workoutsDao().loadAllData();
        workoutData.observe(this, new Observer<WorkoutMaster>() {
            @Override
            public void onChanged(@Nullable final WorkoutMaster workoutMaster) {
                workoutData.removeObserver(this);

                FitnessData fitnessData = gson.fromJson(workoutMaster.getWorkoutMasterData(), new TypeToken<FitnessData>() {}.getType());
                if(fitnessData !=null){
                    Toast.makeText(GetFitHomeActivity.this, "No network. Showing Cached Data.", Toast.LENGTH_SHORT).show();
                    Log.d("GetFitHomeActivity", "------------------Data Already Present. Getting it");
                    loadDataInUI(fitnessData);
                }else{
                    Log.d("GetFitHomeActivity", "------------------Data Not Present.");
                }

            }
        });
    }

    /**
     * Load data received from database in UI
     * @param fitnessData
     */
    private void loadDataInUI(FitnessData fitnessData) {
        loadVideoThumbnail(fitnessData.dailyVideo);
        rvFitnessCards.setAdapter(new FitnessCardsRecyclerAdapter(GetFitHomeActivity.this, fitnessData));
        videoId = fitnessData.dailyVideo;
        videoName = ""+fitnessData.dailyVideoName;
        tvDailyVideo.setText(videoName);
        cvFitness.setContentDescription(videoName+" "+getString(R.string.cd_video_card));
    }

    private void loadVideoThumbnail(String dailyVideo) {

        Picasso.with(this).load(AppConstants.YOUTUBE_VIDEO_THUMBNAIL+""+dailyVideo+""+AppConstants.VIDEO_THUMBNAIL_EXTENSION).into(ivDailyVideo, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                ivDailyVideo.setVisibility(View.VISIBLE);
                ivPlay.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                    ivDailyVideo.setVisibility(View.GONE);
                    ivPlay.setVisibility(View.GONE);
                    pbLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuFavoriteVideos:
                Intent favoriteIntent = new Intent(GetFitHomeActivity.this, FavoriteVideosActivity.class);
                startActivity(favoriteIntent);
                break;

            case R.id.menuReport:
                Intent reportIntent = new Intent(GetFitHomeActivity.this, ReportActivity.class);
                startActivity(reportIntent);
                break;

            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(GetFitHomeActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cvFitness:
                if(TextUtils.isEmpty(videoId))
                    Toast.makeText(this, "Please wait..", Toast.LENGTH_SHORT).show();
                else {
                    Intent youtubePlayerIntent = new Intent(GetFitHomeActivity.this, YoutubePlayerActivity.class);
                    youtubePlayerIntent.putExtra(AppConstants.VIDEO_ID, "" + videoId);
                    youtubePlayerIntent.putExtra(AppConstants.VIDEO_NAME, "" + videoName);
                    startActivity(youtubePlayerIntent);
                }
            break;
        }
    }
}
