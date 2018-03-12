package com.teksystems.newsreader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teksystems.newsreader.Models.News;
import com.teksystems.newsreader.R;

import java.util.LinkedList;

/**
 * Created by Arghadeep on 05-03-2018.
 */

public class NewsAdapter extends BaseAdapter {
  private LinkedList<News> newsDataList;
  private LayoutInflater layoutInflater;

  public NewsAdapter(LinkedList<News> newsData, Context aContext) {
    this.newsDataList = newsData;
    layoutInflater = LayoutInflater.from(aContext);
  }

  @Override
  public int getCount() {
    return newsDataList.size();
  }

  @Override
  public Object getItem(int i) {
    return newsDataList.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder = null;
    if (convertView == null) {
      holder = new ViewHolder();
      convertView = layoutInflater.inflate(R.layout.news_item, null);
      holder.newsTitle = convertView.findViewById(R.id.newsTitle);
      holder.newsDescription = convertView.findViewById(R.id.newsDescription);
      holder.newsImage = convertView.findViewById(R.id.newsImage);
      holder.newsLayout = convertView.findViewById(R.id.newsLayout);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    News newsObject = new News();
    newsObject = (News) getItem(position);
    holder.newsTitle.setText(newsObject.getTitle());
    holder.newsDescription.setText(newsObject.getDescription());
    if (newsObject.getUrlToImage() == null) {
      holder.newsImage.setVisibility(View.GONE);
    } else
      Picasso.get()
        .load(newsObject.getUrlToImage() + "")
        .placeholder(R.drawable.loading)
        .error(R.drawable.ic_launcher_background)
        .into(holder.newsImage);
    return convertView;
  }

  class ViewHolder {
    ImageView newsImage;
    TextView newsTitle;
    TextView newsDescription;
    RelativeLayout newsLayout;
  }
}
