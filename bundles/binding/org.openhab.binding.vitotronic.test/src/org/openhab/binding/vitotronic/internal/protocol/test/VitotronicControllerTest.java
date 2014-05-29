	package org.openhab.binding.vitotronic.internal.protocol.test;
/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */


import static org.junit.Assert.*;

import org.junit.*;
import org.openhab.binding.vitotronic.internal.protocol.*;
import org.openhab.binding.vitotronic.internal.protocol.utils.*;

import static org.mockito.Mockito.*;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class VitotronicControllerTest {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testInit() {
		IByteQueue initRequestByteQueue = mock(IByteQueue.class);
		IByteQueue initResponseByteQueue = mock(IByteQueue.class);
		IVitotronicProtocol protocol = mock(IVitotronicProtocol.class);
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);

		when(protocol.expectedInitResponseSize()).thenReturn(1);
		when(protocol.getByteQueueForInit()).thenReturn(initRequestByteQueue);
		when(gateway.sendBytesAndWaitForResponse(initRequestByteQueue, 1)).thenReturn(initResponseByteQueue);
		when(protocol.isInitialized(initResponseByteQueue)).thenReturn(true);
		
		IVitotronicController testObject = new VitotronicController(protocol, gateway);
		boolean isInitialized = testObject.init();
		
		assertTrue(isInitialized);
		verify(protocol).expectedInitResponseSize();
		verify(protocol).getByteQueueForInit();
		verify(gateway).sendBytesAndWaitForResponse(initRequestByteQueue, 1);
		verify(protocol).isInitialized(initResponseByteQueue);
	}
	
	@Test
	public void testInitWithRetry() {
		IByteQueue initRequestByteQueue = mock(IByteQueue.class);
		IByteQueue initResponseByteQueue = mock(IByteQueue.class);
		IVitotronicProtocol protocol = mock(IVitotronicProtocol.class);
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);

		when(protocol.expectedInitResponseSize()).thenReturn(1);
		when(protocol.getByteQueueForInit()).thenReturn(initRequestByteQueue);
		when(gateway.sendBytesAndWaitForResponse(initRequestByteQueue, 1)).thenReturn(initResponseByteQueue);
		when(protocol.isInitialized(initResponseByteQueue)).thenReturn(false, true);
		
		IVitotronicController testObject = new VitotronicController(protocol, gateway);
		boolean isInitialized = testObject.init();
		
		assertTrue(isInitialized);
		verify(protocol).expectedInitResponseSize();
		verify(protocol).getByteQueueForInit();
		verify(gateway, times(2)).sendBytesAndWaitForResponse(initRequestByteQueue, 1);
		verify(protocol, times(2)).isInitialized(initResponseByteQueue);
	}
	
	@Test
	public void testInitWithMaxRetryExceeded() {
		IByteQueue initRequestByteQueue = mock(IByteQueue.class);
		IByteQueue initResponseByteQueue = mock(IByteQueue.class);
		IVitotronicProtocol protocol = mock(IVitotronicProtocol.class);
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);

		when(protocol.expectedInitResponseSize()).thenReturn(1);
		when(protocol.getByteQueueForInit()).thenReturn(initRequestByteQueue);
		when(gateway.sendBytesAndWaitForResponse(initRequestByteQueue, 1)).thenReturn(initResponseByteQueue);
		when(protocol.isInitialized(initResponseByteQueue)).thenReturn(false, false, false, false, false, false, false, false, false, false);
		
		IVitotronicController testObject = new VitotronicController(protocol, gateway);
		boolean isInitialized = testObject.init();
		
		assertFalse(isInitialized);
		verify(protocol).expectedInitResponseSize();
		verify(protocol).getByteQueueForInit();
		verify(gateway, times(10)).sendBytesAndWaitForResponse(initRequestByteQueue, 1);
		verify(protocol, times(10)).isInitialized(initResponseByteQueue);
	}

	@Test
	public void testReset() {
		IByteQueue resetRequestBytes = mock(IByteQueue.class);
		IVitotronicProtocol vitotronicProtocol = mock(IVitotronicProtocol.class);
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		
		when(vitotronicProtocol.getByteQueueForReset()).thenReturn(resetRequestBytes);
		when(vitotronicProtocol.expectedResetResponseSize()).thenReturn(2);
		
		IVitotronicController testObject =  new VitotronicController(vitotronicProtocol, gateway);		
		testObject.reset();
		
		verify(gateway).sendBytesAndWaitForResponse(resetRequestBytes, 2);
	}
	
	@Test
	public void testReadValueOf() {
		String expectedParameterValue = "testValue";
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		IStringParameter inputParameter = mock(IStringParameter.class);
		IStringParameter outputParameter = mock(IStringParameter.class);
		IByteQueue readParameterRequestBytes = mock(IByteQueue.class);
		IByteQueue readParameterResponseBytes = mock(IByteQueue.class);
		IVitotronicProtocol protocol = mock(IVitotronicProtocol.class);
		
		when(protocol.getByteQueueForReadingParameter(inputParameter)).thenReturn(readParameterRequestBytes);
		when(protocol.expectedReadingParameterResponseSize(inputParameter)).thenReturn(8);
		when(gateway.sendBytesAndWaitForResponse(readParameterRequestBytes, 8)).thenReturn(readParameterResponseBytes);
		when(protocol.<String>parseReadParameterResponse(readParameterResponseBytes)).thenReturn(outputParameter);
		when(outputParameter.getValue()).thenReturn(expectedParameterValue);
		
		IVitotronicController controller = new VitotronicController(protocol, gateway);		
		String readParamerterValue = controller.readValueOf(inputParameter);
		
		verify(outputParameter).getValue();
		assertEquals(expectedParameterValue, readParamerterValue);
	}

	@Test
	public void testWrite() {
		String expectedParameterValue = "testValue";
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		IStringParameter parameterForRequest = mock(IStringParameter.class);
		IStringParameter parameterInResponse = mock(IStringParameter.class);
		IByteQueue writeParameterRequestBytes = mock(IByteQueue.class);
		IByteQueue writeParameterResponseBytes = mock(IByteQueue.class);
		IVitotronicProtocol protocol = mock(IVitotronicProtocol.class);
		
		when(protocol.getByteQueueForWritingParameter(parameterForRequest)).thenReturn(writeParameterRequestBytes);
		when(protocol.expectedWritingParameterResponseSize(parameterForRequest)).thenReturn(8);
		when(gateway.sendBytesAndWaitForResponse(writeParameterRequestBytes, 8)).thenReturn(writeParameterResponseBytes);
		when(protocol.<String>parseWriteParameterResponse(writeParameterResponseBytes)).thenReturn(parameterInResponse);
		when(parameterInResponse.getValue()).thenReturn(expectedParameterValue);
		
		IVitotronicController controller = new VitotronicController(protocol, gateway);		
		String writtenParameterValue = controller.write(parameterForRequest);
		
		assertEquals(expectedParameterValue, writtenParameterValue);
		verify(gateway).sendBytesAndWaitForResponse(writeParameterRequestBytes, 8);
	}
}
