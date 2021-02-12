package com.cu.cleverboy.secretmessageis.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cu.cleverboy.secretmessageis.Database.DatabaseHelper;
import com.cu.cleverboy.secretmessageis.Model.Contact;
import com.cu.cleverboy.secretmessageis.Model.Message;
import com.cu.cleverboy.secretmessageis.R;

import java.util.List;

public class OutboxRecyclerAdapter extends Adapter<OutboxRecyclerAdapter.ViewHolder>{

   private List<Message> smsList;
   private Context context;


    public OutboxRecyclerAdapter(List<Message> messageList, Context context) {
        this.smsList = messageList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_box_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Message sms = smsList.get(position);

        String ss = sms.getName();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Contact contact = databaseHelper.getOneContact(ss);

        if (contact == null){
            holder.nameTV.setText( "(0" + sms.getName().substring(3) + ")");
        }else {
            holder.nameTV.setText(contact.getName() + "(" + ss+ ")");
        }

       // holder.nameTV.setText(sms.getName());
        holder.smsTV.setText(sms.getText());
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, smsTV;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTV = (TextView) itemView.findViewById(R.id.name);
            smsTV = (TextView) itemView.findViewById(R.id.sms);
        }
    }
}
