package com.example.hobbyt.utils;

public class UrlMap {
    private final static int STEP_SIZE = 2;

    private String baseUrl;
    private UrlParam[] params = new UrlParam[STEP_SIZE];
    private int sizeArray = 0;

    private class UrlParam{
        final String key;
        String value;

        public UrlParam(String key, String value){
           this.key = key;
           this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public UrlMap(String url){
        baseUrl = url;
    }

    public int size() {
        return sizeArray;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public String get(String key){
        for (UrlParam param: params) {
            if(key.equals(param.key))
                return param.value;
        }

        return null;
    }

    public void put(String key, String value){
        UrlParam param = new UrlParam(key, value);
        if(params.length == sizeArray)
            addSpace();

        params[sizeArray] = param;
        sizeArray++;
    }

    private void addSpace(){
        params = cloneArray(params, params.length + STEP_SIZE);
    }

    private UrlParam[] cloneArray(UrlParam[] params, int newLength){
        if(newLength < sizeArray)
            newLength = sizeArray;

        UrlParam[] res = new UrlParam[newLength];
        for (int i = 0; i < params.length; i++) {
            res[i] = params[i];
        }

        return res;
    }

    public String generateUrl(){
        String res = baseUrl + "?";
        for (int i = 0; i < sizeArray; i++) {
            res += params[i].key + "=" + params[i].value;
            if(i != sizeArray - 1)
                res += "&";
        }

        return res;
    }
}

