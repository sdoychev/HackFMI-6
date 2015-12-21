package com.smd.studio.hackfmi6;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String command = "";
    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private Button frontLeftButton, frontButton, frontRightButton;
    private Button backLeftButton, backButton, backRightButton;
    private TextView mClockView;

    //Communication with device
    Node node; // the connected device to send the message to
    GoogleApiClient googleApiClient;
    private boolean resolvingError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();
        initViews();
        setListeners();

        //Connect the GoogleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!resolvingError) {
            googleApiClient.connect();
        }
    }

    // Resolve the node = the connected device to send the message to
    private void resolveNode() {
        Wearable.NodeApi.getConnectedNodes(googleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                for (Node currentNode : nodes.getNodes()) {
                    node = currentNode;
                }
            }
        });
    }

    private void initViews() {
        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        frontLeftButton = (Button) findViewById(R.id.frontLeftButton);
        frontButton = (Button) findViewById(R.id.frontButton);
        frontRightButton = (Button) findViewById(R.id.frontRightButton);
        backLeftButton = (Button) findViewById(R.id.backLeftButton);
        backButton = (Button) findViewById(R.id.backButton);
        backRightButton = (Button) findViewById(R.id.backRightButton);
        mClockView = (TextView) findViewById(R.id.clock);
    }

    @Override
    public void onConnected(Bundle bundle) {
        resolveNode();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class PressHoldButtonListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    moveCar(v);
                    break;
                case MotionEvent.ACTION_UP:
                    stopMovingCar();
                    break;
            }
            return true;
        }
    }

    public void moveCar(View v) {
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
        }
        sendMessage(command);
    }

    private void stopMovingCar() {
        command = Constants.STOP;
        sendMessage(command);
    }

    // Send message to mobile handheld
    private void sendMessage(String key) {
        if (node != null && googleApiClient != null && googleApiClient.isConnected()) {
            Log.d("WEAR LOG", "" + googleApiClient.isConnected());
            Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), key, null).setResultCallback(
                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            if (!sendMessageResult.getStatus().isSuccess()) {
                                Log.e("WEAR LOG", "Failed to send message with status code: " + sendMessageResult.getStatus().getStatusCode());
                            }
                        }
                    }
            );
        }
    }

    private void setListeners() {
        PressHoldButtonListener pressHoldButtonListener = new PressHoldButtonListener();
        frontLeftButton.setOnTouchListener(pressHoldButtonListener);
        frontButton.setOnTouchListener(pressHoldButtonListener);
        frontRightButton.setOnTouchListener(pressHoldButtonListener);
        backLeftButton.setOnTouchListener(pressHoldButtonListener);
        backButton.setOnTouchListener(pressHoldButtonListener);
        backRightButton.setOnTouchListener(pressHoldButtonListener);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            frontLeftButton.setTextColor(getResources().getColor(android.R.color.white));
            frontButton.setTextColor(getResources().getColor(android.R.color.white));
            frontRightButton.setTextColor(getResources().getColor(android.R.color.white));
            backLeftButton.setTextColor(getResources().getColor(android.R.color.white));
            backButton.setTextColor(getResources().getColor(android.R.color.white));
            backRightButton.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            frontLeftButton.setTextColor(getResources().getColor(android.R.color.black));
            frontButton.setTextColor(getResources().getColor(android.R.color.black));
            frontRightButton.setTextColor(getResources().getColor(android.R.color.black));
            backLeftButton.setTextColor(getResources().getColor(android.R.color.black));
            backButton.setTextColor(getResources().getColor(android.R.color.black));
            backRightButton.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    }
}
