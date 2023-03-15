package com.ulisfintech.telrpay.helper;

import com.google.gson.annotations.Expose;

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
    public static String toJSONExcludeWithoutExposeFields(Object object){
        Class aClass = object.getClass();
        JSONObject jsonObject = new JSONObject();
        for (Field field : aClass.getDeclaredFields()) {
            if(!field.isAnnotationPresent(Expose.class)){
                continue;
            }
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
        for (Object obj : list) {
            String jsonString = toJSON(obj);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }
}