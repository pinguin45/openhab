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

import java.util.ArrayList;
import java.util.List;

import jssc.SerialPort;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.vitotronic.internal.protocol.utils.Convert;
import org.openhab.binding.vitotronic.internal.protocol.utils.IByteProtocolFrame;
import org.openhab.binding.vitotronic.internal.protocol.utils.IByteProvider;
import org.openhab.binding.vitotronic.internal.protocol.utils.IByteQueue;
import org.openhab.binding.vitotronic.internal.protocol.utils.IReceiveByteProcessor;
import org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort;
import org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPortGateway;
import org.openhab.binding.vitotronic.internal.protocol.utils.SerialPortByteProvider;
import org.openhab.binding.vitotronic.internal.protocol.utils.SerialPortGateway;

import static org.mockito.Mockito.*;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class SerialPortGatwayTest {

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
	public void testSend_WithSerialPortConnected() throws Exception {
		IByteQueue bytes = null;
		IByteProtocolFrame frame = mock(IByteProtocolFrame.class);
		IReceiveByteProcessor processor = mock(IReceiveByteProcessor.class);
		ISerialPort serialPort = mock(ISerialPort.class);
		
		when(serialPort.isOpen()).thenReturn(true);
		when(frame.getByteQueue()).thenReturn(bytes);
		when(serialPort.writeBytes(bytes.toByteArray())).thenReturn(true);
		
		ISerialPortGateway testObject = SerialPortGateway.create(serialPort);
		testObject.send(frame, processor);
		
		verify(serialPort).readBytes();
		verify(serialPort).writeBytes(isA(byte[].class));
		verify(processor).process(isA(SerialPortByteProvider.class));
	}


	@Test
	public void testSend_WithSerialPortNotConnected() throws Exception {
		IByteQueue byteQueue = null;
		IByteProtocolFrame frame = mock(IByteProtocolFrame.class);
		IReceiveByteProcessor processor = mock(IReceiveByteProcessor.class);
		ISerialPort serialPort = mock(ISerialPort.class);
	
		when(serialPort.isOpen()).thenReturn(false);
		when(frame.getByteQueue()).thenReturn(byteQueue);	
		when(serialPort.writeBytes(byteQueue.toByteArray())).thenReturn(true);	
	
		ISerialPortGateway testObject = SerialPortGateway.create(serialPort);
		testObject.send(frame, processor);
	
		verify(serialPort).open();
		verify(serialPort).setParameter(eq(4800), eq(8), eq(2), eq(2));
		verify(serialPort).readBytes();
		verify(serialPort).writeBytes(isA(byte[].class));
		verify(processor).process(isA(SerialPortByteProvider.class));
	}
}
