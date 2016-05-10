package com.github.jhaucke.fritzboxconnector;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.jhaucke.fritzboxconnector.FritzBoxConnector;
import com.github.jhaucke.fritzboxconnector.HttpInterface;
import com.github.jhaucke.fritzboxconnector.helper.HttpHelper;

/**
 * Test class for {@link FritzBoxConnector}.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ HttpHelper.class })
public class FritzBoxConnectorTest {

	private static final String DEFAULT_INVALID_SID = "0000000000000000";
	private static final String TEST_SID = "9c977765016899f8";
	private static final String TEST_AIN = "012340000123";

	/**
	 * Tests the library use with existing username, password and default host
	 * name. Successful result will be expected.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testWithUsernamePasswordDefHost() throws IOException {

		mockStatic(HttpHelper.class);

		String requestWithoutCredentials = "https://fritz.box:48808/login_sid.lua";
		String responseWithoutCredentials = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SessionInfo><SID>"
				+ DEFAULT_INVALID_SID
				+ "</SID><Challenge>1234567z</Challenge><BlockTime></BlockTime><Rights></Rights></SessionInfo>";
		expect(HttpHelper.executeHttpGet(requestWithoutCredentials)).andStubReturn(responseWithoutCredentials);

		String requestWithCredentials = "https://fritz.box:48808/login_sid.lua?username=testuser&response=1234567z-9e224a41eeefa284df7bb0f26c2913e2";
		String responseWithCredentials = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SessionInfo><SID>" + TEST_SID
				+ "</SID><Challenge>1234567z</Challenge><BlockTime></BlockTime><Rights><Name>Phone</Name><Access>2</Access></Rights></SessionInfo>";
		expect(HttpHelper.executeHttpGet(requestWithCredentials)).andStubReturn(responseWithCredentials);

		String requestGetSwitchList = "https://fritz.box:48808/webservices/homeautoswitch.lua?switchcmd=getswitchlist&sid=9c977765016899f8";
		String responseGetSwitchList = TEST_AIN + " ";
		expect(HttpHelper.executeHttpGet(requestGetSwitchList)).andStubReturn(responseGetSwitchList);

		replayAll();

		FritzBoxConnector fritzBoxConnector = new FritzBoxConnector("testuser", "äbc", null);
		assertNotNull("FritzBoxConnector is NULL", fritzBoxConnector);

		HttpInterface httpInterface = fritzBoxConnector.getHttpInterface();
		assertNotNull("HttpInterface is NULL", httpInterface);

		String switchList = httpInterface.getSwitchList();
		assertEquals("Unexpected list", TEST_AIN, switchList.trim());
	}
}
