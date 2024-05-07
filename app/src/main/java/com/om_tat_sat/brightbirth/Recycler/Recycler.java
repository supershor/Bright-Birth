package com.om_tat_sat.brightbirth.Recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.om_tat_sat.brightbirth.Implementations.Recyclerview_Interface;
import com.om_tat_sat.brightbirth.R;
import com.om_tat_sat.brightbirth.data_holders.name_bday_holder;

import java.util.ArrayList;

public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {
    Context context;
    ArrayList<name_bday_holder>arrayList;
    private final Recyclerview_Interface recyclerviewInterface;

    public Recycler(Context context, ArrayList<name_bday_holder> arrayList,Recyclerview_Interface recyclerviewInterface) {
        this.context = context;
        this.arrayList = arrayList;
        this.recyclerviewInterface=recyclerviewInterface;
    }

    @NonNull
    @Override
    public Recycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.name_birthdate_layout,parent,false);
        return new ViewHolder(view,recyclerviewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler.ViewHolder holder, int position) {
        holder.name.append(arrayList.get(position).getName());
        holder.birthdate.append(arrayList.get(position).getDate()+"/"+arrayList.get(position).getMonth()+"/"+arrayList.get(position).getYear());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView birthdate;

        public ViewHolder(@NonNull View itemView,Recyclerview_Interface recyclerviewInterface) {
            super(itemView);
            name=itemView.findViewById(R.id.name_info);
            birthdate=itemView.findViewById(R.id.birthdate_info);
            itemView.setOnClickListener(v -> {
                if(recyclerviewInterface!=null){
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        recyclerviewInterface.click(position,1);
                    }
                }
            });
        }
    }
}
