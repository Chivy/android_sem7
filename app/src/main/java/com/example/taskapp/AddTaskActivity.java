package com.example.taskapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTaskActivity extends Activity implements OnClickListener {

    private EditText nameEditText;
    private DBManager dbManager;
    private Long taskId;
    private int prioritySelected;
    private TextView txtStartDate, txtEndDate;

    private int startYear, startMonth, startDay;
    private int endYear, endMonth, endDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        taskId = intent.getLongExtra("taskId", 0);
        setContentView(R.layout.activity_add_task);
        Spinner spinner = findViewById(R.id.priority_spinner);

        if (taskId > 0) {
            setTitle(R.string.add_sub_task);
            spinner.setVisibility(View.INVISIBLE);
        } else {
            setTitle(R.string.add_task);
            spinner.setVisibility(View.VISIBLE);
        }
        txtStartDate = findViewById(R.id.in_startDate);
        txtEndDate = findViewById(R.id.in_endDate);

        nameEditText = findViewById(R.id.name_edittext);
        Button btnStartDatePicker = findViewById(R.id.btnStartDate);
        Button btnEndDatePicker = findViewById(R.id.btnEndDate);

        btnStartDatePicker.setOnClickListener(this::createStartDatePickerDialog);
        btnEndDatePicker.setOnClickListener(this::createEndDatePickerDialog);

        Button addButton = findViewById(R.id.add_task);

        dbManager = new DBManager(this);
        dbManager.open();
        addButton.setOnClickListener(this);

        List<String> labels = new ArrayList<>();
        labels.add("Low");
        labels.add("High");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, labels);

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prioritySelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_task) {
            final String name = nameEditText.getText().toString();

            String startDate = txtStartDate.getText().toString();
            String endDate = txtEndDate.getText().toString();

            if (name.trim().isEmpty()) {
                Toast.makeText(this, "Field name cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            } else if (startDate.trim().isEmpty()) {
                Toast.makeText(this, "Field start date cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            } else if (endDate.trim().isEmpty()) {
                Toast.makeText(this, "Field end date cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }


            if (taskId > 0) {
                try {
                    dbManager.insertSubTask(
                            taskId,
                            startDate,
                            endDate,
                            name
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    dbManager.insertTask(
                            name,
                            startDate,
                            endDate,
                            prioritySelected
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Intent main = new Intent(AddTaskActivity.this, TasksListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            main.putExtra("taskId", taskId);
            startActivity(main);
        }
    }

    private void createStartDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        startYear = c.get(Calendar.YEAR);
        startMonth = c.get(Calendar.MONTH);
        startDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) ->
                        txtStartDate.setText(String.format(
                                Locale.getDefault(),
                                "%d/%d/%d",
                                year,
                                monthOfYear + 1,
                                dayOfMonth)
                        ), startYear, startMonth, startDay);
        datePickerDialog.show();
    }

    private void createEndDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        endYear = c.get(Calendar.YEAR);
        endMonth = c.get(Calendar.MONTH);
        endDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> txtEndDate.setText(String.format(
                        Locale.getDefault(),
                        "%d/%d/%d",
                        year,
                        monthOfYear + 1,
                        dayOfMonth)
                ), endYear, endMonth, endDay);
        datePickerDialog.show();
    }
}