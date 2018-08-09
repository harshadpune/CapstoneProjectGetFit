package com.udacity.getfit.ui;

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
import com.udacity.getfit.utils.AppConstants;
import com.udacity.getfit.utils.Utils;
import java.util.ArrayList;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements View.OnClickListener {

    private YouTubePlayerView youtubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    private String videoId;
    DatabaseReference favoriteReference;
    private Button btnFavorite;
    private FirebaseUser currentUser;
    private String favoriteKey =  "favorite";
    private ImageView ivFavorite;
    private boolean isAlreadyBookmarked = false;
    private ArrayList<String> favoritesList;
    private String videoName;
    private TextView tvFavorite;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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
                    if(dataSnapshot1.getKey().equalsIgnoreCase(Utils.getCurrentUserForDB(""+FirebaseAuth.getInstance().getCurrentUser().getEmail()))){
                        favoritesList = (ArrayList<String>) dataSnapshot1.getValue();
                        for (int i=0; i<favoritesList.size(); i++) {
                            if(favoritesList.get(i).equalsIgnoreCase(videoId)) {
                                isAlreadyBookmarked = true;
                                ivFavorite.setImageResource(R.drawable.star_selected);
                                Log.d("YoutbeActivity", "--------Matched " + favoritesList.get(i));
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

        youtubePlayerView = findViewById(R.id.youtubePlayer);
        videoId =  getIntent().getStringExtra(AppConstants.VIDEO_ID);
        videoName =  getIntent().getStringExtra(AppConstants.VIDEO_NAME);

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

                    if(favoritesList == null){
                        favoritesList = new ArrayList();
                    }
                    favoritesList.add(videoId);
                    favoriteReference.child(Utils.getCurrentUserForDB(""+FirebaseAuth.getInstance().getCurrentUser().getEmail())).setValue(favoritesList);

                }
                break;
        }
    }
}
