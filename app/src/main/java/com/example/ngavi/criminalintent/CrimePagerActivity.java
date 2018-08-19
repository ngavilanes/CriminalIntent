package com.example.ngavi.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private LinkedHashMap<UUID, Crime> mCrimes;
    private static final String EXTRA_CRIME_ID = "com.example.ngavi.criminalintent.crime_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = findViewById(R.id.crime_view_pager); //Viewpagers allow you to seamless flip between detail fragments by swiping
        mCrimes = CrimeLab.get(this).getCrimes();


        FragmentManager fm = getSupportFragmentManager(); //manages fragment views to be displayed by placing them correctly


        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) { //creating the fragments to be displayed by creating new instances of them using CrimeID and binding them to CrimePagerActivity
                Crime crime = (Crime)mCrimes.values().toArray()[position];
               return CrimeFragment.newInstance(crime.getID());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        UUID id =(UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        int pos = new ArrayList<Crime>(mCrimes.values()).indexOf(mCrimes.get(id));
        mViewPager.setCurrentItem(pos);

    }
    public static Intent NewIntent(Context packageContext, UUID id){
        Intent intent = new Intent(packageContext,CrimePagerActivity.class); //Intent is passing data from CrimeListFragment to CrimeActivity in order to send crime data to CrimeFragment
        intent.putExtra(EXTRA_CRIME_ID,id); //passing Crime ID to crimeFragment to update fields
        return intent;
    }
}
