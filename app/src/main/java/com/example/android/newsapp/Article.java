package com.example.android.newsapp;

/**
 * Created by Jaren Lynch on 8/20/2016.
 */
public class Article {

    //Title of article.
    private String mTitle;

    //Url for article
    private String mUrl;

    /**
     * @param title is the title of the article.
     * @param url is the url of the article.
     */
    public Article(String title, String url) {
        mTitle = title;
        mUrl = url;
    }

    /**
     * @return the title of the article
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * @return the url of the article.
     */
    public String getUrl() {
        return mUrl;
    }

}
