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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditTaskActivity extends Activity implements OnClickListener {

    private EditText nameText, startDateText, endDateText;
    private Button btnStartDate, btnEndDate;

    private Long taskId;
    private long _id;

    private DBManager dbManager;
    private int prioritySelected;

    private int startYear, startMonth, startDay, endYear, endMonth, endDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modify_task);

        dbManager = new DBManager(this);
        dbManager.open();

        nameText = findViewById(R.id.name_edittext);
        startDateText = findViewById(R.id.in_startDate);
        endDateText = findViewById(R.id.in_endDate);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);

        Spinner spinner = findViewById(R.id.priority_spinner);

        Intent intent = getIntent();
        _id = Long.parseLong(intent.getStringExtra("id"));
        taskId = intent.getLongExtra("taskId", 0);
        String name = intent.getStringExtra("name");
        String startDate = intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");
        int priority = intent.getIntExtra("priority", 0);

        nameText.setText(name);
        startDateText.setText(startDate);
        endDateText.setText(endDate);

        Button updateBtn = findViewById(R.id.btn_update);
        updateBtn.setOnClickListener(this);

        Button subTasksBtn = findViewById(R.id.btn_sub_tasks);
        if (taskId > 0) {
            setTitle(R.string.edit_sub_task);
            subTasksBtn.setVisibility(View.GONE);
            spinner.setVisibility(View.INVISIBLE);
        } else {
            setTitle(R.string.edit_task);
            subTasksBtn.setVisibility(View.VISIBLE);
            subTasksBtn.setOnClickListener(this);
            spinner.setVisibility(View.VISIBLE);
        }

        List<String> labels = new ArrayList<>();
        labels.add("Low");
        labels.add("High");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, labels);

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        btnStartDate.setOnClickListener(this::createStartDatePickerDialog);
        btnEndDate.setOnClickListener(this::createEndDatePickerDialog);

        spinner.setAdapter(dataAdapter);
        spinner.setSelection(priority);

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
        switch (v.getId()) {
            case R.id.btn_update:
                String name = nameText.getText().toString();

                String startDate = startDateText.getText().toString();
                String endDate = endDateText.getText().toString();

                if (taskId > 0) {
                    try {
                        dbManager.updateSubTask(_id, startDate, endDate, name);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        dbManager.updateTask(
                                _id,
                                name,
                                startDate,
                                endDate,
                                prioritySelected
                        );
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                Intent main = new Intent(getApplicationContext(), TasksListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                main.putExtra("taskId", taskId);
                startActivity(main);
                break;

            case R.id.btn_sub_tasks:

                Intent modify_intent = new Intent(getApplicationContext(), TasksListActivity.class);
                modify_intent.putExtra("taskId", _id);
                finish();
                startActivity(modify_intent);
                break;
        }
    }

    private void createStartDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        startYear = c.get(Calendar.YEAR);
        startMonth = c.get(Calendar.MONTH);
        startDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> startDateText.setText(
                        String.format(
                                Locale.getDefault(),
                                "%d/%d/%d",
                                year,
                                monthOfYear + 1,
                                dayOfMonth
                        )
                ), startYear, startMonth, startDay);
        datePickerDialog.show();
    }

    private void createEndDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        endYear = c.get(Calendar.YEAR);
        endMonth = c.get(Calendar.MONTH);
        endDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> endDateText.setText(
                        String.format(
                                Locale.getDefault(),
                                "%d/%d/%d",
                                year,
                                monthOfYear + 1,
                                dayOfMonth
                        )
                ), endYear, endMonth, endDay);
        datePickerDialog.show();
    }
}
