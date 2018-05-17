package com.inception.harmeetkaur.notessharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inception.harmeetkaur.notessharing.datamodels.notes_details_data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminHomepage extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<notes_details_data> notes_list;

    ProgressDialog pd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);

        pd = new ProgressDialog(AdminHomepage.this);

        pd.setTitle("Loading");
        pd.setMessage("Please Wait..");
        pd.show();

        notes_list = new ArrayList<>();

        recyclerView = findViewById(R.id.notes_me_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(AdminHomepage.this , LinearLayoutManager.VERTICAL , false));



    }

    private void get_data_from_firebase( )
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String email = auth.getCurrentUser().getEmail();

        database.getReference().child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                notes_list.clear();

                for(DataSnapshot snap0 : dataSnapshot.getChildren()) {

                    for (DataSnapshot snap : snap0.getChildren()) {
                        for (DataSnapshot snap2 : snap.getChildren()) {
                            notes_details_data data = snap2.getValue(notes_details_data.class);

                            notes_details_data data_with_time = new notes_details_data(data.title, data.description, data.department, data.session, data.type, snap2.getKey() , data.status , snap.getKey());
                            notes_list.add(data_with_time);
                        }
                    }
                }

                pd.hide();

                recyclerView.setAdapter(new Adapter());

            }

            @Override

            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }






    public void notes_added(View view) {
        Intent i = new Intent(AdminHomepage.this,notes_uploadedbyadmin.class);
        startActivity(i);
    }

    public void logout(View view) {

        SharedPreferences.Editor sp_editor = getSharedPreferences("app_info" , MODE_PRIVATE).edit();

        sp_editor.clear().commit();

        Intent i = new Intent(AdminHomepage.this,ContinueOptions  .class);
        startActivity(i);

        finish();
    }

    public void view_feedback(View view) {
        Intent i = new Intent(AdminHomepage.this,feedback_admin.class);
        startActivity(i);
    }


    public class view_holder extends RecyclerView.ViewHolder
    {

        TextView notes_title , notes_description , time , department , session , user_email ;

        Button approve_notes ;

        LinearLayout cell_layout ;

        public view_holder(View itemView) {
            super(itemView);

            notes_title = itemView.findViewById(R.id.notes_title);

            notes_description = itemView.findViewById(R.id.notes_description);

            time = itemView.findViewById(R.id.notes_date);

            department = itemView.findViewById(R.id.notes_department);

            session = itemView.findViewById(R.id.notes_session);

            approve_notes = itemView.findViewById(R.id.approve_btn);

            user_email = itemView.findViewById(R.id.user_email);

            cell_layout = itemView.findViewById(R.id.cell_layout);
        }
    }


    public class Adapter extends RecyclerView.Adapter<view_holder>
    {

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new view_holder(LayoutInflater.from(AdminHomepage.this).inflate(R.layout.single_notes_cell_admin , parent , false));
        }

        @Override
        public void onBindViewHolder(final AdminHomepage.view_holder holder, int position) {


            final notes_details_data data = notes_list.get(position);

            holder.notes_title.setText(data.title);

            holder.notes_description.setText(data.description);

            holder.time.setText(convertTime(Long.parseLong(data.time)));

            holder.department.setText(data.department);

            holder.session.setText(data.session);

            holder.user_email.setText(data.email);




            holder.cell_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(data.type.equals("IMAGE"))
                    {
                        Intent i = new Intent(AdminHomepage.this, Show_images_notes.class);

                        i.putExtra("images_key", data.time);

                        startActivity(i);
                    }

                    if(data.type.equals("PDF"))
                    {
                        Intent i = new Intent(AdminHomepage.this , ShowPdfActivity.class);

                        i.putExtra("images_key" , data.time);

                        startActivity(i);
                    }

                    if(data.type.equals("VIDEO"))
                    {
                        Intent i = new Intent(AdminHomepage.this , ShowVideoActivity.class);

                        i.putExtra("images_key" , data.time);

                        startActivity(i);
                    }
                }
            });

            if(data.status.equals("a"))
            {
                holder.approve_notes.setText("DELETE");
            }

            else
            {
                holder.approve_notes.setText("APPROVE");
            }
            holder.approve_notes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(holder.approve_notes.getText().toString().toLowerCase().equals("delete"))
                    {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        database.getReference().child("notes").child(data.department + "_" + data.session).child(data.email.replace(".", "")).child(data.time).setValue(null);

                        Toast.makeText(AdminHomepage.this, "notes deleted", Toast.LENGTH_SHORT).show();



                    }
                    else {
                        notes_details_data dat = new notes_details_data(data.title, data.description, data.department, data.session, data.type, "a");

                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        database.getReference().child("notes").child(data.department + "_" + data.session).child(data.email.replace(".", "")).child(data.time).setValue(dat);

                        Toast.makeText(AdminHomepage.this, "notes approved", Toast.LENGTH_SHORT).show();

                        holder.approve_notes.setText("DELETE");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return notes_list.size();
        }
    }



    public void add(View view) {
        Intent i = new Intent(AdminHomepage.this,Admin_addingnotes.class);
        startActivity(i);
    }

    public void move_next1(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.openDrawer(Gravity.LEFT);
    }


    public String convertTime(long yourmilliseconds)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(yourmilliseconds);
        System.out.println(sdf.format(resultdate));

        return String.valueOf(sdf.format(resultdate));
    }

    @Override
    protected void onResume() {
        super.onResume();

        get_data_from_firebase();
    }
}
