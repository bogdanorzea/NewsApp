package com.bogdanorzea.newsapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


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
        //TODO implement getView
        return super.getView(position, convertView, parent);
    }
}
