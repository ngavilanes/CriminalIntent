package com.example.ngavi.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ngavi.criminalintent.database.CrimeDBSchema;
import com.example.ngavi.criminalintent.database.CrimeDBSchema.CrimeTable; //allows us to refer to string constants without needing entirety of call to database

public class CrimeBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME ="crimeBase.db";

    public CrimeBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION); //calling SQLiteOpenHelper class to initialize database
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + CrimeTable.NAME + "(" + "_id integer primary key autoincrement, " + CrimeTable.Cols.UUID + ", " +CrimeTable.Cols.DATE
               + ", " + CrimeTable.Cols.TITLE + ", " + CrimeTable.Cols.SOLVED + ", " + CrimeTable.Cols.SUSPECT +  ")"



        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
