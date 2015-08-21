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
                Log.i("LAB10", "total = " + result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            parser.setInput(new StringReader(result));

            //  parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }

    public List<String> parse(){

        List<String> items = new ArrayList<>();
        try {
            items = readRss();
            for (String s : items){
                Log.i("PARSE"," " + s);
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
        Log.i("LAB10", String.valueOf(parser.getEventType()));
        int nextEvent = parser.next();
        while (nextEvent != XmlPullParser.START_TAG) {
            nextEvent = parser.next();
        }
        parser.require(XmlPullParser.START_TAG, null, "rss");

        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("channel")) {
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
        parser.require(XmlPullParser.START_TAG, null, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                items.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }
    private String readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        parser.require(XmlPullParser.START_TAG, null, "item");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.d(getClass().getName(),"tag name is " + name);
            switch (name) {
                case "title":{
                   result += readTitle(parser)+ ARTICLE_STRING_DELIMETER;
                    break;
                }
                case "link" : {
                   result += readLink(parser) + ARTICLE_STRING_DELIMETER;
                    break;
                }
                case "description" :{
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
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    private String readLink(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "link");
        return link;

    }

    private String readDescription(XmlPullParser parser)
            throws IOException, XmlPullParserException {

        String imgUrl = "";
        parser.require(XmlPullParser.START_TAG,null, "description");

        imgUrl = readText(parser);
        parser.require(XmlPullParser.END_TAG,null,"description");
        imgUrl = imgUrl.split("img src=\"")[1];
        imgUrl = imgUrl.split("\">")[0];
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
