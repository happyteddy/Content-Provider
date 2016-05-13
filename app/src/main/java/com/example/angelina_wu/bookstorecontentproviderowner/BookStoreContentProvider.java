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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    private static final int INFORMATION_TABLE = 1;
    private static final int INFORMATION_ID = 2;

    private SQLiteDatabase mDb;

    static final Uri CONTENT_URI = Uri.withAppendedPath( Uri.parse("content://" + AUTHORITY) ,TABLE_NAME);

    private static final UriMatcher mUriMatcher;
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY,TABLE_NAME, INFORMATION_TABLE);
        mUriMatcher.addURI(AUTHORITY,TABLE_NAME + "/#" , INFORMATION_ID);
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
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext()); // getContext() :Retrieves the Context this provider is running in
        mDb = databaseHelper.getWritableDatabase();
        Log.d("ContentProvider","- onCreate");
        return mDb != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        if((mUriMatcher.match(uri)) == INFORMATION_ID){
            qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
        }
        Cursor c = qb.query(mDb, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);// register to watch a content URI for changes
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)){
            case INFORMATION_TABLE:
                return "vnd.android.cursor.dir/vnd.example.information";

            case INFORMATION_ID:
                return "vnd.android.cursor.item/vnd.example.information";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long rowID = mDb.insert(TABLE_NAME, "", values);

        // If record is added successfully
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            // This notifies all the observers registered for that particular URI that change happened. 通知觀測者數據改變了

            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        //count = mDb.delete(TABLE_NAME, selection, selectionArgs);

        switch (mUriMatcher.match(uri)){
            case INFORMATION_TABLE :
                count = mDb.delete(TABLE_NAME, selection, selectionArgs);
                break;

            case INFORMATION_ID :
                String id = uri.getPathSegments().get(1);
                if(selection.isEmpty()){
                    count = mDb.delete( TABLE_NAME, _ID +  " = " + id ,selectionArgs);
                }
                else {
                    count = mDb.delete( TABLE_NAME, _ID +  " = " + id + " AND (" + selection + ')' , selectionArgs );
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        //count = mDb.update(TABLE_NAME, values, selection, selectionArgs);
        switch (mUriMatcher.match(uri)){
            case INFORMATION_TABLE :
                count = mDb.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case INFORMATION_ID :
                String id = uri.getPathSegments().get(1);
                if(selection.isEmpty()){
                    selection = _ID + "=" + id ;
                    count = mDb.update(TABLE_NAME, values, selection, selectionArgs);
                }
                else {
                    String selection_id = _ID + "=" + id ;
                    count = mDb.update( TABLE_NAME, values, selection_id + " AND (" + selection + ')' , selectionArgs );
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
