package com.teksystems.newsreader.DAO;

import android.content.Context;
import android.os.AsyncTask;

import com.teksystems.newsreader.Interface.AsyncResponse;
import com.teksystems.newsreader.Utils.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Arghadeep on 05-03-2018.
 */

public class NewsDAO extends AsyncTask<String, Void, String> {
  private final Context mContext;
  public AsyncResponse mainResponse = null;

  public NewsDAO(final Context context) {
    mContext = context;
  }

  @Override
  protected String doInBackground(String... params) {
    String response = null;
    try {
      URL url = new URL(Constants.NEWS_URL_TOP_HEADLINES + Constants.API_TAG + "=" + Constants.API_KEY);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      InputStream in = new BufferedInputStream(conn.getInputStream());
      response = convertStreamToString(in);
    } catch (Exception e) {
    }
    return response;
  }


  @Override
  protected void onPostExecute(String response) {
    mainResponse.processFinish(response);
  }

  public static String convertStreamToString(InputStream is) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();

    String line;
    try {
      while ((line = reader.readLine()) != null) {
        sb.append(line).append('\n');
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return sb.toString();
  }
}
