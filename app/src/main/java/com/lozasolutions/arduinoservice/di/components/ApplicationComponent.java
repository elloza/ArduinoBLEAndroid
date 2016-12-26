package com.lozasolutions.arduinoservice.di.components;

import com.lozasolutions.arduinoservice.BaseApplication;
import com.lozasolutions.arduinoservice.Monitor;
import com.lozasolutions.arduinoservice.di.modules.ApplicationModule;
import com.lozasolutions.arduinoservice.di.modules.BLEModule;
import com.lozasolutions.arduinoservice.di.modules.SocketIOModule;
import com.lozasolutions.arduinoservice.services.ArduinoService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Loza on 25/12/2016.
 */

@Singleton
@Component( modules = {ApplicationModule.class, BLEModule.class, SocketIOModule.class})
public interface ApplicationComponent {

    void inject(BaseApplication baseApplication);
    void inject(Monitor activity);
    void inject(ArduinoService bleService);

}