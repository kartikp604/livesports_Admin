package com.example.livesportsadmin;

import androidx.annotation.NonNull;

public class ClassScheduleid {

    public String Scheduleid;
    public <T extends ClassScheduleid> T withId(@NonNull final String id){
        this.Scheduleid=id;
        return (T) this;
    }
}
