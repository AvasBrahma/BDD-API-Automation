package com.utils;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class APILogger {
	
	
	private static List<String> lstLogs=new ArrayList<String>();
	private static List<String> lstExpectedLogs=new ArrayList<String>();
	private static List<String> lstScreenShotLogs=new ArrayList<String>();
	private static int intStepNumber=0;
	public static String strTestCaseNumber="";
	public static String strTestResultPath="";
	public static String strResponseFilePath="";
	
	
	public static void addTestStepLog(String strLogText) {
		String strStepNumber=GetStep();
		lstLogs.add(strTestCaseNumber + "\t" + strStepNumber + "\t" + strLogText);
		
	}
	
	public static void addExpectedTestStepLog(String strLogText) {
		String strStepNumber=GetStep();
		lstExpectedLogs.add(strTestCaseNumber + "\t" + strStepNumber + "\t" + strLogText);
	}
	
	public static void addTestStepScreenCaptureFromPath(String strDestinationPath) {
		String strStepNumber=GetStep();
		lstScreenShotLogs.add(strTestCaseNumber + "\t" + strStepNumber + "\t" + strDestinationPath);
	}
	
	public static List<String> getStepLogs(){
		return lstLogs;
	}
	
	public static List<String> getExpectedLogs(){
		return lstExpectedLogs;
	}

	public static List<String> getScreenShotLogs(){
		return lstScreenShotLogs;
	}
	
	public static void setStepNumber(int intStepIndex) {
		intStepNumber=intStepIndex;
	}
	
	public static String GetStep() {
		String strStep="";
		strStep="Step Number " + String.valueOf(intStepNumber);
		return strStep;
	}
	
	public static void LogAdditionalContent(String strFileName, String strFileContent) {
		
		strResponseFilePath=strTestResultPath+"/"+strFileName;
		try {
			FileWriter writer=new FileWriter(strTestResultPath+"/"+strFileName);
			writer.write(strFileContent);
			writer.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
