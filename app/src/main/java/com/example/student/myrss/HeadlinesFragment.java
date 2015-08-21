package com.example.student.myrss;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by student on 23.07.2015.
 */
public class HeadlinesFragment extends Fragment  {
    OnHeadlineSelectedListener mCallback;
    private ListView list;
    private List<String> headlinesTitles = new ArrayList<String>();
    private List<String> headlinesReferences = new ArrayList<String>();
    private List<Drawable> headlinesImages = new ArrayList<Drawable>();
    private List<String> getHeadlinesImagesUrls = new ArrayList<String>();
    private ArticleViewAdapter adapter;

    public interface OnHeadlineSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onArticleSelected(int position);
    }
    private ListView mRssFeed;
    List<String[]> rssFeed = new ArrayList<String[]>();
    AndroidPitRssFeedTask androidPitRssFeedTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getClass().getName(), "before rss parse");
        androidPitRssFeedTask = new AndroidPitRssFeedTask(this);
        androidPitRssFeedTask.execute();
        setRetainInstance(true);

    }
    public void createAdapterFeed(){
        Log.d("createAdapter", "create adapter");
        if (mRssFeed!= null){
            Log.d("Shit","before shit");
            for(String[] s: rssFeed){
                headlinesTitles.add(s[0]);
                headlinesReferences.add(s[1]);
               getHeadlinesImagesUrls.add(s[2]);
                Log.d("rssFeed",s[0] + " " + s[1] + " " + s[2] + " ");
            }

            ImageLoaderAsyncTask imageLoaderAsyncTask = new ImageLoaderAsyncTask(this);
            imageLoaderAsyncTask.execute(getHeadlinesImagesUrls);
            Log.d("FastandFurious",headlinesImages.size() + "");


        }else{
            Log.d("Fragment","shit");
        }

    }
    public void createAdapter(){
        adapter = new ArticleViewAdapter(headlinesTitles,headlinesReferences,headlinesImages,this);
        mRssFeed.setAdapter(adapter);
    }

    public void setImagesFromUrl(List<Drawable> images) {

        headlinesImages = images;

    }

    public void setFeed(List<String[]> list){
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
