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
