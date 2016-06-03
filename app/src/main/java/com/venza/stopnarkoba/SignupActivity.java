package com.venza.stopnarkoba;

/**
 * Created by Probook 4341s on 5/20/2016.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.venza.stopnarkoba.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    String username;
    String data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        _nameText = (EditText) findViewById(R.id.input_name);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _signupButton = (Button) findViewById(R.id.btn_signup);


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _signupButton.setEnabled(false);
                _nameText.setEnabled(false);
                _emailText.setEnabled(false);
                _passwordText.setEnabled(false);
                signup();
            }

        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signup() {

        StringRequest user_is_avilable = new StringRequest(Request.Method.POST,
                "http://stopnarkoba.id/service/auth/register",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONObject res = new JSONObject(response);
                            SuperActivityToast superActivityToast = new SuperActivityToast(SignupActivity.this);
                            if(res.getString("status").equals("error")){
                                data = "";
                                superActivityToast.setBackground(SuperToast.Background.RED);
                            }else{
                                data = res.getString("data");
                                superActivityToast.setBackground(SuperToast.Background.GREEN);
                            }
                            String msg = res.getString("message");
                            _signupButton.setEnabled(true);
                            _emailText.setEnabled(true);
                            _nameText.setEnabled(true);
                            _passwordText.setEnabled(true);

                            superActivityToast.setText(msg + " " + data);
                            superActivityToast.setDuration(SuperToast.Duration.SHORT);
                            superActivityToast.setTextColor(Color.WHITE);
                            superActivityToast.setTouchToDismiss(true);
                            superActivityToast.show();

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
                String[] email_parts =  _emailText.getText().toString().split("@");
                username  = email_parts[0];
                params.put("register-form[email]", _emailText.getText().toString());
                params.put("register-form[password]", _passwordText.getText().toString());
                params.put("register-form[username]", username);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(user_is_avilable);

    }

    }



