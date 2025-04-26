package com.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.client.HttpClient;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;



public class HttpClientMain {
	
	private static CloseableHttpClient client;
	
	public static HttpClient getHttpsClient() throws Exception{
		
		client=HttpClients.createDefault();
		return client;
		/* SSL Context sslcontext = getSSLContext();
		 * SSLConnectionSocketFactory factory=new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER);
		 * client = HttpClients.custom().setSSLSocketFactory(factory).build();
		 */
	}
	
	public static HttpClient getHttpClient() throws Exception{
		HttpClient client=HttpClients.createDefault();
		return client;
	}
	
	public static HttpClient getCloseableHttpClient() {
		CloseableHttpClient httpClient=null;
		try {
			httpClient=HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
					.setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
						public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException{
							return true;
						}
					}).build()).build();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return httpClient;
	}
	
	public static HttpClient getClosablePEPCLient() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
		
		HttpClientBuilder httpClientBuilder=HttpClientBuilder.create();
		SSLContext context=SSLContext.getInstance("TLS");
		java.security.Security.addProvider(
				new org.bouncycastle.jce.provider.BouncyCastleProvider()
		);
		String strDirectory=System.getProperty("user.dir");
		byte[] certAndKeyPri=Files.readAllBytes(Paths.get("Path of the PEM File"));
		byte[] certAndKeyPub=Files.readAllBytes(Paths.get("Path of the PEM File"));
		byte[] certBytes=parseDERFromPEM(certAndKeyPub, "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
		byte[] keyBytes=parseDERFromPEM(certAndKeyPri, "-----BEGIN RSA PRIVATE KEY-----", "-----END RSA PRIVATE KEY-----");
		
		X509Certificate cert=generateCertificateFromDER(certBytes);
		RSAPrivateKey key=generatePrivateKeyFromDER(keyBytes);
		
		KeyStore keystore=KeyStore.getInstance("JKS");
		keystore.load(null);
		keystore.setCertificateEntry("alias", cert);
		keystore.setKeyEntry("alias", (Key) key, "password".toCharArray(), new Certificate[] {cert});
		//keystore.setKeyEntry("alias", key, "password".toCharArray(), new Certificate[] {cert});
		
		KeyManagerFactory kmf=KeyManagerFactory.getInstance("SunX509");
		kmf.init(keystore,"password".toCharArray());
		KeyManager[] km=kmf.getKeyManagers();
		
		X509TrustManager acceptAll=new X509TrustManager()
		{
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException{}
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException{}
			
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		
		context.init(km, new TrustManager[] {acceptAll}, null);
		
		DnsResolver dnsResolver=new SystemDefaultDnsResolver()
				{
			        @Override
			        public InetAddress[] resolve(final String host) throws UnknownHostException
			        {
			        	if(host.equalsIgnoreCase("base Url"))
			        	{
			        		return new InetAddress[] { InetAddress.getByName("100.100.100.100")};
			        	}else {
			        		return super.resolve(host);
			        	}
			        }
				};
				
				SSLConnectionSocketFactory socketFactory=new SSLConnectionSocketFactory(context, new NoopHostnameVerifier());
				
				BasicHttpClientConnectionManager connManager=new BasicHttpClientConnectionManager(
						RegistryBuilder.<ConnectionSocketFactory>create()
						.register("http", PlainConnectionSocketFactory.getSocketFactory())
						.register("https", socketFactory)
						.build(),
						null,
						null,
						dnsResolver
				);
				
				httpClientBuilder.setConnectionManager(connManager);
				CloseableHttpClient client=httpClientBuilder.build();
				return client;
	
	}

	private static RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PKCS8EncodedKeySpec spec=new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory factory=KeyFactory.getInstance("RSA");
		return (RSAPrivateKey)factory.generatePrivate(spec);
	}

	private static X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException {
		CertificateFactory factory=CertificateFactory.getInstance("X.509");
		
		return (X509Certificate)factory.generateCertificate(new ByteArrayInputStream(certBytes));
	}

	private static byte[] parseDERFromPEM(byte[] pem, String beginDelimeter, String endDelimiter) {
		String data=new String(pem);
		String[] tokens=data.split(beginDelimeter);
		tokens=tokens[1].split(endDelimiter);
		
		return DatatypeConverter.parseBase64Binary(tokens[0]);
	}

}
