package com.example.qrlo;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class my_qr_adapter extends RecyclerView.Adapter<my_qr_adapter.ViewHolder> {
    private ArrayList<my_qr_item> mData = null;

    my_qr_adapter(ArrayList<my_qr_item> list)
    {
        mData = list;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView title;
        TextView desc;
        ImageView temp;

        ViewHolder(View itemView){
            super(itemView);

            icon = itemView.findViewById(R.id.my_qr_item_image);
            title = itemView.findViewById(R.id.my_qr_item_txt1);
            desc = itemView.findViewById(R.id.my_qr_item_txt2);
            temp = itemView.findViewById(R.id.my_qr_item_image2);

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

        View view = inflater.inflate(R.layout.my_qr_item, parent, false);
        my_qr_adapter.ViewHolder vh = new my_qr_adapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        my_qr_item item = mData.get(position);

        Glide.with(holder.itemView.getContext()).load(item.getIconURI()).into(holder.icon);
        holder.icon.setImageBitmap(item.getIcon());
        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDetailAddress());
        if(item.getTemp()==false)
        holder.temp.setImageResource(R.drawable.ic_baseline_how_to_reg_24g);
        else
            holder.temp.setImageResource(R.drawable.ic_baseline_how_to_reg_24);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

