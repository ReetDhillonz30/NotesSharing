package com.inception.harmeetkaur.notessharing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inception.harmeetkaur.notessharing.datamodels.notes_details_data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Search_activity extends AppCompatActivity {
    RecyclerView recyclerView;

    ArrayList<notes_details_data> notes_list;

    ArrayList<notes_details_data> filtered_notes_list;

    private SearchView searchView;

    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity);

        searchView = findViewById(R.id.search_view);

        adapter = new Adapter();

        filtered_notes_list = new ArrayList<>();

        notes_list = new ArrayList<>();

        recyclerView = findViewById(R.id.notes_me_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(Search_activity.this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if(s.length() == 0)
                {
                    filtered_notes_list.clear();
                    adapter.notifyDataSetChanged();

                    return true;
                }
                filter(s);
                return false;
            }
        });

    }

    private void get_data_from_firebase(String department_name) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String email = auth.getCurrentUser().getEmail();

        database.getReference().child("notes").child(department_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    for (DataSnapshot snap2 : snap.getChildren()) {

                        notes_details_data data = snap2.getValue(notes_details_data.class);

                        if (data.status.equals("a")) {
                            notes_details_data data_with_time = new notes_details_data(data.title, data.description, data.department, data.session, data.type, snap2.getKey(), data.status);
                            notes_list.add(data_with_time);
                         }

                    }
                }

                adapter.notifyDataSetChanged();
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

    public void search_notes(View view) {
        Intent i = new Intent(Search_activity.this, Search_activity.class);
        startActivity(i);
    }


    public class view_holder extends RecyclerView.ViewHolder {

        TextView notes_title, notes_description, time;

        public view_holder(View itemView) {
            super(itemView);

            notes_title = itemView.findViewById(R.id.notes_title);

            notes_description = itemView.findViewById(R.id.notes_description);

            time = itemView.findViewById(R.id.notes_date);
        }
    }


    public class Adapter extends RecyclerView.Adapter<view_holder> {

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new view_holder(LayoutInflater.from(Search_activity.this).inflate(R.layout.single_notes_cell, parent, false));
        }

        @Override
        public void onBindViewHolder(Search_activity.view_holder holder, int position) {


            final notes_details_data data = filtered_notes_list.get(position);

            holder.notes_title.setText(data.title);

            holder.notes_description.setText(data.description);

            holder.time.setText(convertTime(Long.parseLong(data.time)));
            holder.notes_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (data.type.equals("IMAGE")) {
                        Intent i = new Intent(Search_activity.this, Show_images_notes.class);

                        i.putExtra("images_key", data.time);

                        startActivity(i);
                    }

                    if (data.type.equals("PDF")) {
                        Intent i = new Intent(Search_activity.this, ShowPdfActivity.class);

                        i.putExtra("images_key", data.time);

                        startActivity(i);
                    }

                    if (data.type.equals("VIDEO")) {
                        Intent i = new Intent(Search_activity.this, ShowVideoActivity.class);

                        i.putExtra("images_key", data.time);

                        startActivity(i);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return filtered_notes_list.size();
        }
    }


    public void edit(View view) {
        Intent i = new Intent(Search_activity.this, Edit_profile.class);
        startActivity(i);
    }

    public void logout(View view) {


        SharedPreferences.Editor sp_editor = getSharedPreferences("app_info", MODE_PRIVATE).edit();

        sp_editor.clear().commit();


        Intent i = new Intent(Search_activity.this, UserLoginActivity.class);
        startActivity(i);
        finish();


    }

    public void move_next(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    public void feed(View view) {
        Intent i = new Intent(Search_activity.this, feedback.class);
        startActivity(i);
    }

    public void Add(View view) {
        Intent i = new Intent(Search_activity.this, Adding_notes.class);
        startActivity(i);
    }

    public void notes_by_me(View view) {

        Intent i = new Intent(Search_activity.this, NotesUploadedByMe.class);

        startActivity(i);

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

        get_department();
    }


    public void move_back5(View view) {

        finish();
    }

    public void filter(String s) {

        filtered_notes_list.clear();

        for (notes_details_data data : notes_list) {
            if (data.title.toLowerCase().startsWith(s.toLowerCase())) {
                filtered_notes_list.add(data);
            }
        }

        adapter.notifyDataSetChanged();

    }
}
