package com.lozasolutions.arduinoservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lozasolutions.arduinoservice.arduino.ArduinoManager;
import com.lozasolutions.arduinoservice.backend.SocketManager;
import com.lozasolutions.arduinoservice.di.components.ApplicationComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class Monitor extends AppCompatActivity {

    ApplicationComponent applicationComponent;
    @Inject
    ArduinoManager arduinoManager;
    @Inject
    SocketManager socketManager;

    CompositeSubscription compositeSubscription;

    @BindView(R.id.arduino_connection)
    TextView arduinoConnection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        ButterKnife.bind(this);
        injectDependencies();

        compositeSubscription.add(arduinoManager.getConnectionObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {


            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        }));


    }


    protected void injectDependencies() {
        applicationComponent = ((BaseApplication)getApplicationContext()).getApplicationComponent();
        applicationComponent.inject(this);
    }
}

