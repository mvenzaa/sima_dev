package com.venza.stopnarkoba.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.venza.stopnarkoba.DetailArticle;
import com.venza.stopnarkoba.DetailWarga;
import com.venza.stopnarkoba.R;
import com.venza.stopnarkoba.adapter.WargaListAdapter;
import com.venza.stopnarkoba.app.AppController;
import com.venza.stopnarkoba.model.article;
import com.venza.stopnarkoba.model.warga;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Probook 4341s on 5/3/2016.
 */
public class WargaFragment extends Fragment {

    private View rootView;

    // Log tag
    private static final String TAG = WargaFragment.class.getSimpleName();

    // Movies json url
    private static final String url = "http://stopnarkoba.id/service/user-profiles?page=";
    private Integer url_page_default = 1;
    private List<warga> wargaList = new ArrayList<warga>();
    private ListView listView;
    private WargaListAdapter adapter;

    ProgressBar bar;

    public WargaFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("SN","OKE VIEW FRAGMENT");

        rootView = inflater.inflate(R.layout.fragment_warga, container, false);

        listView = (ListView) rootView.findViewById(R.id.list);
        adapter = new WargaListAdapter(getActivity(), wargaList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {

                TextView c = (TextView) v.findViewById(R.id.user_id);
                String articleID = c.getText().toString();
                Intent i = new Intent(getActivity().getApplicationContext(), DetailWarga.class);
                i.putExtra("user_id", articleID);
                startActivity(i);
            }
        });
        bar = (ProgressBar) rootView.findViewById(R.id.loading_progress);
        bar.setVisibility(View.VISIBLE);
        list("default");

        ((PullAndLoadListView) listView)
                .setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
                    public void onRefresh() {
                        // Do work to refresh the list here.
                        url_page_default = 1;
                        list("refresh");
                    }
                });
        ((PullAndLoadListView) listView)
                .setOnLoadMoreListener(new PullAndLoadListView.OnLoadMoreListener() {
                    public void onLoadMore() {
                        url_page_default = url_page_default + 1;
                        list("loadmore");
                    }
                });

        return rootView;
    }

    public void list(final String type) {
        // Creating volley request obj
        JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET,
                url + String.valueOf(url_page_default), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(type == "refresh"){
                                wargaList.clear();
                            }
                            JSONObject meta = response.getJSONObject("_meta");
                            Log.d("pageCount",url_page_default.toString()+" vs "+meta.getString("pageCount"));
                            if (url_page_default <= meta.getInt("pageCount")) {
                                JSONArray items = response.getJSONArray("items");
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject obj = items.getJSONObject(i);
                                    warga w = new warga();
                                    w.setImage_Url(obj.getString("image_small"));
                                    w.setUser_id(obj.getInt("user_id"));
                                    w.setName(obj.getString("name"));

                                    // adding movie to movies array
                                    wargaList.add(w);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            bar.setVisibility(View.GONE);

                            if(type == "refresh"){
                                ((PullAndLoadListView) listView).onRefreshComplete();
                            }else{
                                if (url_page_default >= meta.getInt("pageCount")) {
                                    ((PullAndLoadListView) listView).onLoadMoreComplete();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(movieReq, "SN");
    }
}