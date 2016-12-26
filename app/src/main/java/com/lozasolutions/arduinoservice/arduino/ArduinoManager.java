package com.lozasolutions.arduinoservice.arduino;

import android.content.Context;

import com.jakewharton.rxrelay.PublishRelay;
import com.lozasolutions.arduinoservice.arduino.models.ArduinoMessage;
import com.lozasolutions.arduinoservice.arduino.models.MeasureMessage;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;

import java.util.UUID;

import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Loza on 26/12/2016.
 */

public class ArduinoManager implements IArduinoManager {

    private static final String DEFAULT_MAC_ADDRESS = "00:00:00:00:00:00";
    private static final String READ_CHARACTERISTIC = "";
    private static final String WRITE_CHARACTERISTIC = "";


    boolean arduinoConnection;
    RxBleClient rxBleClient;
    RxBleDevice rxBleDevice;
    Context context;

    private PublishRelay<Boolean> connectionRelay = PublishRelay.create();
    private PublishRelay<ArduinoMessage> messageRelay = PublishRelay.create();

    public ArduinoManager(Context context, RxBleClient rxBleClient) {
        this.rxBleClient = rxBleClient;
        this.context = context;
    }

    @Override
    public void sendMessage(ArduinoMessage message) {

        if(rxBleDevice != null) {
            rxBleDevice.establishConnection(context, false)
                    .flatMap(rxBleConnection -> rxBleConnection.writeCharacteristic(UUID.fromString(WRITE_CHARACTERISTIC), message.getBytes()))
                    .subscribe(
                            characteristicValue -> {
                                // Characteristic value confirmed.
                                Timber.d("Message sended to arduino %s",message.getAsString());
                            },
                            throwable -> {
                                // Handle an error here.
                                //TODO: Retry?
                            }
                    );
        }else{

            //TODO reconnect?
        }

    }

    @Override
    public Observable<ArduinoMessage> getArduinoMessageObservable() {
        return messageRelay.asObservable();
    }

    @Override
    public Observable<Boolean> getConnectionObservable() {
        return connectionRelay.asObservable();
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void connect() {

        rxBleDevice = rxBleClient.getBleDevice(DEFAULT_MAC_ADDRESS);

        rxBleDevice.observeConnectionStateChanges().subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(rxBleConnectionState -> {
                    if (rxBleConnectionState.equals(RxBleConnection.RxBleConnectionState.CONNECTED)) {
                        connectionRelay.call(true);
                        arduinoConnection = true;
                    } else if (rxBleConnectionState.equals(RxBleConnection.RxBleConnectionState.DISCONNECTED)) {
                        connectionRelay.call(false);
                        arduinoConnection = false;
                    }
                }, throwable -> {
                    connectionRelay.call(false);
                });

        rxBleDevice.establishConnection(context, false)
                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(UUID.fromString(READ_CHARACTERISTIC)))
                .flatMap(notificationObservable -> notificationObservable) // <-- Notification has been set up, now observe value changes.
                .subscribe(
                        bytes -> {
                            // Given characteristic has been changes, here is the value.
                            //TODO parse messages
                            messageRelay.call(new MeasureMessage());
                        },
                        throwable -> {
                            // Handle an error here.
                        }
                );
    }

    @Override
    public void disconnect() {
        if(rxBleDevice != null){

        }

    }
}
