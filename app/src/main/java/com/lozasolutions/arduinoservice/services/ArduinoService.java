package com.lozasolutions.arduinoservice.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.lozasolutions.arduinoservice.BaseApplication;
import com.lozasolutions.arduinoservice.Monitor;
import com.lozasolutions.arduinoservice.R;
import com.lozasolutions.arduinoservice.arduino.ArduinoManager;
import com.lozasolutions.arduinoservice.backend.SocketIOManager;
import com.lozasolutions.arduinoservice.di.components.ApplicationComponent;

import javax.inject.Inject;

import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Loza on 25/12/2016.
 */

public class ArduinoService extends Service {

    private static final int NOTIFICATION_ID = 1;

    ApplicationComponent applicationComponent;

    @Inject
    SocketIOManager socketIOManager;

    @Inject
    ArduinoManager arduinoManager;


    @Override
    public void onCreate() {
        super.onCreate();
        injectDependencies();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Timber.i("On start service foreground service");
        Intent notificationIntent = new Intent(this, Monitor.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.ble_service_title))
                .setTicker(getString(R.string.app_name))
                .setContentText(getString(R.string.ble_service_title))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();

        startForeground(NOTIFICATION_ID, notification);

        connectToArduino();

        connectToServer();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.i("BLE service onDestroy");
    }


    public void connectToArduino() {

        arduinoManager.getConnectionObservable().observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    //TODO
                }, throwable -> {
                    //TODO
                });

        arduinoManager.getArduinoMessageObservable().subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(arduinoMessage -> {
                    //TODO
                    //Map to server message and send

                }, throwable -> {
                    //TODO
                });


        arduinoManager.connect();


    }

    public void connectToServer() {

        socketIOManager.getConnectionObservable()
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    //TODO manage this
                }, throwable -> {
                    //TODO manage this
                });

        socketIOManager.getMessagesObservable()
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(socketMessage -> {
                    //TODO
                    //Map to arduino message and send
                }, throwable -> {
                    //TODO
                });


        socketIOManager.connect();
    }



   protected void injectDependencies() {
        applicationComponent = ((BaseApplication)getApplicationContext()).getApplicationComponent();
        applicationComponent.inject(this);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }
}
