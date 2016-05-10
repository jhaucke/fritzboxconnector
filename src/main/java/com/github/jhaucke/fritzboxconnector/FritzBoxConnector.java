package com.github.jhaucke.fritzboxconnector;

import java.io.IOException;

/**
 * Entry point to use the AVM Home Automation HTTP Interface.
 */
public class FritzBoxConnector {

	private String fritzBoxHostName = "fritz.box";
	private HttpInterface httpInterface;

	/**
	 * Constructor for {@link FritzBoxConnector}.
	 * 
	 * @param username
	 * @param password
	 * @param fritzBoxHostName
	 *            If the FritzBox use the default host name "fritz.box",
	 *            {@code null} can be passed, in local network.
	 * @throws IOException
	 */
	public FritzBoxConnector(final String username, final String password, final String fritzBoxHostName)
			throws IOException {
		super();

		if (fritzBoxHostName != null) {
			this.fritzBoxHostName = fritzBoxHostName;
		}

		Authenticator authenticator = new Authenticator(this.fritzBoxHostName);
		httpInterface = new HttpInterface(authenticator.getNewSessionId(username, password), this.fritzBoxHostName);
	}

	public HttpInterface getHttpInterface() {
		return httpInterface;
	}
}
