package com.example.student.myrss;

import android.content.Context;
import android.graphics.drawable.Drawable;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ArticleViewAdapter extends BaseAdapter {
    private Drawable image;
    private List<String> list;
    private List<String> titlesOfArticles;
    private List<String> referencesOfArticles;
    private List<String> imagesOfArticles;
    private List<Drawable> images;
    private String title;
    private String imageURL;
    private String referenceURL;
    private HeadlinesFragment fragment;

    public ArticleViewAdapter(List<String> list,HeadlinesFragment fragment) {
        this.list = list;
        this.fragment = fragment;
        imagesOfArticles = new ArrayList<String>();
        titlesOfArticles = new ArrayList<String>();
        referencesOfArticles = new ArrayList<String>();
        images = new ArrayList<Drawable>();
        Log.d("Pic",images.size()+ "");
        String[]  separateItems = new String [3];
        for (String s: list){
            separateItems = s.split("11110000");
            titlesOfArticles.add(separateItems[0]);
            referencesOfArticles.add(separateItems[1]);
            imagesOfArticles.add(separateItems[2]);
        }
        images = getImageFromURL(imagesOfArticles);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
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
        } else { // convertView != null, �.�. ������� �� �����������
            rootView = convertView;
            holder = (MyHolder)convertView.getTag();
        }


//------------

        holder.text.setText(title);
      //  Log.d("Images size", this.images.size() + "");
        //holder.im .setImageDrawable(this.images.get(position));

        return rootView;
    }

    private List<Drawable> getImageFromURL(List<String> imageURLs) {
        List<Drawable> images = new ArrayList<Drawable>();
        ImageLoaderAsyncTask imageLoaderAsyncTask = new ImageLoaderAsyncTask(fragment);
        imageLoaderAsyncTask.execute(imageURLs);
        images = imageLoaderAsyncTask.getImages();

        return images;
    }


    public static class MyHolder {
        public TextView text;
        public ImageView im;

        public MyHolder(TextView text, ImageView im) {
            this.text = text;
            this.im = im;
        }
    }

}
