package com.example.lpf.databasemodule;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 * Created by lpf on 2017/4/28.
 */

public class MyParseHandler extends DefaultHandler {

    private List<Info> list;

    private String content;

    private Info info = null;

    public MyParseHandler(List<Info> list) {
        this.list = list;
    }


    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        content = new String(ch, start, length);
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if(localName .equals("template")){
            info = new Info();
        }
    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if(localName .equals("id")){
            info.setId(Integer.parseInt(content));
        }else if(localName .equals("catalog")){
            info.setCatalog(content);
        }else if(localName .equals("template")){
            list.add(info);
            info = null;
        }
    }

}
