package com.scar.proxy;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: scar
 * @create 2018-11-18 23:39
 */
@Component
public class SSRCfgLoader {
    private static final String URL_SS_FREE = "https://ssfree.club/";
    private static final String URL_FREE_SS = "https://ss.freess.org/";
    private static final String URL_FREE_SS_SITE = "https://free-ss.site/ss.json/";
    private static final String URL_ISHADOWX = "https://my.ishadowx.net/";
    private static final String URL_SS8 = "https://get.ss8.fun/";
    private static final String URL_DOUB = "https://doub.io/sszhfx/";

    private Proxy proxy = new Proxy(java.net.Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 1080));

    public String loadSsFree() throws IOException {
        Connection.Response response = Jsoup.connect(URL_SS_FREE).method(Connection.Method.GET).execute();
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

    public String loadFreeSsSSL() throws Exception {
        Connection.Response response = Jsoup.connect(URL_FREE_SS).method(Connection.Method.GET).proxy(proxy).execute();
        List<Element> elements = response.parse().getElementsByAttributeValueMatching("href", "^data:image");
        elements.forEach(x -> {
            try {
                String target = StringUtils.substringAfter(x.attr("href").toString(), "data:image/png;base64,");
                byte[] data = new BASE64Decoder().decodeBuffer(target);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
                LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                Binarizer binarizer = new HybridBinarizer(source);
                BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
                Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
                hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
                Result result = new MultiFormatReader().decode(binaryBitmap, hints);
                System.out.println(result.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        });
        return "";
    }

    public String loadFreeSsSiteSSL() throws IOException {
        Connection.Response response = Jsoup.connect(URL_FREE_SS_SITE).method(Connection.Method.GET).proxy(proxy).execute();
        System.out.println(response.parse());
        return "";
    }

    public String loadIShadowXSSL() throws IOException {
        Connection.Response response = Jsoup.connect(URL_ISHADOWX).method(Connection.Method.GET).proxy(proxy).execute();
        List<Element> elements = response.parse().getElementsByClass("hover-text");
        elements.forEach(x -> {
            x.getElementsByTag("h4").forEach(n -> {
                if(n.text().contains("IP")) {
                    System.out.println(n.getElementsByTag("span").get(0).text());
                } else if(n.text().contains("Port")) {
                    System.out.println(n.getElementsByTag("span").get(0).text());
                } else if(n.text().contains("Password")) {
                    System.out.println(n.getElementsByTag("span").get(0).text());
                } else if(n.text().contains("Method")) {
                    System.out.println(StringUtils.substringAfter(n.text(), ":"));
                }
            });
            List<Element> nodes = x.getElementsByAttributeValueMatching("data-clipboard-text", "^vmess\\S+");
            if(nodes != null) {
                nodes.forEach(y -> {
                    System.out.println(y.attr("data-clipboard-text"));
                });
            }
        });
        return "";
    }

    public String loadSS8SSL() throws IOException {
        Connection.Response response = Jsoup.connect(URL_SS8).method(Connection.Method.GET).proxy(proxy).execute();
        System.out.println(response.body());
        return "";
    }
}
