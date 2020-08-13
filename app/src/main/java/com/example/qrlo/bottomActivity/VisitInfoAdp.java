package com.example.qrlo.bottomActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrlo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VisitInfoAdp extends RecyclerView.Adapter<VisitInfoAdp.ViewHolder> {

    public Context context;
    private ArrayList<VisitInfo> arrayList;

    public VisitInfoAdp(Context context, ArrayList<VisitInfo> arrayList)
    {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public VisitInfoAdp.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.list_visit,parent, false);
        VisitInfoAdp.ViewHolder holder =new VisitInfoAdp.ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VisitInfoAdp.ViewHolder holder, int position) {


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy mm:ss");
        holder.tvwhen.setText(sdf.format(new Date(arrayList.get(position).getWhen())));
        holder.tvwhere.setText(arrayList.get(position).getWhere());
    }

    @Override
    public int getItemCount() {
        if(arrayList != null) return arrayList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvwhen;
        TextView tvwhere;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.IVlogo);
            tvwhen = itemView.findViewById(R.id.visitTime);
            tvwhere = itemView.findViewById(R.id.visitWhere);
        }
    }
}
