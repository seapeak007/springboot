package com.lexue.http;

/**
 * 文件名：HTTPUtils.java 版权：Copyright by www.lexue.com 描述：Http请求相关工具类
 * 
 * @author yangfei
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.lexue.exception.APIException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HTTPUtils
{
	public static Map<String, String> parserHeader(HttpServletRequest request)
	{
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements())
		{
			String key = String.valueOf(headerNames.nextElement());
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}

	public static String sendGet(String url)
	    throws Exception
	{
		if (null == url || "".equals(url))
		{
			return null;
		}
		if (!url.startsWith("http"))
		{
			url = "http://" + url;
		}
		String result = "";
		BufferedReader in = null;
		try
		{
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
			    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				result += line;
			}
			return result;
		}
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
	}
	
	public static String executeGet(String url) throws Exception {  
        BufferedReader in = null;  
  
        String content = null;  
        try {  
            // 定义HttpClient  
        	CloseableHttpClient client = HttpClients.createDefault();
            // 实例化HTTP方法  
            HttpGet request = new HttpGet();  
            request.setURI(new URI(url));  
            HttpResponse response = client.execute(request);  
  
            in = new BufferedReader(new InputStreamReader(response.getEntity()  
                    .getContent()));  
            StringBuffer sb = new StringBuffer("");  
            String line = "";  
            while ((line = in.readLine()) != null) {  
                sb.append(line);  
            }  
            in.close();  
            content = sb.toString();  
            return content;  
        } finally {
            if (in != null) {
                try {  
                    in.close();// 最后要关闭BufferedReader  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }

	/**
	 * @param file
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws APIException
	 */
	public static String postFile(File file, String url)
	    throws IOException, APIException
	{
		FileEntity entity = new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM);
		return httpPostHandle(url, entity);
	}
	
	/**
	 * @param httpurl
	 * @param fileInputStream
	 * @param fileLength
	 * @return
	 * @throws IOException
	 * @throws APIException
	 */
	public static String postFile(final String url, final InputStream fileInputStream, long fileLength)
		throws IOException, APIException
	{
		
		InputStreamEntity entity = new InputStreamEntity(fileInputStream, fileLength, ContentType.APPLICATION_OCTET_STREAM);
		return httpPostHandle(url, entity);
	}

	private static String httpPostHandle(final String url, HttpEntity entity)
	    throws IOException, ClientProtocolException, APIException
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		CloseableHttpResponse response = httpClient.execute(httpPost);
		
		try
		{
			if (response.getStatusLine().getStatusCode() == 200)
			{
				return EntityUtils.toString(response.getEntity());
			}
			else
			{
				throw new APIException(response.getStatusLine().getStatusCode(),
					"upload file to tfs failed");
			}
		}
		finally
		{
			response.close();
			EntityUtils.consume(entity);
			httpClient.close();
		}
	}

	/**
	 * 获取所有request请求参数key-value
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestParams(HttpServletRequest request)
	{
		Map<String, String> params = new HashMap<String, String>();
		if (null != request)
		{
			Set<String> paramsKey = request.getParameterMap().keySet();
			for (String key : paramsKey)
			{
				String parameter = request.getParameter(key);
				if (StringUtils.isNotEmpty(parameter))
				{
					params.put(key, parameter);
				}
			}
		}
		return params;
	}
	
	public static String post(String reqURL,
			Map<String, String> headers, Map<String, String> param) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httppost = new HttpPost(reqURL.toString());
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        if (headers != null) {  
            Set<String> keys = headers.keySet();  
            for (Iterator<String> i = keys.iterator(); i.hasNext();) {  
                String key = (String) i.next();  
                httppost.addHeader(key, headers.get(key));  
            }  
        } 
		httppost.setEntity(new UrlEncodedFormEntity(getPostParam(param),"UTF-8"));

		try {
			// System.out.println(httppost.getEntity());
			CloseableHttpResponse response = httpclient.execute(httppost);
			// System.out.println(response.toString());
			HttpEntity entity = response.getEntity();
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			StringBuffer sb = new StringBuffer(128);
			String buffer2;
			while ((buffer2 = reader2.readLine()) != null) {
				sb.append(buffer2);
			}
			return sb.toString();
		} finally {
			httppost.releaseConnection();
			httpclient.close();
		}
	}
	
	private static List<NameValuePair> getPostParam(Map<String, String> param) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry : param.entrySet()) {
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return params;
	}
	
	public static void main(String[] args){
		String url = "www.baidu.com";
		try {
			String re = sendGet(url);
			System.out.println(re);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
