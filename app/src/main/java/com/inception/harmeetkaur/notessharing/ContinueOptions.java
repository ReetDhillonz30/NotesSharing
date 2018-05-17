package com.inception.harmeetkaur.notessharing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ContinueOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_options);

        SharedPreferences sp_editor = getSharedPreferences("app_info" , MODE_PRIVATE);



        if(sp_editor.getString("user_type" , "").equals("admin"))
        {
            Intent i = new Intent(ContinueOptions.this , AdminHomepage.class);

            startActivity(i);

            finish();

        }

        else if(sp_editor.getString("user_type" , "").equals("user")) {

            Intent i = new Intent(ContinueOptions.this , UserHomeActivity.class);

            startActivity(i);

            finish();

        }

    }

    public void user_login(View view) {

        startActivity(new Intent(ContinueOptions.this , UserLoginActivity.class));
        finish();

    }

    public void admin_login(View view) {

        startActivity(new Intent(ContinueOptions.this , AdminLogin.class));
        finish();


    }
}
