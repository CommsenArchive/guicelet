package com.commsen.guicelet.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.commsen.guicelet.test.guicelets.SimpleGuicelet;
import com.commsen.guicelet.test.util.JettyServer;
import com.commsen.guicelet.test.util.TestContext;

public class DispatcherTest {

	private static final String ECHO = "ECHO\n";

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

		String url = JettyServer.getHost() + "/g/echo";
//		testRequest(httpclient, new HttpDelete(url));
		testRequest(httpclient, new HttpGet(url));
//		testRequest(httpclient, new HttpHead(url));
//		testRequest(httpclient, new HttpOptions(url));
		testRequest(httpclient, new HttpPost(url));
//		testRequest(httpclient, new HttpPut(url));
//		testRequest(httpclient, new HttpTrace(url));
		
	}


	private void testRequest(HttpClient httpclient, HttpRequestBase httpRequest)
			throws IOException, ClientProtocolException {
		HttpResponse response = httpclient.execute(httpRequest);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
		Assert.assertNotNull(response.getEntity());
		Assert.assertEquals(ECHO, getResponse(response.getEntity()));
	}


	private String getResponse(HttpEntity entity) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
		String l;
		while ((l = reader.readLine()) != null) {
			sb.append(l).append("\n");
		}
		return sb.toString();
	}


}
