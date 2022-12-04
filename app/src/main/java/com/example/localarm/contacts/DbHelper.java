package com.example.localarm.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    Context context;
    List<ContactModel> contacts;

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactdata";

    // Country table name
    private static final String TABLE_NAME= "contacts";
    //alarm table name
    private static final String TABLE_NAME2= "alarms";
    //connector table name
    private static final String TABLE_NAME3= "connector";

    // Country Table Columns names
    private static final String KEY_ID = "id";
    private static final String NAME = "Name";
    private static final String PHONENO = "PhoneNo";
    private static final String EMERGENCY = "Emergency";

    //alarm table columns names
    private static final String ALARM_ID = "id";
    private static final String ALARM_NAME = "alarmName";
    private static final String ALARM_DESC = "alarmDesc";
    //private static final String ALARM_CONT = "alarmCont";
    // add contact list

    //connector table columns
    private static final String C_ALARM_ID = "c_alarm_id";
    private static final String C_CONTACT_ID = "c_contact_id";


    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
                + PHONENO + " TEXT UNIQUE," + EMERGENCY + " INTEGER " + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
        String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_NAME2 + "("
                + ALARM_ID + " INTEGER PRIMARY KEY," + ALARM_NAME + " TEXT,"
                + ALARM_DESC + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_ALARMS_TABLE);
        String CREATE_CONNECTOR_TABLE = "CREATE TABLE " + TABLE_NAME3 + "("
                + C_ALARM_ID + " INTEGER ," + C_CONTACT_ID + " INTEGER " + ")";
        sqLiteDatabase.execSQL(CREATE_CONNECTOR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        onCreate(db);
    }

    public void addcontact(ContactModel contact){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put(NAME,contact.getName());
        c.put(PHONENO,contact.getPhoneNo());
        c.put(EMERGENCY, contact.getEmergency());
        db.insert(TABLE_NAME,null,c);
        db.close();
    }

    public void addAlContact(ContactModel contact){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c=new ContentValues();
        String contactName = contact.getName();
        String contactNum = contact.getPhoneNo();
        Log.i("hello", " hi" +contactName + " " + contactNum);
        //String UPDATE_ALARM = "UPDATE " + TABLE_NAME2 + " SET " + ALARM_NAME + " = '" + alarmName + "' ," + ALARM_DESC + " = '" + alarmDesc  + "' WHERE " + ALARM_ID + " = " + alarmId + ";";
        String ADD_CON = "INSERT INTO " + TABLE_NAME  +" VALUES (10, '" + contactName + "', '" + contactNum + "', " + "0)";
        db.execSQL(ADD_CON);
    }
    //method to add alarms to database
    public void addAlarm(AlarmModel alarm){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put(ALARM_NAME,alarm.getName());
        c.put(ALARM_DESC,alarm.getDesc());
        db.insert(TABLE_NAME2,null,c);
        db.close();
    }

    public void updateAlarm(AlarmModel alarm){
        SQLiteDatabase db=this.getWritableDatabase();
        int alarmId = alarm.getId();
        String alarmName = alarm.getName();
        String alarmDesc = alarm.getDesc();
        String UPDATE_ALARM = "UPDATE " + TABLE_NAME2 + " SET " + ALARM_NAME + " = '" + alarmName + "' ," + ALARM_DESC + " = '" + alarmDesc  + "' WHERE " + ALARM_ID + " = " + alarmId + ";";
        db.execSQL(UPDATE_ALARM);
    }

    // method to retrieve all the contacts in List
    public List<ContactModel> getAllContacts(){
        List<ContactModel> list=new ArrayList<>();
        String query="SELECT * FROM "+TABLE_NAME + " WHERE "+ EMERGENCY + " = 1" + ";";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()) {
            do {
                list.add(new ContactModel(c.getInt(0),c.getString(1),c.getString(2)));
            } while (c.moveToNext());
        }
        return list;
    }

    // method to retrieve all the contacts in List
    public List<AlarmModel> getAllAlarms(){
        List<AlarmModel> list=new ArrayList<>();
        String query="SELECT * FROM "+TABLE_NAME2;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()) {
            do {
                list.add(new AlarmModel(c.getInt(0),c.getString(1),c.getString(2)));
            } while (c.moveToNext());
        }
        return list;
    }

    // get the count of data, this will allow user
    // to not add more that five contacts in database
    public int count(){
        int count=0;
        String query="SELECT COUNT(*) FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.getCount()>0){
            c.moveToFirst();
            count=c.getInt(0);
        }
        c.close();
        return count;
    }

    // Deleting single country
    public void deleteContact(ContactModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i=db.delete(TABLE_NAME,KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }

}
