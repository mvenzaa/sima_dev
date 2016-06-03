package com.venza.stopnarkoba;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Probook 4341s on 6/1/2016.
 */
public class FacebookLogin {

    private static final String url = "http://stopnarkoba.id/service/users-social-account/";

    public static String user_name;
    public static String user_email;
    public static String user_birthday;
    public static String user_id;
    public static String user_token;
    public final static String provider = "facebook";
    public static Boolean user_is_already;
    public static String error_message = null;
    public static Activity activity;

    public static void init(CallbackManager callbackManager, LoginButton login_button,
                            final LoginActivity activitys, final LinearLayout auth_layout) {

        activity = activitys;

        login_button.setPadding(20, 20, 20, 20);
        login_button.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                auth_layout.setVisibility(View.GONE);
                // App code
                user_id = loginResult.getAccessToken().getUserId();
                user_token = loginResult.getAccessToken().getToken();

                checkUser(user_id);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                    Log.d("SN", response.toString());

                                    user_name = object.getString("name");
                                    user_email = object.getString("email");

                                    SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("http://stopnarkoba.id/service/shared_preferences/", activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putBoolean("user_is_login", true);
                                    editor.putString("user_fb_id", user_id);
                                    editor.putString("user_fb_name", user_name);
                                    editor.putString("user_fb_email", user_email);
                                    editor.putString("user_fb_token", user_token);
                                    editor.commit();



                                    //   user_birthday = object.getString("birthday"); // 01/31/1980 format
                                    if (!user_is_already) {
                                        Log.d("SN", response.toString());
                                        insertUser();
                                    }

                                    CheckAuth.init(activitys);
                                    activitys.finish();
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
        JsonObjectRequest user_is_avilable = new JsonObjectRequest(Request.Method.GET,
                url + "/get-by-code/" + user_fb_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("SN", response.getString("avilable"));
                            if (response.getString("avilable").equals("false")) {
                                user_is_already = false;
                                Log.d("SN", "second" + response.getString("avilable"));
                            } else {
                                user_is_already = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                user_is_already = true;
            }
        });
        AppController.getInstance().addToRequestQueue(user_is_avilable);
    }

    public static void insertUser() {
        Log.d("SN", "insert on method");
        Log.d("SN", "username " + user_name);
        Log.d("SN", "email " + user_email);
        Log.d("SN", "user_id " + user_id);
        Log.d("SN", "user_token " + user_token);


        StringRequest user_is_avilable = new StringRequest(Request.Method.POST,
                "http://stopnarkoba.id/service/users",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SN", response.toString());
                        try{
                            JSONObject val = new JSONObject(response);
                            // JIKA USER SUDAH DI INSERT, INSERT JUGA PROFIL DLL, DENGAN ID USER
                            insertUserSocialAccount(val.getString("id"));
                            insertUserProfile(val.getString("id"));
                            insertUserToken(val.getString("id"));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    switch (networkResponse.statusCode) {
                        case 422:
                            Log.d("SN", "422");
                            error_message = new String(networkResponse.data);
                            error_message = error_message.toString();
                            Log.d("SN", error_message);
                            break;
                    }

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                // buat user name dari email
                String[] email_parts = user_email.split("@");
                user_name =email_parts[0];
                Log.d("SN", user_name);

                params.put("username", user_name);
                params.put("email", user_email);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(user_is_avilable);
    }

    public static void insertUserSocialAccount(final String user_account_id) {

        StringRequest user_is_avilable = new StringRequest(Request.Method.POST,
                "http://stopnarkoba.id/service/users-social-accounts",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SN", response.toString());
                        try {
                            JSONObject val = new JSONObject(response);
                            // insertUserSocialAccount(val.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                // buat user name dari email
                String[] email_parts = user_email.split("@");
                user_name =email_parts[0];
                Log.d("SN",user_name);

                params.put("provider", provider);
                params.put("client_id", user_id);
                params.put("user_id", user_account_id);
                params.put("email", user_email);
                params.put("username", user_name);
                params.put("code", user_token);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(user_is_avilable);

    }

    public static void insertUserProfile(final String user_account_id) {

        StringRequest user_is_avilable = new StringRequest(Request.Method.POST,
                "http://stopnarkoba.id/service/users-profiles",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SN", response.toString());
                        try {
                            JSONObject val = new JSONObject(response);
                            // insertUserSocialAccount(val.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_account_id);
                params.put("public_email", user_email);
                params.put("name", user_name);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(user_is_avilable);

    }

    public static void insertUserToken(final String user_account_id) {

        StringRequest user_is_avilable = new StringRequest(Request.Method.POST,
                "http://stopnarkoba.id/service/users-tokens",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SN", response.toString());
                        try {
                            JSONObject val = new JSONObject(response);
                            // insertUserSocialAccount(val.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_account_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(user_is_avilable);

    }


    }






