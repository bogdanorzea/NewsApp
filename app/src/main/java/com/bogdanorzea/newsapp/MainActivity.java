package com.bogdanorzea.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

            // Add user defined search term
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String search_term = sharedPrefs.getString(getString(R.string.settings_search_content_key), getString(R.string.settings_search_content_default));
            if (!TextUtils.isEmpty(search_term)) {
                uriBuilder.appendQueryParameter("q", search_term);
            }

            uriBuilder.appendQueryParameter("order-by", "newest");
            uriBuilder.appendQueryParameter("show-fields", "headline,trailText,thumbnail");
            uriBuilder.appendQueryParameter("show-tags", "contributor");

            // Add user defined page size
            String page_size = sharedPrefs.getString(getString(R.string.settings_page_size_key), getString(R.string.settings_page_size_default));
            uriBuilder.appendQueryParameter("page-size", page_size);

            uriBuilder.appendQueryParameter("api-key", "test");
            uriBuilder.appendQueryParameter("format", "json");

            emptyView.setText(R.string.news_loading);
            return new NewsLoader(getBaseContext(), uriBuilder.toString());
        }

        @Override
        public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
            newsAdaptor.clear();

            if (data.isEmpty()) {
                emptyView.setText(R.string.news_unavailable);
            } else {
                newsAdaptor.addAll(data);
            }
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

        initializeLoader();
    }

    /**
     * Method used to check for the internet connection
     * adn intialize the Loader used for loading News
     */
    private void initializeLoader() {
        // Check internet connection
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());

        // Initialize Loader if the network state is connected
        if (isConnected) {
            getSupportLoaderManager().initLoader(0, null, mLoaderCallbacks);
        } else {
            newsAdaptor.clear();
            emptyView.setText(R.string.no_connection);
        }
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

        switch (id) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_refresh:
                initializeLoader();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
