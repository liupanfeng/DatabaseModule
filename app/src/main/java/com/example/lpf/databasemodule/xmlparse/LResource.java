package com.example.lpf.databasemodule.xmlparse;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by lpf on 2017/5/4.
 */

public class LResource {


    private SoftReference<HashMap<String, ParamValue>> wkViewMap;
    public HashMap<String, ParamValue> getViewMap() {
        if (wkViewMap == null || wkViewMap.get() == null) {
            HashMap<String, ParamValue> map = new HashMap<String, ParamValue>();
            map.put("templateid", ParamValue.templateid);
            map.put("layout", ParamValue.layout);
            map.put("width", ParamValue.width);
            map.put("heigth", ParamValue.heigth);
            map.put("orientation", ParamValue.orientation);
            map.put("background", ParamValue.background);
            map.put("image", ParamValue.image);
            map.put("value", ParamValue.value);
            map.put("background", ParamValue.background);
            map.put("shape", ParamValue.shape);
            map.put("margin_left", ParamValue.margin_left);
            map.put("margin_right", ParamValue.margin_right);
            map.put("margin_top", ParamValue.margin_top);
            map.put("margin_bottom", ParamValue.margin_bottom);
            map.put("align", ParamValue.align);
            map.put("text", ParamValue.text);
            map.put("color", ParamValue.color);
            map.put("size", ParamValue.size);
            map.put("singleLine", ParamValue.singleLine);
            map.put("line", ParamValue.line);
            map.put("length", ParamValue.length);
            map.put("button", ParamValue.button);
            map.put("end", ParamValue.end);
            wkViewMap = new SoftReference<HashMap<String, ParamValue>>(map);
        }
        return wkViewMap.get();
    }

}
