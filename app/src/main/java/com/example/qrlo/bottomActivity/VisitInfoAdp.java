package com.example.qrlo.bottomActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrlo.R;

public class VisitInfoAdp extends RecyclerView.ViewHolder {

    public TextView when;
    public TextView where;
    public ImageView Ivwherelogo;


    public VisitInfoAdp(@NonNull View itemView) {
        super(itemView);


        when = itemView.findViewById(R.id.visitTime);
        where = itemView.findViewById(R.id.visitWhere);
        Ivwherelogo = itemView.findViewById(R.id.IVlogo);
    }
}
