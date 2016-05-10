package com.github.jhaucke.fritzboxconnector.helper;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to handle http operations.
 */
public class HttpHelper {

	private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);

	/**
	 * This method executes the given uri as a {@link HttpGet} and returns the
	 * response as string.
	 * 
	 * @param uri
	 *            the requested url
	 * @return the response as string
	 * @throws IOException
	 */
	public static String executeHttpGet(String uri) throws IOException {

		CloseableHttpResponse response = null;

		try {
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
			CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			response = client.execute(new HttpGet(uri));
			HttpEntity entity = response.getEntity();
			String responseAsString = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
			return responseAsString;
		} catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
			String newLine = System.getProperty("line.separator");
			logger.error("Message: " + e.getMessage() + newLine + "Cause: " + e.getCause() + newLine + "StackTrace: "
					+ e.getStackTrace());
			return null;
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}
}
