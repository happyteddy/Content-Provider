package com.example.angelina_wu.bookstorecontentproviderowner;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Activity","- onCreate");
        setContentView(R.layout.activity_main);
        //add();
        display();
    }
    public void add() {  // test

        ContentValues values = new ContentValues();

        values.put(BookStoreContentProvider.SELLER_NAME,"A");
        values.put(BookStoreContentProvider.SELLER_ID,123);
        values.put(BookStoreContentProvider.BOOK_NAME,"BOOK1");
        values.put(BookStoreContentProvider.PRICE,100);
        values.put(BookStoreContentProvider.SOLD,true);
        values.put(BookStoreContentProvider.BUYER_NAME,"B");
        values.put(BookStoreContentProvider.BUYER_ID,"321");

        getContentResolver().insert( BookStoreContentProvider.CONTENT_URI, values);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void display() {

        //try (Cursor c = getContentResolver().query(BookStoreContentProvider.CONTENT_URI, null, null, null, null )) {
            Cursor c = getContentResolver().query(BookStoreContentProvider.CONTENT_URI, null, null, null, null );
            startManagingCursor(c);
            if (c != null) {
                c.moveToFirst();
            }

            String[] columns = new String[] {
                    _ID,
                    BookStoreContentProvider.SELLER_NAME,
                    BookStoreContentProvider.SELLER_ID,
                    BookStoreContentProvider.BOOK_NAME,
                    BookStoreContentProvider.PRICE,
                    BookStoreContentProvider.SOLD,
                    BookStoreContentProvider.BUYER_NAME,
                    BookStoreContentProvider.BUYER_ID
            };
            int[] to = new int[] {
                    R.id.id,
                    R.id.seller_name,
                    R.id.seller_id,
                    R.id.book_name,
                    R.id.price,
                    R.id.sold,
                    R.id.buyer_name,
                    R.id.buyer_id
            };

            SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.book_info,c,columns,to,0);
            ListView list = (ListView) findViewById(R.id.listView);
            if( null != list ) {
                list.setAdapter(dataAdapter);
            }
        //}
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        ListView lv = (ListView) findViewById(R.id.listView);
        if( null != lv ) {
            ((CursorAdapter) lv.getAdapter()).getCursor().close();
        }
    }
}
