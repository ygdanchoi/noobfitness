package com.noobfitness.noobfitness.workout;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noobfitness.noobfitness.R;

import java.util.ArrayList;

public class LegacyWorkoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_activity);

        Bundle bundle = getIntent().getExtras();
        ArrayList<LegacyExercise> exercises = bundle.getParcelableArrayList("exercises");

        LegacyExerciseAdapter itemsAdapter = new LegacyExerciseAdapter(exercises);
        RecyclerView recyclerView = findViewById(R.id.workoutExercisesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemsAdapter);

    }

    public void headClicked(View head) {
        // i.e. the ListView
        RecyclerView grandParent = (RecyclerView) head.getParent().getParent().getParent();
        // i.e. @id/workout_exercise_list_item
        LinearLayout parent = (LinearLayout) head.getParent();
        LinearLayout exerciseBody = (LinearLayout) parent.findViewById(R.id.exercise_body);
        ImageView expandArrow = (ImageView) head.findViewById(R.id.expand_arrow);
        TextView secondary = (TextView) head.findViewById(R.id.exercise_secondary);
        TextView muscle = (TextView) head.findViewById(R.id.exercise_muscle);
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(225);

        if (exerciseBody.getVisibility() == View.GONE) {
            // expand @id/exercise_body with animation
            exerciseBody.setVisibility(View.INVISIBLE);
            parent.setLayoutTransition(layoutTransition);
            exerciseBody.setVisibility(View.VISIBLE);
            expandArrow.setImageResource(R.drawable.collapse_list);
            // switch secondary TextView to muscle TextView
            secondary.setVisibility(View.GONE);
            muscle.setVisibility(View.VISIBLE);
            // scroll to expanded body
//            parent.requestChildFocus(exerciseBody, exerciseBody);
        } else {
            // collapse @id/exercise_body without animation
            parent.setLayoutTransition(null);
            exerciseBody.setVisibility(View.GONE);
            expandArrow.setImageResource(R.drawable.expand_list);
            // switch muscle TextView to secondary TextView
            secondary.setVisibility(View.VISIBLE);
            muscle.setVisibility(View.GONE);
        }

    }
}
