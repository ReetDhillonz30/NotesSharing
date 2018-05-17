package com.inception.harmeetkaur.notessharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
    }
    public void log_in(View view) {
        EditText editText = findViewById(R.id.email);
        EditText editText1 = findViewById(R.id.password);
        String email = editText.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

        }
        else{
            Toast.makeText(AdminLogin.this , "invalid email syntax",Toast.LENGTH_SHORT).show();

            return;
        }
        String password = editText1.getText().toString();
        if (password.length()>=8 && password.length()<33)
        {

        }
        else{
            Toast.makeText(AdminLogin.this,"password must be between 8 to 32 characters",Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progress_bar = new ProgressDialog(AdminLogin.this);
        progress_bar.setTitle("please wait");
        progress_bar.setMessage("Logging in....");
        progress_bar.show();


        if(email.equals("harmeet@gmail.com") && password.equals("123456789"))
        {
            SharedPreferences.Editor sp_editor = getSharedPreferences("app_info" , MODE_PRIVATE).edit();

            sp_editor.putString("user_type" , "admin");

            sp_editor.commit();

            Intent i = new Intent(AdminLogin.this , AdminHomepage.class);

            startActivity(i);

            finish();
        }

      /*  FirebaseAuth f_auth = FirebaseAuth.getInstance();

        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progress_bar.hide();
                if (task.isSuccessful())

                {
                    Toast.makeText(AdminLogin.this, "done", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AdminLogin.this , AdminHomepage.class);
                    startActivity(i);
                } else {
                    Toast.makeText(AdminLogin.this, "error try again", Toast.LENGTH_SHORT).show();
                }
            }
        };


        f_auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);*/


    }
}