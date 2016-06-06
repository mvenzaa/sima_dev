package com.venza.stopnarkoba;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.venza.stopnarkoba.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Probook 4341s on 6/1/2016.
 */
public class FacebookLogin {

    private static final String get_code_url = "http://stopnarkoba.id/service/user-social-account/get-by-code?id=";


    public static String user_name;
    public static String user_email;
    public static String user_birthday;
    public static String user_id;
    public static String user_token;
    public final static String provider = "facebook";
    public static Boolean user_is_already;
    public static String error_message = null;
    public static Activity activity;
    public static JSONObject data_after_cek;

    public static void init(CallbackManager callbackManager, LoginButton login_button,
                            final LoginActivity activitys, final LinearLayout auth_layout) {
        activity = activitys;
        login_button.setPadding(20, 20, 20, 20);
        login_button.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // auth_layout.setVisibility(View.GONE);
                user_id = loginResult.getAccessToken().getUserId();
                user_token = loginResult.getAccessToken().getToken();
                checkUser(user_id);
                Log.d("SN", "Coba Login");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    Log.d("SN", "Coba Parsing");
                                    user_name = object.getString("name");
                                    user_email = object.getString("email");
                                    if (!user_is_already) {
                                        // buat user name dari email
                                        String[] email_parts = user_email.split("@");
                                        String user_username = email_parts[0];

                                        Intent i = new Intent(activity, FacebookRegister.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.putExtra("username",user_username);
                                        i.putExtra("name", user_name);
                                        i.putExtra("email", user_email);
                                        i.putExtra("user_id", user_id);
                                        i.putExtra("user_token", user_token);

                                        activity.startActivity(i);
                                    } else {
                                        Intent i = new Intent(activity, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        activity.startActivity(i);
                                    }

                                } catch (Exception e) {

                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }


            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        }

    public static void checkUser(final String user_fb_id) {
        Log.d("SN2",user_fb_id);
        JsonObjectRequest user_is_avilable = new JsonObjectRequest(Request.Method.GET,
                get_code_url + user_fb_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("avilable").equals("false")) {
                                user_is_already = false;
                            } else {
                                JSONObject profile = new JSONObject(response.getString("profile"));
                                JSONObject user = new JSONObject(response.getString("user"));
                                Log.d("SN2", profile.toString());
                                SharedPreferences.Editor editor = activity.getSharedPreferences("stopnarkoba", activity.MODE_PRIVATE).edit();
                                editor.putString("name", profile.getString("name"));
                                editor.putString("email", user.getString("email"));
                                editor.putString("token", user.getString("auth_key"));
                                editor.putString("is_login", "1");
                                editor.commit();
                                user_is_already = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("SNcek", error.getMessage());
                user_is_already = false;
            }
        });
        AppController.getInstance().addToRequestQueue(user_is_avilable);
    }

}






