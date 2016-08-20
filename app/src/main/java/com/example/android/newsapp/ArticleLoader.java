package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Jaren Lynch on 8/20/2016.
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /** Tage for log messages */
    private static final String LOG_TAG  = ArticleLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Article> articles = QueryUtils.fetchArticleData(mUrl);
        return articles;
    }
}
