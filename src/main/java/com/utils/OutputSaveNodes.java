package com.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OutputSaveNodes {
	
	public static String strOutputNodesFilePath="";
	public static Properties propOutputNodes=new Properties();
	
	public static void SetOutputNodesPath(String strPath) {
		if(strPath.isEmpty()) {
			strOutputNodesFilePath=System.getProperty("user.dir")+"//"+ConfigDetails.getPropValue("OutputSaveNodePath");
		}else {
			strOutputNodesFilePath=strPath;
		}
	}
	
	public OutputSaveNodes() {
		LoadOutputNodes();
	}

	public static Properties LoadOutputNodes() {
		try {
			SetOutputNodesPath("");
			propOutputNodes.clear();
			File flOutput=new File(strOutputNodesFilePath);
			if(flOutput.exists()) {
				if(flOutput.isDirectory()) {
					File file=new File(strOutputNodesFilePath);
					file.createNewFile();
				}
			}else {
				File file=new File(strOutputNodesFilePath);
				file.createNewFile();
			}
			FileInputStream fisOutputNodes=new FileInputStream(new File(strOutputNodesFilePath));
			propOutputNodes.load(fisOutputNodes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return propOutputNodes;
		
	}
	
	public static String GetNodeValue(String strNodeName) {
		propOutputNodes.clear();
		if(propOutputNodes.isEmpty()) {
			LoadOutputNodes();
		}
		String strNodeValue="";
		try {
			strNodeValue=propOutputNodes.getProperty(strNodeName);
		} catch (Exception e) {
			System.out.println("Expected Value Unavailable for " + strNodeName);
		}
		return strNodeValue;
	}
	
	public static Map<String, String> GetNodeDataValues(){ 
		Map<String, String> mapOutputNodeTestData=new HashMap<String, String>();
		LoadOutputNodes();
		for(Map.Entry<Object, Object> entry : propOutputNodes.entrySet()) {
			mapOutputNodeTestData.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		}
		return mapOutputNodeTestData;
	}
	
    public static boolean UpdateNodes(String strNodeName, String strNodeValue) {
    	boolean blnUpdateResult=false;
    	try {
			LoadOutputNodes();
			FileOutputStream flOutputNodes=new FileOutputStream(strOutputNodesFilePath);
			propOutputNodes.setProperty(strNodeName, strNodeValue);
			propOutputNodes.store(flOutputNodes, null);
			flOutputNodes.close();
			blnUpdateResult=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return blnUpdateResult;
    }
}
