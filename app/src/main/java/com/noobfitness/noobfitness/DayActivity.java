package com.noobfitness.noobfitness;

import android.animation.LayoutTransition;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Bundle bundle = getIntent().getExtras();
        ArrayList<ExerciseItem> exerciseItems = bundle.getParcelableArrayList("exerciseItems");

        ExerciseItemAdapter itemsAdapter = new ExerciseItemAdapter(this, exerciseItems);
        ListView listView = (ListView) findViewById(R.id.workout_layout);
        listView.setAdapter(itemsAdapter);

    }

    public void headClicked(View head) {
        // i.e. the ListView
        ListView grandParent = (ListView) head.getParent().getParent().getParent();
        // grandParent.setItemsCanFocus(true);
        // i.e. @id/exercise_item
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
