package com.example.shravanapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<ModelClass> userlist;

    public Adapter(List<ModelClass>userlist){
        this.userlist=userlist;
    }


    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sos_contacts_list,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder( Adapter.ViewHolder holder, int position) {

        int image_resource =  userlist.get(position).getImageview1();
        String name=userlist.get(position).getTxt_contact_name();
        String num=userlist.get(position).getTxt_contact_num();

        holder.setData(image_resource, name, num);


    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView txt_contact_name;
        private TextView txt_contact_num;


        public ViewHolder(View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.imageView1);
            txt_contact_name = itemView.findViewById(R.id.id_contact_name);
            txt_contact_num = itemView.findViewById(R.id.id_contact_num);


        }

        public void setData(int image_resource, String name, String num) {
            imageView.setImageResource(image_resource);
            txt_contact_name.setText(name);
            txt_contact_num.setText(num);
        }
    }
}
