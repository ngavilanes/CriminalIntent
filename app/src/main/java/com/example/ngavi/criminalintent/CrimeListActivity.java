package com.example.ngavi.criminalintent;


import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {  //this is the extending aspect of SingleFragmentActivity --> having access to createFragment class methods and implementing it your own way or leave as is
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
