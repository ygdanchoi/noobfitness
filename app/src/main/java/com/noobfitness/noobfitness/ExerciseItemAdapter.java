package com.noobfitness.noobfitness;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
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

public class ExerciseItemAdapter extends ArrayAdapter<ExerciseItem> {

    public ExerciseItemAdapter(Activity context, ArrayList<ExerciseItem> exerciseItems) {
        super(context, 0, exerciseItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.exercise_item, parent, false);
        }

        final ExerciseItem exerciseItem = getItem(position);
        if (exerciseItem.getListItemView() == null) {
            // update exercise name
            TextView nameTextView = (TextView) listItemView.findViewById(R.id.exercise_name);
            nameTextView.setText(exerciseItem.getName());

            // update exercise secondary text
            TextView secondaryTextView = (TextView) listItemView.findViewById(R.id.exercise_secondary);
            secondaryTextView.setText(exerciseItem.getSecondary());

            // update exercise muscle group
            TextView muscleTextView = (TextView) listItemView.findViewById(R.id.exercise_muscle);
            muscleTextView.setText(exerciseItem.getMuscle());

            // update rep counter
            int[] reps = exerciseItem.getReps();
            TextView repCounter = (TextView) listItemView.findViewById(R.id.exercise_reps_counter);
            ProgressBar repsProgress = (ProgressBar) listItemView.findViewById(R.id.exercise_reps_progress);
            repCounter.setText("0/" + reps.length);
            repsProgress.setMax(360);

            // generate formatted checkboxes for reps
            LinearLayout repsLayout = (LinearLayout) listItemView.findViewById(R.id.exercise_reps);
            repsLayout = generateCheckboxes(listItemView, exerciseItem);

            // update exercise weight value
            EditText weightValueEditText = (EditText) listItemView.findViewById(R.id.exercise_weight_value);
            weightValueEditText.setText(exerciseItem.getWeightValue());
            // add listener to update secondary text if weight value is changed
            weightValueEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    exerciseItem.setWeightValue(s.toString());
                    exerciseItem.setSecondary();
                    ((TextView) exerciseItem.getListItemView().findViewById(R.id.exercise_secondary)).setText(exerciseItem.getSecondary());
                }
            });

            // update exercise weight description
            EditText weightDescriptionEditText = (EditText) listItemView.findViewById(R.id.exercise_weight_description);
            weightDescriptionEditText.setText(exerciseItem.getWeightDescription());

            // update exercise drawable src
            ImageView drawableImageView = (ImageView) listItemView.findViewById(R.id.exercise_drawable);
            drawableImageView.setImageResource(exerciseItem.getDrawable());

            // pass generated LinearLayout to exerciseItem
            exerciseItem.setListItemView(listItemView);
            return listItemView;
        } else {
//            return exerciseItem.getListItemView();
        }
         return listItemView;

    }

    private LinearLayout generateCheckboxes(View listItemView, ExerciseItem exerciseItem) {
        LinearLayout repsLayout = (LinearLayout) listItemView.findViewById(R.id.exercise_reps);
        int[] reps = exerciseItem.getReps();
        for (int i = 0; i < reps.length; i++) {
            CheckBox rep = new CheckBox(getContext());
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
                            animation = ObjectAnimator.ofObject(repsCounterCircle, "color", new ArgbEvaluator(), Color.argb(66, 0, 0, 0), getContext().getColor(R.color.colorAccent));
                            animation.setDuration(225);
                            animation.setInterpolator(new DecelerateInterpolator());
                            animation.start();
                            repsCounterCircle.setColor(getContext().getColor(R.color.colorAccent));
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
