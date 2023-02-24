package com.ezio.plugin.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class HttpUtils {

    public static String getTauntHttpGet(){
        String content = HttpUtil.get("https://api.nextrt.com/V1/Dutang");
        JSONObject json = JSONUtil.toBean(content, JSONObject.class);
        String con = json.getByPath("data.content", String.class);
        return con.replace("[","").replace("]","");
    }

    public static void main(String[] args) {
        System.out.println(getTauntHttpGet());
    }
}
