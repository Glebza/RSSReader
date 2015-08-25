package com.example.student.myrss;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity  implements HeadlinesFragment.OnHeadlineSelectedListener{
    public static final String ARTICLE_URL = "Article";
private List<String> articles = new ArrayList<String>();
    private HeadlinesFragment headlinesFragment;
    private List<String> articlesUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageLoader imageLoader;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        FragmentManager fragmentManager = getFragmentManager();
       if( fragmentManager.findFragmentById(R.id.frame)== null){

           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            headlinesFragment = new HeadlinesFragment();
           fragmentTransaction.replace(R.id.frame, headlinesFragment);
           fragmentTransaction.commit();


       }else{

       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        ArticleFragment articleFrag = (ArticleFragment)getFragmentManager().findFragmentById(R.id.article_fragment);
        articlesUrls = headlinesFragment.getArticlesUrl();
        if (articleFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            articleFrag.updateArticleView(position);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            ArticleFragment newFragment = new ArticleFragment();
            Bundle args = new Bundle();
            args.putInt(ArticleFragment.ARG_POSITION, position);
            args.putString(ARTICLE_URL,articlesUrls.get(position));
            newFragment.setArguments(args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back

            transaction.replace(R.id.frame, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

}
