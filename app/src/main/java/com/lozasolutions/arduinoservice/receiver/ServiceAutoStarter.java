package com.lozasolutions.arduinoservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lozasolutions.arduinoservice.services.ArduinoService;

/**
 * Created by Loza on 25/12/2016.
 */

public class ServiceAutoStarter extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ArduinoService.class));
    }
}
