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

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class ArticleViewAdapter extends BaseAdapter {


    private List<String> titlesOfArticles;
    private List<String> referencesOfArticles;
    private List<String> imagesOfArticles;
    private  ImageLoader imageLoader;
    private boolean onLoadComplete = false;

    public ArticleViewAdapter(List<String> titles,List<String> references, List<String> imagesUrls, HeadlinesFragment fragment,ImageLoader imageLoader) {

        Log.d("Fast$FuriousView",imagesUrls.size() + "");
        imagesOfArticles = imagesUrls;
        titlesOfArticles = titles;
        referencesOfArticles = references;
        this.imageLoader = imageLoader;

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
        } else { // convertView != null, �.�. ������� �� �����������
            rootView = convertView;
            holder = (MyHolder)convertView.getTag();
        }


//------------

        holder.text.setText(titlesOfArticles.get(position));
      //  Log.d("Images size", this.images.size() + "");
        if(!onLoadComplete) {

            imageLoader.displayImage(imagesOfArticles.get(position), holder.im);
            onLoadComplete = true;
        }else{

        }
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

}
