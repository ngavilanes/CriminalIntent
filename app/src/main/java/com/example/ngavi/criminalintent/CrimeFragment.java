package com.example.ngavi.criminalintent;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

import static android.widget.CompoundButton.*;

public class CrimeFragment extends Fragment{
    //always have no more than 2-3 fragments on a single page/activity
    private static final String ARG_CRIME_ID ="crime_id";
    private Crime mcrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;



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
        mcrime = CrimeLab.get(getActivity()).getCrime(id);
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

        mDateButton =(Button) v.findViewById(R.id.crime_date);
        mDateButton.setText(mcrime.getDate().toString());
        mDateButton.setEnabled(false);

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mcrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mcrime.setSolved(isChecked);

            }
        });



        return v;



    }
}
