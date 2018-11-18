package com.scar.proxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProxyApplicationTests {
	@Autowired
	private Proxy proxy;
	@Test
	public void contextLoads() {
		try {
			System.out.println(proxy.loadSsFree());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
