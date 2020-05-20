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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener { // implement 센서 값 받음

    public SensorManager sensorManager;
    public Sensor stepCountSensor;
    TextView stepsTextView,kcalTextView; //걸음 수
    //ProgressBar stepsProgressBar;
    private int steps;
    private int stepscounter; //리스너 등록 후

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepsTextView = (TextView) findViewById(R.id.stepsTextView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        //stepsProgressBar = (ProgressBar) findViewById(R.id.stepsProgressBar); //*
        kcalTextView = (TextView) findViewById(R.id.kcalTextView);

        findViewById(R.id.resetbutton).setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        steps=0;
                        stepscounter=0;
                        stepsTextView.setText(Integer.toString(steps));
                    }
                }
        );

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
        /*
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            //stepsTextView.setText(String.valueOf(event.values[0]));
            stepsTextView.setText(String.valueOf((int)event.values[0]));

            steps=Integer.valueOf((int) event.values[0]);
            //stepsProgressBar.setProgress(steps);

            kcalTextView.setText("예상"+steps+"kcal 소모");
        }

         */
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){

            //stepcountsenersor는 앱이 꺼지더라도 초기화 되지않는다. 그러므로 우리는 초기값을 가지고 있어야한다.
            if (stepscounter < 1) {
                // initial value
                stepscounter = (int) event.values[0];
            }
            //리셋 안된 값 + 현재값 - 리셋 안된 값
            steps = (int) event.values[0] - stepscounter;
            stepsTextView.setText(Integer.toString(steps));
            Log.i("log: ", "New step detected by STEP_COUNTER sensor. Total step count: " + steps );

            kcalTextView.setText("예상"+steps+"kcal 소모");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
