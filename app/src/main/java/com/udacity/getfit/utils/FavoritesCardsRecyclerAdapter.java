package com.udacity.getfit.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.getfit.R;
import com.udacity.getfit.ui.YoutubePlayerActivity;

import java.util.ArrayList;

public class FavoritesCardsRecyclerAdapter extends RecyclerView.Adapter<FavoritesCardsRecyclerAdapter.ViewHolder> {


    private final Context context;
    private final ArrayList<String> favoriteList;

    public FavoritesCardsRecyclerAdapter(Context context, ArrayList<String> favoriteList){
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorites_video_card_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
//            holder.tvWorkoutType.setText(""+fitnessData.workoutInformation.get(position).type);
            Picasso.with(context).load(AppConstants.YOUTUBE_VIDEO_THUMBNAIL+""+favoriteList.get(position)+""+AppConstants.VIDEO_THUMBNAIL_EXTENSION).into(holder.ivDailyVideo, new Callback() {
                @Override
                public void onSuccess() {
                    holder.pbLoading.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    holder.pbLoading.setVisibility(View.GONE);
                }
            });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CardView cvFavorites;
        private final ImageView ivDailyVideo;
        private final ProgressBar pbLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            cvFavorites =     itemView.findViewById(R.id.cvFavorites);
            ivDailyVideo =     itemView.findViewById(R.id.ivDailyVideo);
            pbLoading =     itemView.findViewById(R.id.pbLoading);
            cvFavorites.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.cvFavorites:
                    Intent youtubePlayerIntent = new Intent(context, YoutubePlayerActivity.class);
                    youtubePlayerIntent.putExtra(AppConstants.VIDEO_ID, "" + favoriteList.get(getAdapterPosition()));
                    youtubePlayerIntent.putExtra(AppConstants.VIDEO_NAME, "" + context.getString(R.string.bookmarked_video));
                    context.startActivity(youtubePlayerIntent);
                    break;
            }
        }
    }
}
