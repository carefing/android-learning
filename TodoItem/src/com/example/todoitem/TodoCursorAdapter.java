package com.example.todoitem;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TodoCursorAdapter extends CursorAdapter {
    public TodoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        TextView tvPriority = (TextView) view.findViewById(R.id.tvPriority);

        String body = cursor.getString(cursor.getColumnIndexOrThrow(TodoItemsDatabaseHelper.KEY_TODO_BODY));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow(TodoItemsDatabaseHelper.KEY_TODO_PRIORITY));

        tvBody.setText(body);
        tvPriority.setText(String.valueOf(priority));
    }
}