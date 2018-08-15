package com.example.ngavi.criminalintent;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment extends android.support.v4.app.Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        UpdateUI();
        return view;

    }

    private void UpdateUI(){ //communicating between recycler view and adapter
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }





    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextview;
        private Crime mcrime;


        public CrimeHolder(LayoutInflater inflater, ViewGroup parent){ //Crimeholder constructor
            super(inflater.inflate(R.layout.list_item_crime, parent, false));

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextview = (TextView)itemView.findViewById(R.id.crime_date);

            itemView.setOnClickListener(this);



        }
        public void Bind(Crime crime){
            mcrime = crime;
            mTitleTextView.setText(mcrime.getTitle());
            mDateTextview.setText(mcrime.getDate().toString());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),mcrime.getTitle()+ " clicked!",Toast.LENGTH_SHORT).show();
        }



    }




    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> Crimes){
            mCrimes = Crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //called by recyclerview when a viewholder is needed to display an item
           LayoutInflater layoutinflater = LayoutInflater.from(getActivity());
           return new CrimeHolder(layoutinflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.Bind(crime);

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
