package com.example.qrlo.bottomActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qrlo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DateInfoAdp extends RecyclerView.Adapter<DateInfoAdp.ViewHolder> {


    private ArrayList<DateInfo> arrayList;
    Context context;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String TAG = "Recylcer";


    public DateInfoAdp(ArrayList<DateInfo> arrayList, Context context)
    {
        this.arrayList = arrayList;
        this.context = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.list_visit,parent, false);
        ViewHolder holder =new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {




        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        //String localTime = sdf.format(new Date(arrayList.get(position).getWhen()));


        holder.visitTime.setText(sdf.format(new Date(arrayList.get(position).getWhen())));
        holder.visitWhere.setText(arrayList.get(position).getWhere());
        Log.d(TAG, "URL = " + arrayList.get(position).getWherelogo());
        Glide.with(holder.IVlogo.getContext()).load(arrayList.get(position).getWherelogo()).into(holder.IVlogo);






    }

    @Override
    public int getItemCount() {
        if(arrayList != null) return arrayList.size();
        else return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

    TextView visitTime;
    TextView visitWhere;
    ImageView IVlogo;





        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            visitTime = itemView.findViewById(R.id.visitTime);
            visitWhere =itemView.findViewById(R.id.visitWhere);
            IVlogo = itemView.findViewById(R.id.IVlogo);


        }
    }



}
