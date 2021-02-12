package com.cu.cleverboy.secretmessageis.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.cleverboy.secretmessageis.Model.Contact;
import com.cu.cleverboy.secretmessageis.R;

import java.util.List;

public class ContactRecyclerAdapter extends Adapter<ContactRecyclerAdapter.ViewHolder>{

   private List<Contact> contactList;
   private Context context;

    public ContactRecyclerAdapter(List<Contact> contacts, Context context) {
        this.contactList = contacts;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Contact contact = contactList.get(position);

        holder.contactNameTV.setText(contact.getName());
        holder.contactPhoneTV.setText(contact.getPhone());

        holder.contactNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"you click "+contact.getPublic_key(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView contactNameTV, contactPhoneTV;

        public ViewHolder(View itemView) {
            super(itemView);
            contactNameTV = (TextView) itemView.findViewById(R.id.contact_name);
            contactPhoneTV = (TextView) itemView.findViewById(R.id.contact_phone);
           }
    }



    }
