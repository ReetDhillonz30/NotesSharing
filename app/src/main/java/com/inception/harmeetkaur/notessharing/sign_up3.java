package com.inception.harmeetkaur.notessharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.inception.harmeetkaur.notessharing.datamodels.user_profile_data;

public class sign_up3 extends AppCompatActivity {


    private Spinner department_spinner , session_spinner ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

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
    public void sign_up1(View view) {

        EditText editText = findViewById(R.id.email1);
        EditText editText1 = findViewById(R.id.password1);

        EditText name_et = findViewById(R.id.name_et);

        final String name = name_et.getText().toString();

        final String email = editText.getText().toString();

        final String department = department_spinner.getSelectedItem().toString();

        final String session = session_spinner.getSelectedItem().toString();



        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

        }
        else{
            Toast.makeText(sign_up3.this , "invalid email syntax", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = editText1.getText().toString();
        if (password.length()>=8 && password.length()<33)
        {

        }
        else{
            Toast.makeText(sign_up3.this,"password must be between 8 to 32 characters",Toast.LENGTH_SHORT).show();
            return;
        }

        if(department.equals("Select Department"))
        {

            Toast.makeText(sign_up3.this,"department not selected",Toast.LENGTH_SHORT).show();
            return;

        }

        if(session.equals("Select Session"))
        {
            Toast.makeText(sign_up3.this,"session not selected",Toast.LENGTH_SHORT).show();
            return;

        }


        final ProgressDialog progress_bar = new ProgressDialog(sign_up3.this);
        progress_bar.setTitle("please wait");
        progress_bar.setMessage("Creating Account....");
        progress_bar.show();
        FirebaseAuth f_auth = FirebaseAuth.getInstance();

        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progress_bar.hide();
                if (task.isSuccessful())

                {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    user_profile_data data = new user_profile_data(name , department , session);

                    database.getReference().child("users").child(email.replace(".","")).setValue(data);


                    Toast.makeText(sign_up3.this, "done", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(sign_up3.this ,UserLoginActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(sign_up3.this, "error try again", Toast.LENGTH_SHORT).show();
                }
            }
        };


        f_auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);


    }
}

 