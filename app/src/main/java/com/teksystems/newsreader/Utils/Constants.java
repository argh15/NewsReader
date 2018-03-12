package com.teksystems.newsreader.Utils;

/**
 * Created by Arghadeep on 05-03-2018.
 */

public class Constants {
    Constants() {
    }
    public static String API_TAG = "apiKey";

    //API Key
    public static String API_KEY = "36799ac14c6141c1a000829a9da33e0f";

    //News Urls
    public static String NEWS_URL_TOP_HEADLINES = "https://newsapi.org/v2/top-headlines?country=in&";

    //Progress Dialog
    public static String PROGRESS_TITLE = "Fetching News";
    public static String PROGRESS_CONTENT = "Please wait...";

    public static String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";

}
