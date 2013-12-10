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
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsAnything;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.plcbus.internal.protocol.Convert;
import org.openhab.binding.plcbus.internal.protocol.IReceiveFrameContainer;
import org.openhab.binding.plcbus.internal.protocol.ISerialPort;
import org.openhab.binding.plcbus.internal.protocol.ISerialPortGateway;
import org.openhab.binding.plcbus.internal.protocol.SerialPortByteProvider;
import org.openhab.binding.plcbus.internal.protocol.SerialPortGateway;
import org.openhab.binding.plcbus.internal.protocol.TransmitFrame;

/**
 * @author Robin Lenz
 *
 */
public class SerialPortGatewayTest {

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
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.SerialPortGateway#send(org.openhab.binding.plcbus.internal.protocol.TransmitFrame, org.openhab.binding.plcbus.internal.protocol.IReceiveFrameContainer)}.
	 * @throws Exception 
	 */
	@Test
	public void testSend() throws Exception {
		ISerialPort serialPort = mock(ISerialPort.class);
		List<Byte> bytes = new ArrayList<Byte>();
		bytes.add((byte) 0x01);
		bytes.add((byte) 0x02);
		
		TransmitFrame transmitFrame = mock(TransmitFrame.class);
		when(transmitFrame.getBytes()).thenReturn(bytes);
		IReceiveFrameContainer receiveFrameContainer = mock(IReceiveFrameContainer.class);
		
		ISerialPortGateway testObject = SerialPortGateway.create(serialPort);
		testObject.send(transmitFrame, receiveFrameContainer);
		
		verify(serialPort).open();
		verify(serialPort).writeBytes(Convert.toByteArray(bytes));
		verify(receiveFrameContainer).process(isA(SerialPortByteProvider.class));
		verify(serialPort).close();
	}

}
