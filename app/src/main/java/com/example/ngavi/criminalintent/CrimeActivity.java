package com.example.ngavi.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CrimeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() { //this is the extending aspect of SingleFragmentActivity --> having access to createFragment class and implementing it your own way
        return new CrimeFragment();
    }
}
