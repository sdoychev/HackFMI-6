package com.smd.studio.hackfmi6;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Stefan on 20.12.2015 г..
 */
public class WearListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        RCTask rcTask = new RCTask();
        rcTask.execute(messageEvent.getPath());
    }
}
