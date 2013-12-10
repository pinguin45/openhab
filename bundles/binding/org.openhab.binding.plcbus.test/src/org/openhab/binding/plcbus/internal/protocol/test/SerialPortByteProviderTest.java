/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.plcbus.internal.protocol.IByteProvider;
import org.openhab.binding.plcbus.internal.protocol.ISerialPort;
import org.openhab.binding.plcbus.internal.protocol.SerialPortByteProvider;
import org.openhab.binding.plcbus.internal.protocol.SerialPortException;

import static org.mockito.Mockito.*;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class SerialPortByteProviderTest {

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

	/**
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.utils.SerialPortByteProvider#getByte()}.
	 * @throws Exception 
	 */
	@Test
	public void testGetByte() throws Exception {
		byte[] expectedBytes = { 0x01 };
		ISerialPort serialPort = mock(ISerialPort.class);
		when(serialPort.isOpen()).thenReturn(true);
		when(serialPort.getAvailableBytesCount()).thenReturn(1);
		when(serialPort.readBytes(1)).thenReturn(expectedBytes);
		
		IByteProvider testObject = SerialPortByteProvider.create(serialPort);
		byte found = testObject.getByte();
		
		verify(serialPort).readBytes(1);
		assertEquals(expectedBytes[0], found);
	}
	
	@Test (expected=Exception.class)
	public void testGetByte_NoAvailableBytes() throws Exception {
		
		ISerialPort serialPort = mock(ISerialPort.class);
		when(serialPort.isOpen()).thenReturn(true);
		when(serialPort.getAvailableBytesCount()).thenReturn(0);
		
		IByteProvider testObject = SerialPortByteProvider.create(serialPort);
		testObject.getByte();
	}

	@Test (expected=Exception.class)
	public void testGetByte_SerialPortNotOpen() throws Exception {
		
		ISerialPort serialPort = mock(ISerialPort.class);
		when(serialPort.isOpen()).thenReturn(false);
		
		IByteProvider testObject = SerialPortByteProvider.create(serialPort);
		testObject.getByte();
	}
	
	/**
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.utils.SerialPortByteProvider#getBytes(int)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetBytes() throws Exception {
		byte[] expectedBytes = { 0x01, 0x02, 0x03 };
		ISerialPort serialPort = mock(ISerialPort.class);
		when(serialPort.isOpen()).thenReturn(true);
		when(serialPort.getAvailableBytesCount()).thenReturn(3);
		when(serialPort.readBytes(3)).thenReturn(expectedBytes);
		
		IByteProvider testObject = SerialPortByteProvider.create(serialPort);
		byte[] found = testObject.getBytes(3);
		
		verify(serialPort).readBytes(3);
		assertEquals(expectedBytes, found);
	}

}
