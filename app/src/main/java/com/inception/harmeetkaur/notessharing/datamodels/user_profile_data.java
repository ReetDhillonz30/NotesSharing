package com.inception.harmeetkaur.notessharing.datamodels;

/**
 * Created by Harmeet kaur on 11-04-2018.
 */

public class user_profile_data {

    public String name , department , session;


    public user_profile_data()
    {

    }

    public user_profile_data(String name , String department , String session)
    {
        this.name = name;
        this.department = department;
        this.session = session;
    }
}
