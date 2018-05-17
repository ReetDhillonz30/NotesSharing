package com.inception.harmeetkaur.notessharing;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
    }

    public void reset_pass(View view) {
        EditText email = findViewById(R.id.email_new);
        String email_s =email.getText().toString();
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        Auth.sendPasswordResetEmail(email_s).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Forget_password.this,"password link sent",Toast.LENGTH_SHORT).show();
                }
            }

            });

    }
}
