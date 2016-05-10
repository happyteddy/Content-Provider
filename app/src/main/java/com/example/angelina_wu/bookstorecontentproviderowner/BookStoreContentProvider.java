package com.example.angelina_wu.bookstorecontentproviderowner;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import static android.provider.BaseColumns._ID;

public class BookStoreContentProvider extends ContentProvider {

    private final static String DATABASE_NAME = "bookstore.db";
    private static final int DATABASE_VERSION = 1;
    public static final String AUTHORITY = "com.example.angelina_wu.bookstorecontentproviderowner";
    public static final String TABLE_NAME = "information";

    public static final String SELLER_NAME = "seller_name";
    public static final String SELLER_ID = "seller_id";
    public static final String BOOK_NAME = "book_name";
    public static final String PRICE = "price";
    public static final String SOLD = "sold";
    public static final String BUYER_NAME = "buyer_name";
    public static final String BUYER_ID = "buyer_id";

    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDb;

    static final String URL = "content://com.example.angelina_wu.bookstorecontentproviderowner/"+TABLE_NAME;
    static final Uri CONTENT_URI = Uri.parse(URL);

    private static final UriMatcher mUriMatcher;
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY,TABLE_NAME, 1);
        mUriMatcher.addURI(AUTHORITY,TABLE_NAME + "/#" , 2);
    }



    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            final String INIT_TABLE =
                    "CREATE TABLE " + TABLE_NAME + " ("+
                            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            SELLER_NAME + " CHAR," +
                            SELLER_ID + " INT, " +
                            BOOK_NAME + " CHAR," +
                            PRICE + " INT, " +
                            SOLD + " BOOLEAN," +
                            BUYER_NAME + " CHAR," +
                            BUYER_ID + " INT " +
                            ");" ;
            db.execSQL(INIT_TABLE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext()); // getContext() :Retrieves the Context this provider is running in
        mDb = mDatabaseHelper.getWritableDatabase();
        Log.d("ContentProvider","- onCreate");
        if(mDb == null) return false;
        else return true;
    }




    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        Cursor c = qb.query(mDb, projection, selection, selectionArgs, null, null, sortOrder);
        //c.setNotificationUri(getContext().getContentResolver(), uri);// register to watch a content URI for changes
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = mDb.insert(TABLE_NAME, "", values);

        // If record is added successfully
        if (rowID > 0) {
            return ContentUris.withAppendedId(CONTENT_URI, rowID);
            //getContext().getContentResolver().notifyChange(_uri, null);
            // This notifies all the observers registered for that particular URI that change happened. 通知觀測者數據改變了
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        count = mDb.delete(TABLE_NAME, selection, selectionArgs);

       /* switch (mUriMatcher.match(uri)){
            case 1:
                count = mDb.delete(TABLE_NAME, selection, selectionArgs);
                break;

            case 2:
                String id = uri.getPathSegments().get(1);
                count = mDb.delete( TABLE_NAME, _ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                //selection.isEmpty();
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }*/

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        count = mDb.update(TABLE_NAME, values, selection, selectionArgs);
        return count;
    }
}
