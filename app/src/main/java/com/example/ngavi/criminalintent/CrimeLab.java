package com.example.ngavi.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){ //method that will provide CrimeLab unless it is null then it will create a new CrimeLab ArrayList
        if(sCrimeLab==null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private  CrimeLab(Context context){ // private constructor(singleton) for crimelab that will hold every crime
        mCrimes = new ArrayList<>();
        for(int i = 0; i<100; i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i%2==0); //every other crime solved
            mCrimes.add(crime);
        }
    }

    public List<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){

       for(Crime crime : mCrimes){  // for each loop
           if(crime.getID().equals(id)){
               return crime;
           }
       }
       return null;

    }


}
