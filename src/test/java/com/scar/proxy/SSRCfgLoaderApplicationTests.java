package com.scar.proxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SSRCfgLoaderApplicationTests {
	@Autowired
	private SSRCfgLoader sSRCfgLoader;
	@Test
	public void contextLoads() {
		try {
			System.out.println(sSRCfgLoader.loadSS8SSL());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
