package com.lozasolutions.arduinoservice.di.modules;

import android.content.Context;

import com.lozasolutions.arduinoservice.arduino.ArduinoManager;
import com.polidea.rxandroidble.RxBleClient;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Loza on 25/12/2016.
 */

@Module
public final class BLEModule {

    @Provides
    RxBleClient provideBleClient(Context context) {
        return RxBleClient.create(context);
    }

    @Provides
    ArduinoManager provideArduinoManager(Context context, RxBleClient rxClient) {

        return new ArduinoManager(context, rxClient);
    }
}
