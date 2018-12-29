package com.scar.proxy;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SSRCfgLoaderApplicationTests {
	@Autowired
	private SSRCfgLoader sSRCfgLoader;
	@Test
	public void contextLoads() throws Exception {
		List<String> ssList = new ArrayList<>();
		List<Config> configs = new ArrayList<>();
		merge(ssList, configs, sSRCfgLoader.loadFreeSsSSL(), sSRCfgLoader.loadIShadowXSSL(), sSRCfgLoader.loadSS8SSL(), sSRCfgLoader.loadSsFreeSSL());

		configs.forEach(x -> {
			System.out.println(JSONObject.toJSONString(x));
		});
	}


	@Test
	public void addFile() throws Exception {
		sSRCfgLoader.decodeBase64SsrString("MTg0LjE3Mi4yMzMuMTU4OjMwMDI0OmF1dGhfY2hhaW5fYTpub25lOnBsYWluOk1USXpabkpsWldOc2IzVmtMblJyLz9yZW1hcmtzPTU3Nk81WnU5NUx5UjVwYXY2YUdfJmdyb3VwPVUxTlNVMGhCVWtVdVEwOU4mdWRwcG9ydD0yJnVvdD0x");
	}

	public void merge(List<String> ssList, List<Config> configs, Map<String, Object> ...maps) {
		for(Map<String, Object> map: maps) {
			if(map.containsKey("ss")) {
				ssList.addAll((Collection<? extends String>) map.get("ss"));
			}
			if(map.containsKey("config")) {
				configs.addAll((Collection<? extends Config>) map.get("config"));
			}
		}
		for(String s: ssList) {
			System.out.println(s);
//			if(s.startsWith("ss://")) {
//				System.out.println(Base64Utils.decodeFromString(StringUtils.substringAfter(s, "ss://")));
//				String[] ss = new String(Base64Utils.decodeFromString(StringUtils.substringAfter(s, "ss://"))).split(":");
//				Config config = new Config(ss[1].split("@")[1].trim(), Long.parseLong(ss[2].trim()), ss[1].split("@")[0].trim(), ss[0].trim());
//			}
		}
	}
}
