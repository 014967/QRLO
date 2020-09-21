package com.example.qrlo.bottomActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qrlo.R;
import com.example.qrlo.my_qr_adapter;
import com.example.qrlo.my_qr_item;

import java.util.ArrayList;

public class bottom_notice_adapter extends RecyclerView.Adapter<bottom_notice_adapter.ViewHolder>{
    private ArrayList<bottom_notice_item> mData = null;

    public bottom_notice_adapter(ArrayList<bottom_notice_item> list)
    {
        mData = list;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private bottom_notice_adapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(bottom_notice_adapter.OnItemClickListener listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView desc;

        ViewHolder(View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.bottom_notice_item_txt1);
            desc = itemView.findViewById(R.id.bottom_notice_item_txt2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(view, pos);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_bottom_notice_item, parent, false);
        bottom_notice_adapter.ViewHolder vh = new bottom_notice_adapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        bottom_notice_item item = mData.get(position);

        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDesc());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
