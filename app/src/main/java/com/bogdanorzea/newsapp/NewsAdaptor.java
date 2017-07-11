package com.bogdanorzea.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// TODO Implement the ViewHolder pattern
public class NewsAdaptor extends ArrayAdapter<News> {
    private static final String LOG_TAG = NewsAdaptor.class.getSimpleName();
    private ArrayList<News> mNewsArray;

    public NewsAdaptor(Context context, ArrayList<News> objects) {
        super(context, 0, objects);
        mNewsArray = objects;
    }

    @Override
    public int getCount() {
        return mNewsArray.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public News getItem(int position) {
        return mNewsArray.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        }
        // Get current News item
        final News currentNews = mNewsArray.get(position);

        // Set news headline
        TextView newsHeadline = (TextView) convertView.findViewById(R.id.news_headline);
        newsHeadline.setText(currentNews.getHeadline());

        // Set news trail text
        TextView newsTrailText = (TextView) convertView.findViewById(R.id.news_trail_text);
        newsTrailText.setText(Html.fromHtml(currentNews.getTrailText()));

        // Set news contributor
        TextView newsContributors = (TextView) convertView.findViewById(R.id.news_contributors);
        newsContributors.setText(currentNews.getContributors());

        // Set news section
        TextView newsSection = (TextView) convertView.findViewById(R.id.news_section);
        newsSection.setText(currentNews.getSectionName());

        // Set news date
        TextView newsDate = (TextView) convertView.findViewById(R.id.news_date);

        // TODO Date formatting using Android locale settings
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.US);

        try {
            Date curDate = originalFormat.parse(currentNews.getPublicationDate());
            newsDate.setText(newFormat.format(curDate));
        } catch (ParseException e) {
            Log.d(LOG_TAG, "Error parsing date.", e);
        }

        // Set onClickListener to open news website
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view = new Intent(Intent.ACTION_VIEW, Uri.parse(currentNews.getUrl()));
                getContext().startActivity(view);
            }
        });

        return convertView;
    }
}
