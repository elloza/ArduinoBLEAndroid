package com.lozasolutions.arduinoservice;

import android.app.Application;
import android.content.Intent;

import com.lozasolutions.arduinoservice.di.components.ApplicationComponent;
import com.lozasolutions.arduinoservice.di.components.DaggerApplicationComponent;
import com.lozasolutions.arduinoservice.di.modules.ApplicationModule;
import com.lozasolutions.arduinoservice.services.ArduinoService;


/**
 * Created by Loza on 25/12/2016.
 */

public class BaseApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(getApplicationContext())).build();
        startService(new Intent(getApplicationContext(), ArduinoService.class));

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

}
