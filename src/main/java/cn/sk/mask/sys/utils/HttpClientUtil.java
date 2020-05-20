package cn.sk.mask.sys.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * HTTP，请求工具类
 */
@Slf4j
public class HttpClientUtil {
	

	/**
	 * Post请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static JSONObject sendPost(String interfaceName, String url, Map<String, Object> params) {
		long startTime = System.currentTimeMillis();
		String result = "";
		// 初始化请求接收对象
		CloseableHttpResponse response = null;
		
		// 【1】，创建Httpclient对象
		CloseableHttpClient httpclient = HttpClientUtil.createDefault();
		
		// 【2】，Post请求，创建HttpPost对象。
		HttpPost httpPost = new HttpPost(url);
		//httpPost.addHeader("ContentType", "application/json");
		
		// 【3】，拼接参数，里面太深暂且就用着
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			NameValuePair pair = new BasicNameValuePair(key, params.get(key).toString());
			list.add(pair);
		}
		try {
			// 设置请求参数（这里是模仿，web页的提交参数key-value格式）
			// 擦，这里貌似最后还是转化成了 key1=value2&key2=value2&key3=value3 这样的格式
			httpPost.setEntity(new UrlEncodedFormEntity(list,Consts.UTF_8));
			response = httpclient.execute(httpPost);
			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				try {
					// EntityUtils里面解析流的时候，已近关闭了实体流了
					result = EntityUtils.toString(httpEntity);
				} catch (ParseException e) {
					log.error("请求Post,解析resulr数据异常ParseException：", e);
					e.printStackTrace();
				} catch (IOException e) {
					log.error("请求Post,解析resulr数据异常IOException：", e);
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e1) {
			log.error("请求Post异常UnsupportedEncodingException：", e1);
			e1.printStackTrace();
		} catch (ClientProtocolException e2) {
			log.error("请求Post异常ClientProtocolException：", e2);
			e2.printStackTrace();
		} catch (IOException e) {
			log.error("请求Post异常IOException：", e);
			e.printStackTrace();
		}finally{
			try {
				if(null != response){
					response.close();
				}
				if(null != httpclient){
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		long endTime = System.currentTimeMillis();
		String timeConsuming = DateUtils.timeDifferenceTomm(startTime,endTime);
		log.info(interfaceName);
		log.info("请求参数：" + params.toString());
		log.info("返回结果：" + result);
		log.info("耗时： " + timeConsuming);
		log.info("------------------------------------------");
		JSONObject jObj = JSONObject.parseObject(result);
		jObj.put("timeConsuming", timeConsuming);
		return jObj;
	}
	
	/**
	 * Post请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static JSONObject sendPostJson(String interfaceName,String url, String params) {
		long startTime = System.currentTimeMillis();
		// 初始化请求接收对象
		CloseableHttpResponse response = null;
		
		// 【1】，创建Httpclient对象
		CloseableHttpClient httpclient = HttpClientUtil.createDefault();
		
		// 【2】，Post请求，创建HttpPost对象。
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("ContentType", "application/json");
		
		try {
			// 设置请求参数（这里是模仿，web页的提交参数key-value格式）
			// 擦，这里貌似最后还是转化成了 key1=value2&key2=value2&key3=value3 这样的格式
//			httpPost.setEntity(new StringEntity(params, Consts.UTF_8));
			StringEntity requestEntity = new StringEntity(params, Consts.UTF_8);
			requestEntity.setContentType("application/json");
			requestEntity.setContentEncoding(Consts.UTF_8.toString());
			httpPost.setEntity(requestEntity);
			// httpPost.setEntity(new UrlEncodedFormEntity(list,Consts.UTF_8));
			response = httpclient.execute(httpPost);
			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				String result = null;
				try {
					// EntityUtils里面解析流的时候，已近关闭了实体流了
					result = EntityUtils.toString(httpEntity);
				} catch (ParseException e) {
					log.error("请求Post,解析resulr数据异常ParseException：", e);
					e.printStackTrace();
				} catch (IOException e) {
					log.error("请求Post,解析resulr数据异常IOException：", e);
					e.printStackTrace();
				}

				long endTime = System.currentTimeMillis();
				String timeConsuming = DateUtils.timeDifferenceTomm(startTime,endTime);
				log.info(interfaceName);
				log.info("请求参数：" + params.toString());
				log.info("返回结果：" + result);
				log.info("耗时： " + timeConsuming);
				log.info("------------------------------------------");

//				System.out.println("=====请求返回的数据====="+result);
				return JSONObject.parseObject(result);
			}
		} catch (UnsupportedEncodingException e1) {
			log.error("请求Post异常UnsupportedEncodingException：", e1);
			e1.printStackTrace();
		} catch (ClientProtocolException e2) {
			log.error("请求Post异常ClientProtocolException：", e2);
			e2.printStackTrace();
		} catch (IOException e) {
			log.error("请求Post异常IOException：", e);
			e.printStackTrace();
		}finally{
			try {
				if(null != response){
					response.close();
				}
				if(null != httpclient){
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		return null;
	}
	/**
	 * Post请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static JSONObject sendPostJson(String url, String params) {

		// 初始化请求接收对象
		CloseableHttpResponse response = null;

		// 【1】，创建Httpclient对象
		CloseableHttpClient httpclient = HttpClientUtil.createDefault();

		// 【2】，Post请求，创建HttpPost对象。
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("ContentType", "application/json");

		try {
			// 设置请求参数（这里是模仿，web页的提交参数key-value格式）
			// 擦，这里貌似最后还是转化成了 key1=value2&key2=value2&key3=value3 这样的格式
			httpPost.setEntity(new StringEntity(params, Consts.UTF_8));
			// httpPost.setEntity(new UrlEncodedFormEntity(list,Consts.UTF_8));
			response = httpclient.execute(httpPost);
			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				String result = null;
				try {
					// EntityUtils里面解析流的时候，已近关闭了实体流了
					result = EntityUtils.toString(httpEntity);
				} catch (ParseException e) {
					log.error("请求Post,解析resulr数据异常ParseException：", e);
					e.printStackTrace();
				} catch (IOException e) {
					log.error("请求Post,解析resulr数据异常IOException：", e);
					e.printStackTrace();
				}
				System.out.println("=====请求返回的数据====="+result);
				return JSONObject.parseObject(result);
			}
		} catch (UnsupportedEncodingException e1) {
			log.error("请求Post异常UnsupportedEncodingException：", e1);
			e1.printStackTrace();
		} catch (ClientProtocolException e2) {
			log.error("请求Post异常ClientProtocolException：", e2);
			e2.printStackTrace();
		} catch (IOException e) {
			log.error("请求Post异常IOException：", e);
			e.printStackTrace();
		}finally{
			try {
				if(null != response){
					response.close();
				}
				if(null != httpclient){
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * Post请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static JSONObject sendPost(String url, String params) {

		// 初始化请求接收对象
		CloseableHttpResponse response = null;

		// 【1】，创建Httpclient对象
		CloseableHttpClient httpclient = HttpClientUtil.createDefault();

		// 【2】，Post请求，创建HttpPost对象。
		HttpPost httpPost = new HttpPost(url);
		//httpPost.addHeader("ContentType", "application/json");

		try {
			// 设置请求参数（这里是模仿，web页的提交参数key-value格式）
			// 擦，这里貌似最后还是转化成了 key1=value2&key2=value2&key3=value3 这样的格式
			httpPost.setEntity(new StringEntity(params, Consts.UTF_8));
			// httpPost.setEntity(new UrlEncodedFormEntity(list,Consts.UTF_8));
			response = httpclient.execute(httpPost);
			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				String result = null;
				try {
					// EntityUtils里面解析流的时候，已近关闭了实体流了
					result = EntityUtils.toString(httpEntity);
				} catch (ParseException e) {
					log.error("请求Post,解析resulr数据异常ParseException：", e);
					e.printStackTrace();
				} catch (IOException e) {
					log.error("请求Post,解析resulr数据异常IOException：", e);
					e.printStackTrace();
				}
				System.out.println("=====请求返回的数据====="+result);
				return JSONObject.parseObject(result);
			}
		} catch (UnsupportedEncodingException e1) {
			log.error("请求Post异常UnsupportedEncodingException：", e1);
			e1.printStackTrace();
		} catch (ClientProtocolException e2) {
			log.error("请求Post异常ClientProtocolException：", e2);
			e2.printStackTrace();
		} catch (IOException e) {
			log.error("请求Post异常IOException：", e);
			e.printStackTrace();
		}finally{
			try {
				if(null != response){
					response.close();
				}
				if(null != httpclient){
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Post请求(申请流水使用)
	 * @param url
	 * @param params
	 * @return
	 */
	public static String sendPostString(String interfaceName, String url, String params) {
		long startTime = System.currentTimeMillis();
		
		String result = null;
		// 初始化请求接收对象
		CloseableHttpResponse response = null;
		
		// 【1】，创建Httpclient对象
		CloseableHttpClient httpclient = HttpClientUtil.createDefault();
		
		// 【2】，Post请求，创建HttpPost对象。
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("accept", "*/*");
		httpPost.addHeader("connection", "Keep-Alive");
		httpPost.addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
		try {
			// 设置请求参数（这里是模仿，web页的提交参数key-value格式）
			// 擦，这里貌似最后还是转化成了 key1=value2&key2=value2&key3=value3 这样的格式
			httpPost.setEntity(new StringEntity(params, Consts.UTF_8));
			// httpPost.setEntity(new UrlEncodedFormEntity(list,Consts.UTF_8));
			response = httpclient.execute(httpPost);
			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				try {
					// EntityUtils里面解析流的时候，已近关闭了实体流了
					result = EntityUtils.toString(httpEntity);
				} catch (ParseException e) {
					log.error("请求Post,解析resulr数据异常ParseException：", e);
					e.printStackTrace();
				} catch (IOException e) {
					log.error("请求Post,解析resulr数据异常IOException：", e);
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e1) {
			log.error("请求Post异常UnsupportedEncodingException：", e1);
			e1.printStackTrace();
		} catch (ClientProtocolException e2) {
			log.error("请求Post异常ClientProtocolException：", e2);
			e2.printStackTrace();
		} catch (IOException e) {
			log.error("请求Post异常IOException：", e);
			e.printStackTrace();
		}finally{
			try {
				if(null != response){
					response.close();
				}
				if(null != httpclient){
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		
		long endTime = System.currentTimeMillis();
		String timeConsuming = DateUtils.timeDifferenceTomm(startTime,endTime);
		log.info(interfaceName);
		log.info("请求参数：" + params);
		log.info("返回结果：" + result);
		log.info("耗时： " + timeConsuming);
		log.info("------------------------------------------");
		return result;
	}
	
	/**
	 * Post请求(获取支付宝二维码使用)
	 * @param url
	 * @param params
	 * @return
	 */
	public static JSONObject sendPostStringAlipay(String interfaceName, String url, String params) {
		
		long startTime = System.currentTimeMillis();
		String result = "";
		
		// 初始化请求接收对象
		CloseableHttpResponse response = null;
		
		// 【1】，创建Httpclient对象
		CloseableHttpClient httpclient = HttpClientUtil.createDefault();
		
		// 【2】，Post请求，创建HttpPost对象。
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("accept", "*/*");
		httpPost.addHeader("connection", "Keep-Alive");
		httpPost.addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
		try {
			// 设置请求参数（这里是模仿，web页的提交参数key-value格式）
			// 擦，这里貌似最后还是转化成了 key1=value2&key2=value2&key3=value3 这样的格式
			httpPost.setEntity(new StringEntity(params, Consts.UTF_8));
			// httpPost.setEntity(new UrlEncodedFormEntity(list,Consts.UTF_8));
			response = httpclient.execute(httpPost);
			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				try {
					// EntityUtils里面解析流的时候，已近关闭了实体流了
					result = EntityUtils.toString(httpEntity);
				} catch (ParseException e) {
					log.error("请求Post,解析resulr数据异常ParseException：", e);
					e.printStackTrace();
				} catch (IOException e) {
					log.error("请求Post,解析resulr数据异常IOException：", e);
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e1) {
			log.error("请求Post异常UnsupportedEncodingException：", e1);
			e1.printStackTrace();
		} catch (ClientProtocolException e2) {
			log.error("请求Post异常ClientProtocolException：", e2);
			e2.printStackTrace();
		} catch (IOException e) {
			log.error("请求Post异常IOException：", e);
			e.printStackTrace();
		}finally{
			try {
				if(null != response){
					response.close();
				}
				if(null != httpclient){
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		
		long endTime = System.currentTimeMillis();
		String timeConsuming = DateUtils.timeDifferenceTomm(startTime,endTime);
		log.info(interfaceName);
		log.info("请求参数：" + params);
		log.info("返回结果：" + result);
		log.info("耗时： " + timeConsuming);
		log.info("------------------------------------------");
		JSONObject jObj = JSONObject.parseObject(result);
		jObj.put("timeConsuming", timeConsuming);
		return jObj;
	}
	
	/**
	 * Get请求
	 * @param url
	 * @return
	 */
	public static JSONObject sendGet(String url) {
		
		// 初始化请求接收对象
		CloseableHttpResponse response = null;
		
		// 【1】，创建Httpclient对象
		CloseableHttpClient httpclient = HttpClientUtil.createDefault();
		
		// 【2】，Get请求，创建HttpGet对象。
		HttpGet httpget = new HttpGet(url);
		// 这里可以在传个 头部key-value
//		httpget.addHeader(name, value);
		
		try {
			response = httpclient.execute(httpget);
			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				String result = null;
				try {
					// EntityUtils里面解析流的时候，已近关闭了实体流了
					result = EntityUtils.toString(httpEntity);
				} catch (ParseException e) {
					log.error("请求Get,解析resulr数据异常ParseException：", e);
					e.printStackTrace();
				} catch (IOException e) {
					log.error("请求Get,解析resulr数据异常IOException：", e);
					e.printStackTrace();
				}
				System.out.println("=====请求返回的数据====="+result);
				return JSONObject.parseObject(result);
			}
		} catch (UnsupportedEncodingException e1) {
			log.error("请求Get异常UnsupportedEncodingException：", e1);
			e1.printStackTrace();
		} catch (ClientProtocolException e2) {
			log.error("请求Get异常ClientProtocolException：", e2);
			e2.printStackTrace();
		} catch (IOException e) {
			log.error("请求Get异常IOException：", e);
			e.printStackTrace();
		}finally{
			try {
				if(null != response){
					response.close();
				}
				if(null != httpclient){
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		return null;
	}
	
	/**
	 * 创建HttpClient对象
	 * @auth 哎，就这样用吧，里面太深看的头痛
	 * @return
	 */
	public static CloseableHttpClient createDefault() {
		return HttpClientBuilder.create().build();
	}
	
	
	public static void main(String[] args) {
		
	}
}
