package com.noobfitness.noobfitness;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
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

public class ExerciseItemAdapter extends RecyclerView.Adapter<ExerciseItemHolder> {

    private ArrayList<ExerciseItem> exerciseItems;

    public ExerciseItemAdapter(ArrayList<ExerciseItem> exerciseItems) {
        this.exerciseItems = exerciseItems;
    }

    @Override
    public ExerciseItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.exercise_item, parent, false);
        return new ExerciseItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ExerciseItemHolder holder, int position) {
        ExerciseItem exerciseItem = exerciseItems.get(position);
        if (exerciseItem.getListItemView() == null) {
            holder.bindData(exerciseItem);
        }
    }

    @Override
    public int getItemCount() {
        return exerciseItems.size();
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View listItemView = convertView;
//        if (listItemView == null) {
//            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.exercise_item, parent, false);
//        }
//
//        final ExerciseItem exerciseItem = getItem(position);
//        if (exerciseItem.getListItemView() == null) {
//            // update exercise name
//            TextView nameTextView = (TextView) listItemView.findViewById(R.id.exercise_name);
//            nameTextView.setText(exerciseItem.getName());
//
//            // update exercise secondary text
//            TextView secondaryTextView = (TextView) listItemView.findViewById(R.id.exercise_secondary);
//            secondaryTextView.setText(exerciseItem.getSecondary());
//
//            // update exercise muscle group
//            TextView muscleTextView = (TextView) listItemView.findViewById(R.id.exercise_muscle);
//            muscleTextView.setText(exerciseItem.getMuscle());
//
//            // update rep counter
//            int[] reps = exerciseItem.getReps();
//            TextView repCounter = (TextView) listItemView.findViewById(R.id.exercise_reps_counter);
//            ProgressBar repsProgress = (ProgressBar) listItemView.findViewById(R.id.exercise_reps_progress);
//            repCounter.setText("0/" + reps.length);
//            repsProgress.setMax(360);
//
//            // generate formatted checkboxes for reps
//            LinearLayout repsLayout = (LinearLayout) listItemView.findViewById(R.id.exercise_reps);
//            repsLayout = generateCheckboxes(listItemView, exerciseItem);
//
//            // update exercise weight value
//            EditText weightValueEditText = (EditText) listItemView.findViewById(R.id.exercise_weight_value);
//            weightValueEditText.setText(exerciseItem.getWeightValue());
//            // add listener to update secondary text if weight value is changed
//            weightValueEditText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    exerciseItem.setWeightValue(s.toString());
//                    exerciseItem.setSecondary();
//                    ((TextView) exerciseItem.getListItemView().findViewById(R.id.exercise_secondary)).setText(exerciseItem.getSecondary());
//                }
//            });
//
//            // update exercise weight description
//            EditText weightDescriptionEditText = (EditText) listItemView.findViewById(R.id.exercise_weight_description);
//            weightDescriptionEditText.setText(exerciseItem.getWeightDescription());
//
//            // update exercise drawable src
//            ImageView drawableImageView = (ImageView) listItemView.findViewById(R.id.exercise_drawable);
//            drawableImageView.setImageResource(exerciseItem.getDrawable());
//
//            // pass generated LinearLayout to exerciseItem
//            exerciseItem.setListItemView(listItemView);
//            return listItemView;
//        } else {
////            return exerciseItem.getListItemView();
//        }
//         return listItemView;
//
//    }
}
