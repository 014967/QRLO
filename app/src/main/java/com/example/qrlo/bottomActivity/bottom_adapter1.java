package com.example.qrlo.bottomActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrlo.R;

import java.util.ArrayList;

public class bottom_adapter1 extends RecyclerView.Adapter<bottom_adapter1.ViewHolder> {

    private ArrayList<bottom_item1> mData=null;

    public bottom_adapter1(ArrayList<bottom_item1> list){ mData = list;}

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private bottom_adapter1.OnItemClickListener mListener = null;

    public void setOnItemClickListener(bottom_adapter1.OnItemClickListener listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView phone;

        ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.admin_qr_name_txt);
            phone = itemView.findViewById(R.id.admin_qr_phone_txt);

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
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.bottom_item1, parent, false);
        bottom_adapter1.ViewHolder viewHolder = new bottom_adapter1.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        bottom_item1 item = mData.get(position);

        holder.name.setText(item.getNameStr());
        holder.phone.setText(item.getPhoneStr());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
