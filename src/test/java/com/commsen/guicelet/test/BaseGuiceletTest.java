package com.commsen.guicelet.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import junit.framework.Assert;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class BaseGuiceletTest {

	public BaseGuiceletTest() {
		super();
	}

	protected void testRequest(HttpRequestBase httpRequest, int expectedCode) throws ClientProtocolException, IOException {
		testRequest(httpRequest, expectedCode, null);
	}

	protected void testRequest(HttpRequestBase httpRequest, int expectedCode, String expectedResponse) throws IOException, ClientProtocolException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(httpRequest);
		Assert.assertEquals(expectedCode, response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();
		if (expectedResponse != null) {
			Assert.assertNotNull(entity);
			Assert.assertEquals(expectedResponse, getResponse(entity));
		}
		
	}

	private String getResponse(HttpEntity entity) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
		String l;
		int i = 0;
		while ((l = reader.readLine()) != null) {
			if (i++ > 0) sb.append("\n");
			sb.append(l);
		}
		return sb.toString();
	}

}