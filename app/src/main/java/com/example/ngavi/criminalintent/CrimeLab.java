package com.example.ngavi.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private LinkedHashMap<UUID,Crime> mCrimes;

    public static CrimeLab get(Context context){ //method that will provide CrimeLab unless it is null then it will create a new CrimeLab ArrayList
        if(sCrimeLab==null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){ // private constructor(singleton) for crimelab that will hold every crime
      //  mCrimes= new L
        mCrimes = new LinkedHashMap<UUID, Crime>();
        for(int i = 0; i<100; i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i%2==0); //every other crime solved
            crime.setRequiresPolice(i%5==0);
            mCrimes.put(crime.getID(),crime);
//
//
//
        }
    }

    public LinkedHashMap<UUID, Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){

  /*    //  Crime crime =  mCrimes.get(mCrimes.indexOf(id)); //possibly enhance searching of crime
     //   if(crime!=null){
      //      return crime;
        }
        else{
            return null;
        }*/
   /*  *//*  for(Crime crime : mCrimes){  // for each loop
           if(crime.getID().equals(id)){
               return crime;
           }*//*
       }*/
   //  return null;

       return  mCrimes.get(id);
    }

    public void addCrime(Crime crime){
        crime.setTitle("Crime #");

        mCrimes.put(crime.getID(),crime);

    }

    public void deleteCrime(Crime crime){
        mCrimes.remove(crime.getID());
    }





}
