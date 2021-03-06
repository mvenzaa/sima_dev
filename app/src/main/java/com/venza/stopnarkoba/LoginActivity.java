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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.venza.stopnarkoba.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

    private LoginButton login_button;

    private CallbackManager callbackManager;

    LinearLayout auth_layout;

    String data;

    // Logcat tag
    private static final String TAG = "LoginActivity";


    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SETUP LOGIN DENGAN FACEBOOK
        FacebookSdk.sdkInitialize(getApplicationContext());


        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        // SETUP LOGIN DENGAN FACEBOOK
        callbackManager = CallbackManager.Factory.create();
        LoginButton login_button = (LoginButton)findViewById(R.id.login_button);
        auth_layout = (LinearLayout)findViewById(R.id.auth_layout);
        FacebookLogin.init(callbackManager, login_button, this, auth_layout);


        _loginButton = (Button) findViewById(R.id.btn_login);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                _loginButton.setEnabled(false);
                _emailText.setEnabled(false);
                _passwordText.setEnabled(false);
                login();
            }
        });


        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SuperActivityToast superActivityToast = new SuperActivityToast(LoginActivity.this);
            superActivityToast.setText("Anda Berhasil Mendaftar Silakan Login");
            superActivityToast.setDuration(SuperToast.Duration.SHORT);
            superActivityToast.setTextColor(Color.WHITE);
            superActivityToast.setTouchToDismiss(true);
            superActivityToast.show();
            superActivityToast.setBackground(SuperToast.Background.GREEN);
        }
    }


    public void login() {

        StringRequest user_is_avilable = new StringRequest(Request.Method.POST,
                "http://stopnarkoba.id/service/auth/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONObject res = new JSONObject(response);
                            SuperActivityToast superActivityToast = new SuperActivityToast(LoginActivity.this);

                            if(res.getString("status").equals("error")){
                                data = "";
                                superActivityToast.setBackground(SuperToast.Background.RED);
                                String  msg = res.getString("message");
                                _loginButton.setEnabled(true);
                                _emailText.setEnabled(true);
                                _passwordText.setEnabled(true);

                                superActivityToast.setText(msg + " " + data);
                                superActivityToast.setDuration(SuperToast.Duration.SHORT);
                                superActivityToast.setTextColor(Color.WHITE);
                                superActivityToast.setTouchToDismiss(true);
                                superActivityToast.show();
                            }else{
                                JSONObject data = new JSONObject(res.getString("data"));
                                SharedPreferences.Editor editor = getSharedPreferences("stopnarkoba", MODE_PRIVATE).edit();
                                editor.putString("name", data.getString("name"));
                                editor.putString("email", data.getString("email"));
                                editor.putString("token", data.getString("token"));
                                editor.putString("is_login", "1");
                                editor.commit();
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _loginButton.setEnabled(true);
                _emailText.setEnabled(true);
                _passwordText.setEnabled(true);
                SuperActivityToast superActivityToast = new SuperActivityToast(LoginActivity.this);
                superActivityToast.setBackground(SuperToast.Background.RED);
                superActivityToast.setText("Terjadi kesalahan mohon ulangi kembali");
                superActivityToast.setDuration(SuperToast.Duration.SHORT);
                superActivityToast.setTextColor(Color.WHITE);
                superActivityToast.setTouchToDismiss(true);
                superActivityToast.show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("login-form[login]", _emailText.getText().toString());
                params.put("login-form[password]", _passwordText.getText().toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(user_is_avilable);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
