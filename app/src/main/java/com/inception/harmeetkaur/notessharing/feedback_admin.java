package com.inception.harmeetkaur.notessharing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inception.harmeetkaur.notessharing.datamodels.feedback_model;

import java.util.ArrayList;

public class feedback_admin extends AppCompatActivity {
    ArrayList<feedback_model> feedback_list;
    RecyclerView feedback_recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_admin);
        feedback_list = new ArrayList();
        feedback_recycler = findViewById(R.id.feedback_recycler);
        feedback_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
get_feedback_list();
    }

    private void get_feedback_list() {
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("feedback").addListenerForSingleValueEvent(new ValueEventListener() {


                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    feedback_list.clear();


                                                                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                                                        for (DataSnapshot data2 : data.getChildren()) {
                                                                                            feedback_model details = data2.getValue(feedback_model.class);
                                                                                            feedback_list.add(details);

                                                                                            Adapter adapter = new Adapter();

                                                                                            feedback_recycler.setAdapter(adapter);

                                                                                        }
                                                                                    }
                                                                                }
                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }



                                                                            }
        );
    }
    public class view_holder extends RecyclerView.ViewHolder{

        TextView email,date,msg;

        public view_holder(View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.feedback_email);
            date = itemView.findViewById(R.id.feedback_date);
            msg = itemView.findViewById(R.id.feedback_msg);

        }
    }

    public class Adapter extends RecyclerView.Adapter<view_holder>
    {

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {

            view_holder v = new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_cell,parent , false ));

            return v ;
        }

        @Override
        public void onBindViewHolder(view_holder holder, int position) {


            final feedback_model data=feedback_list.get(position);
            holder.email.setText(data.email);
            holder.date.setText(data.date);
            holder.msg.setText(data.feedback);
        }

        @Override
        public int getItemCount() {
            return feedback_list.size();
        }
    }
}

