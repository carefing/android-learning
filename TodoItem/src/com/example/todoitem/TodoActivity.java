package com.example.todoitem;

import android.app.Activity;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

public class TodoActivity extends Activity {
    TodoItemsDatabaseHelper mTodoItemsDatabaseHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_view);

        mTodoItemsDatabaseHelper= TodoItemsDatabaseHelper.getInstance(this);

        loadData();

        SQLiteDatabase db = mTodoItemsDatabaseHelper.getWritableDatabase();
        Cursor todoCursor = db.rawQuery(String.format("SELECT * FROM %s", TodoItemsDatabaseHelper.TABLE_TODO), null);

        ListView listView = (ListView) findViewById(R.id.lvItems);
        TodoCursorAdapter todoAdapter = new TodoCursorAdapter(this, todoCursor);
        listView.setAdapter(todoAdapter);
    }

    private void loadData() {
        TodoItem item = new TodoItem();
        item.body = "Get milk";
        item.priority = 2;
        mTodoItemsDatabaseHelper.addTodoItem(item);

        item.body = "Go work";
        item.priority = 1;
        mTodoItemsDatabaseHelper.addTodoItem(item);
        item.body = "Do laundry";
        item.priority = 3;
        mTodoItemsDatabaseHelper.addTodoItem(item);
        item.body = "Take care baby";
        item.priority = 4;
        mTodoItemsDatabaseHelper.addTodoItem(item);
    }
}