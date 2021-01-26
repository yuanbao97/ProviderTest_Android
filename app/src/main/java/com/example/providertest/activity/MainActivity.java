package com.example.providertest.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.providertest.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.add_button)
    Button addButton;
    @BindView(R.id.query_button)
    Button queryButton;
    @BindView(R.id.update_button)
    Button updateButton;
    @BindView(R.id.delete_button)
    Button deleteButton;

    public static final String CONTENT_URI = "content://com.example.databasetest.provider/book";
    private String newId;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContentResolver = getContentResolver();
    }

    @OnClick({R.id.add_button, R.id.query_button, R.id.update_button, R.id.delete_button})
    public void onClick(View view) {
        Uri uri;
        ContentValues values;
        Cursor cursor;
        switch (view.getId()) {
            case R.id.add_button:
                uri = Uri.parse(CONTENT_URI);
                values = new ContentValues();
                values.put("name", "A Clash of Kings");
                values.put("author", "George Martin");
                values.put("pages", 1040);
                values.put("price", 22.85);
                Uri newUri = mContentResolver.insert(uri, values);
                newId = newUri.getPathSegments().get(1);
                break;
            case R.id.query_button:
                uri = Uri.parse(CONTENT_URI);
                cursor = mContentResolver.query(uri, null, null, null, null);
                if(cursor != null) {
                    while(cursor.moveToNext()) {
                        Log.d(TAG, "book's name is " + cursor.getString(cursor.getColumnIndex("name")));
                        Log.d(TAG, "book's author is " + cursor.getString(cursor.getColumnIndex("author")));
                        Log.d(TAG, "book's pages is " + cursor.getString(cursor.getColumnIndex("pages")));
                        Log.d(TAG, "book's price is " + cursor.getString(cursor.getColumnIndex("price")));
                    }
                    cursor.close();
                }
                Toast.makeText(this, "query success", Toast.LENGTH_SHORT).show();
                break;
            case R.id.update_button:
                uri = Uri.parse(CONTENT_URI + "/" + newId);
                values = new ContentValues();
                values.put("name", "A Storm of Swords");
                values.put("pages", 1216);
                values.put("price", 24.05);
                mContentResolver.update(uri, values, null, null);
                break;
            case R.id.delete_button:
                uri = Uri.parse(CONTENT_URI + "/" + newId);
                mContentResolver.delete(uri, null, null);
                break;
        }
    }
}