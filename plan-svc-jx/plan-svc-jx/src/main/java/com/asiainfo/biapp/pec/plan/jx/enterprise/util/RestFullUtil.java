package com.asiainfo.biapp.pec.plan.jx.enterprise.util;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class RestFullUtil {

	private static Logger log = Logger.getLogger(RestFullUtil.class);
	private static RestFullUtil rf = null;

	private RestFullUtil() {
	}

	public static RestFullUtil getInstance() {
		if (rf == null) {
			rf = new RestFullUtil();
		}
		return rf;
	}

	public String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		URLConnection conn = null;
		String result = "";
		try {
			URL realUrl = new URL(url);

			conn = realUrl.openConnection();

			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			conn.setDoOutput(true);
			conn.setDoInput(true);

			out = new PrintWriter(conn.getOutputStream()); // 这句是否必须添加才能联通，不得而知
			if(null!=param && !"".equals(param)){
			     out.print(param);
			     out.flush();
			}

			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.error("error", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				log.error("error", ex);
			}
		}
		return result;
	}

	public String sendPost2(String targetURL, String jsonData)throws IOException {
		log.info("targetURL="+targetURL);
		URL targetUrl = new URL(targetURL);
		HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
		httpConnection.setDoOutput(true);
		httpConnection.setRequestMethod("POST");
		String result = "";
		httpConnection.setRequestProperty("Content-Type", "application/json");
		OutputStream outputStream = httpConnection.getOutputStream();
		if (!Strings.isNullOrEmpty(jsonData)) {
			outputStream.write(jsonData.getBytes());
			outputStream.flush();
		}
		if (httpConnection.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "+ httpConnection.getResponseCode());
		}
		BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
		String line = "";
		log.info("Output from Server:\n");
		while ((line = responseBuffer.readLine()) != null) {
			result += line;
		}
		httpConnection.disconnect();
		return result;

	}
	
	public String sendGet(String targetURL) {
		String result="";
		try {
			URL restServiceURL = new URL(targetURL);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Accept", "application/json");
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("HTTP GET Request Failed with Error code : "+ httpConnection.getResponseCode());
			}
			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
			String line;
			log.info("Output from Server:  \n");
			while ((line = responseBuffer.readLine()) != null) {
				result+=line;
			}
			httpConnection.disconnect();
		} catch (MalformedURLException e) {
			log.error("error", e);
		} catch (IOException e) {
			log.error("error", e);
		}
		return result;
	}

	public String sendGetRequest(String targetURL,String token) {
		String result="";
		try {
			URL restServiceURL = new URL(targetURL);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Accept", "application/json");
			httpConnection.setRequestProperty("X-Authorization", token);
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("HTTP GET Request Failed with Error code : "+ httpConnection.getResponseCode());
			}
			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
			String line;
			log.info("Output from Server:  \n");
			while ((line = responseBuffer.readLine()) != null) {
				result+=line;
			}
			httpConnection.disconnect();
		} catch (MalformedURLException e) {
			log.error("error", e);
		} catch (IOException e) {
			log.error("error", e);
		}
		return result;
	}

	public String sendPostRequest(String targetURL, String jsonData, String token)throws IOException {
		log.info("targetURL="+targetURL);
		URL targetUrl = new URL(targetURL);
		HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
		httpConnection.setDoOutput(true);
		httpConnection.setRequestMethod("POST");
		String result = "";
		httpConnection.setRequestProperty("Content-Type", "application/json");
		httpConnection.setRequestProperty("X-Authorization", token);
		OutputStream outputStream = httpConnection.getOutputStream();
		if (!Strings.isNullOrEmpty(jsonData)) {
			outputStream.write(jsonData.getBytes());
			outputStream.flush();
		}
		if (httpConnection.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "+ httpConnection.getResponseCode());
		}
		BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
		String line = "";
		log.info("Output from Server:\n");
		while ((line = responseBuffer.readLine()) != null) {
			result += line;
		}
		httpConnection.disconnect();
		return result;
	}


	public static void main(String[] args) throws IOException {
		String url = "http://localhost:8080/mcd-web/api/test/submit.do";
		String param ="{\"aa\":3}";
		RestFullUtil.getInstance().sendPost2(url, param);
	}
}
