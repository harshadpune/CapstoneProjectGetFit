package com.udacity.getfit.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.udacity.getfit.R;

public class GetFitHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_fit_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.workoutCategories));
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
