package com.venza.stopnarkoba.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyLog;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.venza.stopnarkoba.DetailArticle;
import com.venza.stopnarkoba.R;
import com.venza.stopnarkoba.adapter.ArticleListAdapter;
import com.venza.stopnarkoba.app.AppController;
import com.venza.stopnarkoba.model.article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Probook 4341s on 5/3/2016.
 */
public class ArtikelFragment extends Fragment {

    private View rootView;

    // Log tag
    private static final String TAG = ArtikelFragment.class.getSimpleName();

    // Movies json url
    private static final String url = "http://stopnarkoba.id/service/artikels?page=";
    private Integer url_page_default = 0;
    private ProgressDialog pDialog;
    private List<article> articleList = new ArrayList<article>();
    private ListView listView;
    private ArticleListAdapter adapter;


    public ArtikelFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("SN", "OKE VIEW FRAGMENT");

        rootView = inflater.inflate(R.layout.fragment_artikel, container, false);

        listView = (ListView) rootView.findViewById(R.id.list);
        adapter = new ArticleListAdapter(getActivity(), articleList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {

                TextView c = (TextView) v.findViewById(R.id.ID);
                String articleID = c.getText().toString();
                Intent i = new Intent(getActivity().getApplicationContext(), DetailArticle.class);
                i.putExtra("id", articleID);
                startActivity(i);
            }
        });

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        listDefault();

        ((PullAndLoadListView) listView)
                .setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

                    public void onRefresh() {
                        // Do work to refresh the list here.
                        url_page_default = 0;
                        listRefresh();
                    }
                });

        // set a listener to be invoked when the list reaches the end
        ((PullAndLoadListView) listView)
                .setOnLoadMoreListener(new PullAndLoadListView.OnLoadMoreListener() {

                    public void onLoadMore() {
                        // Do the work to load more items at the end of list
                        // here
                        url_page_default = url_page_default + 1;
                        listMore(url_page_default);
                    }
                });

        return rootView;
    }

    public void listDefault() {
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url + String.valueOf(url_page_default),

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        Log.d("SN",response.toString());
                        // Parsing json
                        for (int i = 0; i < response.length(); i++)  {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                article a = new article();
                                a.setImage_small_Url(obj.getString("image_small"));
                                a.setID(obj.getInt("id"));
                                a.setTitle(obj.getString("title"));
                                a.setCreated_at(obj.getString("created_at"));


                                // adding movie to movies array
                                articleList.add(a);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("SN", response.toString());
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                        ((PullAndLoadListView) listView).onRefreshComplete();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
//               Log.d("SN", error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq, "SN");

    }

    public void listRefresh() {
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url + String.valueOf(url_page_default),

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        Log.d("SN",response.toString());
                        // Parsing json
                        for (int i = 0; i < response.length(); i++)  {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                article a = new article();
                                a.setImage_small_Url(obj.getString("image_small"));
                                a.setID(obj.getInt("id"));
                                a.setTitle(obj.getString("title"));
                                a.setCreated_at(obj.getString("created_at"));
                                //a.setContent(obj.getString("content"));



                                // adding movie to movies array
                                articleList.add(a);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("SN", response.toString());
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                        ((PullAndLoadListView) listView).onRefreshComplete();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Log.d("SN", error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq, "SN");

    }

    public void listMore(Integer url_page_default) {
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url + String.valueOf(url_page_default),

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        Log.d("SN",response.toString());
                        // Parsing json
                        for (int i = 0; i < response.length(); i++)  {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                article a = new article();
                                a.setImage_small_Url(obj.getString("image_small"));
                                a.setID(obj.getInt("id"));
                                a.setTitle(obj.getString("title"));
                                a.setCreated_at(obj.getString("created_at"));
                                //a.setContent(obj.getString("content"));



                                // adding movie to movies array
                                articleList.add(a);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("SN", response.toString());
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                        ((PullAndLoadListView) listView).onRefreshComplete();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Log.d("SN", error.getMessage());
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
