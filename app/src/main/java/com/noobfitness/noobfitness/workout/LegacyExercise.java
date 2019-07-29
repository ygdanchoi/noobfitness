package com.noobfitness.noobfitness.workout;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Dan on 11/17/2016.
 */

public class LegacyExercise implements Parcelable {

    private String name;
    private String secondary;
    private String muscle;
    private int[] reps;
    private LinearLayout repsLayout = null;
    private String weightValue;
    private String weightDescription;
    private int drawable;

    private View listItemView = null;

    public LegacyExercise(String name, String muscle, int[] reps,
                          String weightValue, String weightDescription, int drawable) {
        this.name = name;
        this.muscle = muscle;
        this.reps = reps;
        this.weightValue = weightValue;
        this.weightDescription = weightDescription;
        this.drawable = drawable;
        setSecondary();
    }

    public LegacyExercise(Parcel in) {
        this.name = in.readString();
        this.muscle = in.readString();
        reps = new int[in.readInt()];
        in.readIntArray(reps);
        this.weightValue = in.readString();
        this.weightDescription = in.readString();
        this.drawable = in.readInt();
        setSecondary();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(muscle);
        dest.writeInt(reps.length);
        dest.writeIntArray(reps);
        dest.writeString(weightValue);
        dest.writeString(weightDescription);
        dest.writeInt(drawable);
    }

    public static final Parcelable.Creator<LegacyExercise> CREATOR = new Parcelable.Creator<LegacyExercise>() {
        public LegacyExercise createFromParcel(Parcel in) {
            return new LegacyExercise(in);
        }
        public LegacyExercise[] newArray(int size) {
            return new LegacyExercise[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary() {
        if (minReps() == maxReps()) {
            secondary = reps.length + " sets, " + minReps() + " reps, " + weightValue + " lb";
        } else {
            secondary = reps.length + " sets, " + minReps() + "-" + maxReps() + " reps, " + weightValue + " lb";
        }
        return;
    }

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public int[] getReps() {
        return reps;
    }

    public void setReps(int[] reps) {
        this.reps = reps;
    }

    public LinearLayout getRepsLayout() {
        return repsLayout;
    }

    public void setRepsLayout(LinearLayout repsLayout) {
        this.repsLayout = repsLayout;
    }

    public String getWeightValue() {
        return weightValue;
    }

    public void setWeightValue(String weightValue) {
        this.weightValue = weightValue;
    }

    public String getWeightDescription() {
        return weightDescription;
    }

    public void setWeightDescription(String weightDescription) {
        this.weightDescription = weightDescription;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public View getListItemView() {
        return listItemView;
    }

    public void setListItemView(View listItemView) {
        this.listItemView = listItemView;
    }


    private int minReps() {
        int min = reps[0];
        for (int i = 0; i < reps.length; i++) {
            if (reps[i] < min) {
                min = reps[i];
            }
        }
        return min;
    }

    private int maxReps() {
        int max = reps[0];
        for (int i = 0; i < reps.length; i++) {
            if (reps[i] > max) {
                max = reps[i];
            }
        }
        return max;
    }

}
