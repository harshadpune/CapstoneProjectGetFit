package com.udacity.getfit.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.FavoriteData;
import com.udacity.getfit.utils.AppConstants;
import com.udacity.getfit.utils.FavoritesCardsRecyclerAdapter;
import com.udacity.getfit.utils.Utils;

import java.util.ArrayList;

public class FavoriteVideosActivity extends AppCompatActivity {

    private DatabaseReference favoriteReference;
    private RecyclerView rvFavoritesList;
    private ArrayList<FavoriteData> favoriteList;
    private FavoritesCardsRecyclerAdapter favoritesCardsRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_favorite_videos);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.favoriteVideos);
        initComponents();
        setFavoriteData();

    }

    private void initComponents() {
        favoriteReference = FirebaseDatabase.getInstance().getReference("favorites");
        rvFavoritesList = findViewById(R.id.rvFavoritesList);
        favoriteList = new ArrayList();
    }


    private void setFavoriteData() {
        favoriteReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    if(dataSnapshot1.getKey().equalsIgnoreCase(Utils.getCurrentUserForDB(FirebaseAuth.getInstance().getCurrentUser().getEmail()))){
                        for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()) {
                            FavoriteData favoriteData = dataSnapshot2.getValue(FavoriteData.class);
                            favoriteList.add(favoriteData);
                        }

                    }
                }
                populateFavoriteData(favoriteList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void populateFavoriteData(ArrayList<FavoriteData> favoriteList) {
        favoritesCardsRecyclerAdapter = new FavoritesCardsRecyclerAdapter(this, favoriteList);
        rvFavoritesList.setAdapter(favoritesCardsRecyclerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(favoritesCardsRecyclerAdapter !=null)
            favoritesCardsRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100  && resultCode == RESULT_OK){
            String deletedVideo = data.getStringExtra(AppConstants.VIDEO_ID);
            deleteVideoAndUpdateAdapter(deletedVideo);
        }
    }

    private void deleteVideoAndUpdateAdapter(String deletedVideo) {
         for (FavoriteData favoriteData: favoriteList) {
            if(favoriteData.getVideoId().equalsIgnoreCase(deletedVideo)){
                favoriteList.remove(favoriteData);
                favoritesCardsRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }

}
