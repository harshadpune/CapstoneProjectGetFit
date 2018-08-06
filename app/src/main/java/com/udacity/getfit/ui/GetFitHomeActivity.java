package com.udacity.getfit.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.FitnessData;
import com.udacity.getfit.network.RetrofitAPIInterface;
import com.udacity.getfit.network.RetrofitApiClient;
import com.udacity.getfit.utils.AppConstants;
import com.udacity.getfit.utils.FitnessCardsRecyclerAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetFitHomeActivity extends AppCompatActivity {

    private ImageView ivDailyVideo, ivPlay;
    private ProgressBar pbLoading;
    private RecyclerView rvFitnessCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_fit_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.workoutCategories));
        initComponents();
        loadFitnessData();
    }

    private void initComponents() {
        ivDailyVideo = findViewById(R.id.ivDailyVideo);
        ivPlay = findViewById(R.id.ivPlay);
        pbLoading = findViewById(R.id.pbLoading);
        rvFitnessCards = findViewById(R.id.rvFitnessCards);
    }

    private void loadFitnessData() {
        RetrofitAPIInterface retrofitAPIInterface = RetrofitApiClient.getClient().create(RetrofitAPIInterface.class);
        //Get video list
        final Call<List<FitnessData>> fitnessData = retrofitAPIInterface.getFitnessData();

        fitnessData.enqueue(new Callback<List<FitnessData>>() {
            @Override
            public void onResponse(Call<List<FitnessData>> call, Response<List<FitnessData>> response) {
                Toast.makeText(GetFitHomeActivity.this, "Success!!", Toast.LENGTH_SHORT).show();


                List<FitnessData> fitnessData = response.body();
                loadVideoThumbnail(fitnessData.get(0).dailyVideo);
                rvFitnessCards.setAdapter(new FitnessCardsRecyclerAdapter(GetFitHomeActivity.this, fitnessData.get(0)));
                Log.d("Testing", "-----------"+fitnessData.get(0).dailyVideo);
                Log.d("Testing", "-----------"+fitnessData.get(0).workoutInformation.get(0).type);
                Log.d("Testing", "-----------"+fitnessData.get(0).workoutInformation.get(0).workoutLists.get(0).workoutName);
            }

            @Override
            public void onFailure(Call<List<FitnessData>> call, Throwable t) {
                Toast.makeText(GetFitHomeActivity.this, "Failure!!", Toast.LENGTH_SHORT).show();
                fitnessData.cancel();
            }
        });
       /* fitnessData.enqueue(new retrofit2.Callback<FitnessData>() {
            @Override
            public void onResponse(Call<FitnessData> call, Response<FitnessData> response) {
                Toast.makeText(GetFitHomeActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                FitnessData fitnessData = response.body();
                Log.d("Testing", "-----------"+fitnessData.dailyVideo);
                Log.d("Testing", "-----------"+fitnessData.workoutInformation.get(0).type);
                Log.d("Testing", "-----------"+fitnessData.workoutInformation.get(0).workoutLists.get(0).workoutName);
            }

            @Override
            public void onFailure(Call<FitnessData> call, Throwable t) {
                Toast.makeText(GetFitHomeActivity.this, "Failure!!", Toast.LENGTH_SHORT).show();
                fitnessData.cancel();
            }
        });*/
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
}
