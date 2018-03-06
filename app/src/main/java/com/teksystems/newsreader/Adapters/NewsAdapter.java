package com.teksystems.newsreader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teksystems.newsreader.Activity.MainActivity;
import com.teksystems.newsreader.Models.News;
import com.teksystems.newsreader.Module.GlideApp;
import com.teksystems.newsreader.R;
import com.teksystems.newsreader.Utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by archakraborty on 05-03-2018.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private static List<News> newsDataList;
    private Context mContext;
    private ViewHolder viewHolder;

    public NewsAdapter(ArrayList<News> newsData, Context aContext) {
        super(aContext, R.layout.news_item, newsData);
        this.newsDataList = newsData;
        this.mContext = aContext;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = ((MainActivity) mContext).getLayoutInflater();
            view = inflater.inflate(R.layout.news_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        setData(position, viewHolder);
        return view;
    }

    public void setData(int position, ViewHolder holder) {
        News newsObject = getItem(position);
        viewHolder.newsTitle.setText(newsObject.getTitle());
        viewHolder.newsDescription.setText(newsObject.getDescription());
        if (!IsMatch(newsObject.getUrlToImage())) {
            viewHolder.newsImage.setVisibility(View.GONE);
        } else {
            try {
                GlideApp.with(mContext)
                        .load(newsObject.getUrlToImage())
                        .placeholder(R.drawable.loading)
                        .centerCrop()
                        .into(viewHolder.newsImage);
            } catch (Exception e) {
                viewHolder.newsImage.setVisibility(View.GONE);
            }
        }
    }

    static class ViewHolder {
        ImageView newsImage;
        TextView newsTitle;
        TextView newsDescription;
        RelativeLayout newsLayout;

        public ViewHolder(View convertView) {
            newsTitle = convertView.findViewById(R.id.newsTitle);
            newsDescription = convertView.findViewById(R.id.newsDescription);
            newsImage = convertView.findViewById(R.id.newsImage);
            newsLayout = convertView.findViewById(R.id.newsLayout);
        }
    }

    private static boolean IsMatch(String s) {
        try {
            Pattern patt = Pattern.compile(Constants.URL_REGEX);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }
    }
}