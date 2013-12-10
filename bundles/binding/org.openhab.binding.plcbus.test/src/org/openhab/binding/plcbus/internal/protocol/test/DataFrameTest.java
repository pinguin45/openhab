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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.plcbus.internal.protocol.Command;
import org.openhab.binding.plcbus.internal.protocol.CommandFrame;
import org.openhab.binding.plcbus.internal.protocol.Convert;
import org.openhab.binding.plcbus.internal.protocol.DataFrame;


/**
 * @author Robin Lenz
 *
 */
public class DataFrameTest {

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
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#setUserCode(java.lang.String)}.
	 */
	@Test
	public void testSetUserCode() {
		CommandFrame commandFrame = mock(CommandFrame.class);
		
		DataFrame testObject = new DataFrame(commandFrame);
		testObject.setUserCode("D1");
		List<Byte> found = testObject.getBytes(); 
		
		byte[] expectedBytes = {(byte) 0xd1, 0x00 };
		assertArrayEquals(expectedBytes, Convert.toByteArray(found));
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_A1() {
		testSetAddress("A1", (byte) 0x00);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_A2() {
		testSetAddress("A2", (byte) 0x01);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_A3() {
		testSetAddress("A3", (byte) 0x02);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_A4() {
		testSetAddress("A4", (byte) 0x03);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_A5() {
		testSetAddress("A5", (byte) 0x04);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_A6() {
		testSetAddress("A6", (byte) 0x05);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_A7() {
		testSetAddress("A7", (byte) 0x06);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_A8() {
		testSetAddress("A8", (byte) 0x07);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_A9() {
		testSetAddress("A9", (byte) 0x08);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_A10() {
		testSetAddress("A10", (byte) 0x09);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_A11() {
		testSetAddress("A11", (byte) 0x0a);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_B1() {
		testSetAddress("B1", (byte) 0x10);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_B2() {
		testSetAddress("B2", (byte) 0x11);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_B3() {
		testSetAddress("B3", (byte) 0x12);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_B4() {
		testSetAddress("B4", (byte) 0x13);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_B5() {
		testSetAddress("B5", (byte) 0x14);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_B6() {
		testSetAddress("B6", (byte) 0x15);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_B7() {
		testSetAddress("B7", (byte) 0x16);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_B8() {
		testSetAddress("B8", (byte) 0x17);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_B9() {
		testSetAddress("B9", (byte) 0x18);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_B10() {
		testSetAddress("B10", (byte) 0x19);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#SetAddress(java.lang.String)}.
	 */
	@Test
	public void testSetAddress_B11() {
		testSetAddress("B11", (byte) 0x1a);
	}
	
	private void testSetAddress(String address, byte expectedAddressByte) {
		CommandFrame commandFrame = mock(CommandFrame.class);
		
		DataFrame testObject = new DataFrame(commandFrame);
		testObject.setAddress(address);
		List<Byte> found = testObject.getBytes(); 
		
		byte[] expectedBytes = {(byte) 0x00, expectedAddressByte };
		assertArrayEquals(expectedBytes, Convert.toByteArray(found));
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#getBytes()}.
	 */
	@Test
	public void testGetBytes() {
		List<Byte> commandBytes = new ArrayList<Byte>();
		commandBytes.add((byte) 0x01);
		commandBytes.add((byte) 0x02);
		CommandFrame commandFrame = mock(CommandFrame.class);
		when(commandFrame.getBytes()).thenReturn(commandBytes);
		
		DataFrame testObject = new DataFrame(commandFrame);
		testObject.setUserCode("D1");
		testObject.setAddress("B2");
		List<Byte> found = testObject.getBytes(); 
		
		List<Byte> expectedBytes = new ArrayList<Byte>();
		expectedBytes.add((byte) 0xd1);
		expectedBytes.add((byte) 0x11);
		expectedBytes.addAll(commandBytes);
		assertArrayEquals(Convert.toByteArray(expectedBytes), Convert.toByteArray(found));
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#parse(byte[])}.
	 */
	@Test
	public void testParse() {
		byte[] bytes = { (byte)0xd1, 0x10 };
		
		DataFrame testObject = new DataFrame();
		testObject.parse(bytes);
		byte[] found = Convert.toByteArray(testObject.getBytes());
		
		assertArrayEquals(bytes, found);		
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#parse(byte[])}.
	 */
	@Test
	public void testParse_WithoutBytes() {
		byte[] bytes = { };
		
		DataFrame testObject = new DataFrame();
		testObject.parse(bytes);
		byte[] found = Convert.toByteArray(testObject.getBytes());
		
		assertArrayEquals(new byte[2], found);		
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#getFirstParameter()}.
	 */
	@Test
	public void testGetFirstParameter() {
		int expectedFirstParameter = 10;
		CommandFrame commandFrame = mock(CommandFrame.class);
		when(commandFrame.getFirstParameter()).thenReturn(expectedFirstParameter);
		
		DataFrame testObject = new DataFrame(commandFrame);
		int found = testObject.getFirstParameter(); 
		
		assertEquals(expectedFirstParameter, found);
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#getSecondParameter()}.
	 */
	@Test
	public void testGetSecondParameter() {
		int expectedSecondParameter = 10;
		CommandFrame commandFrame = mock(CommandFrame.class);
		when(commandFrame.getSecondParameter()).thenReturn(expectedSecondParameter);
		
		DataFrame testObject = new DataFrame(commandFrame);
		int found = testObject.getSecondParameter(); 
		
		assertEquals(expectedSecondParameter, found);
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.DataFrame#getCommand()}.
	 */
	@Test
	public void testGetCommand() {
		Command command = mock(Command.class);
		CommandFrame commandFrame = mock(CommandFrame.class);
		when(commandFrame.getCommand()).thenReturn(command);
		
		DataFrame testObject = new DataFrame(commandFrame);
		Command found = testObject.getCommand(); 
		
		assertSame(command, found);
	}

}
