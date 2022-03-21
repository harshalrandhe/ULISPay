package com.ulisfintech.artha.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

public class JSONConvector {

    public static String toJSON(Object object){
        Class c = object.getClass();
        JSONObject jsonObject = new JSONObject();
        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            String value = null;
            try {
                value = String.valueOf(field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            try {
                jsonObject.put(name, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println(jsonObject.toString());
        return jsonObject.toString();
    }

    public static String toJSON(List list) {
        JSONArray jsonArray = new JSONArray();
        for (Object i : list) {
            String jstr = toJSON(i);
            try {
                JSONObject jsonObject = new JSONObject(jstr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonArray);
        }
        return jsonArray.toString();
    }
}