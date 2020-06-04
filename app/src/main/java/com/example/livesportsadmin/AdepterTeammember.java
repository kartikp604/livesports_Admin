package com.example.livesportsadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdepterTeammember extends RecyclerView.Adapter<AdepterTeammember.ViewHolder> {
    public FirebaseFirestore firestore;
    public List<String[]> listmembers;
    public AdepterTeammember(List<String[]> memberNameList){
        this.listmembers=memberNameList;

    }




    @Override
    public AdepterTeammember.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_team_member,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdepterTeammember.ViewHolder holder, int position) {
        holder.name.setText(listmembers.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return listmembers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mview;
        public TextView name;
        public ViewHolder(View view) {
            super(view);
            mview=view;
            name=(TextView)mview.findViewById(R.id.memberName);
        }
    }
}
