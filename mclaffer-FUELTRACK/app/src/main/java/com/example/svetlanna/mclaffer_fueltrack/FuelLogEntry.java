package com.example.svetlanna.mclaffer_fueltrack;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Purpose: Compose a fuel log entry with the following attributes:
 * date, station, odometer reading, fuel grade, fuel amount,
 * fuel unit cost, fuel cost
 *
 * Design rational: Is called by FuelLogEntryActivity when user
 * hits "save" button. Implements Serializable so that information
 * can be passed between activities using the intent method .putExtra
 *
 * Outstanding Issues: None
 */
public class FuelLogEntry implements Serializable {

    protected String station;
    protected String date;
    protected String odometer_reading;
    protected String fuel_grade;
    protected String fuel_amount;
    protected String fuel_unit_cost;
    protected String fuel_cost;

    public FuelLogEntry(String date, String station, String odometer_reading,
                        String fuel_grade, String fuel_amount, String fuel_unit_cost){
        this.date = date;
        this.station = station;
        this.odometer_reading = odometer_reading;
        this.fuel_grade = fuel_grade;
        this.fuel_amount = fuel_amount;
        this.fuel_unit_cost = fuel_unit_cost;
        this.fuel_cost = getFuelCost(fuel_amount, fuel_unit_cost); // 2 decimal places
    }

    // Method to calculate fuel cost
    public String getFuelCost(String fuel_amount, String fuel_unit_cost){
        BigDecimal cents_to_dollars = new BigDecimal("100.00");

        BigDecimal fuel_amount_num = new BigDecimal(fuel_amount);
        BigDecimal fuel_unit_cost_num = new BigDecimal(fuel_unit_cost);
        MathContext mc = new MathContext(4); // 4 precision
        BigDecimal fuel_cost = fuel_amount_num.multiply(fuel_unit_cost_num, mc);

        fuel_cost = fuel_cost.divide(cents_to_dollars, mc);
        return fuel_cost.toString();
    }

    // Method to allow for pretty printing
    @Override
    public String toString(){
        return "Date: " + this.date + "\n" + "Station: " + this.station + "\n" +
                "Odometer reading: " + this.odometer_reading + " km" + "\n" + "Fuel grade: " + this.fuel_grade + "\n" +
                "Fuel amount: " + this.fuel_amount + " L"+  "\n" + "Fuel unit cost: " + this.fuel_unit_cost +
                " cents per L" + "\n" + "Fuel cost: " + this.fuel_cost + " dollars";
    }

    // date getter
    public String getDate() {
        return this.date;
    }

    // station getter
    public String getStation() {
        return this.station;
    }

    // odometer getter
    public String getOdometer_reading() {
        return this.odometer_reading;
    }

    // fuel grade getter
    public String getFuel_grade() {
        return this.fuel_grade;
    }

    // fuel amount getter
    public String getFuel_amount() {
        return this.fuel_amount;
    }

    // fuel unit cost getter
    public String getFuel_unit_cost() {
        return this.fuel_unit_cost;
    }

}
