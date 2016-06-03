package com.venza.stopnarkoba.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.venza.stopnarkoba.R;
import com.venza.stopnarkoba.app.AppController;
import com.venza.stopnarkoba.model.video;

import java.util.List;

/**
 * Created by Probook 4341s on 5/16/2016.
 */
public class VideoDetailAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<video> videoItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public VideoDetailAdapter(Activity activity, List<video> videoItems) {
        this.activity = activity;
        this.videoItems = videoItems;
    }

    @Override
    public int getCount() {
        return videoItems.size();
    }

    @Override
    public Object getItem(int location) {
        return videoItems.get(location);
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
            convertView = inflater.inflate(R.layout.video_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView image_small = (NetworkImageView) convertView
                .findViewById(R.id.image_small);

        TextView ID = (TextView) convertView.findViewById(R.id.ID);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView content = (TextView) convertView.findViewById(R.id.content);
        TextView is_streaming = (TextView) convertView.findViewById(R.id.is_streaming);
        TextView created_at = (TextView) convertView.findViewById(R.id.created_at);
        TextView youtube_id = (TextView) convertView.findViewById(R.id.youtube_id);

        // getting video data for the row
        video m = videoItems.get(position);

        // image large
        image_small.setImageUrl(m.getImage_small_Url(), imageLoader);

        // ID
        ID.setText(String.valueOf(m.getID()));

        // title
        title.setText("" + String.valueOf(m.getTitle()));

        // content
        content.setText(Html.fromHtml(String.valueOf(m.getContent())));

        // is streaming
        is_streaming.setText(String.valueOf(m.getIs_streaming()));

        // created at
        created_at.setText(String.valueOf(m.getCreated_at()));

        // youtube id
        youtube_id.setText(String.valueOf(m.getYoutube_id()));


        return convertView;
    }

}
