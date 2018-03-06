package com.teksystems.newsreader.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture;
import com.teksystems.newsreader.Adapters.NewsAdapter;
import com.teksystems.newsreader.DAO.NewsDAO;
import com.teksystems.newsreader.Models.News;
import com.teksystems.newsreader.Module.GlideApp;
import com.teksystems.newsreader.R;
import com.teksystems.newsreader.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    MaterialDialog progressDialog;
    ListView newsListView;
    NewsAdapter newsAdapter;
    ArrayList<News> newsArrayList;
    JSONObject newsObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsListView = (ListView) findViewById(R.id.newsListView);
        fetchNews();
    }

    public void fetchNews() {
        showProgressDialog();
        ListenableFuture<JSONObject> newsFuture = null;
        newsFuture = JdkFutureAdapters.listenInPoolThread(NewsDAO.fetchNews());
        Futures.addCallback(newsFuture, new FutureCallback<JSONObject>() {
            @Override
            public void onSuccess(@Nullable JSONObject result) {
                if (result != null) {
                    try {
                        newsArrayList = new ArrayList<News>();
                        final JSONArray newsArray = result.getJSONArray("articles");
                        for (int i = 0; i < newsArray.length(); i++) {
                            newsObject = newsArray.getJSONObject(i);
                            newsArrayList.add(new News(
                                    checkNullValues(newsObject.get("title")),
                                    checkNullValues(newsObject.get("author")),
                                    checkNullValues(newsObject.get("description")),
                                    checkNullValues(newsObject.get("url")),
                                    checkNullValues(newsObject.get("urlToImage")),
                                    checkNullValues(newsObject.get("publishedAt")),
                                    checkNullValues(newsObject.get("source"))));
                        }
                        newsAdapter = new NewsAdapter(newsArrayList, MainActivity.this);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                newsListView.setAdapter(newsAdapter);
                                newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        ImageView newsHeader;
                                        WebView newsWebView;
                                        final View newsView = (new MaterialDialog.Builder(MainActivity.this)).theme(Theme.LIGHT)
                                                .customView(R.layout.news_web, false).cancelable(true).show().getCustomView();
                                        newsHeader = newsView.findViewById(R.id.newsHeaderImage);
                                        newsWebView = newsView.findViewById(R.id.newsWebView);
                                        if (!IsMatch(newsArrayList.get(i).getUrlToImage())) {
                                            newsHeader.setVisibility(View.GONE);
                                        } else {
                                            try {
                                                GlideApp.with(getApplicationContext())
                                                        .load(newsArrayList.get(i).getUrlToImage())
                                                        .placeholder(R.drawable.loading)
                                                        .centerCrop()
                                                        .into(newsHeader);
                                            } catch (Exception e) {
                                                newsHeader.setVisibility(View.GONE);
                                            }
                                        }
                                        newsWebView.getSettings().setJavaScriptEnabled(true);
                                        newsWebView.getSettings().setLoadWithOverviewMode(true);
                                        newsWebView.setWebViewClient(new WebViewClient());
                                        newsWebView.loadUrl(newsArrayList.get(i).getUrl());
                                    }
                                });
                            }
                        });
                    } catch (Exception es) {
                        es.printStackTrace();
                    }
                    hideProgressDialog();
                } else {
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgressDialog();
            }
        });
    }

    public void showProgressDialog() {
        progressDialog = new MaterialDialog.Builder(this)
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

    public String checkNullValues(Object inputString) {
        if (inputString != null)
            return "" + inputString;
        else
            return null;
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
