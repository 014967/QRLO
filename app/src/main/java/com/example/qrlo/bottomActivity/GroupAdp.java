package com.example.qrlo.bottomActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrlo.R;

import java.util.ArrayList;

class GroupAdp extends RecyclerView.Adapter<GroupAdp.ViewHolder>{


    private Context context;
    ArrayList<String> arrayListGroup;


    GroupAdp(Context context, ArrayList<String> arrayListGroup)
    {
        this.context = context;
        this.arrayListGroup = arrayListGroup;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_group,parent, false);


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
