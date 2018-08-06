package com.udacity.getfit.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.FitnessData;

import java.util.List;

public class FitnessCardsRecyclerAdapter extends RecyclerView.Adapter<FitnessCardsRecyclerAdapter.ViewHolder> {


    private final Context context;
    private final FitnessData fitnessData;

    public FitnessCardsRecyclerAdapter(Context context, FitnessData fitnessData){
        this.context = context;
        this.fitnessData = fitnessData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fitness_card_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tvWorkoutType.setText(""+fitnessData.workoutInformation.get(position).type);
            Picasso.with(context).load(""+fitnessData.workoutInformation.get(position).imageUrl).into(holder.ivWorkoutType, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
    }

    @Override
    public int getItemCount() {
        return fitnessData.workoutInformation.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvWorkoutType;
        private final ImageView ivWorkoutType;
        private final CardView cvFitness;

        public ViewHolder(View itemView) {
            super(itemView);
            cvFitness =     itemView.findViewById(R.id.cvFitness);
            tvWorkoutType = itemView.findViewById(R.id.tvWorkoutType);
            ivWorkoutType = itemView.findViewById(R.id.ivWorkoutType);

            cvFitness.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.cvFitness:

                    break;
            }
        }
    }
}
