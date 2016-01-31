package com.example.svetlanna.mclaffer_fueltrack;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by svetlanna on 16-01-30.
 */
public class FuelLog implements Serializable {
    public ArrayList<FuelLogEntry> log = new ArrayList<FuelLogEntry>();

    public void addEntry(FuelLogEntry fuelLogEntry){
        log.add(fuelLogEntry);
    }

    public void remove(int pos){
        log.remove(pos);
    }

    public void removeEntry(FuelLogEntry fuelLogEntry){
        log.remove(fuelLogEntry);
    }

    public ArrayList<FuelLogEntry> getLog() {
        return log;
    }



}
