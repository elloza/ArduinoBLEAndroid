package com.lozasolutions.arduinoservice.arduino.models;

/**
 * Created by Loza on 26/12/2016.
 */

public class MeasureMessage implements ArduinoMessage {
    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public String getAsString() {
        return null;
    }
}
