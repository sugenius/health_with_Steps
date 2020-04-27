package com.example.step;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener { // implement 센서 값 받음

    public SensorManager sensorManager;
    public Sensor stepCountSensor;
    TextView stepsTextView,kcalTextView;
    ProgressBar stepsProgressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepsTextView = (TextView) findViewById(R.id.stepsTextView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepsProgressBar = (ProgressBar) findViewById(R.id.stepsProgressBar); //*
        kcalTextView = (TextView) findViewById(R.id.kcalTextView);


        if (stepCountSensor == null) {
            Toast.makeText(this, "No step Detect Sensor", Toast.LENGTH_SHORT).show();

        }
    }

    //센서는 일시중지, 화면 종료시에도 계속 작동, 전력소모함.
    // 이를 제어 하기위해 inPause()에서 중지, onResume()에서 활성화 하도록 코드추가
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener((SensorEventListener) this, stepCountSensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) { //센서 동작 감지시 이벤트 발생하여 이 함수에 값 전달
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            //stepsTextView.setText(String.valueOf(event.values[0]));
            stepsTextView.setText(String.valueOf((int)event.values[0]));

            int steps=Integer.valueOf((int) event.values[0]);
            stepsProgressBar.setProgress(steps);

            kcalTextView.setText("예상"+steps+"kcal 소모");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
