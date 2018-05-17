package com.inception.harmeetkaur.notessharing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.inception.harmeetkaur.notessharing.datamodels.feedback_model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class feedback extends AppCompatActivity {
    EditText feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback = findViewById(R.id.feedbackk);

    }

    public void done1(View view) {
      String feed=  feedback.getText().toString();
        FirebaseAuth faauth = FirebaseAuth.getInstance();
        String email = faauth.getCurrentUser().getEmail().replace(".", "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        feedback_model data = new feedback_model(date,feed,email);
        database.getReference().child("feedback").child(email).child(date).setValue(data);
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
        finish();
    }
}
