package com.lozasolutions.arduinoservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lozasolutions.arduinoservice.di.components.ApplicationComponent;

public class Monitor extends AppCompatActivity {

    ApplicationComponent applicationComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        injectDependencies();

    }


    protected void injectDependencies() {
        applicationComponent = ((BaseApplication)getApplicationContext()).getApplicationComponent();
        applicationComponent.inject(this);
    }
}

