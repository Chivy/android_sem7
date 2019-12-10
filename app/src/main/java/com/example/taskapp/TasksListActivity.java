package com.example.taskapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

public class TasksListActivity extends AppCompatActivity {

    private Long taskId;

    private List<String> from = new ArrayList<>();
    private List<Integer> to = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_emp_list);

        DBManager dbManager = new DBManager(this);
        dbManager.open();

        Intent intent = getIntent();
        taskId = intent.getLongExtra("taskId", 0);
        Cursor cursor;

        from.add(DatabaseHelper._ID);
        from.add(DatabaseHelper.NAME);
        from.add(DatabaseHelper.START_DATE);
        from.add(DatabaseHelper.END_DATE);


        to.add(R.id.id);
        to.add(R.id.name);
        to.add(R.id.startDate);
        to.add(R.id.endDate);

        if (taskId > 0) {
            setTitle(R.string.sub_tasks);
            cursor = dbManager.fetchSubTasks(taskId);
        } else {
            setTitle(R.string.tasks);
            cursor = dbManager.fetchTasks();
            from.add(DatabaseHelper.PRIORITY);

            to.add(R.id.priority);
        }

        ListView listView = findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        int[] array = new int[to.size()];
        for (int i = 0; i < to.size(); i++) array[i] = to.get(i);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.activity_view_task,
                cursor,
                from.toArray(new String[0]),
                array,
                0
        );
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, viewId) -> {
            TextView idTextView = view.findViewById(R.id.id);
            TextView titleTextView = view.findViewById(R.id.name);
            TextView startDateTextView = view.findViewById(R.id.startDate);
            TextView endDateTextView = view.findViewById(R.id.endDate);
            TextView priorityTextView = view.findViewById(R.id.priority);

            String id = idTextView.getText().toString();
            String name = titleTextView.getText().toString();
            String startDate = startDateTextView.getText().toString();
            String endDate = endDateTextView.getText().toString();
            String priority = priorityTextView.getText().toString();

            Intent modify_intent = new Intent(getApplicationContext(), EditTaskActivity.class);
            modify_intent.putExtra("name", name);
            modify_intent.putExtra("startDate", startDate);
            modify_intent.putExtra("endDate", endDate);

            if (!priority.equals("")) {
                modify_intent.putExtra("priority", Integer.valueOf(priority));
            }
            modify_intent.putExtra("id", id);
            modify_intent.putExtra("taskId", taskId);

            startActivity(modify_intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem addTask = menu.findItem(R.id.add_task);
        if (taskId > 0) {
            addTask.setTitle(R.string.add_sub_task);
        } else {
            addTask.setTitle(R.string.add_task);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_task) {
            Intent add_mem = new Intent(this, AddTaskActivity.class);
            add_mem.putExtra("taskId", taskId);
            startActivity(add_mem);

        }
        return super.onOptionsItemSelected(item);
    }

}