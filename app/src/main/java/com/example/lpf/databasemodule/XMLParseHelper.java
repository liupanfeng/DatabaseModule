package com.example.lpf.databasemodule;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Created by lpf on 2017/4/28.
 */

public class XMLParseHelper {

    public static final String TAG=XMLParseHelper.class.getName();

    /**
     * 从asset路径下读取对应文件转String输出
     * @param mContext
     * @return
     */
    public static String getXmlStr(Context mContext, String fileName) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }


    /**
     * 用Pull方式解析XML
     * @param xmlData 字符串类型的xml
     */
    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            //设置输入的内容
            xmlPullParser.setInput(new StringReader(xmlData));
            //获取当前解析事件，返回的是数字
            int eventType = xmlPullParser.getEventType();
            //保存内容
            String id = "";
            String name = "";
            String version = "";

            while (eventType != (XmlPullParser.END_DOCUMENT)) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    //开始解析XML
                    case XmlPullParser.START_TAG: {
                        //nextText()用于获取结点内的具体内容
                        if ("id".equals(nodeName))
                            id = xmlPullParser.nextText();
                        else if ("name".equals(nodeName))
                            name = xmlPullParser.nextText();
                        else if ("version".equals(nodeName))
                            version = xmlPullParser.nextText();
                    }
                    break;
                    //结束解析
                    case XmlPullParser.END_TAG: {
                        if ("app".equals(nodeName)) {
                            Log.d(TAG, "parseXMLWithPull: id is " + id);
                            Log.d(TAG, "parseXMLWithPull: name is " + name);
                            Log.d(TAG, "parseXMLWithPull: version is " + version);
                        }
                    }
                    break;
                    default:
                        break;
                }
                //下一个
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Sax 解析
     * @param inputStream
     * @return
     */
    private List<Info> onParseSax(InputStream inputStream) {

        List<Info> infoList = new ArrayList<>();

        //创建工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //创建SAXParse
        try {
            SAXParser parser = factory.newSAXParser();
            //获取事件源
            XMLReader xmlReader = parser.getXMLReader();

            MyParseHandler handler = new MyParseHandler(infoList);

            xmlReader.setContentHandler(handler);

            //解析XML文档
            xmlReader.parse(new InputSource(inputStream));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return infoList;
    }


}
