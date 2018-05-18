package com.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSONObject;

/**
 * Http类
 * @author Seed
 * 2018年5月17日 下午3:57:17
 */
public class HttpUtils {
	
	private static HttpClient httpClient = new DefaultHttpClient();
	
	public static String sendPost(String url,Map<String,Object> params){
		JSONObject root = new JSONObject();
		Iterator<String> iterator=params.keySet().iterator();
		while(iterator.hasNext()){
			String k=iterator.next();
			root.put(k, params.get(k));
		}
		return sendPostAndReturnToken(url, root, httpClient);
	}
	
	public static String sendPostAndReturnToken(String url, JSONObject obj,
			HttpClient httpClient) {
		String token = "";
		try {
			HttpPost httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(obj.toString(), "UTF-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);

			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-Type", "application/json");

			HttpResponse response = httpClient.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			System.out.println(code);
			if (code == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				InputStream inputStream = httpEntity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, "UTF-8"));

				String output;
				while ((output = reader.readLine()) != null) {
					System.out.println(output);
				}
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return token;
	}
	
	

}
