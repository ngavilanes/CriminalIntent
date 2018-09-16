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
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private static ViewPager mViewPager;
    private LinkedHashMap<UUID, Crime> mCrimes;
    private static final String EXTRA_CRIME_ID = "com.example.ngavi.criminalintent.crime_id";
    public static int mpos;
    private Button mEndButton;
    private Button mFrontButton;
    public UUID id;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = findViewById(R.id.crime_view_pager); //Viewpagers allow you to seamless flip between detail fragments by swiping
        mCrimes = CrimeLab.get(this).getCrimes();


        FragmentManager fm = getSupportFragmentManager(); //manages fragment views to be displayed by placing them correctly


        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) { //manages conversation with ViewPager
            @Override
            public Fragment getItem(int position) { //creating the fragments to be displayed by creating new instances of them using CrimeID and binding them to CrimePagerActivity
                Crime crime = (Crime)mCrimes.values().toArray()[position];
                mpos = position; //mpos not changing after getting frag
               return CrimeFragment.newInstance(crime.getID());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        id =(UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);  //getting id of crime from intent that was pressed on the list
        mpos = new ArrayList<Crime>(mCrimes.values()).indexOf(mCrimes.get(id));
        mViewPager.setCurrentItem(mpos);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(mViewPager.getCurrentItem() == 0){
                    mFrontButton.setEnabled(false);

                }
                else if(mViewPager.getCurrentItem()==mCrimes.size()-1){
                    mEndButton.setEnabled(false);
                }
                else{
                    mEndButton.setEnabled(true);
                    mFrontButton.setEnabled(true);
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mEndButton = findViewById(R.id.jump_End_Button);
        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mCrimes.size()-1);
                mFrontButton.setEnabled(true);
            }
        });

        mFrontButton = findViewById(R.id.jump_Front_Button);
        mFrontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
                mEndButton.setEnabled(true);
            }
        });


    }
    public static Intent NewIntent(Context packageContext, UUID id){
        Intent intent = new Intent(packageContext,CrimePagerActivity.class); //Intent is passing data from CrimeListFragment to CrimeActivity in order to send crime data to CrimeFragment
        intent.putExtra(EXTRA_CRIME_ID,id); //passing Crime ID to crimeFragment to update fields
        return intent;
    }



}
