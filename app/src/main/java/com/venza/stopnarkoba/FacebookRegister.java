package com.venza.stopnarkoba;

/**
 * Created by Probook 4341s on 5/20/2016.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.venza.stopnarkoba.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FacebookRegister extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private static final String social_account_url = "http://stopnarkoba.id/service/user-social-accounts";
    private static final String register_url = "http://stopnarkoba.id/service/auth/register";

    @InjectView(R.id.input_username) EditText _usernameText;
    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;

    String user_id;
    String user_token;
    String user_id_after_register;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_register);
        ButterKnife.inject(this);

        _usernameText = (EditText) findViewById(R.id.input_username);
        _nameText = (EditText) findViewById(R.id.input_name);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _signupButton = (Button) findViewById(R.id.btn_signup);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            _usernameText.setText(extras.getString("username"));
            _nameText.setText(extras.getString("name"));
            _emailText.setText(extras.getString("email"));
            user_id = extras.getString("user_id");
            user_token = extras.getString("user_token");

        }

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _signupButton.setEnabled(false);
                _usernameText.setEnabled(false);
                _nameText.setEnabled(false);
                _emailText.setEnabled(false);
                _passwordText.setEnabled(false);
                signup();
            }

        });

    }

    public void signup() {

        StringRequest user_is_avilable = new StringRequest(Request.Method.POST,
                register_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONObject res = new JSONObject(response);
                            SuperActivityToast superActivityToast = new SuperActivityToast(FacebookRegister.this);
                            if(res.getString("status").equals("error")){
                                String data_error = "";
                                JSONObject msg = new JSONObject(res.getString("message"));
                                if (msg.has("username")) {
                                    JSONArray username = msg.getJSONArray("username");
                                    for (int i=0; i<username.length(); i++) {
                                        String error = username.getString(i);
                                        data_error += error+" \n";
                                    }
                                }
                                if (msg.has("email")) {
                                    JSONArray email = msg.getJSONArray("email");
                                    for (int i=0; i<email.length(); i++) {
                                        String error = email.getString(i);
                                        data_error += error+" \n";
                                    }
                                }
                                if (msg.has("password")) {
                                    JSONArray password = msg.getJSONArray("password");
                                    for (int i=0; i<password.length(); i++) {
                                        String error = password.getString(i);
                                        data_error += error+" \n";
                                    }
                                }
                                if (msg.has("name")) {
                                    JSONArray name = msg.getJSONArray("name");
                                    for (int i=0; i<name.length(); i++) {
                                        String error = name.getString(i);
                                        data_error += error+" \n";
                                    }
                                }
                                superActivityToast.setText(data_error);
                                superActivityToast.setDuration(SuperToast.Duration.SHORT);
                                superActivityToast.setTextColor(Color.WHITE);
                                superActivityToast.setTouchToDismiss(true);
                                superActivityToast.show();
                                superActivityToast.setBackground(SuperToast.Background.RED);
                            }else{
                                JSONObject profile = new JSONObject(res.getString("profile"));
                                JSONObject user = new JSONObject(res.getString("user"));

                                user_id_after_register = user.getString("id");
                                insertUserSocialAccount();

                                SharedPreferences.Editor editor = getSharedPreferences("stopnarkoba", MODE_PRIVATE).edit();
                                editor.putString("name", profile.getString("name"));
                                editor.putString("email", user.getString("email"));
                                editor.putString("token", user.getString("auth_key"));
                                editor.putString("is_login", "1");
                                editor.commit();
                                Intent i = new Intent(FacebookRegister.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);


                            }
                            _signupButton.setEnabled(true);
                            _emailText.setEnabled(true);
                            _usernameText.setEnabled(true);
                            _nameText.setEnabled(true);
                            _passwordText.setEnabled(true);
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

                params.put("register-form[email]", _emailText.getText().toString());
                params.put("register-form[password]", _passwordText.getText().toString());
                params.put("register-form[username]", _usernameText.getText().toString());
                params.put("register-form[name]", _nameText.getText().toString());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(user_is_avilable);

    }

    public void insertUserSocialAccount() {

        StringRequest user_is_avilable = new StringRequest(Request.Method.POST,
                social_account_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("SNinsertsocial", error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id_after_register);
                params.put("provider", "facebook");
                params.put("client_id", user_id);
                params.put("email", _emailText.getText().toString());
                params.put("username",  _usernameText.getText().toString());
                params.put("code", user_token);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(user_is_avilable);

    }


}



