package com.cu.cleverboy.secretmessageis.Fragment;


import android.content.Intent;
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
import com.cu.cleverboy.secretmessageis.Database.DatabaseHelper;
import com.cu.cleverboy.secretmessageis.Model.Message;
import com.cu.cleverboy.secretmessageis.R;

import java.util.ArrayList;
import java.util.List;


public class InboxFragment extends Fragment  {
    private RecyclerView recyclerView;
    private List<Message> messageList;
    private InboxRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Inbox");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        recyclerView = view.findViewById(R.id.inbox_recycler);
        messageList = new ArrayList<>();

        adapter = new InboxRecyclerAdapter(messageList,getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareMessage();
        return view;
    }

    private void prepareMessage() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getContext());

        messageList.addAll(databaseHelper.getInboxMessage());
       // messageList.add(new Message("title","hello! , How are you? I hope you are well."));
        adapter.notifyDataSetChanged();
    }


}
