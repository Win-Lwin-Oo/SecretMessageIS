package com.cu.cleverboy.secretmessageis;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.cleverboy.secretmessageis.Database.DatabaseHelper;
import com.cu.cleverboy.secretmessageis.Model.Contact;
import com.cu.cleverboy.secretmessageis.Model.Message;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SendMessageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Spinner spinner;
    private EditText newMessage;
    private ImageView sendMessage;
    private DatabaseHelper databaseHelper;
    private String[] name;
    private String [] phoneNumber;
    private String [] public_Key;
    private String sendNumber;
    private String publicKey;

    private byte[] encodeData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        spinner = (Spinner) findViewById(R.id.contentName);
        newMessage = (EditText) findViewById(R.id.newMessage);
        sendMessage = (ImageView) findViewById(R.id.send_Message);
        databaseHelper = new DatabaseHelper(this);

        List<Contact> contactList = databaseHelper.selectContact();
        name = new String[contactList.size()];
        phoneNumber = new String[contactList.size()];
        public_Key = new String[contactList.size()];
        for (int i=0 ; i < contactList.size() ; i++){
            name[i] = contactList.get(i).getName()+" ("+ contactList.get(i).getPhone()+")";
            phoneNumber[i] = contactList.get(i).getPhone();
            public_Key[i] = contactList.get(i).getPublic_key();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,name);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
        sendMessage.setOnClickListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         sendNumber = phoneNumber[position];
         publicKey = public_Key[position];

        //Toast.makeText(getApplicationContext(),"Data : "+ name[position]+"PP : "+sendNumber+"PUBLIC"+publicKey,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.send_Message){
            String stringMessage = newMessage.getText().toString();

            String encryptString = encrypt(stringMessage , publicKey);

            if (!(encryptString.isEmpty())){
                sendNewMessage(sendNumber,encryptString );
            }else {
                Toast.makeText(getApplicationContext(),"Message empty",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendNewMessage(String sendNumber , String stringMessage ) {
         try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sendNumber, null, stringMessage, null, null);
            Toast.makeText(getApplicationContext(),"Success !" ,Toast.LENGTH_LONG).show();

            DatabaseHelper databaseHelper = new DatabaseHelper(this);
             Date date = Calendar.getInstance().getTime();
             SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
             String dateString = dateFormat.format(date);
             Message message = new Message(null,stringMessage,dateString);

            if (databaseHelper.insertOutboxMessage(sendNumber,message,message.getDate())){
                Toast.makeText(getApplicationContext(),"Send Success",Toast.LENGTH_LONG).show();
                finish();
            }else {
                Toast.makeText(getApplicationContext(),"Error Send",Toast.LENGTH_LONG).show();
            }

         }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        }

    public String encrypt(String sms , String key){
        Rsa rsa = new Rsa();
        byte[] rsaData = sms.getBytes();
        String encodeString = null;

        try {
            encodeData = rsa.encryptByPublicKey(rsaData,key);
            BigInteger bigInteger = new BigInteger(encodeData);
            encodeString = bigInteger.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodeString;
    }
}
