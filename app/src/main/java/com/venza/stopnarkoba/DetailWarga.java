package com.venza.stopnarkoba;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.venza.stopnarkoba.adapter.WargaListAdapter;
import com.venza.stopnarkoba.app.AppController;
import com.venza.stopnarkoba.model.warga;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Probook 4341s on 5/31/2016.
 */
public class DetailWarga extends AppCompatActivity {

    private String user_id;

    private TextView name;

    // Log tag
    private static final String TAG = DetailWarga.class.getSimpleName();

    // Movies json url
    private static final String url = "http://stopnarkoba.id/service/user-profiles/";
    private ProgressDialog pDialog;
    private List<warga> wargaList = new ArrayList<warga>();
    private ListView listView;
    NetworkImageView image;
    private WargaListAdapter adapter;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_warga);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Warga");

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        name = (TextView)findViewById(R.id.name);
        image = (NetworkImageView)findViewById(R.id.image_large);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getString("user_id");

        }
        Log.d("SN", user_id);

        // Creating volley request obj
        JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET,url+user_id,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        Log.d("SN", response.toString());

                        try {

                            name.setText(response.getString("name"));
                            imageLoader.get(response.getString("image_large"), ImageLoader.getImageListener(image,
                                    R.mipmap.ic_launcher, R.mipmap.ic_launcher));
                            image.setImageUrl(response.getString("image_large"), imageLoader);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("SN", response.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d("SN", error.getMessage());
                hidePDialog();

            }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq, "SN");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }



}
