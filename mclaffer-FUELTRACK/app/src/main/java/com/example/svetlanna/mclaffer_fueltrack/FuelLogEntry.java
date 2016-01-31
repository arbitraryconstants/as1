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


        BigDecimal fuel_amount_num = new BigDecimal(fuel_amount);
        BigDecimal fuel_unit_cost_num = new BigDecimal(fuel_unit_cost);
        MathContext mc = new MathContext(4); // 4 precision
        BigDecimal fuel_cost = fuel_amount_num.multiply(fuel_unit_cost_num, mc);

        return fuel_cost.toString();
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString(){
        return "date: " + date + "\n" + "station: " + station;
    }

    public String getStation() {
        return this.station;
    }

}
