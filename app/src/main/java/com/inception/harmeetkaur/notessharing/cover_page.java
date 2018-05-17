package com.inception.harmeetkaur.notessharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class cover_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_page);


        SharedPreferences sp_editor = getSharedPreferences("app_info" , MODE_PRIVATE);



        if(sp_editor.getString("user_type" , "").equals("admin"))
        {
            Intent i = new Intent(cover_page.this , AdminHomepage.class);

            startActivity(i);

        }

        else if(sp_editor.getString("user_type" , "").equals("user")) {

            Intent i = new Intent(cover_page.this , UserHomeActivity.class);

            startActivity(i);

        }


    }

    public void create(View view) {

        final ProgressDialog progress_bar = new ProgressDialog(cover_page.this);
        progress_bar.setTitle("please wait");
        progress_bar.setMessage("Creating group....");
        progress_bar.show();
        Intent i = new Intent(cover_page.this ,Details.class);
        startActivity(i);

        }


            public void join(View view) {

                final ProgressDialog progress_bar = new ProgressDialog(cover_page.this);
                progress_bar.setTitle("please wait");
                progress_bar.setMessage("Joining group....");
                progress_bar.show();
                Intent i = new Intent(cover_page.this, Join_1.class);
                startActivity(i);
            }
        };

