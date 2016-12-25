package com.noobfitness.noobfitness;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Dan on 12/25/2016.
 */

public class ExerciseItemHolder extends RecyclerView.ViewHolder {

    private ExerciseItem exerciseItem;
    TextView nameTextView;
    TextView secondaryTextView;
    TextView muscleTextView;
    int[] reps;
    TextView repCounter;
    ProgressBar repsProgress;
    LinearLayout repsLayout;
    EditText weightValueEditText;
    EditText weightDescriptionEditText;
    ImageView drawableImageView;
    View thisView;

    public ExerciseItemHolder(View listItemView) {
        super(listItemView);
        final View finalListItemView = listItemView;
        nameTextView = (TextView) listItemView.findViewById(R.id.exercise_name);
        secondaryTextView = (TextView) listItemView.findViewById(R.id.exercise_secondary);
        muscleTextView = (TextView) listItemView.findViewById(R.id.exercise_muscle);
        repCounter = (TextView) listItemView.findViewById(R.id.exercise_reps_counter);
        repsProgress = (ProgressBar) listItemView.findViewById(R.id.exercise_reps_progress);
        repsLayout = (LinearLayout) listItemView.findViewById(R.id.exercise_reps);
        weightValueEditText = (EditText) listItemView.findViewById(R.id.exercise_weight_value);
        weightValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                exerciseItem.setWeightValue(s.toString());
                exerciseItem.setSecondary();
                ((TextView) finalListItemView.findViewById(R.id.exercise_secondary)).setText(exerciseItem.getSecondary());
            }
        });
        weightDescriptionEditText = (EditText) listItemView.findViewById(R.id.exercise_weight_description);
        drawableImageView = (ImageView) listItemView.findViewById(R.id.exercise_drawable);
        thisView = listItemView;
    }

    public void bindData(ExerciseItem exerciseItem) {
        this.exerciseItem = exerciseItem;
        // if (exerciseItem.getListItemView() == null) {
        nameTextView.setText(exerciseItem.getName());
        secondaryTextView.setText(exerciseItem.getSecondary());
        muscleTextView.setText(exerciseItem.getMuscle());
        reps = exerciseItem.getReps();
        repCounter.setText("0/" + reps.length);
        repsProgress.setMax(360);
        repsLayout = generateCheckboxes(thisView, exerciseItem);
        weightValueEditText.setText(exerciseItem.getWeightValue());
        weightDescriptionEditText.setText(exerciseItem.getWeightDescription());
        drawableImageView.setImageResource(exerciseItem.getDrawable());
        exerciseItem.setListItemView(thisView);
        // }
    }

    private LinearLayout generateCheckboxes(View listItemView, ExerciseItem exerciseItem) {
        final Context context = listItemView.getContext();
        LinearLayout repsLayout = (LinearLayout) listItemView.findViewById(R.id.exercise_reps);
        int[] reps = exerciseItem.getReps();
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
        return repsLayout;
    }
}