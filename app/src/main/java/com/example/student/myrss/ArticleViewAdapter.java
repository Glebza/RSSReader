package com.example.student.myrss;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Глеб on 13-Aug-15.
 */
public class ArticleViewAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
    public static class MyHolder {
        public TextView text;
        public TextView headline;
        public ImageView im;

        public MyHolder(TextView text, TextView headline, ImageView im) {
            this.text = text;
            this.headline = headline;
            this.im = im;
        }
    }

}
