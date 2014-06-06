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
package org.openhab.binding.vitotronic.internal.protocol.utils.test;

import static org.junit.Assert.*;
import org.junit.*;
import org.openhab.binding.vitotronic.internal.protocol.utils.*;

import static org.mockito.Mockito.*;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class SerialPortGatewayTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSendBytesAndWaitForResponse_WithAllBytesAvailable() throws Exception {
		IByteQueue requestBytes = To.ByteQueue("01 02 03 04 05");
		IByteQueue expectedResponseBytes = To.ByteQueue("05 04 03 02 01");
		ISerialPort serialPort = mock(ISerialPort.class);
		
		when(serialPort.getAvailableBytesCount()).thenReturn(expectedResponseBytes.size());
		when(serialPort.readBytes(expectedResponseBytes.size())).thenReturn(expectedResponseBytes.toByteArray());
		
		ISerialPortGateway testObject = new SerialPortGateway(serialPort);
		IByteQueue foundResponseBytes = testObject.sendBytesAndWaitForResponse(requestBytes, expectedResponseBytes.size());
		
		verify(serialPort).writeBytes(requestBytes.toByteArray());
		assertArrayEquals(expectedResponseBytes.toByteArray(), foundResponseBytes.toByteArray());
	}
	
	@Test
	public void testSendBytesAndWaitForResponse_WithBytesAvailableInPartitions() throws Exception {
		IByteQueue requestBytes = To.ByteQueue("01 02 03 04 05");
		IByteQueue expectedResponseBytes = To.ByteQueue("05 04 03 02 01");
		ISerialPort serialPort = mock(ISerialPort.class);
		
		when(serialPort.getAvailableBytesCount()).thenReturn(3, 2);
		when(serialPort.readBytes(3)).thenReturn(To.ByteArray("05 04 03"));
		when(serialPort.readBytes(2)).thenReturn(To.ByteArray("02 01"));
		
		ISerialPortGateway testObject = new SerialPortGateway(serialPort);
		IByteQueue foundResponseBytes = testObject.sendBytesAndWaitForResponse(requestBytes, expectedResponseBytes.size());
		
		verify(serialPort).writeBytes(requestBytes.toByteArray());
		assertArrayEquals(expectedResponseBytes.toByteArray(), foundResponseBytes.toByteArray());
	}
	
	@Test
	public void testSendBytesAndWaitForResponse_WithMoreAvailabByteThanNeeded() throws Exception {
		IByteQueue requestBytes = To.ByteQueue("01 02 03 04 05");
		IByteQueue expectedResponseBytes = To.ByteQueue("05 04 03 02 01");
		ISerialPort serialPort = mock(ISerialPort.class);
		
		when(serialPort.getAvailableBytesCount()).thenReturn(10);
		when(serialPort.readBytes(expectedResponseBytes.size())).thenReturn(expectedResponseBytes.toByteArray());
		
		ISerialPortGateway testObject = new SerialPortGateway(serialPort);
		IByteQueue foundResponseBytes = testObject.sendBytesAndWaitForResponse(requestBytes, expectedResponseBytes.size());
		
		verify(serialPort).writeBytes(requestBytes.toByteArray());
		assertArrayEquals(expectedResponseBytes.toByteArray(), foundResponseBytes.toByteArray());
	}
	
	@Test(expected = WaitForResponseTimedoutException.class, timeout = 2000)
	public void testSendBytesAndWaitForResponse_WithTimeout() throws Exception {
		IByteQueue requestBytes = To.ByteQueue("01 02 03 04 05");
		ISerialPort serialPort = mock(ISerialPort.class);
		
		when(serialPort.getAvailableBytesCount()).thenReturn(0);
		when(serialPort.readBytes(0)).thenReturn(new byte[0]);
		
		ISerialPortGateway testObject = new SerialPortGateway(serialPort);
		testObject.sendBytesAndWaitForResponse(requestBytes, 6);
		
		verify(serialPort).writeBytes(requestBytes.toByteArray());
	}
}
