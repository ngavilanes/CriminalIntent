package com.example.ngavi.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    private static final String EXTRA_CRIME_ID = "com.example.ngavi.criminalintent.crime_id";

    @Override
    protected Fragment createFragment() { //this is the extending aspect of SingleFragmentActivity --> having access to createFragment class and implementing it your own way
       UUID ID = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID); //getting id from extra
        return CrimeFragment.newInstance(ID); //passing ID to CrimeFragment in order to set arguments that bind ID to Fragment

    }

    public static Intent NewIntent(Context packageContext, UUID id){
        Intent intent = new Intent(packageContext,CrimeActivity.class); //Intent is passing data from CrimeListFragment to CrimeActivity in order to send crime data to CrimeFragment
        intent.putExtra(EXTRA_CRIME_ID,id); //passing Crime ID to crimeFragment to update fields
        return intent;
    }
}
