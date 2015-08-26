package com.example.student.myrss;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
/*TODO: Разобрать документ на составные части img и р. Потом положить все в map и затем вывести на экран в порядке добавления*/
    @Override
    protected Void doInBackground(String... params) {
        try {
            Log.d(TAG, "in doInBackground to make a dirty");

            Document document = Jsoup.connect(params[0]).get();
            Element header = document.getElementsByTag("h1").first();
            articleHeader = header.text() ;
            Log.d(TAG, articleHeader);
            Elements images = document.getElementsByTag("section");
            String imgSrc = "";
            for (Element e: images){
                for(Element element :e.children()){
                    if (element.tag().equals("img")){
                        markup.put(element.text(),"img");
                    }else{
                        if (element.tag().equals("")){
                            markup.put(element.text(),"p");
                        }
                    }
                }

                //if (e.attr("alt").equals("User picture")){}
                    imgSrc = e.attr("src");
                    Log.d(TAG, imgSrc);
                    //InputStream input = new java.net.URL(imgSrc).openStream();
                    bitmapImagesUrls.add(imgSrc);

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
       texView.setTextAppearance(articleFragment.getActivity().getApplicationContext(),R.style.headerText);
        articleFragment.getLinearLayout().addView(texView);
        texView.setText(articleHeader);
        // ImageView logo = (ImageView) articleFragment.getActivity().findViewById(R.id.newsPicture);
        for (String url : bitmapImagesUrls){
            if (articleFragment == null){
                Log.d(TAG,"link to fragment is NUUUUUUUUUUUL bitch!");
            }
            ImageView imageView = new ImageView(articleFragment.getActivity());
            articleFragment.getLinearLayout().addView(imageView);
            ImageLoader.getInstance().displayImage(url,imageView);
        }
    }

}
