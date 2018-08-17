package com.example.ngavi.criminalintent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {   //abstract classes-> you cannot create any singleFragmentActivity objects only implement methods
    protected abstract Fragment createFragment(); // placeholder method that must be implemented by the subclass that extends SingleFragmentActivity (abstract)

    @Override // all classes that extend SingleFragmentActivity will have this override executed
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        FragmentManager fm = getSupportFragmentManager();


        android.support.v4.app.Fragment fragment = fm.findFragmentById(R.id.fragment_container); //from activity_fragment_container.xml
        if (fragment == null) {
            fragment = createFragment(); //create fragment determined by subclass type of framgent
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();

        }

    }
}
