package com.example.angelina_wu.bookstorecontentproviderowner;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //add();
        display();
    }
    public void add() {

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

    public void display() {
        // Retrieve student records
        String URL = "content://com.example.angelina_wu.bookstorecontentproviderowner/" + BookStoreContentProvider.TABLE_NAME ;
        Uri books = Uri.parse(URL);
        Cursor c = getContentResolver().query( books, null, null, null, null );
        //getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

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
    }
}
