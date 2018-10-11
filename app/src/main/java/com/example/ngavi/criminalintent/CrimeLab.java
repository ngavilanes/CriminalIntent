package com.example.ngavi.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ngavi.criminalintent.database.CrimeBaseHelper;
import com.example.ngavi.criminalintent.database.CrimeCursorWrapper;

import java.util.LinkedHashMap;
import java.util.UUID;

import static com.example.ngavi.criminalintent.database.CrimeDBSchema.*;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    //private LinkedHashMap<UUID,Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){ //method that will provide CrimeLab unless it is null then it will create a new CrimeLab
        if(sCrimeLab==null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){ // private constructor(singleton) for crimelab that will hold every crime
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
      //  mCrimes = new LinkedHashMap<UUID, Crime>();
//
    }

    public LinkedHashMap<UUID, Crime> getCrimes(){
        LinkedHashMap<UUID,Crime> mCrimes = new LinkedHashMap<>();
         CrimeCursorWrapper cursor = queryCrime(null, null); //null means going through all rows of db

         //cursor will traverse through database to get all crimes and store in linked hashmap
         try{
             cursor.moveToFirst(); //moves to first row of database
             while(!cursor.isAfterLast()){ //cursor is after the last row of db --> end loop
                 mCrimes.put(cursor.getCrime().getID(),cursor.getCrime()); //storing crime and its id to hashmap
                 cursor.moveToNext();
             }
         }
         finally {
             cursor.close();
         }



        return mCrimes;
    }

    public Crime getCrime(UUID id){

        CrimeCursorWrapper cursor = queryCrime(CrimeTable.Cols.UUID + " = ?",new String[] {id.toString()});

        try{
            if(cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();

        }
        finally {
            cursor.close();
        }


    }

    public void addCrime(Crime crime){
        ContentValues values = getContentValues(crime);
        crime.setTitle("Crime #");
        mDatabase.insert(CrimeTable.NAME,null,values);


   //     mCrimes.put(crime.getID(),crime);

    }

    public void deleteCrime(Crime crime){

        mDatabase.delete(CrimeTable.NAME,CrimeTable.Cols.UUID + " = ?", new String[] {crime.getID().toString()});


     //   mCrimes.remove(crime.getID());
    }


    private static ContentValues getContentValues(Crime crime){ //converting values of crime into values suitable to be written onto database
        //key value pairs - column names = key | crime info = value
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getID().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1:0);
        values.put(CrimeTable.Cols.SUSPECT,crime.getSuspect());

        return values;



    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getID().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME,values,CrimeTable.Cols.UUID + " = ?", new String[] {uuidString}); //using uuid as where clause to find crime to be updated
    }

    private CrimeCursorWrapper queryCrime(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, //null means getting every column
                whereClause,
                whereArgs,
                null,
                null,
                null );

        return new CrimeCursorWrapper(cursor);

    }






}
