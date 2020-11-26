package com.junsoft.perofu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import at.grabner.circleprogress.AnimationState;
import at.grabner.circleprogress.AnimationStateChangedListener;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

public class UpdateActivity extends Activity {

    CircleProgressView mCircleView;
    Boolean mShowUnit = true;
    CircularProgressBar circularProgressBar;
    com.progress.progressview.ProgressView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

     //   progress = (com.progress.progressview.ProgressView)findViewById(R.id.progressView);

       // int[] colorList = new int[]{Color.GREEN,  Color.RED};

       // progress.applyGradient(colorList);

    }
    // let colors = [UIColor(hex: "fad336"), UIColor(hex: "ed1c24")]
    //
}