package com.cu.cleverboy.secretmessageis.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cu.cleverboy.secretmessageis.Model.Contact;
import com.cu.cleverboy.secretmessageis.Model.Key;
import com.cu.cleverboy.secretmessageis.Model.Message;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "key";
    public static final String TABLE_NAME = "keyuser";
    public static final String ID = "id";
    public static final String PUBLIC_REF = "public_ref";
    public static final String PRIVATE_REF = "private_ref";
    public static final String PRIVATE_REAL = "private_real";
    public static final String PUBLIC_REAL = "public_real";

    public static final String content_table = "content";
    public static final String cid= "cid";
    public static final String content_name = "name";
    public static final String content_phone = "phone";
    public static final String reference_key = "reference_key";
    public static final String content_key = "publicKey";

    public static final String INBOX_TABLE = "inbox";
    public static final String CONTACT_ID = "contact_id";
    public static final String MESSAGE = "message";
    public static final String RECEIVE_DATE = "receive_date";

    public static final String OUTBOX_TABLE = "outbox";
    public static final String SEND_DATE = "send_date";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"+ PUBLIC_REF+" text ,"+ PUBLIC_REAL +" text , "+ PRIVATE_REF +" Text, "+ PRIVATE_REAL+" text )");

       }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public void createContactTable(){
        SQLiteDatabase database = getWritableDatabase();
        String createTable = "CREATE TABLE "+content_table+" ( " + cid + " INTEGER PRIMARY KEY AUTOINCREMENT ,"+ content_name+" TEXT , " + content_phone +" TEXT , "+ reference_key + " Text ,"+ content_key +" Text )";
        database.execSQL(createTable);
    }
    public void createInboxTable(){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "Create table "+ INBOX_TABLE+ " ( id Integer primary key autoincrement ,"+ CONTACT_ID + " Text , "+MESSAGE+" text , "+RECEIVE_DATE+" text )";
        database.execSQL(sql);
    }

    public void createOutboxTable(){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "Create table "+ OUTBOX_TABLE+" ( id Integer primary key autoincrement ,"+ CONTACT_ID + " Text , "+MESSAGE+" text , "+SEND_DATE +" text)";
        database.execSQL(sql);
    }

    public boolean insertOutboxMessage(String id, Message message, String date){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACT_ID, id );
        contentValues.put(MESSAGE, message.getText());
        contentValues.put(SEND_DATE, date);

        long result = database.insert(OUTBOX_TABLE, null, contentValues);
        database.close();
        if (result == -1)
            return false;
        else
            return true;

    }

    public boolean insertInboxMessage(String id, Message message, String date){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACT_ID, id );
        contentValues.put(MESSAGE, message.getText());
        contentValues.put(RECEIVE_DATE, date);

        long result = database.insert(INBOX_TABLE, null, contentValues);
        database.close();
        if (result == -1)
            return false;
        else
            return true;

    }

    public boolean insertContact(Contact c){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(content_name , c.getName());
        contentValues.put(content_phone, c.getPhone());
        contentValues.put(reference_key, c.getReference_key());
        contentValues.put(content_key, c.getPublic_key());
        long result = sqLiteDatabase.insert(content_table,null,contentValues);
        sqLiteDatabase.close();

        if (result == -1){
            return false;
        } else {
            return true;
        }

    }

    public List<Message> getOutboxMessage(){
        List<Message> messageList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ OUTBOX_TABLE , null);
        if (cursor != null && cursor.moveToFirst()){
            do{
                messageList.add(new Message(cursor.getString(1),cursor.getString(2), cursor.getString(3)));
            }while (cursor.moveToNext());

        }

        return messageList;
    }

    public List<Message> getInboxMessage(){
        List<Message> messageList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ INBOX_TABLE , null);
        if (cursor != null && cursor.moveToFirst()){
            do{
                messageList.add(new Message(cursor.getString(1),cursor.getString(2), cursor.getString(3)));
            }while (cursor.moveToNext());

        }

        return messageList;
    }

    public List<Contact> selectContact(){
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ content_table , null);
        if (cursor != null && cursor.moveToFirst()){
            do{
                contactList.add(new Contact(cursor.getString(1),cursor.getString(2), cursor.getString(4), cursor.getString(3)));
            }while (cursor.moveToNext());

        }

        return contactList;
    }

    public boolean insertKey(String public_ref , String private_ref, String public_real, String private_real){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PUBLIC_REF , public_ref);
        contentValues.put(PUBLIC_REAL , public_real);
        contentValues.put(PRIVATE_REF , private_ref);
        contentValues.put(PRIVATE_REAL , private_real);
        long result = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        sqLiteDatabase.close();

        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public Key getKey(){
        Key key=null;
        int id ;
        String privateKey , publicKey , privateRef, publicRef;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_NAME , null);
        if (cursor != null && cursor.moveToFirst()){
            do{
                id = cursor.getInt(0);
                publicRef = cursor.getString(1);
                publicKey = cursor.getString(2);
                privateRef = cursor.getString(3);
                privateKey = cursor.getString(4);

                key = new Key(id,privateRef,publicRef,privateKey,publicKey);
            }while (cursor.moveToNext());

        }
       // key = new Key(1,12,123);
        return key;
    }

    public int getKeyCount(){
        int count=0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT count(*) FROM "+ TABLE_NAME , null);
        if (cursor != null && cursor.moveToFirst()){
            do{
                count = cursor.getInt(0);
            }while (cursor.moveToNext());

        }
        return count;
    }
    public Contact getOneContact(String phone){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Contact contact = null;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ content_table + " where "+content_phone+" = '"+phone +"'", null);
        if (cursor != null && cursor.moveToFirst()){
            do{
                contact = new Contact(cursor.getString(1),cursor.getString(2), cursor.getString(4), cursor.getString(3));
            }while (cursor.moveToNext());

        }
        return contact;
    }


}
