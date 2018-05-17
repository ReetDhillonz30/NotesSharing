package com.inception.harmeetkaur.notessharing;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inception.harmeetkaur.notessharing.datamodels.notes_details_data;

public class Adding_notes extends AppCompatActivity {

    EditText department_et , session_et , title_et , description_et  ;

    RadioGroup select_notes_type;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_notes);

        title_et = findViewById(R.id.title);
        description_et = findViewById(R.id.description);

        department_et = findViewById(R.id.department);

        session_et = findViewById(R.id.session);

        get_department();
    }

    private void get_department()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();

       String email =  auth.getCurrentUser().getEmail();

        database.getReference().child("users").child(email.replace(".","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

              department_et.setText( dataSnapshot.child("department").getValue().toString() );

              session_et.setText( dataSnapshot.child("session").getValue().toString() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void upload_notes(View view) {

        String title = title_et.getText().toString();

        String description = description_et.getText().toString();

        String session = session_et.getText().toString();

        String department = department_et.getText().toString();

        RadioGroup radioGroup = findViewById(R.id.select_notes_type);

        RadioButton selected_radio_btn = findViewById(radioGroup.getCheckedRadioButtonId());

        String type = selected_radio_btn.getText().toString();

        notes_details_data data = new notes_details_data(title , description , department , session , type , "u");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String email = auth.getCurrentUser().getEmail();

        long current_time = System.currentTimeMillis();

        database.getReference().child("notes").child(department+"_"+session).child(email.replace(".","")).child(String.valueOf(current_time)).setValue(data);


        if(type.equals("PDF"))
        {
            Intent i = new Intent(Adding_notes.this,uploading_pdf_files.class);

            i.putExtra("current_time" , current_time);

            startActivity(i);
            finish();
        }

        if(type.equals("IMAGE"))
        {
            Intent i = new Intent(Adding_notes.this,uploading_images.class);

            i.putExtra("current_time" , current_time);

            startActivity(i);
            finish();
        }

        if(type.equals("VIDEO"))
        {
            Intent i = new Intent(Adding_notes.this,UploadVideoActivity.class);

            i.putExtra("current_time" , current_time);

            startActivity(i);
            finish();
        }
    }
}
