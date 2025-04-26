package com.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PrintAllHandlerSax extends DefaultHandler{
       
	private StringBuffer path;
	private StringBuffer textContent;
	private Hashtable hashtable;
	
	public PrintAllHandlerSax(String fn) throws SAXException, IOException, ParserConfigurationException{
		InputSource is=new InputSource(new StringReader(fn));
		SAXParserFactory factory=SAXParserFactory.newInstance();
		SAXParser parser=factory.newSAXParser();
		
		this.path=new StringBuffer();
		this.hashtable=new Hashtable();
		parser.parse(is, this);
		
	}
	
	public void startElement(String uri, String local, String qname, Attributes atts) {
		path.append('/');
		path.append(qname);
		int nattrs=atts.getLength();
		for(int i=0;i<nattrs;i++) {
			addValue(path.toString()+"/@"+atts.getQName(i), atts.getValue(i));
		}
		this.textContent=new StringBuffer();
	}
	
	public void endElement(String uri, String local, String qname) {
		if(this.textContent!=null) {
			addValue(path.toString(), this.textContent.toString());
			this.textContent=null;
		}
		
		int pathLen=path.length();
		path.delete(pathLen-qname.length()-1, pathLen);
	}
	
	public void characters(char[] ch, int start, int length) {
		if(this.textContent!=null) {
			this.textContent.append(ch, start, length);
		}
	}
		Hashtable getHashtable() {
			return this.hashtable;
		}
	

	private void addValue(String key, String value) {
		Vector v = (Vector)this.hashtable.get(key);
		if(v==null) {
			v=new Vector();
			this.hashtable.put(key, v);
		}
		v.add(value);
		
	}
	
}
