package com.udacity.getfit.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.udacity.getfit.R;
import com.udacity.getfit.utils.AppConstants;

public class YoutubePlayerActivity extends YouTubeBaseActivity {

    private YouTubePlayerView youtubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    private String videoId;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube_player);
        initComponents();
        setListeners();
    }

    private void setListeners() {

    }

    private void initComponents() {
        youtubePlayerView = findViewById(R.id.youtubePlayer);
        videoId =  getIntent().getStringExtra(AppConstants.VIDEO_ID);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(!TextUtils.isEmpty(videoId))
                    youTubePlayer.loadVideo(videoId);
                else
                    Toast.makeText(YoutubePlayerActivity.this, "Some Error Occurred!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        youtubePlayerView.initialize(AppConstants.YOUTUBE_API_KEY, onInitializedListener);
    }
}
