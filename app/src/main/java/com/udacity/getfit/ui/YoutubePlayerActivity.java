package com.udacity.getfit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.FavoriteData;
import com.udacity.getfit.utils.AppConstants;
import com.udacity.getfit.utils.Utils;
import java.util.ArrayList;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements View.OnClickListener {

    private YouTubePlayerView youtubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    private String videoId;
    DatabaseReference favoriteReference;
    DatabaseReference favoriteRemoveReference;
    private Button btnFavorite;
    private FirebaseUser currentUser;
    private String favoriteKey =  "favorite";
    private ImageView ivFavorite;
    private boolean isAlreadyBookmarked = false;
    private ArrayList<FavoriteData> favoritesList;
    private String videoName;
    private TextView tvFavorite;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube_player);
        initComponents();
        setListeners();
        checkIfAlreadyBookmarked();
    }

    private void checkIfAlreadyBookmarked() {
        favoriteReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    if(dataSnapshot1.getKey().equalsIgnoreCase(Utils.getCurrentUserForDB(""+FirebaseAuth.getInstance().getCurrentUser().getEmail()))) {
                        for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                            FavoriteData favoriteData = dataSnapshot2.getValue(FavoriteData.class);
                            if(favoriteData.getVideoId().equalsIgnoreCase(videoId)){
                                isAlreadyBookmarked = true;
                                ivFavorite.setImageResource(R.drawable.star_selected);
                                Log.d("YoutubePlayer","-------------- isAlreadyBookmarked true");
                                favoriteReference.removeEventListener(this);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setListeners() {
        ivFavorite.setOnClickListener(this);
    }

    private void initComponents() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ivFavorite = findViewById(R.id.ivFavorite);
        tvFavorite = findViewById(R.id.tvFavorite);
        favoriteReference = FirebaseDatabase.getInstance().getReference("favorites");
        favoriteRemoveReference = FirebaseDatabase.getInstance().getReference("favorites");
        youtubePlayerView = findViewById(R.id.youtubePlayer);
        videoId =  getIntent().getStringExtra(AppConstants.VIDEO_ID);
        videoName =  getIntent().getStringExtra(AppConstants.VIDEO_NAME);
        favoritesList = new ArrayList();


        if(!TextUtils.isEmpty(videoName))
            tvFavorite.setText(""+videoName);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivFavorite:
                if(!isAlreadyBookmarked) {
                    ivFavorite.setImageResource(R.drawable.star_selected);
                    FavoriteData favoriteData = new FavoriteData(videoName, videoId);
                    favoritesList.add(favoriteData);
                    favoriteReference.child(Utils.getCurrentUserForDB(""+FirebaseAuth.getInstance().getCurrentUser().getEmail())).setValue(favoritesList);

                }else{
                    ivFavorite.setImageResource(R.drawable.star_unselected);
                    isAlreadyBookmarked = false;
                    favoriteReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                                if(dataSnapshot1.getKey().equalsIgnoreCase(Utils.getCurrentUserForDB(""+FirebaseAuth.getInstance().getCurrentUser().getEmail()))) {
                                    for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                                        FavoriteData favoriteData = dataSnapshot2.getValue(FavoriteData.class);
                                        if(favoriteData.getVideoId().equalsIgnoreCase(videoId)){
                                            String removeKey = dataSnapshot2.getKey();
                                            favoriteRemoveReference.child(Utils.getCurrentUserForDB(""+FirebaseAuth.getInstance().getCurrentUser().getEmail())).child(removeKey).removeValue();
                                            Toast.makeText(YoutubePlayerActivity.this, "Removed "+ removeKey, Toast.LENGTH_SHORT).show();
                                            deleteVideoAndUpdateAdapter(videoId);
                                            Log.d("YoutubePlayer","-------------- isAlreadyBookmarked false");
                                            favoriteReference.removeEventListener(this);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                break;
        }
    }


    private void deleteVideoAndUpdateAdapter(String videoId) {
        Intent intent = new Intent();
        intent.putExtra(AppConstants.VIDEO_ID, videoId);
        setResult(RESULT_OK,intent);
        finish();
    }
}
