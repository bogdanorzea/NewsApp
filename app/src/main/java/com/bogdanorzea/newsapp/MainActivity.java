package com.bogdanorzea.newsapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String GUARDIAN_BASE_API_LINK = "https://content.guardianapis.com/search";
    ListView mNewsList;
    NewsAdaptor newsAdaptor;
    TextView emptyView;

    private LoaderManager.LoaderCallbacks<List<News>> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<News>>() {
        @Override
        public Loader<List<News>> onCreateLoader(int id, Bundle args) {
            Uri baseUri = Uri.parse(GUARDIAN_BASE_API_LINK);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            // TODO Add user defined search term
            //uriBuilder.appendQueryParameter("q", "android");
            uriBuilder.appendQueryParameter("order-by", "newest");
            uriBuilder.appendQueryParameter("show-fields", "headline,trailText,thumbnail");
            uriBuilder.appendQueryParameter("show-tags", "contributor");
            uriBuilder.appendQueryParameter("api-key", "test");
            // TODO display another page of results
            //uriBuilder.appendQueryParameter("page", "1");
            uriBuilder.appendQueryParameter("format", "json");

            emptyView.setText("Loading...");

            return new NewsLoader(getBaseContext(), uriBuilder.toString());
        }

        @Override
        public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
            newsAdaptor.clear();
            newsAdaptor.addAll(data);
        }

        @Override
        public void onLoaderReset(Loader<List<News>> loader) {
            newsAdaptor.clear();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNewsList = (ListView) findViewById(R.id.news_list);
        emptyView = (TextView) findViewById(R.id.empty_view);
        mNewsList.setEmptyView(emptyView);

        // Empty adaptor
        newsAdaptor = new NewsAdaptor(getBaseContext(), new ArrayList<News>());
        mNewsList.setAdapter(newsAdaptor);

        // Initialize Loader
        getSupportLoaderManager().initLoader(0, null, mLoaderCallbacks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
