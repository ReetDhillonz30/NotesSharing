package com.inception.harmeetkaur.notessharing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inception.harmeetkaur.notessharing.datamodels.fav;
import com.inception.harmeetkaur.notessharing.datamodels.notes_details_data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class favorite_notes extends AppCompatActivity {
    RecyclerView recyclerView;
    boolean check = true;
    ArrayList<notes_details_data> notes_list;
    ArrayList<String> fav_notes_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_notes);

        notes_list = new ArrayList<>();
        fav_notes_list = new ArrayList<>();
        recyclerView = findViewById(R.id.notes_fav_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(favorite_notes.this, LinearLayoutManager.VERTICAL, false));

    }

    private void get_data_from_firebase(String department_name) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String email = auth.getCurrentUser().getEmail();

        database.getReference().child("notes").child(department_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                notes_list.clear();

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    for (DataSnapshot snap2 : snap.getChildren()) {

                        notes_details_data data = snap2.getValue(notes_details_data.class);

                        if (data.status.equals("a")) {

                            if (fav_notes_list.contains(snap2.getKey())) {
                                notes_details_data data_with_time = new notes_details_data(data.title, data.description, data.department, data.session, data.type, snap2.getKey(), data.status);
                                notes_list.add(data_with_time);
                            }
                        }

                    }
                }

                recyclerView.setAdapter(new Adapter());

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void get_fav_notes() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String email = auth.getCurrentUser().getEmail();

        database.getReference().child("favorite").child(email.replace(".", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                fav_notes_list.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    fav fav_key = data.getValue(fav.class);
                    fav_notes_list.add(fav_key.key);
                }

                get_department();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void get_department() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String email = auth.getCurrentUser().getEmail();

        database.getReference().child("users").child(email.replace(".", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String departmentSession = dataSnapshot.child("department").getValue().toString() + "_" + dataSnapshot.child("session").getValue().toString();

                get_data_from_firebase(departmentSession);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void move_back8(View view) {
        Intent i = new Intent(favorite_notes.this, UserHomeActivity.class);
        startActivity(i);
    }

    public class view_holder extends RecyclerView.ViewHolder {

        TextView notes_title, notes_description, time;
        ImageView favorite;

        public view_holder(View itemView) {
            super(itemView);
            favorite = itemView.findViewById(R.id.favorite);
            notes_title = itemView.findViewById(R.id.notes_title);

            notes_description = itemView.findViewById(R.id.notes_description);

            time = itemView.findViewById(R.id.notes_date);
        }
    }


    public class Adapter extends RecyclerView.Adapter<favorite_notes.view_holder> {

        @Override
        public favorite_notes.view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new view_holder(LayoutInflater.from(favorite_notes.this).inflate(R.layout.single_notes_cell, parent, false));
        }


        @Override
        public void onBindViewHolder(final favorite_notes.view_holder holder, int position) {


            final notes_details_data data = notes_list.get(position);
            if (fav_notes_list.contains(data.time)) {
                holder.favorite.setImageDrawable(getResources().getDrawable(R.drawable.star));
            } else {
                holder.favorite.setImageDrawable(getResources().getDrawable(R.drawable.star_blank));
            }
            holder.notes_title.setText(data.title);

            holder.notes_description.setText(data.description);

            holder.time.setText(convertTime(Long.parseLong(data.time)));
            holder.notes_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (data.type.equals("IMAGE")) {
                        Intent i = new Intent(favorite_notes.this, Show_images_notes.class);

                        i.putExtra("images_key", data.time);

                        startActivity(i);
                    }

                    if (data.type.equals("PDF")) {
                        Intent i = new Intent(favorite_notes.this, ShowPdfActivity.class);

                        i.putExtra("images_key", data.time);

                        startActivity(i);
                    }

                    if (data.type.equals("VIDEO")) {
                        Intent i = new Intent(favorite_notes.this, ShowVideoActivity.class);

                        i.putExtra("images_key", data.time);

                        startActivity(i);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return notes_list.size();
        }
    }

    public String convertTime(long yourmilliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(yourmilliseconds);
        System.out.println(sdf.format(resultdate));

        return String.valueOf(sdf.format(resultdate));
    }

    @Override
    protected void onResume() {
        super.onResume();

        get_fav_notes();
    }
}