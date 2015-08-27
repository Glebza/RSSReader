package com.example.student.myrss;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 24.07.2015.
 */
public class RssParser {
    private static final String ARTICLE_STRING_DELIMETER = "11110000";
    public static final String TAG = RssParser.class.getCanonicalName();
    public static final String RSS = "rss";
    public static final String CHANNEL = "channel";
    public static final String ITEM = "item";
    public static final String TITLE = "title";
    public static final String LINK = "link";
    public static final String DESCRIPTION = "description";
    public static final String BEFORE_IMG_URL = "img src=\"";
    public static final String AFTER_IMG_URL = "\">";
    XmlPullParserFactory factory;
    XmlPullParser parser;
    InputStream is;

    public RssParser(InputStream is) {
        this.is = is;
        try {
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.is));
            String s;
            String result = "";
            try {
                while ((s = bufferedReader.readLine())!= null){
                    result += s;
                }
                Log.i(TAG, "total = " + result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            parser.setInput(new StringReader(result));

            //  parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }
    public List<String[]> parse(){
        List<String[]> result = new ArrayList<String[]>();
        List<String> temporary ;
        String [] items ;
        temporary = parseItems();
        for (String s : temporary){
            items = s.split(ARTICLE_STRING_DELIMETER);
            result.add(items);
        }
        return result;


    }
    public List<String> parseItems(){

        List<String> items = new ArrayList<>();
        try {
            items = readRss();
            for (String s : items){
                Log.i(TAG," " + s);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  items;
    }
    private List<String> readRss()  throws XmlPullParserException, IOException {

        List<String> items = new ArrayList<>();
        Log.i(TAG, String.valueOf(parser.getEventType()));
        int nextEvent = parser.next();
        while (nextEvent != XmlPullParser.START_TAG) {
            nextEvent = parser.next();
        }
        parser.require(XmlPullParser.START_TAG, null, RSS);

        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(CHANNEL)) {
                items.addAll(readChannel(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }
    private List<String> readChannel(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        List<String> items = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, CHANNEL);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(ITEM)) {
                items.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }
    private String readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        parser.require(XmlPullParser.START_TAG, null, ITEM);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case TITLE:{
                   result += readTitle(parser)+ ARTICLE_STRING_DELIMETER;
                    break;
                }
                case LINK: {
                   result += readLink(parser) + ARTICLE_STRING_DELIMETER;
                    break;
                }
                case DESCRIPTION:{
                    result += readDescription(parser);
                    break;
                }
                default:
                    skip(parser);
            }
        }
        return result;
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, TITLE);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, TITLE);
        return title;
    }

    private String readLink(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, LINK);
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, LINK);
        return link;

    }

    private String readDescription(XmlPullParser parser)
            throws IOException, XmlPullParserException {

        String imgUrl = "";
        parser.require(XmlPullParser.START_TAG,null, DESCRIPTION);
        imgUrl = readText(parser);
        parser.require(XmlPullParser.END_TAG,null,DESCRIPTION);

        imgUrl = imgUrl.split(BEFORE_IMG_URL)[1];
        imgUrl = imgUrl.split(AFTER_IMG_URL)[0];
        return imgUrl;
    }

    private String readText(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth --;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
