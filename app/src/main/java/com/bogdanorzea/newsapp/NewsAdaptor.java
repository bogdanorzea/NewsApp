package com.bogdanorzea.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

// TODO Try to apply the ViewHolder pattern
public class NewsAdaptor extends ArrayAdapter<News> {
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
        News currentNews = mNewsArray.get(position);

        // Set news title
        TextView newsTitle = (TextView) convertView.findViewById(R.id.news_title);
        newsTitle.setText(currentNews.getTitle());

        // Set news section
        TextView newsSection = (TextView) convertView.findViewById(R.id.news_title);
        newsSection.setText(currentNews.getSectionId());

        return convertView;
    }
}
