package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jaren Lynch on 8/20/2016.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {
    public ArticleAdapter (Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        Article currentArticle = getItem(position);

        //Find the TextView with id article_title.
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.article_title);

        //Display article title in TextView article_title
        titleTextView.setText(currentArticle.getTitle());

        return listItemView;
    }
}
