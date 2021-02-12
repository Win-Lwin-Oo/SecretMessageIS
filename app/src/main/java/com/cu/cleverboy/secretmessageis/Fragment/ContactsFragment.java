package com.cu.cleverboy.secretmessageis.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cu.cleverboy.secretmessageis.Adapter.ContactRecyclerAdapter;
import com.cu.cleverboy.secretmessageis.Database.DatabaseHelper;
import com.cu.cleverboy.secretmessageis.MainActivity;
import com.cu.cleverboy.secretmessageis.Model.Contact;
import com.cu.cleverboy.secretmessageis.R;
import com.cu.cleverboy.secretmessageis.WelcomeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactRecyclerAdapter adapter;
    private FloatingActionButton add_new_content;
    private DatabaseHelper databaseHelper;

    private AlertDialog.Builder alert;
   // private String URL="http://192.168.43.34/IS/receiveKey.php";
    //private String URL="http://192.168.43.206/IS/getUserData.php";
    private String URL="https://issecretmessage.000webhostapp.com/IS/getUserData.php";

    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Contacts");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        recyclerView = view.findViewById(R.id.contact_recycler);
        add_new_content = (FloatingActionButton) view.findViewById(R.id.add_new_content);

        contactList = new ArrayList<>();

        databaseHelper = new DatabaseHelper(getContext());
        adapter = new ContactRecyclerAdapter(contactList,getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        add_new_content.setOnClickListener(this);

        prepareContact();


       // Toast.makeText(getContext(),"Data" + databaseHelper.getKeyCount(),Toast.LENGTH_LONG).show();

        return view;

    }

    private void prepareContact() {
        contactList.addAll(databaseHelper.selectContact());
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {

        add_new_content_alert();
    }

    public void getPublicKey(final String key, final String name, final String phone){

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String s;
                        if (response.equals("error")){
                           Toast.makeText(getContext(),response.toString(),Toast.LENGTH_LONG).show();
                        }else{
                            s = response.toString();
                            if (!(s.equals(""))) {


                                Contact contact = new Contact(name, phone, s, key);
                                //Toast.makeText(getContext(), "key : " + contact.getPublic_key(), Toast.LENGTH_SHORT).show();

                                if (databaseHelper.insertContact(contact)) {

                                    progressDialog.dismiss();
                                    adapter.notifyDataSetChanged();


                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Insert error", Toast.LENGTH_SHORT).show();

                                }
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Key does not exit!", Toast.LENGTH_SHORT).show();

                            }


                            //Toast.makeText(getContext(),response.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Connection Fail",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("publicRef",key);

                return param;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void add_new_content_alert() {
        alert = new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_new_content,null);
        alert.setView(dialogView);
        final AlertDialog a = alert.show();
        a.setCancelable(false);

        final EditText name , phone , key ;
        ImageView close,done;
        name = (EditText)dialogView.findViewById(R.id.name);
        phone = (EditText)dialogView.findViewById(R.id.phone);
        key = (EditText)dialogView.findViewById(R.id.publicKey);
        close = (ImageView)dialogView.findViewById(R.id.close);
        done = (ImageView)dialogView.findViewById(R.id.done);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sname,sphone,skey;
                sname = name.getText().toString();
                sphone = phone.getText().toString();
                skey = key.getText().toString();

                if (sname.isEmpty()) {
                    name.setError("Enter name");
                }

                if (sphone.isEmpty()) {
                    phone.setError("Enter phone number");
                }

                if (skey.isEmpty()) {
                    key.setError("Enter key");
                }
                else if(!(sname.isEmpty()) || !(sphone.isEmpty()) || !(skey.isEmpty())) {
                   // String publicKey = databaseHelper.getKey().getPublicKey();
                    showProgres();
                    progressDialog.setCancelable(false);
                    getPublicKey(skey,sname,sphone);
                    a.dismiss();
                    //Toast.makeText(getContext(), "Receive key is :"+realKey, Toast.LENGTH_LONG).show();
                   /* if (databaseHelper.insertContact(contact)){
                        Toast.makeText(getContext(),"Data : "+sname+" "+ sphone + " " + skey , Toast.LENGTH_SHORT).show();

                        adapter.notifyDataSetChanged();
                        a.dismiss();


                    } else {
                        Toast.makeText(getContext(),"insert error" , Toast.LENGTH_SHORT).show();

                    }*/
                }
            }
        });

    }

    private void showProgres() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
}
