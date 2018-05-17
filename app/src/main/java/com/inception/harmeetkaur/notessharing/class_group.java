package com.inception.harmeetkaur.notessharing;

/**
 * Created by Harmeet kaur on 09-04-2018.
 */

public class class_group {
    public String email , name,classname,department_name,session,contact_number,passcode;
    class_group()
    {

    }
    class_group(String email , String name,String classname,String department_name,String session, String contact_number, String passcode){
        this.name=name;
        this.classname = classname;
        this.department_name =  department_name;
        this.session = session;
        this.contact_number = contact_number;
        this.passcode = passcode;

        this.email = email ;

    }
}
