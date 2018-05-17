package com.inception.harmeetkaur.notessharing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Details extends AppCompatActivity {
    EditText name , class_name , department_name , session , contact_number , passcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name = findViewById(R.id.name_1);
        class_name = findViewById(R.id.class_name);
        department_name = findViewById(R.id.dept_1);
        session = findViewById(R.id.sesion_1);
        contact_number = findViewById(R.id.cont_no_1);
        passcode = findViewById(R.id.passcode);

    }

    public void save(View view) {
        String name_s = name.getText().toString();
        String class_name_s = class_name.getText().toString();
        String department_name_s = department_name.getText().toString();
        String session_s = session.getText().toString();
        String contact_number_s = contact_number.getText().toString();
        String passcode_s = passcode.getText().toString();



        Intent i = new Intent(Details.this ,UserHomeActivity.class);
        startActivity(i);

        FirebaseAuth Auth = FirebaseAuth.getInstance();


       class_group data = new class_group(Auth.getCurrentUser().getEmail() ,name_s, class_name_s, department_name_s, session_s,  contact_number_s, passcode_s);


        FirebaseDatabase database = FirebaseDatabase.getInstance();



        database.getReference().child("class_group").child(class_name_s+"_"+session_s).setValue(data);

    }

}
