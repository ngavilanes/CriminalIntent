package com.example.ngavi.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
    //dialogs demand input from users
    public static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "com.example.ngavi.criminalintent.date";
    private DatePicker mDatePicker;
    private static final int REQUEST_TIME = 1;
    private static final String DIALOG_TIME = "time";
    private static Date inputDate;


    public static DatePickerFragment newInstance (Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(args);

        return datePickerFragment;




    }




    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Date date = (Date)getArguments().getSerializable(ARG_DATE);
         //must  create calendar to get date for month, year, day from Date object
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);




        //inflating the actual date dialog box
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date,null); //its easy to make changes to a layout and inflate as it will make the changes instantly

        mDatePicker = v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year,month , day, null);

        return new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.date_picker_title).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) { //override method for dialog builder changing the date to the date the user picked
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                inputDate = new GregorianCalendar(year,month,day).getTime();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment mTimePicker = TimePickerFragment.newInstance(inputDate);
                mTimePicker.setTargetFragment(DatePickerFragment.this, REQUEST_TIME);
                mTimePicker.show(fm, DIALOG_TIME);

                Log.d("debug", "Timepicker ended");



                Log.d("debug", "About to send result to CrimeFRAGMENT");

            }

        }).create();
        //positive button - what user should press to accept the dialog
    }

    private void sendResult (int resultCode, Date date){
        if(getTargetFragment()==null){
            return;
        }
        else{
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATE,date);
            getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_TIME) {
            Log.d("debug", "Using the intent");
            Date dateExtra = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            inputDate = dateExtra;
            sendResult(Activity.RESULT_OK, inputDate);  //sending result to crimefragment class


        }
    }
}
