package com.example.qrlo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class my_qr_adapter extends RecyclerView.Adapter<my_qr_adapter.ViewHolder> {
    private ArrayList<my_qr_item> mData = null;

    my_qr_adapter(ArrayList<my_qr_item> list)
    {
        mData = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView title;
        TextView desc;

        ViewHolder(View itemView){
            super(itemView);

            icon = itemView.findViewById(R.id.my_qr_item_image);
            title = itemView.findViewById(R.id.my_qr_item_txt1);
            desc = itemView.findViewById(R.id.my_qr_item_txt2);


        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.my_qr_item, parent, false);
        my_qr_adapter.ViewHolder vh = new my_qr_adapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        my_qr_item item = mData.get(position);

        holder.icon.setImageDrawable(item.getIcon());
        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDesc());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

