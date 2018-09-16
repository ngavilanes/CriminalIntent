package com.example.ngavi.criminalintent;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

public class CrimeListFragment extends android.support.v4.app.Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    public int mLastPositionClicked;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState!= null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false); //converting xml file to java view objects
        setHasOptionsMenu(true);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view); //using fragment_crime_list view to find the recyclerview ID
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //recyclerView require LayoutManagers to work properly--> it positions objects and manages scrolling
        //LinearLayoutManager - positions items in the list vertically

        UpdateUI();
        return view;

    }

    @Override
    public void onResume() { //must override because you cannot assume current activity is stopped when another activity is called - safest place to update fragment view
        super.onResume();
        Log.d("ViewTAG", "View closed updatng UI");

        UpdateUI(); //checks for changes to the Crime detail view

    }


    public void UpdateUI() { //communicating between recycler view and adapter
        CrimeLab crimeLab = CrimeLab.get(getActivity()); //getting the static crimelab from the fragment's associated activity
      //  List<Crime> crimes = crimeLab.getCrimes();
        LinkedHashMap<UUID,Crime> crimes = crimeLab.getCrimes();

        if(mAdapter==null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else{
            mAdapter.notifyItemChanged(mLastPositionClicked); //detects changes to UI (title change)

        }

        updateSubtitle();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else{
            subtitleItem.setTitle(R.string.show_subtitle);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.new_crime){
            Crime crime = new Crime();
            CrimeLab.get(getActivity()).addCrime(crime);
            Intent intent = CrimePagerActivity.NewIntent(getActivity(), crime.getID());
            startActivity(intent);
            return true;
        }
        else if(item.getItemId()==R.id.show_subtitle){
            mSubtitleVisible = !mSubtitleVisible;
            getActivity().invalidateOptionsMenu();
            updateSubtitle();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);

    }




    private abstract class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //fields determined by the list fragment layout (fragment_crime_list.xml)
        private TextView mTitleTextView;
        private TextView mDateTextview;
        private Crime mcrime;
        private ImageView mImageView;
        private int mposition;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent, int layoutid) { //Crimeholder constructor

            super(inflater.inflate(layoutid, parent, false));   //inflating xml file of custom row element with superclass constructor--> provides itemview (reference to view you just passed)

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextview = (TextView) itemView.findViewById(R.id.crime_date);
            mImageView =(ImageView) itemView.findViewById(R.id.crime_solved);

            itemView.setOnClickListener(this); //creates listener for entire itemview/list_item_crime instance/row of recycler_view
        }




        public void Bind(Crime crime, int position) { //binding the current viewHolder ItemView with its specific crime sent by the Adapter
            mcrime = crime;
            mposition = position;
            mTitleTextView.setText(mcrime.getTitle());
           // mDateTextview.setText(mcrime.getDate().toString());
           mDateTextview.setText(mcrime.getParsedDate(mcrime.getDate()));
            mImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }


        @Override
        public void onClick(View v) { //when each viewholder row is touched the specific crime will show --> will use Intent to start Detail activity
            //Toast.makeText(getActivity(), mcrime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = new CrimePagerActivity().NewIntent(getActivity(), mcrime.getID()); //calling new intent method from crimePager
            startActivity(intent);
            mLastPositionClicked = mposition;


        }
        public void UpdateDate(){ //AHHHHHH
            mDateTextview.setText(mcrime.getParsedDate(mcrime.getDate()));
        }




    }
        //-------------------------------------------------------------------------------------------------------------------------------------//

        //ViewHolder for Serious items
    //
    private class SeriousCrimeHolder extends CrimeHolder {

            private Button mButton;


            public SeriousCrimeHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater,parent,R.layout.list_item_crime_serious);
                mButton = (Button) itemView.findViewById(R.id.serious_crime_button);
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),"Calling the Police...", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }

    private class RegularCrimeHolder extends CrimeHolder{
        public RegularCrimeHolder(LayoutInflater inflater, ViewGroup parent){
          //  super(inflater.inflate(R.layout.list_item_crime, parent,false));
            super(inflater,parent,R.layout.list_item_crime);
        }

    }












    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private LinkedHashMap<UUID,Crime> mCrimes;

        public CrimeAdapter(LinkedHashMap<UUID,Crime> Crimes){
            mCrimes = Crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //called by recyclerview when a viewholder is needed to display an item
           LayoutInflater layoutinflater = LayoutInflater.from(getActivity());
           if(viewType==0){ //requires police- serious
               return new SeriousCrimeHolder(layoutinflater,parent);
           }
           else{
               return new RegularCrimeHolder(layoutinflater, parent); //used to construct new crimeholder using LayoutInflater just created

           }



        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            //Crime crime = mCrimes.get(position);
            Crime crime = (Crime) mCrimes.values().toArray()[position];
            holder.Bind(crime, position);

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) { //used to determine what type of view holder to inflate
          // Crime mcrime = mCrimes.get(position);
            Crime mcrime = (Crime) mCrimes.values().toArray()[position];
           if(mcrime.getRequiresPolice()){
               return 0;

           }
           else{
               return 1;
           }
        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }
}
