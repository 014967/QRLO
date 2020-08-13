package com.example.qrlo.bottomActivity;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrlo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class GroupAdp extends RecyclerView.Adapter<GroupAdp.ViewHolder>{




    FirebaseUser user;
    FirebaseAuth mAuth;
    Context context;
    ArrayList<Group> arrayListGroup;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    String when;


    GroupAdp(Context context, ArrayList<Group> arrayListGroup)
    {
        this.context = context;
        this.arrayListGroup = arrayListGroup;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_group,parent, false);


        return new GroupAdp.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        database =FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("users"); // DB테이블 연결 작업
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();



        databaseReference.child(user.getUid()).child("history").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                long count = snapshot.getChildrenCount();

             //   ArrayList<>
                for(DataSnapshot dataSnapshotChildren : snapshot.getChildren())
                {
                Group group =dataSnapshotChildren.getValue(Group.class);
                Date d = new Date(group.getWhen());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                when = format.format(d);
                holder.Date.setText(arrayListGroup.get(position).when);

                }
            }



            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.visitHistory.setLayoutManager(layoutManager);
  //      holder.visitHistory.setAdapter









    }

    @Override
    public int getItemCount() {
        return arrayListGroup.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Date;
        RecyclerView visitHistory;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            Date = itemView.findViewById(R.id.Date);
            visitHistory = itemView.findViewById(R.id.visitHistory);






        }
    }

}
