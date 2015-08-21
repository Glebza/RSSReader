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


    private List<String> titlesOfArticles;
    private List<String> referencesOfArticles;
    private List<Drawable> imagesOfArticles;



    public ArticleViewAdapter(List<String> titles,List<String> references, List<Drawable> images, HeadlinesFragment fragment) {

        Log.d("Fast$FuriousView",images.size() + "");
        imagesOfArticles = images;
        titlesOfArticles = titles;
        referencesOfArticles = references;

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
        if(imagesOfArticles.size()!=0) {
            holder.im.setImageDrawable(imagesOfArticles.get(position));
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
