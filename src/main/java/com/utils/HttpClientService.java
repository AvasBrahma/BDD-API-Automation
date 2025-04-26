package com.utils;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class HttpClientService extends HttpClientMain{
	
	 HttpClient client;
	
	 private static String strEndPointURL="";
	 private static String strAuthorizationType="";
	 private static Map<String, String> mapHeaders=new HashMap<String, String>();
	 private static String strRequestBody="";
	 private static String strResponseCode="";
	 private static String strResponseBody="";
	 

	public static String getStrEndPointURL() {
		return strEndPointURL;
	}
	public static void setStrEndPointURL(String strEndPointURL) {
		HttpClientService.strEndPointURL = strEndPointURL;
	}
	public static String getStrAuthorizationType() {
		return strAuthorizationType;
	}
	public static void setStrAuthorizationType(String strAuthorizationType) {
		HttpClientService.strAuthorizationType = strAuthorizationType;
	}
	public static Map<String, String> getMapHeaders() {
		return mapHeaders;
	}
	public static void setMapHeaders(Map<String, String> mapHeaders) {
		HttpClientService.mapHeaders = mapHeaders;
	}
	public static String getStrRequestBody() {
		return strRequestBody;
	}
	public static void setStrRequestBody(String strRequestBody) {
		HttpClientService.strRequestBody = strRequestBody;
	}
	public static String getStrResponseCode() {
		return strResponseCode;
	}
	public static void setStrResponseCode(String strResponseCode) {
		HttpClientService.strResponseCode = strResponseCode;
	}
	public static String getStrResponseBody() {
		return strResponseBody;
	}
	public static void setStrResponseBody(String strResponseBody) {
		HttpClientService.strResponseBody = strResponseBody;
	}
	 
	 
	public static boolean SubmitRequest(String strWebMethod) {
		
		boolean blnRequest=false;
		
		if(strWebMethod.equalsIgnoreCase("POST-JSON")) {
			blnRequest=SubmitPostAPI();
		}
        if(strWebMethod.equalsIgnoreCase("POST-XML")) {
        	blnRequest=SubmitPostXMLAPI();
		}
        if(strWebMethod.equalsIgnoreCase("PUT")) {
        	blnRequest=SubmitPUTAPI();
		}
        if(strWebMethod.equalsIgnoreCase("GET")) {
        	blnRequest=SubmitGetAPI();
		}
        if(strWebMethod.equalsIgnoreCase("DELETE")) {
        	blnRequest=SubmitDeleteAPI();
		}
        
        return blnRequest;
		
	}
	
	public static boolean SubmitPostAPI() {
		boolean blnResponse=false;
		HttpPost post=null;
		HttpResponse response=null;
		StringEntity params;
		
		try {
			params=new StringEntity(strRequestBody, "UTF-8");
			post=new HttpPost(strEndPointURL);
			for(Map.Entry<String,String> entry:mapHeaders.entrySet()) {
				String strKey=(String)entry.getKey();
				if(!strAuthorizationType.isEmpty()&&strKey.equals("Authorization")) {
					post.addHeader(strKey, strAuthorizationType+ " "+(String)entry.getValue());
				}else {
					post.addHeader(strKey, (String)entry.getValue());
				}
			}
			
			post.setEntity(params);
			
			response=getCloseableHttpClient().execute(post);
			if(response!=null) {
				blnResponse=true;
			}
			FetchResponseDetails(response);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return blnResponse;
	}
	
	public static boolean SubmitPUTAPI() {
		boolean blnResponse=false;
		HttpPost post=null;
		HttpResponse response=null;
		StringEntity params;
		
		try {
			params=new StringEntity(strRequestBody, "UTF-8");
			post=new HttpPost(strEndPointURL);
			for(Map.Entry<String,String> entry:mapHeaders.entrySet()) {
				String strKey=(String)entry.getKey();
				if(!strAuthorizationType.isEmpty()&&strKey.equals("Authorization")) {
					post.addHeader(strKey, strAuthorizationType+ " "+(String)entry.getValue());
				}else {
					post.addHeader(strKey, (String)entry.getValue());
				}
			}
			
			post.setEntity(params);
			
			response=getCloseableHttpClient().execute(post);
			if(response!=null) {
				blnResponse=true;
			}
			FetchResponseDetails(response);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return blnResponse;
	}
	public static boolean SubmitGetAPI() {
		boolean blnResponse=false;
		HttpGet get=null;
		HttpResponse response=null;
		//StringEntity params;
		
		try {
			//params=new StringEntity(strRequestBody, "UTF-8");
			get=new HttpGet(strEndPointURL);
			for(Map.Entry<String,String> entry:mapHeaders.entrySet()) {
				String strKey=(String)entry.getKey();
				if(!strAuthorizationType.isEmpty()&&strKey.equals("Authorization")) {
					get.addHeader(strKey, strAuthorizationType+ " "+(String)entry.getValue());
				}else {
					get.addHeader(strKey, (String)entry.getValue());
				}
			}
			
			response=getHttpsClient().execute(get);
			if(response!=null) {
				blnResponse=true;
			}
			FetchResponseDetails(response);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return blnResponse;
	}
	
	
	public static boolean SubmitDeleteAPI() {
		boolean blnResponse=false;
		HttpDelete delete=null;
		HttpResponse response=null;
		//StringEntity params;
		
		try {
			//params=new StringEntity(strRequestBody, "UTF-8");
			delete=new HttpDelete(strEndPointURL);
			for(Map.Entry<String,String> entry:mapHeaders.entrySet()) {
				String strKey=(String)entry.getKey();
				if(!strAuthorizationType.isEmpty()&&strKey.equals("Authorization")) {
					delete.addHeader(strKey, strAuthorizationType+ " "+(String)entry.getValue());
				}else {
					delete.addHeader(strKey, (String)entry.getValue());
				}
			}
			response=getCloseableHttpClient().execute(delete);
			if(response!=null) {
				blnResponse=true;
			}
			FetchResponseDetails(response);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return blnResponse;
	}
	
	public static boolean SubmitPostXMLAPI() {
		boolean blnResponse=false;	
		try {
			URL url=new URL(strEndPointURL);
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			//connection.setRequestProperty("Accept", "application/json");
			for(Map.Entry<String,String> entry:mapHeaders.entrySet()) {
				connection.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
			}
			DataOutputStream stream=new DataOutputStream(connection.getOutputStream());
			stream.writeBytes(strRequestBody);
			stream.flush();
			stream.close();
			
			int responseStatus=connection.getResponseCode();
			InputStream inputStream=connection.getInputStream();
			byte[] res=new byte[2048];
			int i=0;
			StringBuilder responseXML=new StringBuilder();
			while((i=inputStream.read(res))!=-1) {
				responseXML.append(new String(res,0,i));
			}
			inputStream.close();
			strResponseCode=String.valueOf(responseStatus);
			strResponseBody=responseXML.toString();
			System.out.println("Response Code : " + strResponseCode);
			System.out.println("Response Body : " + strResponseBody);

			if(inputStream!=null) {
				blnResponse=true;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return blnResponse;
	}
	
	
	
	
	private static void FetchResponseDetails(HttpResponse response) {
		HttpEntity entity=null;
		String strResponse="Response is getting Error";
		String strResponseStatus="";
		if(response!=null) {
			entity=response.getEntity();
			strResponseStatus=String.valueOf(response.getStatusLine().getStatusCode());
			try {
				strResponse=EntityUtils.toString(entity, "UTF-8");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		strResponseCode=strResponseStatus;
		strResponseBody=strResponse;
		System.out.println("Response Code : " + strResponseCode);
		System.out.println("Response Body : " + strResponseBody);
		
		
	}
	
	public static String getXMLTagValue(String strNodeTagName) {
		String strXMLTagValue="";
		Hashtable xmlHT;
		try {
			String strXMLContent=getStrResponseBody();
			PrintAllHandlerSax theConFig=new PrintAllHandlerSax(strXMLContent);
			xmlHT=theConFig.getHashtable();
			if(xmlHT.size()>0) {
				if(xmlHT.containsKey(strNodeTagName)) {
					strXMLTagValue=xmlHT.get(strNodeTagName).toString();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strXMLTagValue;
	}
	
	public static String GetNodeTagData(String strNodeTagName) {
		//JSONParser jsonParser=new JSONParser();
		JSONObject jsonObj=new JSONObject();
		String strNodeTagValue="";
		Hashtable<String,String> htblActDataRow=new Hashtable<String, String>();
		String strJSONContent=getStrResponseBody();
		try {
			if(!strJSONContent.equalsIgnoreCase("Response is getting error")) {
				jsonObj=new JSONObject(strJSONContent);
				GetJson(jsonObj, ".", htblActDataRow);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(htblActDataRow.size()>0) {
			if(htblActDataRow.containsKey(strNodeTagName)) {
				strNodeTagValue=htblActDataRow.get(strNodeTagName);
			}
		}
		return strNodeTagValue;
	}
	private static JSONObject GetJson(JSONObject jsonObj, String strClass, Hashtable<String, String> htblActDataRow) {
		JSONObject json=new JSONObject();
		if(strClass.charAt(0)=='.') {
			strClass=strClass.substring(1);
		}
		Iterator iterator=jsonObj.keys();
		String key=null;
		while(iterator.hasNext()) {
			key=(String)iterator.next();
			if((jsonObj.optJSONArray(key)==null)&&(jsonObj.optJSONObject(key)==null)) {
				if(strClass.length()==0) {
					htblActDataRow.put(key, jsonObj.get(key).toString());
				}else {
					htblActDataRow.put(strClass+"."+key,jsonObj.get(key).toString());
				}
			}
		}
		
		if(jsonObj.optJSONObject(key)!=null) {
			GetJson(jsonObj.getJSONObject(key), strClass+"."+key, htblActDataRow);
		}
		if(jsonObj.optJSONArray(key)!=null) {
			JSONArray jArr=jsonObj.getJSONArray(key);
			for(int intIndex=0;intIndex<jArr.length();intIndex++) {
				Object objTemp=jArr.get(intIndex);
				String strClassText=objTemp.getClass().toString();
				String[] arrClassText=strClassText.split("\\.");
				strClassText= arrClassText[arrClassText.length-1];
				
				if(strClassText.equalsIgnoreCase("jsonobject")) {
					JSONObject jObj=jArr.getJSONObject(intIndex);
					String strObjectText=jObj.toString();
					GetJson(jObj, strClass+"."+key+"["+intIndex+"]", htblActDataRow);
				}else {
					if(strClass.isEmpty()) {
						htblActDataRow.put(key+"["+intIndex+"]", objTemp.toString());
					}else {
						htblActDataRow.put(strClass +"."+key+"["+intIndex+"]",objTemp.toString());
					}
					
				}
			}
		}
		return jsonObj;
	}
	
	

}
