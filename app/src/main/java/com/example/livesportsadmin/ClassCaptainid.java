package com.example.livesportsadmin;

import androidx.annotation.NonNull;

public class ClassCaptainid {

    public String Captainid;
    public <T extends ClassCaptainid> T withId(@NonNull final String id){
        this.Captainid=id;
        return (T) this;
    }
}
