package com.example.svetlanna.mclaffer_fueltrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.Serializable;


/**
 * Purpose: Receive user input to create an new fuel log entry
 * or to edit an existing fuel log entry by obtaining the following
 * fields: date, station, odometer reading, fuel grade,
 * fuel amount, fuel unit cost
 *
 * Design rational: Is called by FuelLogActivity when user
 * hits "Create New Log Entry" button or the user clicks a fuel log
 * entry in the list view. Implements Serializable so that information
 * can be passed between activities using the intent method .putExtra
 *
 * Outstanding Issues: Input validation for date, and input validation
 * for ensuring that the correct number of decimal places are entered
 * by the user
 */
public class FuelLogEntryActivity extends Activity implements Serializable {

    private EditText date;
    private EditText station;
    private EditText odometer_reading;
    private EditText fuel_grade;
    private EditText fuel_amount;
    private EditText fuel_unit_cost;

    // Used as a flag to determine whether entry is new or to be edited
    public String flag = "edit";


    // Only called once per activity lifetime
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_fuel_log_entry);


        // Receive intent if one exists
        // intentRcvEdit exists if we are supposed to edit an entry
        // Capture user's previous entry into strings
        Intent intentRcvEdit = getIntent();
        String date_str = intentRcvEdit.getStringExtra("date");
        String station_str = intentRcvEdit.getStringExtra("station");
        String odometer_reading_str = intentRcvEdit.getStringExtra("odometer_reading");
        String fuel_grade_str = intentRcvEdit.getStringExtra("fuel_grade");
        String fuel_amount_str = intentRcvEdit.getStringExtra("fuel_amount");
        String fuel_unit_cost_str = intentRcvEdit.getStringExtra("fuel_unit_cost");

        // If intentRcvEdit was not received then date_str will be null
        // If date_str is null, then we know that the entry is a new entry
        if (date_str == null) {
            // Update flag
            flag = "new";
        }

        // Set EditText boxes to display previous information if it exists
        // Idea from: https://stackoverflow.com/questions/4590957/how-to-set-text-in-an-edittext
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

        // "Save" and "Cancel" buttons
        Button cancelButton = (Button) findViewById(R.id.cancel);
        Button saveButton = (Button) findViewById(R.id.save);

        // Save button is pressed
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Save user input to strings
                String date_text = date.getText().toString();
                String station_text = station.getText().toString();
                String odometer_reading_text = odometer_reading.getText().toString();
                String fuel_grade_text = fuel_grade.getText().toString();
                String fuel_amount_text = fuel_amount.getText().toString();
                String fuel_unit_cost_text = fuel_unit_cost.getText().toString();
                System.out.println(date_text);

                // Use these strings to create a new entry
                FuelLogEntry newestEntry = new FuelLogEntry(date_text, station_text, odometer_reading_text, fuel_grade_text, fuel_amount_text, fuel_unit_cost_text);

                // Pass this new entry back to the FuelLogActivity so that it can be added to the log list
                // use "FuelLogEntryActivity.this" instead of "this". Source: https://stackoverflow.com/questions/5257003/how-to-start-second-activity-in-android-getting-error
                Intent intentPassEntry = new Intent (FuelLogEntryActivity.this, FuelLogActivity.class);
                intentPassEntry.putExtra("newestEntry",newestEntry);
                setResult(Activity.RESULT_OK, intentPassEntry);
                finish();

            }
        });

        // Cancel button is pressed
        // Behavior of Cancel button depends on whether the entry is being edited or created.
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Only need to save entry if it was being edited
                if (flag == "edit") {

                    // Save user input to strings
                    String date_text = date.getText().toString();
                    String station_text = station.getText().toString();
                    String odometer_reading_text = odometer_reading.getText().toString();
                    String fuel_grade_text = fuel_grade.getText().toString();
                    String fuel_amount_text = fuel_amount.getText().toString();
                    String fuel_unit_cost_text = fuel_unit_cost.getText().toString();
                    System.out.println(date_text);

                    // Use these strings to create a new entry
                    FuelLogEntry newestEntry = new FuelLogEntry(date_text, station_text, odometer_reading_text, fuel_grade_text, fuel_amount_text, fuel_unit_cost_text);

                    // Pass this new entry back to the FuelLogActivity so that it can be added to the log list
                    Intent intentPassEntry = new Intent(FuelLogEntryActivity.this, FuelLogActivity.class);
                    intentPassEntry.putExtra("newestEntry", newestEntry);
                    setResult(Activity.RESULT_OK, intentPassEntry);

                }

                finish();

            }
        });


    }
}
