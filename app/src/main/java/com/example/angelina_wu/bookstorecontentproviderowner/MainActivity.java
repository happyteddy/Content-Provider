package com.example.angelina_wu.bookstorecontentproviderowner;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import static android.provider.BaseColumns._ID;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private SimpleCursorAdapter mDataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Activity","- onCreate");
        setContentView(R.layout.activity_main);
        //add();
        getSupportLoaderManager().initLoader(0, null, this);
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

            mDataAdapter = new SimpleCursorAdapter(this, R.layout.book_info,null,columns,to,0);
            ListView list = (ListView) findViewById(R.id.listView);
            if( null != list ) {
                list.setAdapter(mDataAdapter);
                getSupportLoaderManager().restartLoader(0, null, this);
            }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(this, BookStoreContentProvider.CONTENT_URI,
                null, null, null, null);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mDataAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDataAdapter.swapCursor(null);
    }
}
