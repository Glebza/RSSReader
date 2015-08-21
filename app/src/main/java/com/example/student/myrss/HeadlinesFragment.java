package com.example.student.myrss;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 23.07.2015.
 */
public class HeadlinesFragment extends Fragment  {
    OnHeadlineSelectedListener mCallback;
    private ListView list;
    private List<String> headlinesTitles;
    private List<String> headlinesReferences;
    private List<Drawable> headlinesImages;
    private ArticleViewAdapter adapter;

    public interface OnHeadlineSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onArticleSelected(int position);
    }
    private ListView mRssFeed;
    List<String> rssFeed = new ArrayList<String>();
    AndroidPitRssFeedTask androidPitRssFeedTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidPitRssFeedTask = new AndroidPitRssFeedTask(this);
        androidPitRssFeedTask.execute();
        setRetainInstance(true);

    }
    public void createAdapter(){
        if (mRssFeed!= null){


            adapter = new ArticleViewAdapter(rssFeed,this);
            mRssFeed.setAdapter(adapter);

        }

    }

    public void setFeed(List<String> list){
        rssFeed = list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRssFeed = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRssFeed = (ListView) rootView.findViewById(R.id.list);
        createAdapter();



        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        list = (ListView) getActivity().findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"Wooohooo",Toast.LENGTH_SHORT).show();
               mCallback.onArticleSelected(position);
            }
        }) ;


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }
}
