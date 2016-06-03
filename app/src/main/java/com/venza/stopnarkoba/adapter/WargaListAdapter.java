package com.venza.stopnarkoba.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.venza.stopnarkoba.R;
import com.venza.stopnarkoba.app.AppController;
import com.venza.stopnarkoba.model.warga;

import java.util.List;

/**
 * Created by Probook 4341s on 5/21/2016.
 */
public class WargaListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<warga> wargaItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public WargaListAdapter(Activity activity, List<warga> wargaItems) {
        this.activity = activity;
        this.wargaItems = wargaItems;
    }

    @Override
    public int getCount() {
        return wargaItems.size();
    }

    @Override
    public Object getItem(int location) {
        return wargaItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.warga_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView image = (NetworkImageView) convertView
                .findViewById(R.id.image);

        TextView user_id = (TextView) convertView.findViewById(R.id.user_id);
        TextView name = (TextView) convertView.findViewById(R.id.name);


        // getting video data for the row
        warga m = wargaItems.get(position);

        // image
        image.setImageUrl(m.getImage_Url(), imageLoader);

        // user id
        user_id.setText(String.valueOf(m.getUser_id()));

        // name
        name.setText("" + String.valueOf(m.getName()));



        return convertView;
    }
}
