package com.android.jokeapp.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpTools {
	public String getResultForHttpGet(String name, String pwd)
			throws ClientProtocolException, IOException {
		// 服务器 ：服务器项目 ：servlet名称
		String path = "http://192.168.1.150:8080/test/test";
		String uri = path + "?name=" + name + "&pwd=" + pwd;
		// name:服务器端的用户名，pwd:服务器端的密码
		// 注意字符串连接时不能带空格
		String result = "";

		HttpGet httpGet = new HttpGet(uri);
		// 编者按：与HttpPost区别所在，这里是将参数在地址中传递
		HttpResponse response = new DefaultHttpClient().execute(httpGet);
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, HTTP.UTF_8);
		}
		return result;
	}

	public static String getReultForHttpPost(String url, String name, String pwd)
			throws ClientProtocolException, IOException {
		// 服务器 ：服务器项目 ：servlet名称
		String path = "http://192.168.1.150:8080/test/test";
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("username", name));
		list.add(new BasicNameValuePair("password", pwd));
		httpPost.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
		// 编者按：与HttpGet区别所在，这里是将参数用List传递

		String result = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, HTTP.UTF_8);
			CookieStore cookiestore = httpclient.getCookieStore();
			cookiestore.getCookies();
		}
		return result;
	}

	public static final String bmapBase64Encode = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4&";

	public static HttpGet getHttpGet(String url) {
		HttpGet request = new HttpGet(url);
		return request;
	}

	public static HttpPost getHttpPost(String url) {
		HttpPost request = new HttpPost(url);
		return request;
	}

	public static HttpResponse getHttpResponse(HttpGet request)
			throws ClientProtocolException, IOException {
		// HttpResponse response = new DefaultHttpClient().execute(request);
		HttpClient defaultHttpClient = new DefaultHttpClient();
		// 请求超时
		defaultHttpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		// 读取超时

		defaultHttpClient.getParams().setParameter(
				CoreConnectionPNames.SO_TIMEOUT, 10000);
		HttpResponse response = defaultHttpClient.execute(request);
		return response;
	}

	@SuppressWarnings("deprecation")
	public static HttpResponse getHttpResponse(HttpPost request)
			throws ClientProtocolException, IOException {
		// HttpClient defaultHttpClient = new DefaultHttpClient();
		// // 请求超时
		// defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
		// 10000);
		// 读取超时

		// defaultHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
		// 10000);
		// HttpResponse response = defaultHttpClient.execute(request);

		BasicHttpParams httpParams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);

		HttpConnectionParams.setSoTimeout(httpParams, 30000);

		HttpClient client = new DefaultHttpClient(httpParams);

		HttpResponse response = client.execute(request);

		return response;
	}

	public static String queryStringForPost(String url) throws Exception {
		HttpPost request = getHttpPost(url);
		return queryStringForPost(request);
		// String result = null;
		// try {
		// HttpResponse response = getHttpResponse(request);
		// if (200 == response.getStatusLine().getStatusCode()) {
		// result = EntityUtils.toString(response.getEntity());
		// return result;
		// }
		// } catch (ClientProtocolException e) {
		// e.printStackTrace();
		// result = "Failed in queryStringForPost！";
		// return result;
		// } catch (IOException e) {
		// e.printStackTrace();
		// result = "Failed in queryStringForPost！";
		// return result;
		// }
		// return null;
	}

	public static String queryStringForPost(HttpPost request) throws Exception {
		String result = null;
		// try {
		HttpResponse response = getHttpResponse(request);
		if (200 == response.getStatusLine().getStatusCode()) {
			result = EntityUtils.toString(response.getEntity());
			return result;
		}
		// } catch (ClientProtocolException e) {
		// e.printStackTrace();
		// result = "Failed in queryStringForPost！";
		// return result;
		// } catch (IOException e) {
		// e.printStackTrace();
		// result = "Failed in queryStringForPost！";
		// return result;
		// }
		return null;
	}

	public static String queryStringForGet(String url) {
		HttpGet request = getHttpGet(url);
		String result = null;
		try {
			HttpResponse response = getHttpResponse(request);
			if (200 == response.getStatusLine().getStatusCode()) {
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = "Failed in queryStringForGet！";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = "Failed in queryStringForGet！";
			return result;
		}
		return null;
	}
}
