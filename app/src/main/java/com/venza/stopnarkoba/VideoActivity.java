package com.venza.stopnarkoba;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import com.venza.stopnarkoba.fragment.VideoFragment;
import com.venza.stopnarkoba.model.video;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;


import com.google.android.youtube.player.YouTubePlayer;

import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import com.venza.stopnarkoba.adapter.VideoDetailAdapter;
import com.venza.stopnarkoba.adapter.VideoListAdapter;
import com.venza.stopnarkoba.app.AppController;
import com.venza.stopnarkoba.model.video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class VideoActivity extends AppCompatActivity {


    private String id;

    private TextView ID, title, content, created_at, youtube_id, image_small;

    // Log tag
    private static final String TAG = VideoActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "http://stopnarkoba.id/service/videos/";
    private ProgressDialog pDialog;
    private List<video> videoList = new ArrayList<video>();
    private ListView listView;
    NetworkImageView image;
    private VideoListAdapter adapter;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_video);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Video");

        listView = (ListView) findViewById(R.id.list);
        adapter = new VideoListAdapter(this, videoList);
        listView.setAdapter(adapter);


        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        ID = (TextView) findViewById(R.id.ID);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        created_at = (TextView) findViewById(R.id.created_at);
        image = (NetworkImageView) findViewById(R.id.image_large);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");

        }
        Log.d("SN-id", id);


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
                                final String youtube_id = response.getString("youtube_id");
                                image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        play_Youtube(youtube_id);
                                    }
                                });

                                // Creating volley request obj
                                JSONArray RandomPostArr = response.getJSONArray("randomPost");
                                for (int i = 0; i < RandomPostArr.length(); i++) {

                                    JSONObject obj = RandomPostArr.getJSONObject(i);
                                    video v = new video();
                                    int ID = obj.getInt("id");
                                    String title = obj.getString("title");
                                    String created_at = obj.getString("created_at");
                                    String image_small = obj.getString("image_small");

                                    v.setID(ID);
                                    v.setTitle(title);
                                    v.setCreated_at(created_at);
                                    v.setImage_small_Url(image_small);
                                    videoList.add(v);

                                }
                                adapter.notifyDataSetChanged();

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



    public void play_Youtube(String ID)
    {
        Intent intent = new Intent(VideoActivity.this, YouTubePlayerActivity.class);

        // Youtube video ID (Required, You can use YouTubeUrlParser to parse Video Id from url)
        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, ID);

        // Youtube player style (DEFAULT as default)
        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);

        // Screen Orientation Setting (AUTO for default)
        // AUTO, AUTO_START_WITH_LANDSCAPE, ONLY_LANDSCAPE, ONLY_PORTRAIT
        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);

        // Show audio interface when user adjust volume (true for default)
        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);

        // If the video is not playable, use Youtube app or Internet Browser to play it
        // (true for default)
        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);

        // Animation when closing youtubeplayeractivity (none for default)
        // intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.);
        // intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.fade_out);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                Intent i = new Intent(getApplicationContext(), VideoFragment.class);
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
            Intent i = new Intent(VideoActivity.this, LoginActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}


