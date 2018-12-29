package com.scar.proxy;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Map<String, Object> loadSsFreeSSL() throws IOException {
        Map<String, Object> map = new HashMap<>();
        Connection.Response response = Jsoup.connect(URL_SS_FREE).proxy(proxy).method(Connection.Method.GET).execute();
        Document document = response.parse();
        System.out.println(document);
        List<Element> elements = document.getElementsByTag("code");
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
        map.put("config", configs);
        return map;
    }

    public Map<String, Object> loadFreeSsSSL() throws Exception {
        Map<String, Object> map = new HashMap<>();
        Connection.Response response = Jsoup.connect(URL_FREE_SS).method(Connection.Method.GET).proxy(proxy).execute();
        List<Element> elements = response.parse().getElementsByAttributeValueMatching("href", "^data:image");
        List<String> ssList = new ArrayList<>();
        elements.forEach(x -> {
            try {
                String target = StringUtils.substringAfter(x.attr("href").toString(), "data:image/png;base64,");
                byte[] data = new BASE64Decoder().decodeBuffer(target);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                String result = img2QRCodeString(byteArrayInputStream);
                ssList.add(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        map.put("ss", ssList);
        return map;
    }

    public String loadFreeSsSiteSSL() throws IOException {
        Connection.Response response = Jsoup.connect(URL_FREE_SS_SITE).method(Connection.Method.GET).proxy(proxy).execute();
        System.out.println(response.parse());
        return "";
    }

    public Map<String, Object> loadIShadowXSSL() throws IOException {
        Map<String, Object> map = new HashMap<>();
        Connection.Response response = Jsoup.connect(URL_ISHADOWX).method(Connection.Method.GET).proxy(proxy).execute();
        List<Element> elements = response.parse().getElementsByClass("hover-text");
        List<Config> configs = new ArrayList<>();
        List<String> ssList = new ArrayList<>();
        elements.forEach(x -> {
            String ip = "";
            String port = "";
            String password = "";
            String method = "";
            for(Element n: x.getElementsByTag("h4")) {
                // System.out.println(n.toString());
                if(n.text().contains("IP")) {
                    ip = n.getElementsByTag("span").get(0).text();
                } else if(n.text().contains("Port")) {
                    port = n.getElementsByTag("span").get(0).text();
                } else if(n.text().contains("Password")) {
                    password = n.getElementsByTag("span").get(0).text();
                } else if(n.text().contains("Method")) {
                    method = StringUtils.substringAfter(n.text(), ":");
                }
            }
            if(!"".equals(ip)) {
                configs.add(new Config(ip, Long.parseLong(port), password, method));
            }
            List<Element> nodes = x.getElementsByAttributeValueMatching("data-clipboard-text", "^vmess\\S+");
            if(nodes != null) {
                nodes.forEach(y -> {
                    ssList.add(y.attr("data-clipboard-text"));
                });
            }
        });
        map.put("config", configs);
        map.put("ss", ssList);
        return map;
    }

    public Map<String, Object> loadSS8SSL() throws IOException {
        Map<String, Object> map = new HashMap<>();
        Connection.Response response = Jsoup.connect(URL_SS8).method(Connection.Method.GET).proxy(proxy).execute();
        List<String> ssList = new ArrayList<>();
        response.parse().getElementsByClass("image").forEach(x -> {
            try {
                String url = x.attr("href");
                Connection.Response r = Jsoup.connect(URL_SS8 + url).ignoreContentType(true).method(Connection.Method.GET).proxy(proxy).execute();
                String result = img2QRCodeString(r.bodyStream());
                ssList.add(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        map.put("ss", ssList);
        return map;
    }

    public String img2QRCodeString(InputStream inputStream) throws Exception {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        Result result = new MultiFormatReader().decode(binaryBitmap, hints);
        return result.toString();
    }
    public String img2Base64String(String url) throws IOException {
        Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET).proxy(proxy).execute();
        InputStream inputStream = response.bodyStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(data)) != -1) {
            outputStream.write(data, 0, len);
        }
        inputStream.close();
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(outputStream.toByteArray());
    }

    public Map<String, String> readRepertory() throws Exception {
        Map<String, String> map = new HashMap<>();
        Scanner scanner = new Scanner(new File("src/main/resources/data.properties"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] data = line.split("=");
            if(data.length == 2) {
                map.put(data[0].trim(), data[1].trim());
            }
        }
        return map;
    }

    public void addRepertory(List<String> data) throws Exception {
        FileWriter writer = new FileWriter("src/main/resources/data.properties", false);
        PrintWriter printWriter = new PrintWriter(writer);
        data.forEach(x -> {
            printWriter.println(x);
        });
        writer.flush();
        printWriter.close();
        writer.close();
    }

    public List<Config> loadStringSsrSSL(String url) throws Exception {
        Connection.Response response = Jsoup.connect(url).proxy(proxy).method(Connection.Method.GET).execute();
        String html = response.parse().toString();
        Pattern pattern = Pattern.compile("ss(r)?://[a-zA-Z0-9+/=]+");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
        return null;
    }

    public void decodeBase64SsrString(String url) {
        String protocol = url.contains("ssr://") ? "ssr": url.contains("ss://") ? "ss": "";
        assert !"".equals(protocol);
        if("ss".equals(protocol)) {
            return;
        }
        url = url.replaceAll("ss(r)?://", "");
        String result = new String(Base64Utils.decodeFromUrlSafeString(url));
        JSONObject object = new JSONObject();
        String paramStr = StringUtils.substringAfter(result, "/?");
        if(!"".equals(paramStr)) {
            String[] params = paramStr.split("&");
            for(String s: params) {
                String key = s.split("=")[0];
                String value = "group".equals(key) || "remarks".equals(key) ? new String(Base64Utils.decodeFromUrlSafeString(s.split("=")[1])): s.split("=")[1];
                object.put(key, value);
            }
        }
        String cfgString = StringUtils.substringBefore(result, "/?");
        String[] values = cfgString.split(":");
        //host, port, protocol, method, obfs
        object.put("host", values[0]);
        object.put("port", Long.parseLong(values[1]));
        object.put("protocol", values[2]);
        object.put("method", values[3]);
        object.put("obfs", values[4]);
        object.put("password", new String(Base64Utils.decodeFromUrlSafeString(values[5])));
        System.out.println(object.toJSONString());
    }
}
