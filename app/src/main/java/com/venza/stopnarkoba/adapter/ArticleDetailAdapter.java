package com.venza.stopnarkoba.adapter;

/**
 * Created by Probook 4341s on 5/26/2016.
 */
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
import com.venza.stopnarkoba.model.article;

import java.util.List;

/**
 * Created by Probook 4341s on 5/19/2016.
 */
public class ArticleDetailAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<article> articleItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ArticleDetailAdapter(Activity activity, List<article> articleItems) {
        this.activity = activity;
        this.articleItems = articleItems;
    }

    @Override
    public int getCount() {
        return articleItems.size();
    }

    @Override
    public Object getItem(int location) {
        return articleItems.get(location);
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
            convertView = inflater.inflate(R.layout.article_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView image_small = (NetworkImageView) convertView
                .findViewById(R.id.image_small);

        TextView ID = (TextView) convertView.findViewById(R.id.ID);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView content = (TextView) convertView.findViewById(R.id.content);
        TextView created_at = (TextView) convertView.findViewById(R.id.created_at);

        // getting video data for the row
        article m = articleItems.get(position);

        // image original
        image_small.setImageUrl(m.getImage_small_Url(), imageLoader);

        // ID
        ID.setText(String.valueOf(m.getID()));

        // title
        title.setText("" + String.valueOf(m.getTitle()));

        // content
        content.setText(Html.fromHtml(String.valueOf(m.getContent())));

        // created at
        created_at.setText(String.valueOf(m.getCreated_at()));


        return convertView;
    }
}
