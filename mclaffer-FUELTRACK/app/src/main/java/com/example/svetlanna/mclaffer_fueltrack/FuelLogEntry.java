package com.example.svetlanna.mclaffer_fueltrack;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

/**
 * Created by svetlanna on 16-01-30.
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

    public String getFuelCost(String fuel_amount, String fuel_unit_cost){
        BigDecimal cents_to_dollars = new BigDecimal("100.00");

        BigDecimal fuel_amount_num = new BigDecimal(fuel_amount);
        BigDecimal fuel_unit_cost_num = new BigDecimal(fuel_unit_cost);
        MathContext mc = new MathContext(4); // 4 precision
        BigDecimal fuel_cost = fuel_amount_num.multiply(fuel_unit_cost_num, mc);

        fuel_cost = fuel_cost.divide(cents_to_dollars, mc);
        return fuel_cost.toString();
    }


    @Override
    public String toString(){
        return "Date: " + this.date + "\n" + "Station: " + this.station + "\n" +
                "Odometer reading: " + this.odometer_reading + " km" + "\n" + "Fuel grade: " + this.fuel_grade + "\n" +
                "Fuel amount: " + this.fuel_amount + " L"+  "\n" + "Fuel unit cost: " + this.fuel_unit_cost +
                " cents per L" + "\n" + "Fuel_cost: " + this.fuel_cost + " dollars";
    }

    public String getDate() {
        return this.date;
    }

    public String getStation() {
        return this.station;
    }

    public String getOdometer_reading() {
        return this.odometer_reading;
    }

    public String getFuel_grade() {
        return this.fuel_grade;
    }

    public String getFuel_amount() {
        return this.fuel_amount;
    }

    public String getFuel_unit_cost() {
        return this.fuel_unit_cost;
    }

    public String getFuel_cost() {
        return this.fuel_cost;
    }

}
