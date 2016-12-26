package com.lozasolutions.arduinoservice.arduino.models;

/**
 * Created by Loza on 26/12/2016.
 */

public interface  ArduinoMessage {

    byte[] getBytes();
    String getAsString();
}
