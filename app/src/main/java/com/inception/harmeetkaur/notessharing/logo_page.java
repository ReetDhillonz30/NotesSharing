package com.inception.harmeetkaur.notessharing;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class logo_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_page);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(logo_page.this, ContinueOptions.class);
                startActivity(i);
                overridePendingTransition(R.anim.fader_in, R.anim.fade_out);
                finish();
            }
        }, 3000);

    }

}
