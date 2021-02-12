package com.cu.cleverboy.secretmessageis.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cu.cleverboy.secretmessageis.Database.DatabaseHelper;
import com.cu.cleverboy.secretmessageis.Model.Contact;
import com.cu.cleverboy.secretmessageis.Model.Message;
import com.cu.cleverboy.secretmessageis.R;
import com.cu.cleverboy.secretmessageis.ReadMessage;

import java.io.Serializable;
import java.util.List;

public class InboxRecyclerAdapter extends Adapter<InboxRecyclerAdapter.ViewHolder>{

   private List<Message> smsList;
   private Context context;
    private AlertDialog.Builder alert;


    public InboxRecyclerAdapter(List<Message> messageList, Context context) {
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Message sms = smsList.get(position);

        String ss = "0"+sms.getName().substring(3);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Contact contact = databaseHelper.getOneContact(ss);

        if (contact == null){
            holder.nameTV.setText( "(0" + sms.getName().substring(3) + ")");
        }else {
            holder.nameTV.setText(contact.getName() + "(" + ss+ ")");
        }
        holder.smsTV.setText(sms.getText());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, sms.getText(),Toast.LENGTH_LONG).show();

                alert = new AlertDialog.Builder(context);
                alert.setIcon(R.drawable.ic_key_24dp);
                alert.setTitle("Key");
                alert.setMessage("Enter your private key");
                final EditText key = new EditText(context);
               /* LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                key.setLayoutParams(lp);*/
                alert.setView(key);

             //   final AlertDialog alertDialog = alert.show();
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context,key.getText(),Toast.LENGTH_LONG).show();
                        if (!(key.getText().toString().isEmpty())) {
                            Intent intent = new Intent(context, ReadMessage.class);
                            intent.putExtra("key", key.getText().toString());
                            intent.putExtra("sms", holder.smsTV.getText().toString());
                            context.startActivity(intent);
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, smsTV;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTV = (TextView) itemView.findViewById(R.id.name);
            smsTV = (TextView) itemView.findViewById(R.id.sms);
            cardView = (CardView) itemView.findViewById(R.id.inbox_card);


        }
    }




}
