package com.example.student.myrss;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class ArticleViewAdapter extends BaseAdapter {


    public static final int DURATION_MILLIS = 500;
    private List<String> titlesOfArticles;
    private List<String> referencesOfArticles;
    private List<String> imagesOfArticles;
    private  ImageLoader imageLoader;
    private boolean onLoadComplete = false;
    Bundle bundle = new Bundle();
    private boolean[] imagesLoaded;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener ;
    public ArticleViewAdapter(List<String> titles,List<String> references, List<String> imagesUrls, HeadlinesFragment fragment) {

        Log.d("Fast$FuriousView",imagesUrls.size() + "");
        imagesOfArticles = imagesUrls;
        titlesOfArticles = titles;
        referencesOfArticles = references;
        this.imagesLoaded = new boolean[imagesUrls.size()];
        animateFirstListener = new AnimateFirstDisplayListener();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

    }
    @Override
    public int getCount() {
        return titlesOfArticles.size();
    }

    @Override
    public String getItem(int position) {
        return titlesOfArticles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView;
        MyHolder holder;

        if (convertView == null) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            rootView = inflater.inflate(R.layout.layout_headlines_listview, parent, false);

            TextView headlineText = (TextView) rootView.findViewById(R.id.headlines_text);
            ImageView headlineImage = (ImageView) rootView.findViewById(R.id.headlines_image);

            holder = new MyHolder(headlineText, headlineImage);

            rootView.setTag(holder);


        } else {
            rootView = convertView;
            holder = (MyHolder)convertView.getTag();
               }


//------------
        holder.text.setText(titlesOfArticles.get(position));
        ImageLoader.getInstance().displayImage(imagesOfArticles.get(position), holder.im, options, animateFirstListener);

        return rootView;
    }

    public static class MyHolder {
        public TextView text;
        public ImageView im;

        public MyHolder(TextView text, ImageView im) {
            this.text = text;
            this.im = im;
        }
    }

    private static class AnimateFirstDisplayListener implements ImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, DURATION_MILLIS);
                    displayedImages.add(imageUri);
                }
            }
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    }
}
