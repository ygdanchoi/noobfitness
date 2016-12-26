package com.noobfitness.noobfitness;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dan on 11/23/2016.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {

    private ArrayList<Exercise> exercises;

    public static class ExerciseHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView secondaryTextView;
        public TextView muscleTextView;
        public TextView repCounter;
        public ProgressBar repsProgress;
        public LinearLayout repsLayout;
        public EditText weightValueEditText;
        public EditText weightDescriptionEditText;
        public ImageView drawableImageView;
        public View view;

        public ExerciseHolder(View listItemView) {
            super(listItemView);
            nameTextView = (TextView) listItemView.findViewById(R.id.exercise_name);
            secondaryTextView = (TextView) listItemView.findViewById(R.id.exercise_secondary);
            muscleTextView = (TextView) listItemView.findViewById(R.id.exercise_muscle);
            repCounter = (TextView) listItemView.findViewById(R.id.exercise_reps_counter);
            repsProgress = (ProgressBar) listItemView.findViewById(R.id.exercise_reps_progress);
            repsLayout = (LinearLayout) listItemView.findViewById(R.id.exercise_reps);
            weightValueEditText = (EditText) listItemView.findViewById(R.id.exercise_weight_value);
            weightDescriptionEditText = (EditText) listItemView.findViewById(R.id.exercise_weight_description);
            drawableImageView = (ImageView) listItemView.findViewById(R.id.exercise_drawable);
            view = listItemView;
        }

    }

    public ExerciseAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public ExerciseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.exercise_item, parent, false);
        return new ExerciseHolder(view);
    }

    @Override
    public void onBindViewHolder(ExerciseHolder holder, int position) {
        final Exercise exercise = exercises.get(position);
        if (exercise.getListItemView() == null) {
            holder.nameTextView.setText(exercise.getName());
            holder.secondaryTextView.setText(exercise.getSecondary());
            holder.muscleTextView.setText(exercise.getMuscle());
            holder.repCounter.setText("0/" + exercise.getReps().length);
            holder.repsProgress.setMax(360);
            holder.repsLayout = generateCheckboxes(holder.view, exercise);
            holder.weightValueEditText.setText(exercise.getWeightValue());
            holder.weightValueEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    exercise.setWeightValue(s.toString());
                    exercise.setSecondary();
                    ((TextView) exercise.getListItemView().findViewById(R.id.exercise_secondary)).setText(exercise.getSecondary());
                }
            });
            holder.weightDescriptionEditText.setText(exercise.getWeightDescription());
            holder.drawableImageView.setImageResource(exercise.getDrawable());
            exercise.setListItemView(holder.view);
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    private LinearLayout generateCheckboxes(View listItemView, Exercise exercise) {
//            if (exercise.getRepsLayout() == null) {
        final Context context = listItemView.getContext();
        LinearLayout repsLayout = (LinearLayout) listItemView.findViewById(R.id.exercise_reps);
        int[] reps = exercise.getReps();
        for (int i = 0; i < reps.length; i++) {
            CheckBox rep = new CheckBox(context);
            rep.setId(i);
            rep.setAlpha((float) 0.87);
            rep.setText(reps[i] + "×");
            rep.setTextColor(Color.BLACK);
            rep.setTextSize(16);
            repsLayout.addView(rep);
            rep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // i.e. @id/exercise_item
                    LinearLayout greatGrandparent = (LinearLayout) buttonView.getParent().getParent().getParent();
                    TextView repsCounter = (TextView) greatGrandparent.findViewById(R.id.exercise_reps_counter);
                    ProgressBar repsProgress = (ProgressBar) greatGrandparent.findViewById(R.id.exercise_reps_progress);
                    String[] counts = repsCounter.getText().toString().split("/");
                    GradientDrawable repsCounterCircle = (GradientDrawable) repsCounter.getBackground();
                    ObjectAnimator animation;
                    if (isChecked) {
                        // update @id/reps_counter
                        repsCounter.setText((Integer.parseInt(counts[0]) + 1) + "/" + counts[1]);
                        // update @id/reps_progress
                        animation = ObjectAnimator.ofInt(repsProgress, "progress", Integer.parseInt(counts[0]) * 360 / Integer.parseInt(counts[1]), (Integer.parseInt(counts[0]) + 1) * 360 / Integer.parseInt(counts[1]));
                        animation.setDuration(225);
                        animation.setInterpolator(new DecelerateInterpolator());
                        animation.start();
                        repsProgress.setProgress((Integer.parseInt(counts[0]) + 1) * 360 / Integer.parseInt(counts[1]));
                        // if all checkboxes are checked, make rep counter circle blue
                        if (Integer.parseInt(counts[0]) + 1 == Integer.parseInt(counts[1])) {
                            animation = ObjectAnimator.ofObject(repsCounterCircle, "color", new ArgbEvaluator(), Color.argb(66, 0, 0, 0), context.getColor(R.color.colorAccent));
                            animation.setDuration(225);
                            animation.setInterpolator(new DecelerateInterpolator());
                            animation.start();
                            repsCounterCircle.setColor(context.getColor(R.color.colorAccent));
                        }
                    } else {
                        // update @id/reps_counter
                        repsCounter.setText((Integer.parseInt(counts[0]) - 1) + "/" + counts[1]);
                        // update @id/reps_progress
                        animation = ObjectAnimator.ofInt(repsProgress, "progress", Integer.parseInt(counts[0]) * 360 / Integer.parseInt(counts[1]), (Integer.parseInt(counts[0]) - 1) * 360 / Integer.parseInt(counts[1]));
                        animation.setDuration(225);
                        animation.setInterpolator(new DecelerateInterpolator());
                        animation.start();
                        repsProgress.setProgress((Integer.parseInt(counts[0]) - 1) * 360 / Integer.parseInt(counts[1]));
                        // if all but one checkboxes are checked, make rep counter circle gray
                        if (Integer.parseInt(counts[0]) - 1 == Integer.parseInt(counts[1]) - 1) {
//                            animation = ObjectAnimator.ofObject(repsCounterCircle, "color", new ArgbEvaluator(), getResources().getColor(R.color.colorAccent), Color.argb(66, 0, 0, 0));
//                            animation.setDuration(225);
//                            animation.start();
                            repsCounterCircle.setColor(Color.argb(66, 0, 0, 0));
                        }
                    }
                }
            });
        }
        // workaround for unwanted blue circles after checking all checkboxes
        ((GradientDrawable) listItemView.findViewById(R.id.exercise_reps_counter).getBackground()).setColor(Color.argb(66, 0, 0, 0));
//                exercise.setRepsLayout(repsLayout);
        return repsLayout;
//            } else {
//                return exercise.getRepsLayout();
//            }

    }

}
