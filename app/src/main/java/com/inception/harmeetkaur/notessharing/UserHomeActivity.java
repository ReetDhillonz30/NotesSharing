package com.inception.harmeetkaur.notessharing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inception.harmeetkaur.notessharing.datamodels.fav;
import com.inception.harmeetkaur.notessharing.datamodels.notes_details_data;
import com.inception.harmeetkaur.notessharing.datamodels.user_profile_data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserHomeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<notes_details_data> notes_list;
    ArrayList<String> fav_notes_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_layout);
        notes_list = new ArrayList<>();
        fav_notes_list = new ArrayList<>();
        recyclerView = findViewById(R.id.notes_me_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(UserHomeActivity.this, LinearLayoutManager.VERTICAL, false));


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

                            notes_details_data data_with_time = new notes_details_data(data.title, data.description, data.department, data.session, data.type, snap2.getKey(), data.status);
                            notes_list.add(data_with_time);
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

        database.getReference().child("favorite").child(email.replace(".", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notes_list.clear();
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

        database.getReference().child("users").child(email.replace(".", "")).addListenerForSingleValueEvent(new ValueEventListener() {
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
        Intent i = new Intent(UserHomeActivity.this, Search_activity.class);
        startActivity(i);
    }

    public void favorite(View view) {
        Intent i = new Intent(UserHomeActivity.this, favorite_notes.class);
        startActivity(i);

    }

    public void share_app(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out amazing notes sharing   app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    public class view_holder extends RecyclerView.ViewHolder {

        TextView notes_title, notes_description, time;
        ImageView favorite;

        LinearLayout cell_layout ;

        public view_holder(View itemView) {
            super(itemView);
            favorite = itemView.findViewById(R.id.favorite);
            notes_title = itemView.findViewById(R.id.notes_title);

            notes_description = itemView.findViewById(R.id.notes_description);

            time = itemView.findViewById(R.id.notes_date);

            cell_layout = itemView.findViewById(R.id.cell_layout);
        }
    }


    public class Adapter extends RecyclerView.Adapter<view_holder> {

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new view_holder(LayoutInflater.from(UserHomeActivity.this).inflate(R.layout.single_notes_cell, parent, false));
        }

        @Override
        public void onBindViewHolder(final view_holder holder, int position) {


            final notes_details_data data = notes_list.get(position);

            if (fav_notes_list.contains(data.time)) {
                holder.favorite.setImageDrawable(getResources().getDrawable(R.drawable.star));

                holder.favorite.setTag("fav");
            } else {
                holder.favorite.setTag("not");
                holder.favorite.setImageDrawable(getResources().getDrawable(R.drawable.star_blank));
            }
            holder.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(holder.favorite.getTag().equals("not")) {

                        holder.favorite.setImageDrawable(getResources().getDrawable(R.drawable.star));
                        FirebaseAuth f_auth = FirebaseAuth.getInstance();
                        String email = f_auth.getCurrentUser().getEmail().replace(".", "");
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        database.getReference().child("favorite").child(email).child(data.time).child("key").setValue(data.time);
                        Toast.makeText(UserHomeActivity.this, "done", Toast.LENGTH_SHORT).show();

                        holder.favorite.setTag("fav");

                    }
                    else {

                        holder.favorite.setImageDrawable(getResources().getDrawable(R.drawable.star_blank));
                        FirebaseAuth f_auth = FirebaseAuth.getInstance();
                        String email = f_auth.getCurrentUser().getEmail().replace(".", "");
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        database.getReference().child("favorite").child(email).child(data.time).child("key").setValue(null);
                        Toast.makeText(UserHomeActivity.this, "done", Toast.LENGTH_SHORT).show();
                        holder.favorite.setTag("not");


                    }

                }
            });
            holder.notes_title.setText(data.title);

            holder.notes_description.setText(data.description);

            holder.time.setText(convertTime(Long.parseLong(data.time)));
            holder.cell_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (data.type.equals("IMAGE")) {
                        Intent i = new Intent(UserHomeActivity.this, Show_images_notes.class);

                        i.putExtra("images_key", data.time);

                        startActivity(i);
                    }

                    if (data.type.equals("PDF")) {
                        Intent i = new Intent(UserHomeActivity.this, ShowPdfActivity.class);

                        i.putExtra("images_key", data.time);

                        startActivity(i);
                    }

                    if (data.type.equals("VIDEO")) {
                        Intent i = new Intent(UserHomeActivity.this, ShowVideoActivity.class);

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


    public void edit(View view) {
        Intent i = new Intent(UserHomeActivity.this, Edit_profile.class);
        startActivity(i);
    }

    public void logout(View view) {


        SharedPreferences.Editor sp_editor = getSharedPreferences("app_info", MODE_PRIVATE).edit();

        sp_editor.clear().commit();


        Intent i = new Intent(UserHomeActivity.this, ContinueOptions.class);
        startActivity(i);
        finish();


    }

    public void move_next(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    public void feed(View view) {
        Intent i = new Intent(UserHomeActivity.this, feedback.class);
        startActivity(i);
    }

    public void Add(View view) {
        Intent i = new Intent(UserHomeActivity.this, Adding_notes.class);
        startActivity(i);
    }

    public void notes_by_me(View view) {

        Intent i = new Intent(UserHomeActivity.this, NotesUploadedByMe.class);

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
        get_fav_notes();

    }
}
