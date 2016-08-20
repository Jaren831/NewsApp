package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Article>>{

    private static final String GUARDIAN_URL = "http://content.guardianapis.com/us?show-editors-picks=true&api-key=test";

    private static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Constant value forn the article laoder ID.
     */
    private static final int ARTICLE_LOADER_ID = 1;

    /** Adapter for the list of articles */
    ArticleAdapter mAdapter;

    /** ListView for the list of articles */
    ListView articleListView;

    /** Empty TextView if nothing to show */
    TextView emptyTextView;

    /** Spinner to show loading progress */
    ProgressBar progressBar;

    /**Swipe refresh */
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_list);

        //Find a reference to the ListView in the layout.
        articleListView = (ListView) findViewById(R.id.article_list);

        //Create a new adapter that takes an empty list of articles as input.
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        //Set the adapter on the ListView.
        articleListView.setAdapter(mAdapter);

        //Set the TextView with id empty to an empty view.
        emptyTextView = (TextView) findViewById(R.id.empty);
        articleListView.setEmptyView(emptyTextView);

        //Set progress bar to loading spinner.
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        //Get a reference to the LoaderManager, in order to interact with loaders.
        final LoaderManager loaderManager = getLoaderManager();

        //Create instance of connectivity manager object.
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Check for connectivity
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, MainActivity.this);
        } else {
            progressBar.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet);
        }

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Find the article that was clicked on
                Article currentArticle = mAdapter.getItem(position);

                //Convert the String URL into a URI object (to pass into the intent constructor)
                Uri articleUri = Uri.parse(currentArticle.getUrl());

                //Create a new intent to view the article URI.
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                //Send the intent to launch a new activity.
                startActivity(websiteIntent);
            }
        });

        //Implements pull down to refresh gesture.
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh () {
                Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                loaderManager.initLoader(ARTICLE_LOADER_ID, null, MainActivity.this);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        return new ArticleLoader(this, GUARDIAN_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> article) {
        emptyTextView.setText(R.string.empty);
        mAdapter.clear();
        progressBar.setVisibility(View.GONE);

        if (article != null && !article.isEmpty()) {
            mAdapter.addAll(article);
        }
    }
    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }

}
