package com.example.ngavi.criminalintent;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.UUID;

import static android.widget.CompoundButton.*;

public class CrimeFragment extends Fragment{
    //always have no more than 2-3 fragments on a single page/activity
    private static final String ARG_CRIME_ID ="crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private Crime mcrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mSuspectButton;
    private Button mSendCrimeReportButton;
    private CheckBox mSeriousCheckBox;
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT =1;





    public static CrimeFragment newInstance(UUID CrimeId){ //constructor called from CrimeActivity to create CrimeFragment object and bind to crime id
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,CrimeId);
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mcrime = new Crime();
       // UUID id = (UUID)getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID id = (UUID)getArguments().getSerializable(ARG_CRIME_ID); //getting id from attached arguments instead of associated activity(Keeps fragment independent)
        mcrime = CrimeLab.get(getActivity()).getCrime(id); //using the id put into the arguments to obtain the crime from crimelab
        setHasOptionsMenu(true);  //MUST ALWAYS SET TRUE WHEN THERE IS A MENU LAYOUT TO BE INFLATED
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_crime,container,false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mcrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mcrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        mDateButton =(Button) v.findViewById(R.id.crime_date); //We will place a dialog feature on the date button so users can change the date
        updateDate();
        mDateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mcrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE); //tells target what receiving the result of picking a date

                dialog.show(fm, DIALOG_DATE); // tag uniquely identifies DialogFragment in the list of fragments managed by the fragment manager
            }
        });





        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mcrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mcrime.setSolved(isChecked);

            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.suspect_button);
        mSuspectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact,REQUEST_CONTACT);



            }
        });
        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY)==null){
            mSuspectButton.setEnabled(false);
        }

        if(mcrime.getSuspect() != null){
            mSuspectButton.setText("SUSPECT: " + mcrime.getSuspect());
        }


        mSendCrimeReportButton = v.findViewById(R.id.send_crime_report_button);
        mSendCrimeReportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 ShareCompat.IntentBuilder.from(getActivity()).setType("text/plain").setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject)).setChooserTitle(getString(R.string.send_report)).startChooser();
//

            }
        });









//        mSeriousCheckBox = (CheckBox) v.findViewById(R.id.crime_serious);
//        mSolvedCheckBox.setChecked(mcrime.getRequiresPolice());
//        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//              //  mcrime.setRequiresPolice(isChecked);
//
//            }
//        });





        return v;



    }
    //end of onCreateView

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //getting the data for date after picking from the calendar dialogue
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mcrime.setDate(date);
            updateDate();
        }
        else if(requestCode == REQUEST_CONTACT && data!= null){
            Uri contactUri = data.getData();
            //specify what fields you want query to return values for

            String[] queryFields = new String[] {ContactsContract.Contacts.DISPLAY_NAME};

            //perfrom query --contacturi serves as a "where"

            Cursor c = getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);

            try{ //double check if you got results
                if(c.getCount()==0){
                    return;
                }
                //pull out first column of first row of data -thats suspect's name
                c.moveToFirst();
                String suspect =c.getString(0);
                mcrime.setSuspect(suspect);
                mSuspectButton.setText("SUSPECT: " + mcrime.getSuspect());

            }
            finally {
                c.close();
            }





        }

    }

    private void updateDate() {
        mDateButton.setText(mcrime.getDate().toString());
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.delete_crime){
            CrimeLab crimeLab = CrimeLab.get(getActivity());
            crimeLab.deleteCrime(mcrime);
            getActivity().finish();
            return true;


        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private String getCrimeReport(){
        String solvedString = null;
        if(mcrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }
        else{
            solvedString=getString(R.string.crime_report_unsolved);
        }

        SimpleDateFormat df =  new SimpleDateFormat("EEE, MMM dd");
        String dateString = df.format(mcrime.getDate());

        String suspect = mcrime.getSuspect();
        if(suspect==null){
            suspect = getString(R.string.crime_report_no_suspect);

        }
        else{
            suspect = getString(R.string.crime_report_suspect,mcrime.getSuspect());
        }

        String Report = getString(R.string.crime_report,mcrime.getTitle(),dateString,solvedString,suspect);
        return Report;
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mcrime);
    }


}
