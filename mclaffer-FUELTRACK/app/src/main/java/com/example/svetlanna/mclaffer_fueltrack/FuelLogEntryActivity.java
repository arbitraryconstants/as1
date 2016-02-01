package com.example.svetlanna.mclaffer_fueltrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.math.BigDecimal;

public class FuelLogEntryActivity extends Activity implements Serializable {

    private EditText date;
    private EditText station;
    private EditText odometer_reading;
    private EditText fuel_grade;
    private EditText fuel_amount;
    private EditText fuel_unit_cost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_fuel_log_entry);


        /// Receive intent if one exists
        /// intent exists if we are supposed to edit an entry
        Intent intentRcvEdit = getIntent();
        String date_str = intentRcvEdit.getStringExtra("date");
        String station_str = intentRcvEdit.getStringExtra("station");
        String odometer_reading_str = intentRcvEdit.getStringExtra("odometer_reading");
        String fuel_grade_str = intentRcvEdit.getStringExtra("fuel_grade");
        String fuel_amount_str = intentRcvEdit.getStringExtra("fuel_amount");
        String fuel_unit_cost_str = intentRcvEdit.getStringExtra("fuel_unit_cost");
        System.out.println("Made it to FuelLogActivity");

        // Capture the shared variable and display it in the relevant textview
        date = (EditText)findViewById(R.id.date);
        date.setText(date_str, TextView.BufferType.EDITABLE);
        station = (EditText)findViewById(R.id.station);
        station.setText(station_str, TextView.BufferType.EDITABLE);
        odometer_reading = (EditText)findViewById(R.id.odometer_reading);
        odometer_reading.setText(odometer_reading_str, TextView.BufferType.EDITABLE);
        fuel_grade = (EditText)findViewById(R.id.fuel_grade);
        fuel_grade.setText(fuel_grade_str, TextView.BufferType.EDITABLE);
        fuel_amount = (EditText)findViewById(R.id.fuel_amount);
        fuel_amount.setText(fuel_amount_str, TextView.BufferType.EDITABLE);
        fuel_unit_cost = (EditText)findViewById(R.id.fuel_unit_cost);
        fuel_unit_cost.setText(fuel_unit_cost_str, TextView.BufferType.EDITABLE);

        /// Do this if these are null.
        // only happens if we are creating new fuel log entry. ie. no intent items are received.
        if (date == null) {
            date = (EditText) findViewById(R.id.date);
            station = (EditText) findViewById(R.id.station);
            //if( station.getText().toString().length() == 0 )
            //    station.setError( "First name is required!" );

            odometer_reading = (EditText) findViewById(R.id.odometer_reading);
            fuel_grade = (EditText) findViewById(R.id.fuel_grade);
            fuel_amount = (EditText) findViewById(R.id.fuel_amount);
            fuel_unit_cost = (EditText) findViewById(R.id.fuel_unit_cost);
        }

        ///Otherwise call gettters?
        //else {
            // Get stuff.
        //}



        Button saveButton = (Button) findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //setResult(RESULT_OK);
                System.out.println("Made it to onClick");
                String date_text = date.getText().toString();
                String station_text = station.getText().toString();
                String odometer_reading_text = odometer_reading.getText().toString();
                String fuel_grade_text = fuel_grade.getText().toString();
                String fuel_amount_text = fuel_amount.getText().toString();
                String fuel_unit_cost_text = fuel_unit_cost.getText().toString();
                System.out.println(date_text);

                FuelLogEntry newestEntry = new FuelLogEntry(date_text, station_text, odometer_reading_text, fuel_grade_text, fuel_amount_text, fuel_unit_cost_text);

                Intent intentPassEntry = new Intent (FuelLogEntryActivity.this, FuelLogActivity.class);
                intentPassEntry.putExtra("newestEntry",newestEntry);
                setResult(Activity.RESULT_OK, intentPassEntry);
                finish();

                //startActivity(intentPassEntry);

                //Bundle bundle = new Bundle();
                //bundle.putSerializable("newestEntry", newestEntry);
                //intentPassEntry.putExtras(bundle);

            }
        });


    }
}
