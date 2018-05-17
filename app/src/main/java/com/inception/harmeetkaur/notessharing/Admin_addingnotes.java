package com.inception.harmeetkaur.notessharing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.inception.harmeetkaur.notessharing.datamodels.notes_details_data;

public class Admin_addingnotes extends AppCompatActivity {
    private Spinner department_spinner , session_spinner ;
    EditText  title_et , description_et  ;
    RadioGroup select_notes_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_addingnotes);

        select_notes_type = findViewById(R.id.select_notes_type);

        title_et = findViewById(R.id.title);
        description_et = findViewById(R.id.description);

        department_spinner = findViewById(R.id.department_spinner);

        session_spinner = findViewById(R.id.session_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.department_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        department_spinner.setAdapter(adapter);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> session_adapter = ArrayAdapter.createFromResource(this,
                R.array.session_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        session_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        session_spinner.setAdapter(session_adapter);
    }

    public void upload_notes(View view) {

        String title = title_et.getText().toString();

        String description = description_et.getText().toString();

        String session = session_spinner.getSelectedItem().toString();

        String department = department_spinner.getSelectedItem().toString();

        RadioGroup radioGroup = findViewById(R.id.select_notes_type);

        RadioButton selected_radio_btn = findViewById(radioGroup.getCheckedRadioButtonId());

        String type = selected_radio_btn.getText().toString();

        notes_details_data data = new notes_details_data(title , description , department , session , type , "a");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String email = auth.getCurrentUser().getEmail();

        long current_time = System.currentTimeMillis();

        System.out.println(department+"_"+session);

        database.getReference().child("notes").child(department+"_"+session).child("harmeet@gmailcom").child(String.valueOf(current_time)).setValue(data);


        if(type.equals("PDF"))
        {
            Intent i = new Intent(Admin_addingnotes.this,uploading_pdf_files.class);

            i.putExtra("current_time" , current_time);

            startActivity(i);
            finish();
        }

        if(type.equals("IMAGE"))
        {
            Intent i = new Intent(Admin_addingnotes.this,uploading_images.class);

            i.putExtra("current_time" , current_time);

            startActivity(i);
            finish();
        }

        if(type.equals("VIDEO"))
        {
            Intent i = new Intent(Admin_addingnotes.this,UploadVideoActivity.class);

            i.putExtra("current_time" , current_time);

            startActivity(i);
            finish();
        }
    }


}
