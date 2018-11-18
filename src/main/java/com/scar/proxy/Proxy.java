package com.scar.proxy;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @author: scar
 * @create 2018-11-18 23:39
 */
@Component
public class Proxy {
    private static final String URL_1 = "https://ssfree.club/";

    public String loadSsFree() throws IOException {
        Connection.Response response = Jsoup.connect(URL_1).method(Connection.Method.GET).execute();
        List<Element> elements = response.parse().getElementsByTag("code");
        List<Config> configs = new ArrayList<>();
        elements.forEach(x -> {
            if(x.text().startsWith("ip")) {
                String ip = StringUtils.substringBetween(x.text(), "ip地址", " ");
                String port = StringUtils.substringBetween(x.text(), "端口号", " ");
                String password = StringUtils.substringBetween(x.text(), "密码", " ");
                String method = StringUtils.substringAfter(x.text(), "加密");
                Config cfg = new Config(ip, Long.parseLong(port), password, method);
                configs.add(cfg);
            }
        });

        return JSONObject.toJSONString(configs);
    }
}
