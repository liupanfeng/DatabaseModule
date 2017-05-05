package com.example.lpf.databasemodule.xmlparse;

import android.content.Context;
import android.content.res.AssetManager;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Created by lpf on 2017/4/28.
 */

public class XMLParseHelper {

    public static final String TAG = XMLParseHelper.class.getName();

    /**
     * 从asset路径下读取对应文件转String输出
     *
     * @param mContext
     * @return
     */
    public static String getXmlStr(Context mContext, String fileName) {
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
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }

    /**
     * 用Pull方式解析XML
     *
     * @param xmlData 字符串类型的xml
     */
    public static void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            //设置输入的内容
            xmlPullParser.setInput(new StringReader(xmlData));
            //获取当前解析事件，返回的是数字
            int eventType = xmlPullParser.getEventType();
            File file = null;
            int num = 1;
            LResource lpfResource = new LResource();
            HashMap<String, ParamValue> map = lpfResource.getViewMap();
            while (eventType != (XmlPullParser.END_DOCUMENT)) {
                String nodeName = xmlPullParser.getName();
                if (nodeName==null||nodeName.equals("")){
                    eventType = xmlPullParser.next();
                    continue;
                }
                ParamValue paramValue = map.get(nodeName);
                if (paramValue==null||paramValue.equals("")){
                    eventType = xmlPullParser.next();
                    continue;
                }
                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        switch (paramValue) {
                            case templateid:
                                String templateid = xmlPullParser.nextText();
                                file = new File(Constant.SD_CARD_ROOT_PATH + templateid + ".xml");
                                if (file.exists()) {
                                    file.delete();
                                }
                                file.createNewFile();
                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
                                break;

                            case layout:
                                if (file == null) return;
                                if (num == 1) {
                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n");
                                } else {
                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "<LinearLayout\n");
                                }
                                num++;
                                int count=xmlPullParser.getAttributeCount();
                                for (int i=0;i<count;i++){
                                    String attrName=xmlPullParser.getAttributeName(i);
                                    ParamValue paramName = map.get(attrName);
                                    String value=xmlPullParser.getAttributeValue(i);

                                    switch (paramName){
                                        case width:
                                            if (file == null) return;
                                            String width = value.replace("px", "");
                                            if (width.equals("0")||width.equals("")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + "wrap_content" + '"' + "\n");
                                                return;
                                            }
                                            if (width.contains("match_parent") || width.contains("fill_parent") || width.contains("wrap_content")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + width + '"' + "\n");
                                            } else {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + width + "dp" + '"' + "\n");
                                            }
                                            break;
                                        case heigth:
                                            if (file == null) return;
                                            String height = value.replace("px", "");
                                            if (height.equals("0")||height.equals("")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + "wrap_content" + '"' + "\n");
                                                return;
                                            }
                                            if (height.contains("match_parent") || height.contains("fill_parent") || height.contains("wrap_content")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + height + '"' + "\n");
                                            } else {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + height + "dp" + '"' + "\n");
                                            }
                                            break;
                                        case background:
                                            if (file == null) return;
                                            String background = value;
                                            if (background.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.BACKGROUND + '"' + "#" + background + '"' + "\n");
                                            break;
                                        case orientation:
                                            if (file == null) return;
                                            String orientation = xmlPullParser.nextText();
                                            if (!"".equals(orientation)){
                                                if (orientation.equals("1")) {
                                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.ORIENTATION + '"' + "vertical" + '"' + "\n");
                                                } else {
                                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.ORIENTATION + '"' + "horizontal" + '"' + "\n");
                                                }
                                            }
                                            break;
                                    }
                                }
                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), ">\n");
                                break;

                            case text:
                                if (file == null) return;
                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "<TextView\n");
                                count=xmlPullParser.getAttributeCount();
                                for (int i=0;i<count;i++){
                                    String attrName=xmlPullParser.getAttributeName(i);
                                    ParamValue paramName = map.get(attrName);
                                    String value=xmlPullParser.getAttributeValue(i);
                                    switch (paramName){
                                        case width:
                                            if (file == null) return;
                                            String width = value.replace("px", "");
                                            if (width.equals("0")||width.equals("")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + "wrap_content" + '"' + "\n");
                                                return;
                                            }
                                            if (width.contains("match_parent") || width.contains("fill_parent") || width.contains("wrap_content")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + width + '"' + "\n");
                                            } else {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + width + "dp" + '"' + "\n");
                                            }
                                            break;
                                        case heigth:
                                            if (file == null) return;
                                            String height = value.replace("px", "");
                                            if (height.equals("0")||height.equals("")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + "wrap_content" + '"' + "\n");
                                                return;
                                            }
                                            if (height.contains("match_parent") || height.contains("fill_parent") || height.contains("wrap_content")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + height + '"' + "\n");
                                            } else {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + height + "dp" + '"' + "\n");
                                            }
                                            break;
                                        case background:
                                            if (file == null) return;
                                            String background = value;
                                            if (background.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.BACKGROUND + '"' + "#" + background + '"' + "\n");
                                            break;
                                        case value:
                                            if (file==null)return;
                                            value=xmlPullParser.nextText();
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH+file.getName(),"android:text="+'"'+value+'"'+"\n");
                                            break;
                                        case margin_left:
                                            if (file == null) return;
                                            String margin_left = xmlPullParser.nextText().replace("px", "");
                                            if (margin_left.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_LEFT + '"' + margin_left + "dp" + '"' + "\n");
                                            break;
                                        case margin_right:
                                            if (file == null) return;
                                            String margin_right = xmlPullParser.nextText().replace("px", "");
                                            if (margin_right.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_RIGHT + '"' + margin_right + "dp" + '"' + "\n");
                                            break;
                                        case margin_top:
                                            if (file == null) return;
                                            String margin_top = xmlPullParser.nextText().replace("px", "");
                                            if (margin_top.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_TOP + '"' + margin_top + "dp" + '"' + "\n");
                                            break;
                                        case margin_bottom:
                                            if (file == null) return;
                                            String margin_bottom = xmlPullParser.nextText().replace("px", "");
                                            if (margin_bottom.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_BOTTOM + '"' + margin_bottom + "dp" + '"' + "\n");
                                            break;
                                        case color:
                                            if (file == null) return;
                                            String color = xmlPullParser.nextText();
                                            if (color.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.TEXT_COLOR + '"' + "#" + color + '"' + "\n");
                                            break;
                                        case size:
                                            if (file == null) return;
                                            String size = xmlPullParser.nextText().replace("px", "");
                                            if (size.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.TEXT_SIZE + '"' + size + "sp" + '"' + "\n");
                                            break;
                                        case singleLine:
                                            String singleLine = xmlPullParser.nextText();
                                            if (singleLine.equals(""))return;
                                            if (singleLine.equals("1")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.ELLIPSIZE + '"' + "true" + '"' + "\n");
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.SINGLE_LINE + '"' + "true" + '"' + "\n");
                                            } else {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.SINGLE_LINE + '"' + "false" + '"' + "\n");
                                            }
                                            break;
                                        case horizontal_align:
                                            String align = xmlPullParser.nextText();
                                            if (align.equals(""))return;
                                            switch (align) {
                                                case Constant.LAYOUT_GRAVITY_LEFT:
                                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_GRAVITY + '"' + "left" + '"' + "\n");
                                                    break;
                                                case Constant.LAYOUT_GRAVITY_CENTER:
                                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_GRAVITY + '"' + "center" + '"' + "\n");
                                                    break;
                                                case Constant.LAYOUT_GRAVITY_RIGHT:
                                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_GRAVITY + '"' + "right" + '"' + "\n");
                                                    break;
                                            }
                                            break;
                                    }
                                }
                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "/>\n");
                                break;

                            case image:
                                if (file == null) return;
                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "<ImageView\n");
                                count=xmlPullParser.getAttributeCount();
                                for (int i=0;i<count;i++){
                                    String attrName=xmlPullParser.getAttributeName(i);
                                    ParamValue paramName = map.get(attrName);
                                    String value=xmlPullParser.getAttributeValue(i);
                                    switch (paramName){
                                        case width:
                                            if (file == null) return;
                                            String width = value.replace("px", "");
                                            if (width.equals("0")||width.equals("")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + "wrap_content" + '"' + "\n");
                                                return;
                                            }
                                            if (width.contains("match_parent") || width.contains("fill_parent") || width.contains("wrap_content")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + width + '"' + "\n");
                                            } else {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + width + "dp" + '"' + "\n");
                                            }
                                            break;
                                        case heigth:
                                            if (file == null) return;
                                            String height = value.replace("px", "");
                                            if (height.equals("0")||height.equals("")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + "wrap_content" + '"' + "\n");
                                                return;
                                            }
                                            if (height.contains("match_parent") || height.contains("fill_parent") || height.contains("wrap_content")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + height + '"' + "\n");
                                            } else {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + height + "dp" + '"' + "\n");
                                            }
                                            break;
                                        case value:
//                                            if (file==null)return;
//                                            value=xmlPullParser.nextText();
//                                            appendMethodA(Constant.SD_CARD_ROOT_PATH+file.getName(),"android:text="+'"'+value+'"'+"\n");
                                            break;
                                        case margin_left:
                                            if (file == null) return;
                                            String margin_left = xmlPullParser.nextText().replace("px", "");
                                            if (margin_left.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_LEFT + '"' + margin_left + "dp" + '"' + "\n");
                                            break;
                                        case margin_right:
                                            if (file == null) return;
                                            String margin_right = xmlPullParser.nextText().replace("px", "");
                                            if (margin_right.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_RIGHT + '"' + margin_right + "dp" + '"' + "\n");
                                            break;
                                        case margin_top:
                                            if (file == null) return;
                                            String margin_top = xmlPullParser.nextText().replace("px", "");
                                            if (margin_top.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_TOP + '"' + margin_top + "dp" + '"' + "\n");
                                            break;
                                        case margin_bottom:
                                            if (file == null) return;
                                            String margin_bottom = xmlPullParser.nextText().replace("px", "");
                                            if (margin_bottom.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_BOTTOM + '"' + margin_bottom + "dp" + '"' + "\n");
                                            break;
                                        case horizontal_align:
                                            String align = xmlPullParser.nextText();
                                            if (align.equals(""))return;
                                            switch (align) {
                                                case Constant.LAYOUT_GRAVITY_LEFT:
                                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_GRAVITY + '"' + "left" + '"' + "\n");
                                                    break;
                                                case Constant.LAYOUT_GRAVITY_CENTER:
                                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_GRAVITY + '"' + "center" + '"' + "\n");
                                                    break;
                                                case Constant.LAYOUT_GRAVITY_RIGHT:
                                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_GRAVITY + '"' + "right" + '"' + "\n");
                                                    break;
                                            }
                                            break;
                                        case shape:
                                            break;
                                    }
                                }
                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "/>\n");
                                break;
                            case button:
                                if (file == null) return;
                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "<Button\n");
                                count=xmlPullParser.getAttributeCount();
                                for (int i=0;i<count;i++){
                                    String attrName=xmlPullParser.getAttributeName(i);
                                    ParamValue paramName = map.get(attrName);
                                    String value=xmlPullParser.getAttributeValue(i);
                                    switch (paramName){
                                        case width:
                                            if (file == null) return;
                                            String width = value.replace("px", "");
                                            if (width.equals("0")||width.equals("")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + "wrap_content" + '"' + "\n");
                                                return;
                                            }
                                            if (width.contains("match_parent") || width.contains("fill_parent") || width.contains("wrap_content")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + width + '"' + "\n");
                                            } else {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + width + "dp" + '"' + "\n");
                                            }
                                            break;
                                        case heigth:
                                            if (file == null) return;
                                            String height = value.replace("px", "");
                                            if (height.equals("0")||height.equals("")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + "wrap_content" + '"' + "\n");
                                                return;
                                            }
                                            if (height.contains("match_parent") || height.contains("fill_parent") || height.contains("wrap_content")) {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + height + '"' + "\n");
                                            } else {
                                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + height + "dp" + '"' + "\n");
                                            }
                                            break;
                                        case background:
                                            if (file == null) return;
                                            String background = value;
                                            if (background.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.BACKGROUND + '"' + "#" + background + '"' + "\n");
                                            break;
                                        case value:
                                            if (file==null)return;
                                            value=xmlPullParser.nextText();
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH+file.getName(),"android:text="+'"'+value+'"'+"\n");
                                            break;
                                        case margin_left:
                                            if (file == null) return;
                                            String margin_left = xmlPullParser.nextText().replace("px", "");
                                            if (margin_left.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_LEFT + '"' + margin_left + "dp" + '"' + "\n");
                                            break;
                                        case margin_right:
                                            if (file == null) return;
                                            String margin_right = xmlPullParser.nextText().replace("px", "");
                                            if (margin_right.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_RIGHT + '"' + margin_right + "dp" + '"' + "\n");
                                            break;
                                        case margin_top:
                                            if (file == null) return;
                                            String margin_top = xmlPullParser.nextText().replace("px", "");
                                            if (margin_top.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_TOP + '"' + margin_top + "dp" + '"' + "\n");
                                            break;
                                        case margin_bottom:
                                            if (file == null) return;
                                            String margin_bottom = xmlPullParser.nextText().replace("px", "");
                                            if (margin_bottom.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_BOTTOM + '"' + margin_bottom + "dp" + '"' + "\n");
                                            break;
                                        case color:
                                            if (file == null) return;
                                            String color = xmlPullParser.nextText();
                                            if (color.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.TEXT_COLOR + '"' + "#" + color + '"' + "\n");
                                            break;
                                        case size:
                                            if (file == null) return;
                                            String size = xmlPullParser.nextText().replace("px", "");
                                            if (size.equals(""))return;
                                            appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.TEXT_SIZE + '"' + size + "sp" + '"' + "\n");
                                            break;
                                        case horizontal_align:
                                            String align = xmlPullParser.nextText();
                                            if (align.equals(""))return;
                                            switch (align) {
                                                case Constant.LAYOUT_GRAVITY_LEFT:
                                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_GRAVITY + '"' + "left" + '"' + "\n");
                                                    break;
                                                case Constant.LAYOUT_GRAVITY_CENTER:
                                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_GRAVITY + '"' + "center" + '"' + "\n");
                                                    break;
                                                case Constant.LAYOUT_GRAVITY_RIGHT:
                                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_GRAVITY + '"' + "right" + '"' + "\n");
                                                    break;
                                            }
                                            break;
                                    }
                                }
                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "/>\n");
                                break;
                        }
                    }
                    break;
                    case XmlPullParser.END_TAG: {
                       //nothing
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

//    /**
//     * 用Pull方式解析XML
//     *
//     * @param xmlData 字符串类型的xml
//     */
//    public static void parseXMLWithPull(String xmlData) {
//        try {
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            XmlPullParser xmlPullParser = factory.newPullParser();
//            //设置输入的内容
//            xmlPullParser.setInput(new StringReader(xmlData));
//            //获取当前解析事件，返回的是数字
//            int eventType = xmlPullParser.getEventType();
//            File file = null;
//            int num = 1;
//            LResource lpfResource = new LResource();
//            HashMap<String, ParamValue> map = lpfResource.getViewMap();
//            while (eventType != (XmlPullParser.END_DOCUMENT)) {
//                String nodeName = xmlPullParser.getName();
//                if (nodeName==null||nodeName.equals("")){
//                    eventType = xmlPullParser.next();
//                    continue;
//                }
//                ParamValue paramValue = map.get(nodeName);
//                if (paramValue==null||paramValue.equals("")){
//                    eventType = xmlPullParser.next();
//                    continue;
//                }
//                switch (eventType) {
//                    case XmlPullParser.START_TAG: {
//                        switch (paramValue) {
//                            case templateid:
//                                String templateid = xmlPullParser.nextText();
//                                file = new File(Constant.SD_CARD_ROOT_PATH + templateid + ".xml");
//                                if (file.exists()) {
//                                    file.delete();
//                                }
//                                file.createNewFile();
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
//                                break;
//                            case layout:
//                                if (file == null) return;
//                                if (num == 1) {
//                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n");
//                                } else {
//                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "<LinearLayout\n");
//                                }
//                                num++;
//                                break;
//                            case width:
//                                if (file == null) return;
//                                String width = xmlPullParser.nextText().replace("px", "");
//                                if (width.equals("0")||width.equals("")) {
//                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + "wrap_content" + '"' + "\n");
//                                    return;
//                                }
//                                if (width.contains("match_parent") || width.contains("fill_parent") || width.contains("wrap_content")) {
//                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + width + '"' + "\n");
//                                } else {
//                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + width + "dp" + '"' + "\n");
//                                }
//                                break;
//                            case heigth:
//                                if (file == null) return;
//                                String heigth = xmlPullParser.nextText().replace("px", "");
//                                if (heigth.equals("0")||heigth.equals("")) {
//                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + "wrap_content" + '"' + "\n");
//                                    return;
//                                }
//                                if (heigth.contains("match_parent") || heigth.contains("fill_parent") || heigth.contains("wrap_content")) {
//                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + heigth + '"' + "\n");
//                                } else {
//                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + heigth + "dp" + '"' + "\n");
//                                }
//                                break;
//                            case orientation:
//                                if (file == null) return;
//                                String orientation = xmlPullParser.nextText();
//                                if (orientation.equals("1")) {
//                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.ORIENTATION + '"' + "vertical" + '"' + "\n");
//                                } else {
//                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.ORIENTATION + '"' + "horizontal" + '"' + "\n");
//                                }
//                                break;
//                            case background:
//                                if (file == null) return;
//                                String background = xmlPullParser.nextText();
//                                if (background.equals(""))return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.BACKGROUND + '"' + "#" + background + '"' + "\n");
//                                break;
//                            case image:
//                                if (file == null) return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "<ImageView\n");
//                                break;
//                            case value:
////                                if (file==null)return;
////                                value=xmlPullParser.nextText();
////                                appendMethodA( SD_CARD_ROOT_PATH+file.getName(),"android:text="+'"'+value+'"'+"\n");
//
//                                break;
//                            case shape:
//                                break;
//                            case margin_left:
//                                if (file == null) return;
//                                String margin_left = xmlPullParser.nextText().replace("px", "");
//                                if (margin_left.equals(""))return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_LEFT + '"' + margin_left + "dp" + '"' + "\n");
//                                break;
//                            case margin_right:
//                                if (file == null) return;
//                                String margin_right = xmlPullParser.nextText().replace("px", "");
//                                if (margin_right.equals(""))return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_RIGHT + '"' + margin_right + "dp" + '"' + "\n");
//                                break;
//                            case margin_top:
//                                if (file == null) return;
//                                String margin_top = xmlPullParser.nextText().replace("px", "");
//                                if (margin_top.equals(""))return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_TOP + '"' + margin_top + "dp" + '"' + "\n");
//                                break;
//                            case margin_bottom:
//                                if (file == null) return;
//                                String margin_bottom = xmlPullParser.nextText().replace("px", "");
//                                if (margin_bottom.equals(""))return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.MARGIN_BOTTOM + '"' + margin_bottom + "dp" + '"' + "\n");
//                                break;
//                            case align:
//                                String align = xmlPullParser.nextText();
//                                if (align.equals(""))return;
//                                switch (align) {
//                                    case Constant.LAYOUT_GRAVITY_LEFT:
//                                        appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_GRAVITY + '"' + "left" + '"' + "\n");
//                                        break;
//                                    case Constant.LAYOUT_GRAVITY_CENTER:
//                                        appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_GRAVITY + '"' + "center" + '"' + "\n");
//                                        break;
//                                    case Constant.LAYOUT_GRAVITY_RIGHT:
//                                        appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_GRAVITY + '"' + "right" + '"' + "\n");
//                                        break;
//                                }
//                                break;
//                            case text:
//                                if (file == null) return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "<TextView\n");
//
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.TEXT + '"' + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa333" + '"' + "\n");
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_HEIGHT + '"' + "wrap_content" + '"' + "\n");
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.LAYOUT_WIDTH + '"' + "wrap_content" + '"' + "\n");
//                                break;
//                            case color:
//                                if (file == null) return;
//                                String color = xmlPullParser.nextText();
//                                if (color.equals(""))return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.TEXT_COLOR + '"' + "#" + color + '"' + "\n");
//                                break;
//                            case size:
//                                if (file == null) return;
//                                String size = xmlPullParser.nextText().replace("px", "");
//                                if (size.equals(""))return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.TEXT_SIZE + '"' + size + "sp" + '"' + "\n");
//                                break;
//                            case singleLine:
//                                String singleLine = xmlPullParser.nextText();
//                                if (singleLine.equals(""))return;
//                                if (singleLine.equals("1")) {
//                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.ELLIPSIZE + '"' + "true" + '"' + "\n");
//                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.SINGLE_LINE + '"' + "true" + '"' + "\n");
//                                } else {
//                                    appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), Constant.SINGLE_LINE + '"' + "false" + '"' + "\n");
//                                }
//                                break;
//                            case line:
//                                if (file == null) return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "<View\n");
//                                break;
//                            case length:
//                                break;
//                            case button:
//                                if (file == null) return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "<Button\n");
//                                break;
//                            case end:
//                                if (file == null) return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), ">\n");
//                                break;
//                        }
//                    }
//                    break;
//                    case XmlPullParser.END_TAG: {
//                        switch (paramValue) {
//                            case layout:
//                                if (file == null) return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "</LinearLayout>\n");
//                                break;
//                            case text:
//                                if (file == null) return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "/>\n");
//                                break;
//                            case image:
//                                if (file == null) return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "/>\n");
//                                break;
//                            case button:
//                                if (file == null) return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "/>\n");
//                                break;
//                            case line:
//                                if (file == null) return;
//                                appendMethodA(Constant.SD_CARD_ROOT_PATH + file.getName(), "/>\n");
//                                break;
//                        }
//                    }
//                    break;
//                    default:
//                        break;
//                }
//                //下一个
//                eventType = xmlPullParser.next();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public static void parseXMLWithDom(InputStream xmlData) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder builder=builderFactory.newDocumentBuilder();
        Document document=builder.parse(xmlData);
    }


    /**
     * 追加文件：使用RandomAccessFile
     */
    public static void appendMethodA(String fileName, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
