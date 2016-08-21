package com.example.android.newsapp;

/**
 * Created by Jaren Lynch on 8/20/2016.
 */
public class Article {

    //Title of article.
    private String mTitle;

    //Url for article
    private String mUrl;

    //Topic for the article
    private String mTopic;

    //Author for the artic;e
    private String mAuthor;

    /**
     * @param title is the title of the article.
     * @param url is the url of the article.
     * @param topic is the topic of the article
     * @param author is the author of the article
     */
    public Article(String title, String url, String topic, String author) {
        mTitle = title;
        mUrl = url;
        mTopic  = topic;
        mAuthor = author;
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

    /**
     * @return the topic of the article
     */
    public String getTopic() {
        return mTopic;
    }

    /**
     * @return the author of the article.
     */
    public String getAuthor() {
        return mAuthor;
    }
}
