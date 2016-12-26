package com.lozasolutions.arduinoservice.arduino;

import android.content.Context;

import com.jakewharton.rxrelay.PublishRelay;
import com.lozasolutions.arduinoservice.arduino.models.ArduinoMessage;
import com.lozasolutions.arduinoservice.arduino.models.CommandMessage;
import com.lozasolutions.arduinoservice.arduino.models.MeasureMessage;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Loza on 26/12/2016.
 */

public class ArduinoManager implements IArduinoManager {

    private static final String DEFAULT_MAC_ADDRESS = "00:15:83:00:58:B9";
    private static final String SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private static final String READ_CHARACTERISTIC = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private static final String WRITE_CHARACTERISTIC = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private static final boolean AUTOCONNECTION = false;

    final int NO_RECEIVED_MESSAGES_TIMEOUT = 3000;
    Long lastMessage;


    boolean arduinoConnection;
    RxBleConnection.RxBleConnectionState previousStatus = RxBleConnection.RxBleConnectionState.DISCONNECTED;
    RxBleClient rxBleClient;
    RxBleDevice rxBleDevice;
    Subscription connectionState;
    Subscription connection;
    Context context;
    Timer timer;


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
                    .flatMap(rxBleConnection -> rxBleConnection.writeCharacteristic(UUID.fromString(READ_CHARACTERISTIC), message.getBytes()))
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

        connectionState = rxBleDevice.observeConnectionStateChanges().subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(rxBleConnectionState -> {
                    if (rxBleConnectionState.equals(RxBleConnection.RxBleConnectionState.CONNECTED)) {
                        connectionRelay.call(true);
                        arduinoConnection = true;
                        initConnecttionChecker();
                    } else if (rxBleConnectionState.equals(RxBleConnection.RxBleConnectionState.DISCONNECTED)) {
                        connectionRelay.call(false);
                        arduinoConnection = false;
                        if(!rxBleConnectionState.equals(previousStatus)){
                            reconnect();
                        }
                    }
                    previousStatus = rxBleConnectionState;

                }, throwable -> {
                    connectionRelay.call(false);
                });

        connection = connectToBLEAndSetNotification();
    }

    public void reconnect(){

        if(connection != null && !connection.isUnsubscribed()){

            connection.unsubscribe();
        }

        connection = connectToBLEAndSetNotification();

    }



    public Subscription connectToBLEAndSetNotification(){
        return rxBleDevice.establishConnection(context, AUTOCONNECTION).flatMap(rxBleConnection -> rxBleConnection.setupNotification(UUID.fromString(READ_CHARACTERISTIC)))
                .flatMap(notificationObservable -> notificationObservable) // <-- Notification has been set up, now observe value changes.
                .subscribe(
                        bytes -> {
                            // Given characteristic has been changes, here is the value.
                            //TODO parse messages
                            Timber.d("Message received from arduino %s",new String(bytes));
                            messageRelay.call(new MeasureMessage());
                            sendMessage(new CommandMessage());
                            lastMessage = System.currentTimeMillis();
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

    public void initConnecttionChecker(){

        if (timer != null) {
            timer.cancel();
            timer.purge();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (lastMessage != null && System.currentTimeMillis() - lastMessage > NO_RECEIVED_MESSAGES_TIMEOUT) {

                    Timber.d("NO RECEIVING MESSAGES...Disconnecting and reconnecting");
                    disconnect();

                    if (timer != null) {
                        timer.cancel();
                        timer.purge();
                    }

                    lastMessage = null;
                    reconnect();
                }
            }
        }, 0, 5000);
    }
}
