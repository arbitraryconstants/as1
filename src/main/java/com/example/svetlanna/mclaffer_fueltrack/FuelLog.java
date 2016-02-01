package com.example.svetlanna.mclaffer_fueltrack;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Purpose: Hold a list of FuelLogEntrys. Allows method overrides for
 * ArrayList<E> type
 *
 * Design rational: Implements Serializable so that information
 * can be passed between activities using the intent method .putExtra
 *
 * Outstanding Issues: Does not have exception handling. Is only used for
 * testing. I was not able to use this code from FuelLogActivity
 * because I needed to be able to iterate through the entries in
 * this object when calculating the total fuel cost of the log.
 */
public class FuelLog implements Serializable {

    public ArrayList<FuelLogEntry> log = new ArrayList<FuelLogEntry>();

    public void addEntry(FuelLogEntry fuelLogEntry){
        log.add(fuelLogEntry);
    }

    public void clearLog(){
        log.clear();
    }

    public void removeEntry(int pos){
        log.remove(pos);
    }

    public ArrayList<FuelLogEntry> getLog() {
        return log;
    }



}
