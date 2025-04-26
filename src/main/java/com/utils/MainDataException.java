package com.utils;

public class MainDataException extends RuntimeException {

	 private boolean suppressStacktrace=false;
	 
	 public MainDataException(String message, boolean suppressStacktrace) {
		 super(message, null, suppressStacktrace, !suppressStacktrace);
		 this.suppressStacktrace=suppressStacktrace;
	 }
	 
	 @Override
	 public String toString() {
		 if(suppressStacktrace) {
			 return getLocalizedMessage();
		 }else {
			 return super.toString();
		 }
	 }
	 
	 public MainDataException(String errorMessage, Throwable error) {
		 super(errorMessage, error);
	 }
}
