package com.noobfitness.noobfitness;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class RoutineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        LinearLayout routineLinearLayout = (LinearLayout) findViewById(R.id.activity_routine);

        Button dayOne = new Button(this);
        dayOne.setText("Chest + Triceps");
        dayOne.setGravity(Gravity.LEFT);
        dayOne.setPadding(96, 96, 96, 96);
        dayOne.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        dayOne.setTextSize(24);
        dayOne.setAllCaps(false);
        dayOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DayActivity.class);
                Bundle bundle = new Bundle();
                ArrayList<ExerciseItem> exerciseItems = new ArrayList<ExerciseItem>();
                exerciseItems.add(new ExerciseItem("Barbell Bench Press", "Chest", new int[] {10, 8, 8, 6}, "55-60", "lb each side\nw/ barbell", R.drawable.barbell_bench_press));
                exerciseItems.add(new ExerciseItem("Incline Bench Press", "Chest", new int[] {8, 8, 6}, "30-35", "lb each side\nw/ barbell", R.drawable.incline_bench_press));
                exerciseItems.add(new ExerciseItem("Decline Dumbbell Bench Press", "Chest", new int[] {8, 8, 6}, "45-50", "lb dumbbells", R.drawable.decline_dumbbell_bench_press));
                exerciseItems.add(new ExerciseItem("Dumbbell Flys", "Chest", new int[] {10, 10}, "30-35", "lb dumbbells", R.drawable.dumbbell_flys));
                exerciseItems.add(new ExerciseItem("Dumbbell Pullover", "Chest", new int[] {8, 8}, "15-20", "lb dumbbell", R.drawable.dumbbell_pullover));
                exerciseItems.add(new ExerciseItem("Cable Tricep Extension", "Triceps", new int[] {10, 8, 8, 6}, "110-120", "lb setting", R.drawable.cable_triceps_extension));
                exerciseItems.add(new ExerciseItem("Tricep Dips", "Triceps", new int[] {10, 10, 10}, "0-15", "lb dumbbell\nw/ feet", R.drawable.tricep_dips));
                exerciseItems.add(new ExerciseItem("Bench Dips", "Triceps", new int[] {8, 8, 8}, "--", "N/A", R.drawable.bench_dips));
                bundle.putParcelableArrayList("exerciseItems", exerciseItems);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        routineLinearLayout.addView(dayOne);

        Button dayTwo = new Button(this);
        dayTwo.setText("Back + Biceps");
        dayTwo.setGravity(Gravity.LEFT);
        dayTwo.setPadding(96, 96, 96, 96);
        dayTwo.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        dayTwo.setTextSize(24);
        dayTwo.setAllCaps(false);
        dayTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DayActivity.class);
                Bundle bundle = new Bundle();
                ArrayList<ExerciseItem> exerciseItems = new ArrayList<ExerciseItem>();
                exerciseItems.add(new ExerciseItem("Chin Up", "Lats", new int[] {8, 8}, "0-15", "lb dumbbell\nw/ feet", R.drawable.chin_up));
                exerciseItems.add(new ExerciseItem("One Arm Dumbbell Row", "Middle Back", new int[] {8, 8, 8}, "40-45", "lb dumbbell", R.drawable.one_arm_dumbbell_row));
                exerciseItems.add(new ExerciseItem("Seated High Cable Row", "Middle Back", new int[] {8, 8}, "90-100", "lb setting", R.drawable.seated_high_cable_row));
                exerciseItems.add(new ExerciseItem("Machine Row", "Middle Back", new int[] {8, 8}, "80-85", "lb setting", R.drawable.machine_row));
                exerciseItems.add(new ExerciseItem("Lat Pull Down", "Lats", new int[] {10, 10, 8}, "100-110", "lb setting", R.drawable.lat_pull_down));
                exerciseItems.add(new ExerciseItem("Standing Barbell Curl", "Biceps", new int[] {8, 8, 6}, "50-55", "lb fixed bar", R.drawable.standing_barbell_curl));
                exerciseItems.add(new ExerciseItem("Bicep Machine Curl", "Biceps", new int[] {8, 8, 6}, "45-50", "lb setting", R.drawable.bicep_machine_curl));
                exerciseItems.add(new ExerciseItem("Incline Dumbbell Curl", "Biceps", new int[] {14, 12}, "15-20", "lb dumbbell", R.drawable.incline_dumbbell_curl));
                exerciseItems.add(new ExerciseItem("Concentration Curl", "Biceps", new int[] {10, 10}, "10-15", "lb dumbbell", R.drawable.concentration_curl));
                bundle.putParcelableArrayList("exerciseItems", exerciseItems);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        routineLinearLayout.addView(dayTwo);

        Button dayThree = new Button(this);
        dayThree.setText("Shoulders + Forearms");
        dayThree.setGravity(Gravity.LEFT);
        dayThree.setPadding(96, 96, 96, 96);
        dayThree.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        dayThree.setTextSize(24);
        dayThree.setAllCaps(false);
        dayThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DayActivity.class);
                Bundle bundle = new Bundle();
                ArrayList<ExerciseItem> exerciseItems = new ArrayList<ExerciseItem>();
                exerciseItems.add(new ExerciseItem("Machine Shoulder Press", "Shoulders", new int[] {10, 10, 10}, "100-115", "lb setting", R.drawable.machine_shoulder_press));
                exerciseItems.add(new ExerciseItem("Dumbbell Reverse Fly", "Shoulders", new int[] {10, 10, 8}, "10-15", "lb dumbbells", R.drawable.dumbbell_reverse_fly));
                exerciseItems.add(new ExerciseItem("Military Press", "Shoulders", new int[] {10, 10, 10, 10}, "55-60", "lb fixed bar", R.drawable.military_press));
                exerciseItems.add(new ExerciseItem("Dumbbell Lateral Raise", "Shoulders", new int[] {10, 10}, "15-20", "lb dumbbells", R.drawable.dumbbell_lateral_raise));
                exerciseItems.add(new ExerciseItem("Dumbbell Shrugs", "Traps", new int[] {10, 10}, "50-55", "lb dumbbells", R.drawable.dumbbell_shrugs));
                exerciseItems.add(new ExerciseItem("Barbell Upright Row", "Traps", new int[] {10, 10}, "55", "lb fixed bar", R.drawable.barbell_upright_row));
                exerciseItems.add(new ExerciseItem("Standing Wrist Curl", "Forearms", new int[] {10, 10, 10, 10}, "20", "lb fixed bar", R.drawable.standing_wrist_curl));
                exerciseItems.add(new ExerciseItem("Barbell Wrist Curl", "Forearms", new int[] {10, 10, 10, 10}, "20", "lb fixed bar", R.drawable.barbell_wrist_curl));
                bundle.putParcelableArrayList("exerciseItems", exerciseItems);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        routineLinearLayout.addView(dayThree);

        Button dayFour = new Button(this);
        dayFour.setText("Legs");
        dayFour.setGravity(Gravity.LEFT);
        dayFour.setPadding(96, 96, 96, 96);
        dayFour.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        dayFour.setTextSize(24);
        dayFour.setAllCaps(false);
        dayFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DayActivity.class);
                Bundle bundle = new Bundle();
                ArrayList<ExerciseItem> exerciseItems = new ArrayList<ExerciseItem>();
                exerciseItems.add(new ExerciseItem("Dumbbell Squat", "Quads", new int[] {10, 8, 8, 6, 4}, "40-55", "lb dumbbells", R.drawable.dumbbell_squat));
                exerciseItems.add(new ExerciseItem("Leg Extension", "Quads", new int[] {12, 12, 12}, "30-40", "lb setting", R.drawable.leg_extension));
                exerciseItems.add(new ExerciseItem("Leg Curl", "Hamstrings", new int[] {12, 12, 12}, "40-50", "lb setting", R.drawable.leg_curl));
                exerciseItems.add(new ExerciseItem("Standing Calf Raise", "Calves", new int[] {12, 12, 12, 12}, "35-45", "lb fixed bar", R.drawable.standing_calf_raise));
                exerciseItems.add(new ExerciseItem("Seated Calf Raise", "Calves", new int[] {12, 12}, "90-100", "lb each side\nw/ machine", R.drawable.seated_calf_raise));
                bundle.putParcelableArrayList("exerciseItems", exerciseItems);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        routineLinearLayout.addView(dayFour);

    }
}
