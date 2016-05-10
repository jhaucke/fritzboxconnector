package com.github.jhaucke.fritzboxconnector;

import java.io.IOException;

import com.github.jhaucke.fritzboxconnector.helper.HttpHelper;

/**
 * Implementation of the AVM Home Automation HTTP Interface.
 */
public class HttpInterface {

	private final String sid;
	private final String fritzBoxHostName;

	/**
	 * Constructor for {@link HttpInterface}.
	 * 
	 * @param sid
	 *            session ID
	 * @param fritzBoxHostName
	 *            host name of the FritzBox
	 */
	public HttpInterface(String sid, String fritzBoxHostName) {
		super();

		this.sid = sid;
		this.fritzBoxHostName = fritzBoxHostName;
	}

	/**
	 * Returns the comma-separated AIN/MAC list of all known electric sockets.
	 * 
	 * @return comma separated AIN/MAC list, empty if no electric socket known
	 * @throws IOException
	 */
	public String getSwitchList() throws IOException {

		String response = HttpHelper.executeHttpGet(getCommandURL("getswitchlist", null));

		return response.trim();
	}

	/**
	 * Turns on the electric socket.
	 * 
	 * @param ain
	 *            the identification number of the actuator
	 * @return "1" (socket on)
	 * @throws IOException
	 */
	public String setSwitchOn(String ain) throws IOException {

		String response = HttpHelper.executeHttpGet(getCommandURL("setswitchon", ain));

		return response.trim();
	}

	/**
	 * Turns off the electric socket.
	 * 
	 * @param ain
	 *            the identification number of the actuator
	 * @return "0" (socket off)
	 * @throws IOException
	 */
	public String setSwitchOff(String ain) throws IOException {

		String response = HttpHelper.executeHttpGet(getCommandURL("setswitchoff", ain));

		return response.trim();
	}

	/**
	 * Toggles the electric socket.
	 * 
	 * @param ain
	 *            the identification number of the actuator
	 * @return "0" or "1" (socket off or on)
	 * @throws IOException
	 */
	public String setSwitchToggle(String ain) throws IOException {

		String response = HttpHelper.executeHttpGet(getCommandURL("setswitchtoggle", ain));

		return response.trim();
	}

	/**
	 * Returns the switching state of the electric socket.
	 * 
	 * @param ain
	 *            the identification number of the actuator
	 * @return "0" or "1" (socket off or on), "inval" if unknown
	 * @throws IOException
	 */
	public String getSwitchState(String ain) throws IOException {

		String response = HttpHelper.executeHttpGet(getCommandURL("getswitchstate", ain));

		return response.trim();
	}

	/**
	 * Returns the connection state of the actuator.<br>
	 * If the connection gets lost, the status switches with a delay of some
	 * minutes.
	 * 
	 * @param ain
	 *            the identification number of the actuator
	 * @return "0" or "1" (socket connected or not)
	 * @throws IOException
	 */
	public String getSwitchPresent(String ain) throws IOException {

		String response = HttpHelper.executeHttpGet(getCommandURL("getswitchpresent", ain));

		return response.trim();
	}

	/**
	 * Returns the currently extracted power of the electric socket.
	 * 
	 * @param ain
	 *            the identification number of the actuator
	 * @return power in mW, "inval" if unknown
	 * @throws IOException
	 */
	public String getSwitchPower(String ain) throws IOException {

		String response = HttpHelper.executeHttpGet(getCommandURL("getswitchpower", ain));

		return response.trim();
	}

	/**
	 * Returns the extracted energy of the electric socket since first use or
	 * reset.
	 * 
	 * @param ain
	 *            the identification number of the actuator
	 * @return energy in Wh, "inval" if unknown
	 * @throws IOException
	 */
	public String getSwitchEnergy(String ain) throws IOException {

		String response = HttpHelper.executeHttpGet(getCommandURL("getswitchenergy", ain));

		return response.trim();
	}

	/**
	 * Returns the name of the actuator.
	 * 
	 * @param ain
	 *            the identification number of the actuator
	 * @return the name
	 * @throws IOException
	 */
	public String getDeviceName(String ain) throws IOException {

		String response = HttpHelper.executeHttpGet(getCommandURL("getswitchname", ain));

		return response.trim();
	}

	/**
	 * Returns information of all smart home devices.
	 * 
	 * @return XML with basic and function-specific information
	 * @throws IOException
	 */
	public String getDeviceListInfos() throws IOException {

		String response = HttpHelper.executeHttpGet(getCommandURL("getdevicelistinfos", null));

		return response.trim();
	}

	/**
	 * Returns the last temperature information of the actuator.
	 * 
	 * @return temperature in 0,1 °C, negative and positive values are possible
	 *         (e.g. "200" means 20 °C)
	 * @throws IOException
	 */
	public String getTemperature(String ain) throws IOException {

		String response = HttpHelper.executeHttpGet(getCommandURL("gettemperature", null));

		return response.trim();
	}

	private String getCommandURL(String cmd, String ain) {

		String ainParameter = "&ain=";

		if (ain == null) {
			ainParameter = "";
		} else {
			ainParameter = ainParameter.concat(ain);
		}

		return "https://" + fritzBoxHostName + ":48808/webservices/homeautoswitch.lua?switchcmd=" + cmd + "&sid=" + sid
				+ ainParameter;
	}
}
