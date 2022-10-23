package com.creationedge.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.creationedge.android.model.CartDBModel;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "myCart";
    private static final String TABLE_BOOKMARK = "cartItem";
    private static final String KEY_ID = "id";
    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_NAME = "product_name";
    private static final String THUMBNAIL = "thumbnailTEXT";
    private static final String PRICE = "price";
    private static final String QUANTITY = "quantity";
    private static final String SINGLE_PRICE = "single_price";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BOOKMARK + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PRODUCT_ID + " INT,"
                + PRICE + " INT,"
                + QUANTITY + " INT,"
                + SINGLE_PRICE + " INT,"
                + PRODUCT_NAME + " TEXT,"
                + THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARK);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addContact(CartDBModel contact) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PRODUCT_ID, contact.getProductId()); // Contact Name
        values.put(PRODUCT_NAME, contact.getProductName()); // Contact Phone
        values.put(THUMBNAIL, contact.getProduct_image()); // Contact Phone
        values.put(PRICE, contact.getLineTotal()); // Contact Phone
        values.put(SINGLE_PRICE,contact.getSinglePrice());
        values.put(QUANTITY, contact.getQuantity()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_BOOKMARK, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    CartDBModel getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BOOKMARK, new String[] { KEY_ID,
                        PRODUCT_ID, PRODUCT_NAME, THUMBNAIL,PRICE,QUANTITY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        CartDBModel contact = new CartDBModel(Integer.parseInt(cursor.getString(0)),
                cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6));
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public List<CartDBModel> getAllContacts() {
        List<CartDBModel> contactList = new ArrayList<CartDBModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOOKMARK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CartDBModel contact = new CartDBModel();
                contact.set_id(Integer.parseInt(cursor.getString(0)));
                contact.setProductId(cursor.getInt(1));
                contact.setLineTotal(cursor.getInt(2));
                contact.setQuantity(cursor.getInt(3));
                contact.setSinglePrice(cursor.getInt(4));
                contact.setProductName(cursor.getString(5));
                contact.setProduct_image(cursor.getString(6));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(CartDBModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PRODUCT_ID, contact.getProductId());
        values.put(PRODUCT_NAME, contact.getProductName());
        values.put(THUMBNAIL, contact.getProduct_image());
        values.put(PRICE, contact.getLineTotal());
        values.put(SINGLE_PRICE,contact.getSinglePrice());
        values.put(QUANTITY, contact.getQuantity());

        // updating row
        return db.update(TABLE_BOOKMARK, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.get_id()) });
    }

    // Deleting single contact
    public void deleteContact(CartDBModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKMARK, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.get_id()) });

        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BOOKMARK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
