package com.udacity.getfit.utils;

import android.app.Activity;
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
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.FavoriteData;
import com.udacity.getfit.ui.FavoriteVideosActivity;
import com.udacity.getfit.ui.YoutubePlayerActivity;

import java.util.ArrayList;

public class FavoritesCardsRecyclerAdapter extends RecyclerView.Adapter<FavoritesCardsRecyclerAdapter.ViewHolder> {


    private final Activity mActivity;
    private final ArrayList<FavoriteData> favoriteList;

    public FavoritesCardsRecyclerAdapter(Activity activity, ArrayList<FavoriteData> favoriteList){
        this.mActivity = activity;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.favorites_video_card_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            Picasso.with(mActivity).load(AppConstants.YOUTUBE_VIDEO_THUMBNAIL+""+favoriteList.get(position).getVideoId()+""+AppConstants.VIDEO_THUMBNAIL_EXTENSION).into(holder.ivDailyVideo, new Callback() {
                @Override
                public void onSuccess() {
                    holder.ivDailyVideo.setVisibility(View.VISIBLE);
                    holder.ivPlay.setVisibility(View.VISIBLE);
                    holder.pbLoading.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    holder.ivDailyVideo.setVisibility(View.GONE);
                    holder.ivPlay.setVisibility(View.GONE);
                    holder.pbLoading.setVisibility(View.GONE);
                }
            });

            holder.tvDailyVideo.setText(favoriteList.get(position).getVideoName());
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CardView cvFavorites;
        private final ImageView ivDailyVideo, ivPlay;
        private final ProgressBar pbLoading;
        private final TextView tvDailyVideo;

        public ViewHolder(View itemView) {
            super(itemView);
            cvFavorites =     itemView.findViewById(R.id.cvFavorites);
            ivDailyVideo =     itemView.findViewById(R.id.ivDailyVideo);
            ivPlay =     itemView.findViewById(R.id.ivPlay);
            pbLoading =     itemView.findViewById(R.id.pbLoading);
            tvDailyVideo =     itemView.findViewById(R.id.tvDailyVideo);
            cvFavorites.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.cvFavorites:
                    Intent youtubePlayerIntent = new Intent(mActivity, YoutubePlayerActivity.class);
                    youtubePlayerIntent.putExtra(AppConstants.VIDEO_ID, "" + favoriteList.get(getAdapterPosition()).getVideoId());
                    youtubePlayerIntent.putExtra(AppConstants.VIDEO_NAME, "" + favoriteList.get(getAdapterPosition()).getVideoName());
                    mActivity.startActivityForResult(youtubePlayerIntent, 100);
                    break;
            }
        }
    }
}
