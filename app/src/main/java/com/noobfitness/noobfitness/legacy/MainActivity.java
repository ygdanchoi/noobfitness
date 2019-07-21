package com.noobfitness.noobfitness.legacy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noobfitness.noobfitness.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legacy_activity_main);
        LinearLayout mainLinearLayout = findViewById(R.id.activity_main);

        Button fourDayDefault = new Button(this);
        fourDayDefault.setText("4-day default routine");
        fourDayDefault.setGravity(Gravity.LEFT);
        fourDayDefault.setPadding(96, 96, 96, 96);
        fourDayDefault.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        fourDayDefault.setTextSize(24);
        fourDayDefault.setAllCaps(false);
        fourDayDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RoutinesActivity.class);
                startActivity(i);
            }
        });
        mainLinearLayout.addView(fourDayDefault);

        TextView imageCredit = new TextView(this);
        imageCredit.setText("Image source: Everkinetic.com (CC BY-SA 3.0)");
        mainLinearLayout.addView(imageCredit);

    }

}
