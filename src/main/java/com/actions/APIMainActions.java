package com.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.utils.APILogger;
import com.utils.ConfigDetails;
import com.utils.HttpClientService;
import com.utils.MainDataException;
import com.utils.OutputSaveNodes;

public class APIMainActions {
	
	private String strWebMethod="";
	private String strInputType="";
	private String strAPIReqBody="";
	private String strRequestName="";
	Map<String, String> mapHeaders=new HashMap<String, String>();
	public void setEndPointURL(String strEndPointURL) {
		
		HttpClientService.setStrEndPointURL(strEndPointURL);
		String strLogText=strEndPointURL+ " Selected Successfully ";
		System.out.println(strLogText);
		
	}
	
	public void CreateHeaders() {
		String strHeaderFile=System.getProperty("user.dir")+"\\"+ConfigDetails.getPropValue("HeadersFile");
		mapHeaders.clear();
		String strLogText="";
		FileInputStream fstream;
		try {
			fstream=new FileInputStream(strHeaderFile);
			BufferedReader br=new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while((strLine=br.readLine())!=null) {
				
				System.out.println(strLine);
				String[] arrHeaders=strLine.split("=");
				mapHeaders.put(arrHeaders[0],arrHeaders[1]);
				
			}
			fstream.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(!mapHeaders.isEmpty()) {
			HttpClientService.setMapHeaders(mapHeaders);
			strLogText="PASS: Headers Set Successfully ";
			System.out.println(strLogText);
		}else {
		    strLogText="Fail:  Headers File is Empty ";
			System.out.println(strLogText);
		}
		
	}
	
	public void SetMethod(String strMethod) {
		strWebMethod=strMethod;
		String strLogText=strMethod+ " Web Method Selected Successfully";
		System.out.println(strLogText);
	}
	
	public void SetInputBody(String strInputFile, Hashtable<String, String> htblInputData) {
		boolean blnInputSucess=true;
		byte[] dataContent=null;
		String strInputContent="";
		String strInputFilePath="";
		strInputFilePath=System.getProperty("user.dir")+File.separator+strInputFile;
		File flInput=new File(strInputFilePath);
		FileInputStream fis;
		try {
			fis=new FileInputStream(flInput);
			dataContent=new byte[(int) flInput.length()];
			fis.read(dataContent);
			fis.close();
			strInputContent=new String(dataContent, "UTF-8");
			strInputType=FilenameUtils.getExtension(strInputFilePath);
			strRequestName=FilenameUtils.getName(strInputFilePath);
		} catch (Exception e) {
			// TODO: handle exception
			blnInputSucess=false;
		}
		
		if(!strInputContent.isEmpty()) {
			String strColumnName="";
			String strColumnValue="";
			
			Enumeration enumElems=htblInputData.keys();
			while(enumElems.hasMoreElements()) {
				strColumnName=enumElems.nextElement().toString();
				strColumnValue=htblInputData.get(strColumnName);
				if(!strColumnValue.isEmpty()) {
					if(strColumnValue.charAt(0)=='{') {
						strColumnValue=strColumnValue.replace("{", "");
						strColumnValue=strColumnValue.replace("}","");
					}	
				}
				
				//it will replace the template with data $Header_Name$ with actual value
				strInputContent=strInputContent.replaceAll("\\$"+strColumnName+"\\$", strColumnValue);
				
				
			}
			
			strAPIReqBody=strInputContent;
			HttpClientService.setStrRequestBody(strInputContent);
			strInputFile=strInputFile.replace("\\", "\\");
			String[] strArrRequestFileName=strInputFile.split("/");
			strInputFile=strArrRequestFileName[strArrRequestFileName.length-1];
			APILogger.LogAdditionalContent("REQ_"+strInputFile, strInputContent);
			
				
		}
		
		if(blnInputSucess) {
			APILogger.addTestStepLog(strInputFile + " File Content Selected Successfully");
			System.out.println("strInput = " + strInputFile);
		}else { 
			APILogger.addTestStepLog(strInputFile + " File Unavailable or unmatched content available");
			throw new MainDataException(strInputFile + " File Unavailable or unmatched content available", blnInputSucess);
			
		}
		
	}
	
	public void submitAndGetResponse(String strResponseFileName) {
		String strLogText="Request Should be Submitted and get response from Server";
		APILogger.addExpectedTestStepLog(strLogText);
		boolean blnResult=HttpClientService.SubmitRequest(strWebMethod);
		if(blnResult) {
			String strOutPutContent=HttpClientService.getStrResponseBody();
			strLogText="Request Submitted and Received Response Code from Server as " + HttpClientService.getStrResponseCode();
		    if(strRequestName.isEmpty()) {
		    	strRequestName=strWebMethod+ "." + strInputType;
		    }
		    APILogger.LogAdditionalContent("RES_"+strRequestName, strOutPutContent);
		    strInputType=FilenameUtils.getExtension(strResponseFileName);
		    APILogger.addTestStepLog(strLogText);
		}else {
			strLogText="Response Submitted and received response error";
			APILogger.addTestStepLog(strLogText);
			throw new MainDataException(strLogText, blnResult);
		}
	}
	
	public void verifyAPIResponseValue(String strNodeTagElementName, String strNodeTagElementValue, boolean blnIfExit) {
      String strLogText="";
      String strActualValue="";
      if(!strNodeTagElementValue.isEmpty()) {
    	  if(strNodeTagElementValue.charAt(0)=='{') {
    		 strNodeTagElementValue=strNodeTagElementValue.replace("{", "");
    		 strNodeTagElementValue=strNodeTagElementValue.replace("}", "");
    		 strNodeTagElementValue=OutputSaveNodes.GetNodeValue(strNodeTagElementValue);
    	  }
      }
      strLogText=strNodeTagElementName + " Should be verified as '" + strNodeTagElementValue + "'";
      APILogger.addExpectedTestStepLog(strLogText);
      if(strInputType.equalsIgnoreCase("XML")) {
    	  strActualValue=HttpClientService.getXMLTagValue(strNodeTagElementName);
      }
      if(strInputType.equalsIgnoreCase("JSON")) {
    	  strActualValue=HttpClientService.GetNodeTagData(strNodeTagElementValue);
      }
      
      if(strNodeTagElementValue.equalsIgnoreCase(strActualValue)) {
    	  strLogText=strNodeTagElementName + " Verified '" + strNodeTagElementValue + "' is equal";
    	  System.out.println(strLogText);
    	  APILogger.addTestStepLog(strLogText);
      }else {
    	  if(blnIfExit) {
    		  strLogText=strNodeTagElementName + " cannot be Verified '" + strNodeTagElementValue + "' is not equal to " +strActualValue;
    	  }else {
    		  strLogText=strNodeTagElementName + " cannot be Verified '" + strNodeTagElementValue + "' is not equal to " +strActualValue;
    		  APILogger.addTestStepLog(strLogText);
    		  throw new MainDataException(strLogText, false);
    	  }
      }
	}

}
