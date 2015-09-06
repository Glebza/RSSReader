package com.example.student.myrss;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//TODO: ПЕРЕДЕЛАТЬ ВЕСЬ КЛАСС! Сделать все проще и понятнее
public class ArticleLoaderAsyncTask extends AsyncTask<String,Void,Void> {
    public static final String TAG = ArticleLoaderAsyncTask.class.getName();
    public static final int SECTIONS_WITH_COMMENTS = 4;
    private ArticleFragment articleFragment;
    private List<String> bitmapImagesUrls;
    private String articleHeader;
    private LinkedHashMap<String,String> markup;
    private ProgressDialog progDailog;

    public ArticleLoaderAsyncTask(ArticleFragment articleFragment) {
        this.articleFragment = articleFragment;
        bitmapImagesUrls = new ArrayList<>();
        markup = new LinkedHashMap<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        lockScreenOrientation();
        progDailog = new ProgressDialog(articleFragment.getActivity());
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }
    private void lockScreenOrientation() {
        int currentOrientation = articleFragment.getActivity().getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            articleFragment.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            articleFragment.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

    }

    @Override
    protected Void doInBackground(String... params) {
        try {
//Берем всю веб страницу и выделяем из нее атрубуты и затем динамически создаем необходимые View
            Document document = Jsoup.connect(params[0]).get();
            Element header = document.getElementsByTag("h1").first();
            articleHeader = header.text() ;

            Elements sections = document.getElementsByTag("section");

            String imgSrc = "";
            for (int i = 0; i < sections.size()- SECTIONS_WITH_COMMENTS ;i++){
                Element e = sections.get(i);
                if (!e.attr("class").equals("articlePart articleComments")){

                    Elements elements = e.getAllElements();
                    for (Element element : elements){
                       Log.d(TAG,"element " + i + "= " + element.tag());

                        if (element.tag().toString().equals("ul")) {

                                if (element.attr("class").contains("deviceSpecs specTeaser")){
                                    Log.d(TAG,"ul class = " + i + element.attr("class"));
                                    markup.put(element.children().size()+" " + i,"ulTab");
                                }
                                if (element.attr("class").contains("deviceSpecs specLine")) {
                                    Log.d(TAG,"ul class specLine = " + i + element.attr("class"));
                                    markup.put(element.children().size()+" " + i,"ulTabRow");
                                }
                            if ( !(element.attr("class").contains("deviceSpecs specLine" ) && !(element.attr("class").contains("deviceSpecs specTeaser" ))) ) {
                                markup.put(element.children().size()+" " + i,"ul");
                            }


                        }
                        //для того, чтобы не обрабатывать отдельно теги <a>, будем разбирать их внутри более крупных структурных единиц
                        if (element.tag().toString().equals("li")) {
                            String hyperLink = element.html();
                            markup.put(hyperLink,"li");
                          /*  if (element.getElementsByTag("a")!=null){


                                String hyperLink = element.html();
                                markup.put(hyperLink,"liForLink");


                            }else{markup.put(element.text(),"li");}*/
                        }
                        if (element.tag().toString().equals("p")){
                            markup.put(element.html(),"p");
                        }
                        if (element.tag().toString().equals("h2")){
                            markup.put(element.text(),"h2");
                        }
                        if (element.tag().toString().equals("img")){
                            markup.put(element.attr("src"),"img");
                        }
                        if (element.tag().toString().equals("figcaption")){
                            markup.put(element.text(),"figcaption");
                         //   Log.d(TAG,"im figcaption and i've puted into the map ");
                        }
                    }
                }


            }
            // Decode Bitmap
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPostExecute(Void aVoid) {


        TextView texView = new TextView(articleFragment.getActivity());
        texView.setTextAppearance(articleFragment.getActivity().getApplicationContext(), R.style.headerText);
        articleFragment.getLinearLayout().addView(texView);
        texView.setText(articleHeader);
        Iterator<Map.Entry<String, String>> itr1 = markup.entrySet().iterator();
        while (itr1.hasNext()) {

            Map.Entry<String, String> entry = itr1.next();
            Log.d(TAG,"entry =" + entry.getValue());

            if (entry.getValue().equals("ul") ) {
                TableLayout tableLayout = new TableLayout(articleFragment.getActivity());
                tableLayout.setColumnStretchable(1, true);
                tableLayout.setColumnShrinkable(1,true);
                articleFragment.getLinearLayout().addView(tableLayout);
                int numberOfColumns = Integer.parseInt(entry.getKey().split(" ")[0]);
                for (int i = 0; i < numberOfColumns;i++) {
                    TableRow tableRow = new TableRow(articleFragment.getActivity());
                    entry = itr1.next();
                    TextView textView = new TextView(articleFragment.getActivity());
                    textView.setText(Html.fromHtml(entry.getKey()));
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    ImageView imageView = setIconImageView();
                    //order is important
                    tableRow.addView(imageView);
                    tableRow.addView(textView);
                    tableLayout.addView(tableRow);
                }
            }
            if (entry.getValue().equals("ulTab") ) {
                TableLayout tableLayout = new TableLayout(articleFragment.getActivity());
                tableLayout.setColumnStretchable(1,true);
                tableLayout.setColumnStretchable(0,true);
                tableLayout.setColumnShrinkable(0,true);
                tableLayout.setColumnShrinkable(1,true);
                articleFragment.getLinearLayout().addView(tableLayout);
                int numberOfColumns = Integer.parseInt(entry.getKey().split(" ")[0]);
                for (int i = 0; i < numberOfColumns;i++) {
                    TableRow tableRow = new TableRow(articleFragment.getActivity());
                    entry = itr1.next();
                    if (entry.getValue().equals("ulTabRaw") ){
                        int numberOfElements = Integer.parseInt(entry.getKey().split(" ")[0]);
                        for (int j = 0;j< numberOfElements;j++) {
                            entry = itr1.next();
                            TextView textView = new TextView(articleFragment.getActivity());
                            textView.setText(Html.fromHtml(entry.getKey()));
                            textView.setMovementMethod(LinkMovementMethod.getInstance());
                            tableRow.addView(textView);
                        }
                    }



                    tableLayout.addView(tableRow);
                }
            }
            if (entry.getValue().equals("p")){
                TextView textView = new TextView(articleFragment.getActivity());
                texView.setTextAppearance(articleFragment.getActivity().getApplicationContext(), R.style.headerText);
                articleFragment.getLinearLayout().addView(textView);
                textView.setText(Html.fromHtml(entry.getKey()));
                textView.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if (entry.getValue().equals("h2")){
                TextView textView = new TextView(articleFragment.getActivity());
                texView.setTextAppearance(articleFragment.getActivity().getApplicationContext(), R.style.h2Text);
                articleFragment.getLinearLayout().addView(textView);
                textView.setText(entry.getKey());
            }
            if (entry.getValue().equals("figcaption")){
                TextView textView = new TextView(articleFragment.getActivity());
                texView.setTextAppearance(articleFragment.getActivity().getApplicationContext(), R.style.figcaptionText);
                articleFragment.getLinearLayout().addView(textView);
                textView.setText(entry.getKey());
            }
            if (entry.getValue().equals("img")){
                ImageView imageView = new ImageView(articleFragment.getActivity());
                articleFragment.getLinearLayout().addView(imageView);
                ImageLoader.getInstance().displayImage(entry.getKey(), imageView);
            }

        }

        unlockScreenOrientation();
        progDailog.dismiss();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    private ImageView setIconImageView() {
        ImageView sampleImageView = new ImageView(articleFragment.getActivity());
        Resources resources = articleFragment.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.android_small, articleFragment.getActivity().getTheme());
        sampleImageView.setImageDrawable(drawable);
        return sampleImageView;
    }

    private void unlockScreenOrientation() {
        articleFragment.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
    public void stopLoading(){
        progDailog.cancel();
    }

}
