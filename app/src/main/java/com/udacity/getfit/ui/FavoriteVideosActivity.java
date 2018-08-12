package com.udacity.getfit.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.getfit.R;
import com.udacity.getfit.utils.FavoritesCardsRecyclerAdapter;
import com.udacity.getfit.utils.Utils;

import java.util.ArrayList;

public class FavoriteVideosActivity extends AppCompatActivity {

    private DatabaseReference favoriteReference;
    private RecyclerView rvFavoritesList;

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
    }


    private void setFavoriteData() {
        favoriteReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    Log.d("YoutbeActivity","--------"+dataSnapshot1.getKey());
                    if(dataSnapshot1.getKey().equalsIgnoreCase(Utils.getCurrentUserForDB(FirebaseAuth.getInstance().getCurrentUser().getEmail()))){
                        ArrayList<String> favoriteList = (ArrayList<String>) dataSnapshot1.getValue();
                        populateFavoriteData(favoriteList);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void populateFavoriteData(ArrayList<String> favoriteList) {
        rvFavoritesList.setAdapter(new FavoritesCardsRecyclerAdapter(this, favoriteList));
    }


}
