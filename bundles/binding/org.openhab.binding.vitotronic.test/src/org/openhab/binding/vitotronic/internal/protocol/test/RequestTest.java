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
package org.openhab.binding.vitotronic.internal.protocol.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.vitotronic.internal.protocol.Read;
import org.openhab.binding.vitotronic.internal.protocol.Request;
import org.openhab.binding.vitotronic.internal.protocol.Telegram;
import org.openhab.binding.vitotronic.internal.protocol.Write;
import org.openhab.binding.vitotronic.internal.protocol.parameters.DeviceIdentitynumber;
import org.openhab.binding.vitotronic.internal.protocol.parameters.Partybetrieb;
import org.openhab.binding.vitotronic.internal.protocol.utils.Convert;

import static org.mockito.Mockito.*;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class RequestTest {

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
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.Telegram#parse(java.util.List)}.
	 */
	@Test
	public void testParse() {
		byte[] bytes = { 0x01, 0x00, (byte) 0xF8, 0x02, 0x20, (byte) 0xB8, (byte) 0xDB};
		
		Request<DeviceIdentitynumber> testObject = new Request<DeviceIdentitynumber>();
		testObject.parse(Convert.toByteList(bytes));
		
		DeviceIdentitynumber parameter = testObject.getParameter();
		
		assertNotNull(parameter);
		assertEquals("20b8", parameter.getIdentitynumber());
	}
	
	/**
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.Telegram#parse(java.util.List)}.
	 */
	@Test
	public void testParse_WithUnknownCommand() {
		byte[] bytes = { 0x03, 0x00, (byte) 0xF8, 0x02, 0x20, (byte) 0xB8, (byte) 0xDB};
		
		Request<DeviceIdentitynumber> testObject = new Request<DeviceIdentitynumber>();
		testObject.parse(Convert.toByteList(bytes));
		
		DeviceIdentitynumber parameter = testObject.getParameter();
		
		assertNull(parameter);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.Telegram#parse(java.util.List)}.
	 */
	@Test
	public void testParse_WithUnknownAddress() {
		byte[] bytes = { 0x03, 0x00, 0x00, 0x02, 0x20, (byte) 0xB8, (byte) 0xDB};
		
		Request<DeviceIdentitynumber> testObject = new Request<DeviceIdentitynumber>();
		testObject.parse(Convert.toByteList(bytes));
		
		DeviceIdentitynumber parameter = testObject.getParameter();
		
		assertNull(parameter);
	}

	/**
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.Telegram#getBytes(java.util.List)}.
	 */
	@Test
	public void testGetBytes() {
		byte[] bytes = {  Telegram.START_BYTE, 0x05, 0x00, 0x01, 0x00, (byte)0xF8, 0x02, 0x00 };
		byte[] commandBytes = { 0x01, 0x00, (byte)0xF8, 0x02 };
		Read<DeviceIdentitynumber> command = mock(Read.class);
		when(command.getBytes()).thenReturn(Convert.toByteList(commandBytes));
		
		Request<DeviceIdentitynumber> testObject = new Request<DeviceIdentitynumber>(command);
		byte[] found = Convert.toByteArray(testObject.getBytes());
		
		assertArrayEquals(bytes, found);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.Telegram#getBytes(java.util.List)}.
	 */
	@Test
	public void testGetBytes_WithWritePartymodus() {
		byte[] bytes = {  Telegram.START_BYTE, 0x06, 0x00, 0x02, 0x23, 0x03, 0x01, 0x01, 0x30 };
		Partybetrieb parameter = new Partybetrieb();
		parameter.set(true);
		Write<Partybetrieb> command = new Write<Partybetrieb>(parameter);
		
		Request<Partybetrieb> testObject = new Request<Partybetrieb>(command);
		byte[] found = Convert.toByteArray(testObject.getBytes());
		
		assertArrayEquals(bytes, found);
	}
}
