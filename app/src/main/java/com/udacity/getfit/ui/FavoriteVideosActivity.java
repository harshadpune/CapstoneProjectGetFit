package com.udacity.getfit.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.getfit.R;

import java.util.ArrayList;

public class FavoriteVideosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_favorite_videos);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.favoriteVideos);
        setFavoriteData();

    }

    private void setFavoriteData() {
       /* favoriteReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    Log.d("YoutbeActivity","--------"+dataSnapshot1.getKey());
                    if(dataSnapshot1.getKey().equalsIgnoreCase("harshadpune")){
                        ArrayList<String> arrayList = (ArrayList<String>) dataSnapshot1.getValue();
                        Log.d( "YoutbeActivity", "--------Matched "+ arrayList.get(0));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}
