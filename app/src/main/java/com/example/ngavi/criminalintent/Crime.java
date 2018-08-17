package com.example.ngavi.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    //class that will set up individual crime objects
    private UUID mID;
    private String mTitle;
    private Date mDate;
    private boolean msolved;
    private boolean mRequiresPolice;

    public Crime(){
        mID = UUID.randomUUID();
        mDate = new Date();
    }


    //right click --> generate--> getter and setter shortcut
    public UUID getID() {
        return mID;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return msolved;
    }

    public void setSolved(boolean solved) {
        this.msolved = solved;
    }

    public boolean getRequiresPolice(){
        return mRequiresPolice;
    }
    public void setRequiresPolice(boolean RequiresPolice){
        mRequiresPolice = RequiresPolice;
    }
}
