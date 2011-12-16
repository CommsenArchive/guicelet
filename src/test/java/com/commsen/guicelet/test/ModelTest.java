package com.commsen.guicelet.test;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.commsen.guicelet.test.guicelets.GuiceletWithModel;
import com.commsen.guicelet.test.util.JettyServer;
import com.commsen.guicelet.test.util.TestContext;

public class ModelTest extends BaseGuiceletTest {


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestContext.addRoot("/g/");
		TestContext.addGuicelet(GuiceletWithModel.class);
		JettyServer.start();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		JettyServer.stop();
	}

	@Test
	public void test() throws Exception {

		
		String url;
		for (int i = 0; i < 10; i++) {
			url = JettyServer.getHost() + "/g/model?long=" + i + "&string=test" + i + "&number=" + i;
			testRequest(new HttpGet(url), HttpServletResponse.SC_OK, i + ",test" + i + "," + i);
		}

		url = JettyServer.getHost() + "/g/model?long=1a23&string=test";
		testRequest(new HttpGet(url), HttpServletResponse.SC_OK, "{l=[ERROR_CONVERTING_VALUE]}");
		url = JettyServer.getHost() + "/g/model?long=123";
		testRequest(new HttpGet(url), HttpServletResponse.SC_OK, "{s=[MISSING_REQUIRED_FIELD]}");
	}



}
