package com.teksystems.newsreader.Activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.teksystems.newsreader.Adapters.NewsAdapter;
import com.teksystems.newsreader.DAO.NewsDAO;
import com.teksystems.newsreader.Interface.AsyncResponse;
import com.teksystems.newsreader.Models.News;
import com.teksystems.newsreader.Module.GlideApp;
import com.teksystems.newsreader.R;
import com.teksystems.newsreader.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
  MaterialDialog progressDialog;
  NewsDAO newsFetcher = new NewsDAO(this);
  ListView newsListView;
  LinkedList<News> newsArrayList;
  JSONObject newsObject;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    newsListView = (ListView) findViewById(R.id.newsListView);
    newsFetcher.mainResponse = this;
    fetchNews();
  }

  public void fetchNews() {
    try {
      showProgressDialog();
    } catch (Exception e) {
    }
    newsFetcher.execute();
  }

  public void showProgressDialog() {
    progressDialog = new MaterialDialog.Builder(MainActivity.this)
      .title(Constants.PROGRESS_TITLE)
      .content(Constants.PROGRESS_CONTENT)
      .autoDismiss(false)
      .progress(true, 0)
      .show();
  }

  public void hideProgressDialog() {
    if (progressDialog != null) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          progressDialog.hide();
        }
      });
    }
  }

  public String checkNullValues(Object inputString, int choice) {
    if (choice == 0) {
      if (inputString != null)
        return "" + inputString;
      else {
        return null;
      }
    } else {
      if (inputString != null && IsMatch("" + inputString))
        return "" + inputString;
      else {
        return null;
      }
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

  @Override
  public void processFinish(String response) {
    try {
      if (response != null) {
        JSONObject obj = null;
        obj = new JSONObject(response);
        newsArrayList = new LinkedList<News>();
        final JSONArray newsArray = obj.getJSONArray("articles");
        for (int i = 0; i < newsArray.length(); i++) {
          newsObject = newsArray.getJSONObject(i);
          newsArrayList.add(new News(
            checkNullValues(newsObject.get("title"), 0),
            checkNullValues(newsObject.get("author"), 0),
            checkNullValues(newsObject.get("description"), 0),
            checkNullValues(newsObject.get("url"), 0),
            checkNullValues(newsObject.get("urlToImage"), 1),
            checkNullValues(newsObject.get("publishedAt"), 0),
            checkNullValues(newsObject.get("source"), 0)));
        }
        NewsAdapter newsAdapter = new NewsAdapter(newsArrayList, MainActivity.this);
        newsListView.setAdapter(newsAdapter);
        hideProgressDialog();
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            WebView newsWebView;
            final View newsView = (new MaterialDialog.Builder(MainActivity.this)).theme(Theme.LIGHT)
              .customView(R.layout.news_web, false).cancelable(true).show().getCustomView();
            final ImageView newsHeader = newsView.findViewById(R.id.newsHeaderImage);
            newsWebView = newsView.findViewById(R.id.newsWebView);
            GlideApp.with(MainActivity.this)
              .load(newsArrayList.get(i).getUrlToImage())
              .placeholder(R.drawable.loading)
              .centerCrop()
              .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                  newsHeader.setVisibility(View.GONE);
                  return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                  return false;
                }
              })
              .into(newsHeader);
            newsWebView.getSettings().setJavaScriptEnabled(true);
            newsWebView.getSettings().setLoadWithOverviewMode(true);
            newsWebView.setWebViewClient(new WebViewClient());
            newsWebView.loadUrl(newsArrayList.get(i).getUrl());
          }
        });
      }
    } catch (Exception e) {
      hideProgressDialog();
    }
  }

  public View getViewByPosition(int pos, ListView listView) {
    final int firstListItemPosition = listView.getFirstVisiblePosition();
    final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

    if (pos < firstListItemPosition || pos > lastListItemPosition) {
      return listView.getAdapter().getView(pos, null, listView);
    } else {
      final int childIndex = pos - firstListItemPosition;
      return listView.getChildAt(childIndex);
    }
  }
}
