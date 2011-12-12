package com.commsen.guicelet.test;


import javax.servlet.http.HttpServletResponse;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.commsen.guicelet.test.guicelets.SimpleGuicelet;
import com.commsen.guicelet.test.util.JettyServer;
import com.commsen.guicelet.test.util.TestContext;

public class DispatcherTest extends BaseGuiceletTest {


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestContext.addRoot("/g/");
		TestContext.addGuicelet(SimpleGuicelet.class);
		JettyServer.start();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		JettyServer.stop();
	}

	@Test
	public void test() throws Exception {

		HttpClient httpclient = new DefaultHttpClient();

		// test all methods
		String url = JettyServer.getHost() + "/g/echo";
		testRequest(httpclient, new HttpDelete(url), HttpServletResponse.SC_OK);
		testRequest(httpclient, new HttpGet(url), HttpServletResponse.SC_OK, SimpleGuicelet.ECHO);
		testRequest(httpclient, new HttpHead(url), HttpServletResponse.SC_OK);
		testRequest(httpclient, new HttpOptions(url), HttpServletResponse.SC_OK);
		testRequest(httpclient, new HttpPost(url), HttpServletResponse.SC_OK, SimpleGuicelet.ECHO);
		testRequest(httpclient, new HttpPut(url), HttpServletResponse.SC_OK);
		testRequest(httpclient, new HttpTrace(url), HttpServletResponse.SC_OK);

		// test trailing slash
		testRequest(httpclient, new HttpGet("/g/test"), HttpServletResponse.SC_OK, SimpleGuicelet.TEST);
		testRequest(httpclient, new HttpGet("/g/test/"), HttpServletResponse.SC_OK, SimpleGuicelet.TEST);

		// test second level mapping
		testRequest(httpclient, new HttpGet("/g/test/test2"), HttpServletResponse.SC_OK, SimpleGuicelet.TEST2);
		
		// test not found 
		testRequest(httpclient, new HttpDelete("/g/test"), HttpServletResponse.SC_NOT_FOUND);
		testRequest(httpclient, new HttpHead("/g/test"), HttpServletResponse.SC_NOT_FOUND);
		testRequest(httpclient, new HttpOptions("/g/test"), HttpServletResponse.SC_NOT_FOUND);
		testRequest(httpclient, new HttpPost("/g/test"), HttpServletResponse.SC_NOT_FOUND);
		testRequest(httpclient, new HttpPut("/g/test"), HttpServletResponse.SC_NOT_FOUND);
		testRequest(httpclient, new HttpTrace("/g/test"), HttpServletResponse.SC_NOT_FOUND);
	}

}
