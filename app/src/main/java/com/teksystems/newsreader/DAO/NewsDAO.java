package com.teksystems.newsreader.DAO;

import android.os.AsyncTask;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.teksystems.newsreader.Utils.Constants;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by archakraborty on 05-03-2018.
 */

public class NewsDAO {

    NewsDAO() {
    }

    public static ListenableFuture<JSONObject> fetchNews() {
        final SettableFuture<JSONObject> newsFuture = SettableFuture.create();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String response = null;
                try {
                    URL url = new URL(Constants.NEWS_URL_TOP_HEADLINES + Constants.API_TAG + "=" + Constants.API_KEY);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    response = convertStreamToString(in);
                    if (response != null) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            newsFuture.set(obj);
                        } catch (Exception e) {
                            newsFuture.set(null);
                        }
                    } else {
                        newsFuture.set(null);
                    }
                } catch (Exception e) {
                    newsFuture.set(null);
                }
            }
        });
        return newsFuture;
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
