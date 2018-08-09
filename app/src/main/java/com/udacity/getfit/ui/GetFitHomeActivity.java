package com.udacity.getfit.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class GetFitHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivDailyVideo, ivPlay;
    private ProgressBar pbLoading;
    private RecyclerView rvFitnessCards;
    private TextView tvUserName;
    private CardView cvFitness;
    private String videoId = "";

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
        ivDailyVideo = findViewById(R.id.ivDailyVideo);
        ivPlay = findViewById(R.id.ivPlay);
        pbLoading = findViewById(R.id.pbLoading);
        rvFitnessCards = findViewById(R.id.rvFitnessCards);
        tvUserName = findViewById(R.id.tvUserName);
        cvFitness = findViewById(R.id.cvFitness);
        tvUserName.setText(getString(R.string.logged_in)+" "+FirebaseAuth.getInstance().getCurrentUser().getEmail());
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
            }

            @Override
            public void onFailure(Call<List<FitnessData>> call, Throwable t) {
                Toast.makeText(GetFitHomeActivity.this, "Failure!!", Toast.LENGTH_SHORT).show();
                fitnessData.cancel();
            }
        });
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
                    startActivity(youtubePlayerIntent);
                }
            break;
        }
    }
}
