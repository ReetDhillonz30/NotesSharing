package com.inception.harmeetkaur.notessharing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inception.harmeetkaur.notessharing.datamodels.notes_details_data;
import com.inception.harmeetkaur.notessharing.datamodels.user_profile_data;

public class Edit_profile extends AppCompatActivity {
    EditText name , email_id , department_name , session ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        name = findViewById(R.id.name);
        email_id = findViewById(R.id.email_id);
        department_name = findViewById(R.id.dept);
        session = findViewById(R.id.session);

        get_data();
    }


    public void get_data()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        final String email = auth.getCurrentUser().getEmail().replace(".","");


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("users").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user_profile_data  data = dataSnapshot.getValue(user_profile_data.class);

                name.setText(data.name);
                department_name.setText(data.department);
                session.setText(data.session);

                email_id.setText(email);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void done(View view) {


        String name_s = name.getText().toString();

        String email = email_id.getText().toString();

        String department = department_name.getText().toString();

        String session_s = session.getText().toString();

        user_profile_data data  =  new user_profile_data(name_s , department ,session_s);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("users").child(email).setValue(data);

        Toast.makeText(Edit_profile.this , "profile updated successfully" , Toast.LENGTH_SHORT).show();

    }

    public void move_back1(View view) {
        finish();
    }
}
