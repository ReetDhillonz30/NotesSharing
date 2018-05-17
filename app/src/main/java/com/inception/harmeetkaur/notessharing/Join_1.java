package com.inception.harmeetkaur.notessharing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Join_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_1);
    }

    public void join_group(View view) {
        Intent i = new Intent(Join_1.this ,UserHomeActivity.class);
        startActivity(i);
    }
}
