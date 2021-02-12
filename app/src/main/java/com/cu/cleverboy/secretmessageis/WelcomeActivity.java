package com.cu.cleverboy.secretmessageis;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cu.cleverboy.secretmessageis.Database.DatabaseHelper;

import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    Boolean isFirstTime;
    private EditText public_ET,private_ET;
    private Button btnOK;
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    ProgressDialog progressDialog;

   //private String URL="http://192.168.43.34/IS/receiveKey.php";
    //private String URL="http://192.168.43.206/IS/receiveKey.php";
    private String URL="https://issecretmessage.000webhostapp.com/IS/receiveKey.php";
    HasPermission hasPermission;

    String publicKey = null;
    String privateKey = null;

    String edit1,edit2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        permissionCheck();

        public_ET = (EditText) findViewById(R.id.public_key);
        private_ET = (EditText) findViewById(R.id.private_key);
        btnOK = (Button) findViewById(R.id.btn_ok);
        hideActionBar();
        checkFirstTime();

        btnOK.setOnClickListener(this);
    }

    private void checkFirstTime(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        int dataCount = databaseHelper.getKeyCount();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        isFirstTime = sharedPreferences.getBoolean("isFirstTime",true);


        if (isFirstTime || dataCount == 0){

            //Toast.makeText(this.getApplicationContext(),"FirstTime",Toast.LENGTH_LONG).show();
            editor.putBoolean("isFirstTime",false);
            editor.commit();
        }else {
            //Toast.makeText(this.getApplicationContext(),"Already Login",Toast.LENGTH_LONG).show();
            startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
            //Toast.makeText(this.getApplicationContext(),"Ref Public"+databaseHelper.getKey().getRefPublic()+",Real Public"+databaseHelper.getKey().getPublicKey(),Toast.LENGTH_LONG).show();

        }
    }

    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    public void onClick(View v) {
        Rsa rsa = new Rsa();

        edit1 = public_ET.getText().toString();
        edit2 = private_ET.getText().toString();

        if (!(edit1.isEmpty()) || !(edit2.isEmpty())) {
            showProgress();
            progressDialog.setCancelable(false);
            Map<String, Object> keyMap = null;
            try {
                keyMap = rsa.initKey();
                publicKey = rsa.getPublicKey(keyMap);
                privateKey = rsa.getPrivateKey(keyMap);

            } catch (Exception e) {
                e.printStackTrace();
            }

            //UPLOAD TO SERVER++++++++++++++
            uploadPublicKey();
        }else{
            public_ET.setError("Required !");
            private_ET.setError("Required !");
        }
    }

    //UPLOAD TO SERVER++++++++++++++
    private void uploadPublicKey() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")){
                            boolean success = databaseHelper.insertKey(public_ET.getText().toString(), private_ET.getText().toString(),publicKey,privateKey);

                            if (success){
                                databaseHelper.createContactTable();
                                databaseHelper.createInboxTable();
                                databaseHelper.createOutboxTable();
                                Toast.makeText(WelcomeActivity.this,"Insert Success",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(WelcomeActivity.this,"Your KEY already exits, Please try another",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(WelcomeActivity.this,"Your KEY already exits, Please try another",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(WelcomeActivity.this,"Connection Fail",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("ref",public_ET.getText().toString());
                param.put("real",publicKey);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void permissionCheck() {
        hasPermission = new HasPermission();

        int PERMISSION_CODE=1;

        String[] PERMISSIONS={Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};

        if (!hasPermission.hasPermissions(this,PERMISSIONS)){
            ActivityCompat.requestPermissions(this,PERMISSIONS,PERMISSION_CODE);
        }

    }

    private void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
}
