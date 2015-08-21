package com.example.student.myrss;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Глеб on 20-Aug-15.
 */

public class ImageLoaderAsyncTask extends AsyncTask<List<String>, Void, List<Drawable>> {

    private HeadlinesFragment headlinesFragment;
    private  List<Drawable> images;
    public ImageLoaderAsyncTask(HeadlinesFragment headlinesFragment) {
        this.headlinesFragment = headlinesFragment;
    }


    @Override
    protected List<Drawable> doInBackground(List<String>... params) {
        images = new ArrayList<Drawable>();
        try {
            for (String imageUrl : params[0]){

                Log.d("ImagesBeforeLoad", new Date().getTime() + "");
                InputStream is = (InputStream) new URL(imageUrl).getContent();
                Drawable drawable = Drawable.createFromStream(is, "src name");
                Log.d("ImagesAftereLoad", new Date().getTime() + "");
                images.add(drawable);

                Log.d("ImagesLoaded",images.get(0).hashCode() + "");
                is.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return images ;
    }

    @Override
    protected void onPostExecute(List<Drawable> drawables) {
        headlinesFragment.setImagesFromUrl(drawables);
        headlinesFragment.createAdapter();
    }
}
