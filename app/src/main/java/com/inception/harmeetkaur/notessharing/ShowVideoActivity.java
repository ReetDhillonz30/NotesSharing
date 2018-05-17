package com.inception.harmeetkaur.notessharing;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inception.harmeetkaur.notessharing.datamodels.Upload;

public class ShowVideoActivity extends AppCompatActivity {


    private VideoView videoView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);

        videoView = findViewById(R.id.videoView);

        get_video_from_firebase();
    }


    private void get_video_from_firebase()
    {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("video_urls").child(getIntent().getStringExtra("images_key")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Upload video_url = dataSnapshot.getValue(Upload.class);

                String str = video_url.getUrl();
                Uri uri = Uri.parse(str);

                videoView.setVideoURI(uri);
                videoView.setVisibility(View.VISIBLE);
                videoView.requestFocus();
                videoView.start();




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
