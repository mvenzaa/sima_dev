package com.venza.stopnarkoba;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.Menu;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.venza.stopnarkoba.adapter.ArticleDetailAdapter;
import com.venza.stopnarkoba.app.AppController;
import com.venza.stopnarkoba.fragment.ArtikelFragment;
import com.venza.stopnarkoba.fragment.VideoFragment;
import com.venza.stopnarkoba.model.article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Probook 4341s on 5/26/2016.
 */
public class DetailArticle extends AppCompatActivity {

    private String id;

    private TextView ID, title, content, created_at, youtube_id, image_small;

    // Log tag
    private static final String TAG = DetailArticle.class.getSimpleName();

    // Movies json url
    private static final String url = "http://stopnarkoba.id/service/artikels/";
    private ProgressDialog pDialog;
    private List<article> articleList = new ArrayList<article>();
    private ListView listView;
    NetworkImageView image;
    private ArticleDetailAdapter adapter;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Artikel");


        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        ID = (TextView)findViewById(R.id.ID);
        title = (TextView)findViewById(R.id.title);
        content = (TextView)findViewById(R.id.content);
        created_at = (TextView)findViewById(R.id.created_at);
        image = (NetworkImageView)findViewById(R.id.image_large);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");

        }
        Log.d("SN",id);


        // Creating volley request obj
        JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET,url+id,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        Log.d("SN", response.toString());

                        try {

                            title.setText(response.getString("title"));
                            created_at.setText(response.getString("created_at"));
                            content.setText(Html.fromHtml(response.getString("content")));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(getApplicationContext(), ArtikelFragment.class);
                i.putExtra("key", query);
                startActivity(i);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Intent i = new Intent(DetailArticle.this, LoginActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
