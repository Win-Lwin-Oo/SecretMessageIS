package com.cu.cleverboy.secretmessageis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.cleverboy.secretmessageis.Database.DatabaseHelper;
import com.cu.cleverboy.secretmessageis.Model.Key;
import com.cu.cleverboy.secretmessageis.Model.Message;

import java.math.BigInteger;

public class ReadMessage extends AppCompatActivity {
    private TextView viewText;
    String key,sms;
    private DatabaseHelper databaseHelper;
    private byte[] encodeData=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);

        databaseHelper = new DatabaseHelper(this);
        viewText = findViewById(R.id.view_sms);


        viewText.setText("hello");

        key = getIntent().getStringExtra("key");
        sms = getIntent().getStringExtra("sms");

        //viewText.setText(key+"BB"+sms);

        convertPlainText(key,sms);
    }

    private void convertPlainText(String key, String sms) {

        Key key1 = databaseHelper.getKey();
        if (key.equals(key1.getRefPrivate())){
            Rsa rsa = new Rsa();
            BigInteger bigInteger = new BigInteger(sms);
            encodeData = bigInteger.toByteArray();
            try {
                byte[] decodeData  = rsa.encryptByPrivateKey(encodeData,key1.getPrivateKey());
                String decodeString = new String(decodeData);
                viewText.setText(decodeString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            viewText.setText("Invalid Key");
        }
    }


}
