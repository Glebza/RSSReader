package com.example.student.myrss;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Глеб on 25-Aug-15.
 */
public class ArticleLoaderAsyncTask extends AsyncTask<String,Void,Void> {
    public static final String TAG = ArticleLoaderAsyncTask.class.getName();
    private ArticleFragment articleFragment;
    private List<String> bitmapImagesUrls;
    private String articleHeader;
    private LinkedHashMap<String,String> markup;
    public ArticleLoaderAsyncTask(ArticleFragment articleFragment) {
        this.articleFragment = articleFragment;
        bitmapImagesUrls = new ArrayList<>();
        markup = new LinkedHashMap<>();
    }

    @Override
    protected Void doInBackground(String... params) {
        try {

            Document document = Jsoup.connect(params[0]).get();
            Element header = document.getElementsByTag("h1").first();
            articleHeader = header.text() ;

            Elements sections = document.getElementsByTag("section");
            Log.d(TAG, "Numbers of section: " + sections.size() + " " + sections.attr("class"));

            String imgSrc = "";
            for (Element e: sections){
                    Elements elements = e.getAllElements();
               // Log.d(TAG, "Numbers of section's children: " + elements.size() + "");
                    for (Element element : elements){
                        Log.d(TAG,element.tag() + "");
                        if (element.tag().toString().equals("p")){
                            markup.put(element.text(),"p");
                        }
                        if (element.tag().toString().equals("img")){
                            Log.d(TAG,"im an image and im jump into map" + element.attr("src"));
                            markup.put(element.attr("src"),"img");

                        }
                    }


            }
            // Decode Bitmap
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {


        TextView texView = new TextView(articleFragment.getActivity());
        texView.setTextAppearance(articleFragment.getActivity().getApplicationContext(), R.style.headerText);
        articleFragment.getLinearLayout().addView(texView);
        texView.setText(articleHeader);
        Iterator<Map.Entry<String, String>> itr1 = markup.entrySet().iterator();
        while (itr1.hasNext()) {
            Map.Entry<String, String> entry = itr1.next();
            if (entry.getValue().equals("p")){
                TextView textView = new TextView(articleFragment.getActivity());
                texView.setTextAppearance(articleFragment.getActivity().getApplicationContext(), R.style.headerText);
                articleFragment.getLinearLayout().addView(textView);
                textView.setText(entry.getKey());
            }  if (entry.getValue().equals("img")){
                ImageView imageView = new ImageView(articleFragment.getActivity());
                articleFragment.getLinearLayout().addView(imageView);
                ImageLoader.getInstance().displayImage(entry.getKey(), imageView);
            }

        }

        // ImageView logo = (ImageView) articleFragment.getActivity().findViewById(R.id.newsPicture);
        for (String url : bitmapImagesUrls){
            if (articleFragment == null){
                Log.d(TAG,"link to fragment is NUUUUUUUUUUUL bitch!");
            }

        }
    }

}
