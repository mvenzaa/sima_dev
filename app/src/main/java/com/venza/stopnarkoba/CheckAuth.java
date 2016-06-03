package com.venza.stopnarkoba;

/**
 * Created by Probook 4341s on 6/1/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.venza.stopnarkoba.MainActivity;

public class CheckAuth extends AppCompatActivity{

    public static void init(Activity activity){
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("shared_preferences", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        boolean user_is_login = pref.getBoolean("user_is_login", false);

        if(!user_is_login){
            activity.finish();
            Intent i = new Intent(activity.getApplicationContext(), LoginActivity.class);
            activity.startActivity(i);
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }else{
            Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
            activity.startActivity(i);
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

}
