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
import org.openhab.binding.plcbus.internal.protocol.*;
import org.openhab.binding.plcbus.internal.protocol.commands.*;

import static org.mockito.Mockito.*;

/**
 * @author Robin Lenz
 *
 */
public class PLCBusControllerTest {

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
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.PLCBusController#bright(org.openhab.binding.plcbus.internal.protocol.PLCUnit, int)}.
	 */
	@Test
	public void testBright() {
		int seconds = 10;

		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		PLCUnit unit = mock(PLCUnit.class);
		when(unit.getAddress()).thenReturn("A1");
		when(unit.getUsercode()).thenReturn("D1");
		ActorResponse receiveFrame = mock(ActorResponse.class);
		when(receiveFrame.isAcknowledged()).thenReturn(true);
		IPLCBusProtocol plcBusProtocol = mock(IPLCBusProtocol.class);
		when(plcBusProtocol.getSendCommandBytes(same(unit), any(Bright.class))).thenReturn(new ByteQueue());
		when(plcBusProtocol.expectedSendCommandResponseSize()).thenReturn(18);
		when(plcBusProtocol.parseResponseFromActor(any(IByteQueue.class))).thenReturn(receiveFrame);
		
		IPLCBusController controller = PLCBusController.create(gateway, plcBusProtocol);
		boolean found = controller.bright(unit, seconds);
		
		verify(gateway).sendBytesAndWaitForResponse(any(IByteQueue.class), eq(18));
		assertEquals(true, found);		
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.PLCBusController#dim(org.openhab.binding.plcbus.internal.protocol.PLCUnit, int)}.
	 */
	@Test
	public void testDim() {
		int seconds = 10;

		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		PLCUnit unit = mock(PLCUnit.class);
		when(unit.getAddress()).thenReturn("A1");
		when(unit.getUsercode()).thenReturn("D1");
		ActorResponse actorResponse = mock(ActorResponse.class);
		when(actorResponse.isAcknowledged()).thenReturn(true);
		IPLCBusProtocol plcBusProtocol = mock(IPLCBusProtocol.class);
		when(plcBusProtocol.getSendCommandBytes(same(unit), any(Dim.class))).thenReturn(new ByteQueue());
		when(plcBusProtocol.expectedSendCommandResponseSize()).thenReturn(18);
		when(plcBusProtocol.parseResponseFromActor(any(IByteQueue.class))).thenReturn(actorResponse);
		
		IPLCBusController controller = PLCBusController.create(gateway, plcBusProtocol);
		boolean found = controller.dim(unit, seconds);
		
		verify(gateway).sendBytesAndWaitForResponse(any(IByteQueue.class), eq(18));
		assertEquals(true, found);
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.PLCBusController#switchOff(org.openhab.binding.plcbus.internal.protocol.PLCUnit)}.
	 */
	@Test
	public void testSwitchOff() {

		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		PLCUnit unit = mock(PLCUnit.class);
		when(unit.getAddress()).thenReturn("A1");
		when(unit.getUsercode()).thenReturn("D1");
		ActorResponse actorResponse = mock(ActorResponse.class);
		when(actorResponse.isAcknowledged()).thenReturn(true);
		IPLCBusProtocol plcBusProtocol = mock(IPLCBusProtocol.class);
		when(plcBusProtocol.getSendCommandBytes(same(unit), any(UnitOff.class))).thenReturn(new ByteQueue());
		when(plcBusProtocol.expectedSendCommandResponseSize()).thenReturn(18);
		when(plcBusProtocol.parseResponseFromActor(any(IByteQueue.class))).thenReturn(actorResponse);
		
		IPLCBusController controller = PLCBusController.create(gateway, plcBusProtocol);
		boolean found = controller.switchOff(unit);
		
		verify(gateway).sendBytesAndWaitForResponse(any(IByteQueue.class), eq(18));
		assertEquals(true, found);
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.PLCBusController#switchOn(org.openhab.binding.plcbus.internal.protocol.PLCUnit)}.
	 */
	@Test
	public void testSwitchOn() {

		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		PLCUnit unit = mock(PLCUnit.class);
		when(unit.getAddress()).thenReturn("A1");
		when(unit.getUsercode()).thenReturn("D1");
		ActorResponse actorResponse = mock(ActorResponse.class);
		when(actorResponse.isAcknowledged()).thenReturn(true);
		IPLCBusProtocol plcBusProtocol = mock(IPLCBusProtocol.class);
		when(plcBusProtocol.getSendCommandBytes(same(unit), any(UnitOn.class))).thenReturn(new ByteQueue());
		when(plcBusProtocol.expectedSendCommandResponseSize()).thenReturn(18);
		when(plcBusProtocol.parseResponseFromActor(any(IByteQueue.class))).thenReturn(actorResponse);
		
		IPLCBusController controller = PLCBusController.create(gateway, plcBusProtocol);
		boolean found = controller.switchOn(unit);
		
		verify(gateway).sendBytesAndWaitForResponse(any(IByteQueue.class), eq(18));
		assertEquals(true, found);
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.PLCBusController#fadeStop(org.openhab.binding.plcbus.internal.protocol.PLCUnit)}.
	 */
	@Test
	public void testFadeStop() {

		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		PLCUnit unit = mock(PLCUnit.class);
		when(unit.getAddress()).thenReturn("A1");
		when(unit.getUsercode()).thenReturn("D1");
		ActorResponse actorResponse = mock(ActorResponse.class);
		when(actorResponse.isAcknowledged()).thenReturn(true);
		IPLCBusProtocol plcBusProtocol = mock(IPLCBusProtocol.class);
		when(plcBusProtocol.getSendCommandBytes(same(unit), any(FadeStop.class))).thenReturn(new ByteQueue());
		when(plcBusProtocol.expectedSendCommandResponseSize()).thenReturn(18);
		when(plcBusProtocol.parseResponseFromActor(any(IByteQueue.class))).thenReturn(actorResponse);
		
		IPLCBusController controller = PLCBusController.create(gateway, plcBusProtocol);
		boolean found = controller.fadeStop(unit);
		
		verify(gateway).sendBytesAndWaitForResponse(any(IByteQueue.class), eq(18));
		assertEquals(true, found);
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.PLCBusController#requestStatusFor(org.openhab.binding.plcbus.internal.protocol.PLCUnit)}.
	 */
	@Test
	public void testRequestStatusFor() {
		
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		PLCUnit unit = mock(PLCUnit.class);
		when(unit.getAddress()).thenReturn("A1");
		when(unit.getUsercode()).thenReturn("D1");
		ActorResponse receiveFrame = mock(ActorResponse.class);
		when(receiveFrame.isAcknowledged()).thenReturn(true);
		Command command = mock(Command.class);
		when(receiveFrame.getCommand()).thenReturn(command);
		int firstParameter = 1;
		when(receiveFrame.getFirstParameter()).thenReturn(firstParameter);
		int secondParameter = 2;
		when(receiveFrame.getSecondParameter()).thenReturn(secondParameter);
		IPLCBusProtocol plcBusProtocol = mock(IPLCBusProtocol.class);
		when(plcBusProtocol.getSendCommandBytes(same(unit), any(Bright.class))).thenReturn(new ByteQueue());
		when(plcBusProtocol.expectedSendCommandResponseSize()).thenReturn(18);
		when(plcBusProtocol.parseResponseFromActor(any(IByteQueue.class))).thenReturn(receiveFrame);
		
		IPLCBusController controller = PLCBusController.create(gateway, plcBusProtocol);
		StatusResponse found = controller.requestStatusFor(unit);
		
		verify(gateway).sendBytesAndWaitForResponse(any(IByteQueue.class), eq(18));
		assertNotNull(found);
		assertEquals(firstParameter, found.getFirstParameter());
		assertEquals(secondParameter, found.getSecondParameter());
	}
}
