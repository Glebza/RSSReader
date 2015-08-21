package com.example.student.myrss;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by student on 23.07.2015.
 */
public class AndroidPitRssFeedTask extends AsyncTask<Void, Void, List<String[]>> {



    private HeadlinesFragment fragment;

    public AndroidPitRssFeedTask(HeadlinesFragment fragment) {
        this.fragment = fragment;
      //  this.activity = activity;
       // textView = (TextView)activity.findViewById(R.id.rss_feed);

    }

    @Override
    protected List<String[]> doInBackground(Void...voids ) {
        Log.d(getClass().getName(),"in parse");
        List<String[]> result = null;
        RssParser rssParser = new RssParser(getAndroidPitRssFeed());
        result = rssParser.parse();
        return result;
    }

    @Override
    protected void onPostExecute(List<String[]> rssFeed) {
        fragment.setFeed(rssFeed);
        fragment.createAdapterFeed();




    }
    public InputStream getAndroidPitRssFeed(){
        InputStream in = null;

        try {

            URL url = new URL("http://www.androidpit.com/feed/main.xml");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(true);
            HttpURLConnection.setFollowRedirects(true);


            String newURL;
            Log.i("LAb 10 lkgh", String.valueOf(conn.getResponseCode()));

            while (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP || conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM){
                newURL = conn.getHeaderField("Location");
                conn = (HttpURLConnection)new URL(newURL).openConnection();
            }
            in = conn.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
}