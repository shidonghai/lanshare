
package com.nano.lanshare.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

public class Extentions {
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";
    public static final String IMAGE = "image";
    public static final String TEXT = "text";
    public static final String WORD = "word";
    public static final String EXCEL = "excel";
    public static final String WEB = "web";
    public static final String PKG = "pkg";
    public static final String PPT = "ppt";
    public static final String PDF = "pdf";
    public static final String APK = "apk";
    public static final String UNKNOWN = "unknown";

    private static final String IMAGE_MIME_TYPE = "image/*";
    private static final String VIDEO_MIME_TYPE = "video/*";
    private static final String AUDIO_MIME_TYPE = "audio/*";
    private static final String TEXT_MIME_TYPE = "text/plain";
    private static final String WEB_MIME_TYPE = "text/html";
    private static final String PPT_MIME_TYPE = "application/vnd.ms-powerpoint";
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String EXCEL_MIME_TYPE = "application/vnd.ms-excel";
    private static final String WORD_MIME_TYPE = "application/msword";
    private static final String PKG_MIME_TYPE = "application/octet-stream";
    private static final String APK_MIME_TYPE = "application/vnd.android.package-archive";

    private String mFilename = "extentions.xml";
    private Context mContext;
    private Map<String, List<String>> mExtentionMap;
    private static Extentions mExtentions;

    private Extentions(Context context) {
        mContext = context;
        mExtentionMap = new HashMap<String, List<String>>();
    }

    public static Extentions getIntance(Context context) {
        if (mExtentions == null) {
            mExtentions = new Extentions(context);
        }
        return mExtentions;
    }

    public static void clear() {
        mExtentions = null;
    }

    public String getType(File file) {
        if (mExtentionMap == null) {
            return UNKNOWN;
        }
        Set<String> keys = mExtentionMap.keySet();
        if (keys == null) {
            return UNKNOWN;
        }
        String filename = file.getName().toLowerCase();
        for (String key : keys) {
            List<String> list = mExtentionMap.get(key);
            if (list == null) {
                return UNKNOWN;
            }
            for (String extention : list) {
                if (filename.endsWith(extention)) {
                    return key;
                }
            }
        }

        return UNKNOWN;
    }

    public String getMimeType(String type) {
        if (type.equals(VIDEO)) {
            return VIDEO_MIME_TYPE;
        }
        if (type.equals(AUDIO)) {
            return AUDIO_MIME_TYPE;
        }
        if (type.equals(IMAGE)) {
            return IMAGE_MIME_TYPE;
        }
        if (type.equals(TEXT)) {
            return TEXT_MIME_TYPE;
        }
        if (type.equals(WEB)) {
            return WEB_MIME_TYPE;
        }
        if (type.equals(WORD)) {
            return WORD_MIME_TYPE;
        }
        if (type.equals(EXCEL)) {
            return EXCEL_MIME_TYPE;
        }
        if (type.equals(APK))
        {
            return APK_MIME_TYPE;
        }
        if (type.equals(PKG)) {
            return PKG_MIME_TYPE;
        }
        if (type.equals(PPT)) {
            return PPT_MIME_TYPE;
        }
        if (type.equals(PDF)) {
            return PDF_MIME_TYPE;
        }
        return "*/*";
    }

    public String getMimeType(File file) {
        String type = getType(file);
        return getMimeType(type);
    }

    public void init() {
        new Thread(new Runnable() {
            public void run() {
                initRun();
            }
        }).start();
    }

    private void initRun() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            InputStream is = mContext.getAssets().open(mFilename);
            parser.parse(is, new ExtendsHandler(mExtentionMap));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ExtendsHandler extends DefaultHandler {
        private static final String CATEGORY = "category";
        private static final String ITEM = "item";
        private Map<String, List<String>> extendsMap;
        private List<String> list;
        private String category;
        private String tag;

        public ExtendsHandler(Map<String, List<String>> extendsMap) {
            this.extendsMap = extendsMap;
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            tag = localName;
            if (tag.equals(CATEGORY)) {
                list = new ArrayList<String>();
                category = attributes.getValue(0);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            super.endElement(uri, localName, qName);
            if (localName.equals(CATEGORY)) {
                extendsMap.put(category, list);
            }
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            if (tag.equals(ITEM)) {
                String extention = new String(ch, start, length);
                if (extention == null || extention.trim().equals("")) {
                    return;
                }
                list.add(extention);
            }

        }
    }

}
