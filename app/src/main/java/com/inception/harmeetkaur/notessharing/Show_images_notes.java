package com.inception.harmeetkaur.notessharing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inception.harmeetkaur.notessharing.datamodels.notesUrls;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Show_images_notes extends AppCompatActivity {

    ZoomageView selectedImage;

    private ArrayList<String> url_list ;

    Gallery gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images_notes);

        url_list = new ArrayList<>();

        gallery = (Gallery) findViewById(R.id.gallery);
        selectedImage= findViewById(R.id.imageView);
        gallery.setSpacing(1);


        get_images();

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // show the selected Image

                Picasso.get().load(url_list.get(position)).into(selectedImage);
            }
        });

    }


    public class GalleryImageAdapter extends BaseAdapter {
        private Context mContext;


        public GalleryImageAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return url_list.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }


        // Override this method according to your need
        public View getView(int index, View view, ViewGroup viewGroup) {
            // TODO Auto-generated method stub
            ImageView i = new ImageView(mContext);

            Picasso.get().load(url_list.get(index)).into(i);
            i.setLayoutParams(new Gallery.LayoutParams(200, 200));

            i.setScaleType(ImageView.ScaleType.FIT_XY);


            return i;
        }


    }


    private void get_images()
    {


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("images_url").child(getIntent().getStringExtra("images_key")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    notesUrls urls = dataSnapshot.getValue(notesUrls.class);

                    String[] ur = urls.urls.split(",");

                    for ( int i = 0 ; i < ur.length ; i++)
                    {
                        if(ur[i].length() > 10)
                        {
                            url_list.add(ur[i]);
                        }
                    }





                final GalleryImageAdapter galleryImageAdapter= new GalleryImageAdapter(Show_images_notes.this);
                gallery.setAdapter(galleryImageAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }


    }
