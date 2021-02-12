package com.cu.cleverboy.secretmessageis;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Win Lwin Oo on 23-Jan-18.
 */

public class HasPermission extends AppCompatActivity{
    public static boolean hasPermissions(Context context , String... permissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context!= null && permissions!=null){
            for (String permision : permissions){
                if (ActivityCompat.checkSelfPermission(context,permision) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }
}
