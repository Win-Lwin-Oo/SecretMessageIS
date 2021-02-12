package com.cu.cleverboy.secretmessageis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.cu.cleverboy.secretmessageis.Database.DatabaseHelper;
import com.cu.cleverboy.secretmessageis.Model.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyReceiver extends BroadcastReceiver {

    private Message message1;
    private String phoneNumber,message="11";
    private String dateString;

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                DatabaseHelper databaseHelper = new DatabaseHelper(context);

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                    phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    message += currentMessage.getDisplayMessageBody();

                    //Toast.makeText(context,"senderNum: "+ phoneNumber + ", message: " + message, Toast.LENGTH_LONG ).show();



                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    dateString = dateFormat.format(date);


                } // end for loop
                message1 = new Message(null,message.substring(2), dateString);
                if (databaseHelper.insertInboxMessage(phoneNumber,message1,dateString)){
                    Toast.makeText(context,"Received",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context,"Insert Error",Toast.LENGTH_LONG).show();
                }

              } // bundle is null

        } catch (Exception e) {

            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }    
}