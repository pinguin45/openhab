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
import org.openhab.binding.vitotronic.internal.protocol.Telegram;
import org.openhab.binding.vitotronic.internal.protocol.VitotronicParameterProcessor;
import org.openhab.binding.vitotronic.internal.protocol.parameters.DeviceIdentitynumber;
import org.openhab.binding.vitotronic.internal.protocol.utils.IByteProvider;

import static org.mockito.Mockito.*;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class VitotronicParameterProcessorTest {

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
	public void testProcess_DeviceIdentitynumber() throws Exception {
		byte[] ack = { 0x06 };
		byte[] startByte = { Telegram.START_BYTE };
		byte[] length = { 0x07 };
		byte[] number = { 0x01, 0x01, 0x00, (byte) 0xF8, 0x02, 0x20, (byte) 0xB8, (byte) 0xDB};
		
		IByteProvider provider = mock(IByteProvider.class);
		when(provider.getBytes(1)).thenReturn(ack, startByte, length);
		when(provider.getBytes(8)).thenReturn(number);
		
		VitotronicParameterProcessor<DeviceIdentitynumber> testObject =  new VitotronicParameterProcessor<DeviceIdentitynumber>();
		testObject.process(provider);
		
		verify(provider, times(3)).getBytes(1);
		verify(provider).getBytes(8);
		assertEquals("20b8", testObject.getParameter().getIdentitynumber());
	}


	@Test (expected = Exception.class)
	public void testProcess_WithoutWorkdata() throws Exception {
		byte[] ack = { 0x06 };
		byte[] startByte = { Telegram.START_BYTE };
		byte[] length = { 0x07 };
		byte[] number = { };
		
		IByteProvider provider = mock(IByteProvider.class);
		when(provider.getBytes(1)).thenReturn(ack, startByte, length);
		when(provider.getBytes(8)).thenReturn(number);
		
		VitotronicParameterProcessor<DeviceIdentitynumber> testObject =  new VitotronicParameterProcessor<DeviceIdentitynumber>();
		try {
			testObject.process(provider);
		} catch (Exception e) {			
			verify(provider, times(3)).getBytes(1);
			verify(provider).getBytes(8);
			assertEquals("Length of wordata is not as expected", e.getMessage());
			throw e;
		}
	}
	
	@Test (expected = Exception.class)
	public void testProcess_WithWorkdataLengthZero() throws Exception {
		byte[] ack = { 0x06 };
		byte[] startByte = { Telegram.START_BYTE };
		byte[] length = { 0x00 };
		
		IByteProvider provider = mock(IByteProvider.class);
		when(provider.getBytes(1)).thenReturn(ack, startByte, length);
		
		VitotronicParameterProcessor<DeviceIdentitynumber> testObject =  new VitotronicParameterProcessor<DeviceIdentitynumber>();
		
		try {
			testObject.process(provider);
		} catch (Exception e) {			
			verify(provider, times(3)).getBytes(1);
			assertEquals("Length of workdata is zero", e.getMessage());
			throw e;
		}
	}
	
}
