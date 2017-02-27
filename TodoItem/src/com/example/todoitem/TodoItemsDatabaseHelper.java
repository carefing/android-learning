package com.example.todoitem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class TodoItemsDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "TodoItemsDatabaseHelper";

    // Database info
    private static final String DATABASE_NAME = "todoItemsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_TODO = "todo";

    // Todo table columns
    public static final String KEY_TODO_ID = "_id";
    public static final String KEY_TODO_BODY = "body";
    public static final String KEY_TODO_PRIORITY = "priority";

    private static TodoItemsDatabaseHelper sTodoItemsDatabaseHelper;

    public static synchronized TodoItemsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you 
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sTodoItemsDatabaseHelper == null) {
            sTodoItemsDatabaseHelper = new TodoItemsDatabaseHelper(context.getApplicationContext());
        }
        return sTodoItemsDatabaseHelper;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private TodoItemsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO +
                "(" +
                    KEY_TODO_ID + " INTEGER PRIMARY KEY," +
                    KEY_TODO_BODY + " TEXT," +
                    KEY_TODO_PRIORITY + " INTEGER" +
                ")";

        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            onCreate(db);
        }
    }

    public void addTodoItem(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_BODY, todoItem.body);
            values.put(KEY_TODO_PRIORITY, todoItem.priority);

            db.insertOrThrow(TABLE_TODO, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add todo item to database");
        } finally {
            db.endTransaction();
        }
    }

    public List<TodoItem> getAllTodoItems() {
        List<TodoItem> todoItems = new ArrayList<>();

        String TODOITEM_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_TODO);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TODOITEM_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    TodoItem item = new TodoItem();
                    item.body = cursor.getString(cursor.getColumnIndex(KEY_TODO_BODY));
                    item.priority = cursor.getInt(cursor.getColumnIndex(KEY_TODO_PRIORITY));

                    todoItems.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get todo items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return todoItems;
    }

    public void updateTodoItemPriority(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO_PRIORITY, todoItem.priority);

        db.update(TABLE_TODO, values, KEY_TODO_BODY + " = ?",
                new String[]{String.valueOf(todoItem.body)});
    }

    public void deleteAllTodoItems() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_TODO, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all todo items");
        } finally {
            db.endTransaction();
        }
    }

}

