package com.example.svetlanna.mclaffer_fueltrack;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Purpose: Hold a list of FuelLogEntrys
 *
 * Design rational: Implements Serializable so that information
 * can be passed between activities using the intent method .putExtra
 *
 * Outstanding Issues: Does not have exception handling
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
