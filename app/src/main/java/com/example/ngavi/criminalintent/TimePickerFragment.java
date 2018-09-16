package com.example.ngavi.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment{
    public static final String ARG_TIME= "time";
    private Date mTime;
    private TimePicker mTimePicker;
    public static final String EXTRA_TIME = "com.example.ngavi.criminalintent.Time";
    public static int year,month,day;
    public static TimePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TIME,date);
        TimePickerFragment ins = new TimePickerFragment();
        ins.setArguments(bundle);
        return ins;

    }

    private void sendResult (int resultCode, Date date){
        if(getTargetFragment()==null){
            return;
        }
        else{
            Intent intent = new Intent();
            intent.putExtra(EXTRA_TIME,date);
            getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);

        }
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) { //when getting the original time- send in the date from the datepicker args
        mTime = (Date)getArguments().getSerializable(ARG_TIME);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mTime);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
         year = calendar.get(Calendar.YEAR);
         month = calendar.get(Calendar.MONTH);
         day = calendar.get(Calendar.DAY_OF_MONTH);
      // int sec = calendar.get(Calendar.SECOND);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);

        mTimePicker = v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(min);
//        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(mTime);
//
//                mTime.setHours(hourOfDay);
//                mTime.setMinutes(minute);
//            }
//        });

        return new AlertDialog.Builder(getActivity()).setView(v).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int hour = mTimePicker.getCurrentHour();
                Log.d("Time","This is the current hour " + hour);
               int minute= mTimePicker.getCurrentMinute();
               Log.d("Time","This is the current minute " +minute );

               Date date = new GregorianCalendar(year, month, day, hour, minute).getTime();


                sendResult(Activity.RESULT_OK,date);
            }
        }).create();








    }
}

