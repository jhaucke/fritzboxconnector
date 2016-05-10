package com.github.jhaucke.fritzboxconnector;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jhaucke.fritzboxconnector.helper.HttpHelper;
import com.github.jhaucke.fritzboxconnector.types.SessionInfo;

/**
 * This class handles the authentication over the FritzBox-Session-ID.
 */
public class Authenticator {

	private static final String DEFAULT_INVALID_SID = "0000000000000000";
	private String fritzBoxHostName;
	private final Logger logger;

	/**
	 * Constructor for {@link Authenticator}.
	 * 
	 * @param fritzBoxHostName
	 *            host name of the FritzBox
	 */
	public Authenticator(String fritzBoxHostName) {
		super();
		logger = LoggerFactory.getLogger(Authenticator.class);

		this.fritzBoxHostName = fritzBoxHostName;
	}

	public String getNewSessionId(final String username, final String password) throws IOException {

		SessionInfo sessionInfo = null;

		String responseWithoutCredentials = HttpHelper
				.executeHttpGet("https://" + fritzBoxHostName + ":48808/login_sid.lua");
		sessionInfo = convertSessionInfoXML(responseWithoutCredentials);

		if (sessionInfo.getSid().equals(DEFAULT_INVALID_SID)) {

			String responseWithCredentials = HttpHelper
					.executeHttpGet("https://" + fritzBoxHostName + ":48808/login_sid.lua?username=" + username
							+ "&response="
							+ getResponse(sessionInfo.getChallenge(), password));
			sessionInfo = convertSessionInfoXML(responseWithCredentials);
		}

		return sessionInfo.getSid();
	}

	private String getResponse(String challenge, String password) {

		return challenge + "-" + getMD5Hash(challenge + "-" + password);
	}

	private String getMD5Hash(String stringToHash) {

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(stringToHash.getBytes("UTF-16LE"));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private SessionInfo convertSessionInfoXML(String xmlString) {

		SessionInfo unmarshaledString = null;

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(SessionInfo.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(xmlString);
			unmarshaledString = (SessionInfo) unmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			String newLine = System.getProperty("line.separator");
			logger.error("Message: " + e.getMessage() + newLine + "Cause: " + e.getCause() + newLine + "StackTrace: "
					+ e.getStackTrace());
		}
		return unmarshaledString;
	}
}
