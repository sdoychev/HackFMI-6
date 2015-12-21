package com.smd.studio.hackfmi6;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    String command = "";
    Button forwardLeft, forward, forwardRight, backward, backwardLeft, backwardRight, autonomousDrivingOn, autonomousDrivingOff, cameraButton;
    WebView webcamView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
    }

    private void initLayout() {
        forwardLeft = (Button) findViewById(R.id.frontLeftButton);
        forward = (Button) findViewById(R.id.frontButton);
        forwardRight = (Button) findViewById(R.id.frontRightButton);
        backwardLeft = (Button) findViewById(R.id.backLeftButton);
        backward = (Button) findViewById(R.id.backButton);
        backwardRight = (Button) findViewById(R.id.backRightButton);
        autonomousDrivingOn = (Button) findViewById(R.id.autonomousDrivingOnButton);
        autonomousDrivingOff = (Button) findViewById(R.id.autonomousDrivingOffButton);
        cameraButton = (Button) findViewById(R.id.cameraButton);
        webcamView = (WebView) findViewById(R.id.webcamView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PressHoldButtonListener pressHoldButtonListener = new PressHoldButtonListener();
        PressButtonListener pressButtonListener = new PressButtonListener();
        forwardLeft.setOnTouchListener(pressHoldButtonListener);
        forward.setOnTouchListener(pressHoldButtonListener);
        forwardRight.setOnTouchListener(pressHoldButtonListener);
        backwardLeft.setOnTouchListener(pressHoldButtonListener);
        backward.setOnTouchListener(pressHoldButtonListener);
        backwardRight.setOnTouchListener(pressHoldButtonListener);
        autonomousDrivingOn.setOnClickListener(pressButtonListener);
        autonomousDrivingOff.setOnClickListener(pressButtonListener);
        cameraButton.setOnClickListener(pressButtonListener);
        webcamView.getSettings().setJavaScriptEnabled(true);
        webcamView.setWebViewClient(new WebViewClient());
        webcamView.loadUrl(ConnectionConfig.getInstance().getHostAddress() + Constants.WEBCAM_PORT_AND_ACTION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMovingCar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void moveCar(View v) {
        RCTask rcTask = new RCTask();
        if (v.getId() == R.id.frontButton) {
            command = Constants.FORWARD;
        } else if (v.getId() == R.id.frontLeftButton) {
            command = Constants.FORWARD_LEFT;
        } else if (v.getId() == R.id.frontRightButton) {
            command = Constants.FORWARD_RIGHT;
        } else if (v.getId() == R.id.backButton) {
            command = Constants.BACKWARD;
        } else if (v.getId() == R.id.backLeftButton) {
            command = Constants.BACKWARD_LEFT;
        } else if (v.getId() == R.id.backRightButton) {
            command = Constants.BACKWARD_RIGHT;
        } else if (v.getId() == R.id.autonomousDrivingOnButton) {
            command = Constants.AUTONOMOUS_DRIVING_ON;
        } else if (v.getId() == R.id.autonomousDrivingOffButton) {
            command = Constants.AUTONOMOUS_DRIVING_OFF;
        } else if (v.getId() == R.id.cameraButton) {
            command = Constants.TURN_ON_CAMERA;
        }
        rcTask.execute(command);
    }

    private void stopMovingCar() {
        RCTask rcTask = new RCTask();
        rcTask.execute(Constants.STOP);
    }

    public class PressButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            moveCar(v);
        }
    }

    public class PressHoldButtonListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setAlpha(0.2f);
                    moveCar(v);
                    break;
                case MotionEvent.ACTION_UP:
                    stopMovingCar();
                    v.setAlpha(0.0f);
                    break;
            }
            return true;
        }
    }
}