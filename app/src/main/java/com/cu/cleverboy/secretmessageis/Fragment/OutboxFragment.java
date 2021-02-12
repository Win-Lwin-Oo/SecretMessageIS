package com.cu.cleverboy.secretmessageis.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cu.cleverboy.secretmessageis.Adapter.InboxRecyclerAdapter;
import com.cu.cleverboy.secretmessageis.Adapter.OutboxRecyclerAdapter;
import com.cu.cleverboy.secretmessageis.Database.DatabaseHelper;
import com.cu.cleverboy.secretmessageis.Model.Message;
import com.cu.cleverboy.secretmessageis.R;
import com.cu.cleverboy.secretmessageis.SendMessageActivity;

import java.util.ArrayList;
import java.util.List;


public class OutboxFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<Message> messageList;
    private OutboxRecyclerAdapter adapter;
    private FloatingActionButton send;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Outbox");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outbox, container,
                false);
        send = (FloatingActionButton)view.findViewById(R.id.send_sms);
        recyclerView = view.findViewById(R.id.outbox_recycler);
        messageList = new ArrayList<>();

        adapter = new OutboxRecyclerAdapter(messageList,getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareMessage();
        send.setOnClickListener(this);
        return view;
    }

    private void prepareMessage() {
       // messageList.add(new Message("title","hello! , How are you? I hope you are well."));
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getContext());
        messageList.addAll(databaseHelper.getOutboxMessage());
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.send_sms){
            startActivity(new Intent(this.getContext(), SendMessageActivity.class));
        }
    }
}
