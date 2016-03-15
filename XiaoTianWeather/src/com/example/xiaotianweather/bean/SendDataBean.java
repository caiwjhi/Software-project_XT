
package com.example.xiaotianweather.bean;

public class SendDataBean {

    public static String city = "";
    public static String json = "json";
    public static String ak = "jDQ5wAofyqCLMAD1dOUqj48K";

    public static void setCity(String city) {
        SendDataBean.city = city;
    }

    public static void setJson(String json) {
        SendDataBean.json = json;
    }

    public static void setAk(String ak) {
        SendDataBean.ak = ak;
    }

    public static String getCity() {
        return city;
    }

    public static String getJson() {
        return json;
    }

    public static String getAk() {
        return ak;
    }

    public static String getData() {
        // return "http://101.5.130.191:8080/TEST/sky/" +
        // city; //注释掉的是新写的服务器网址，以后再改回来
        return "http://api.map.baidu.com/telematics/v3/weather?location="
                + city + "&output=" + json + "&ak=" + ak;
    }
    // http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=iGs8rFvzh1e8c7C9DjXT5toK

}
