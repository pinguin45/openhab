/**
 * 
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
import org.openhab.binding.plcbus.internal.protocol.commands.UnitOn;

/**
 * @author pinguin
 *
 */
public class CommandFrameTest {

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
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.CommandFrame#setExtendAddressTo(boolean)}.
	 */
	@Test
	public void testSetExtendAddressTo() {
		CommandFrame testObject = new CommandFrame();
		testObject.setExtendAddressTo(true);
		
		assertTrue(testObject.shouldExtendAddress());
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.CommandFrame#setThreePhaseTo(boolean)}.
	 */
	@Test
	public void testSetThreePhaseTo() {
		CommandFrame testObject = new CommandFrame();
		testObject.setThreePhaseTo(true);
		
		assertTrue(testObject.isThreePhase());
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.CommandFrame#setDemandAckTo(boolean)}.
	 */
	@Test
	public void testSetDemandAckTo() {
		CommandFrame testObject = new CommandFrame();
		testObject.setDemandAckTo(true);
		
		assertTrue(testObject.shouldDemandAck());
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.CommandFrame#getBytes()}.
	 */
	@Test
	public void testGetBytes() {
		List<Byte> expectedBytes = new ArrayList<Byte>();
		expectedBytes.add((byte) 0xe2);
		Command command = mock(Command.class);
		when(command.getId()).thenReturn((byte) 0x02);
		when(command.getDataBytes()).thenReturn(new ArrayList<Byte>());
		
		CommandFrame testObject = new CommandFrame(command);
		testObject.setDemandAckTo(true);
		testObject.setExtendAddressTo(true);
		testObject.setThreePhaseTo(true);
		List<Byte> found = testObject.getBytes();
		
		assertArrayEquals(Convert.toByteArray(expectedBytes), Convert.toByteArray(found));
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.CommandFrame#getFirstParameter()}.
	 */
	@Test
	public void testGetFirstParameter() {
		byte expectedByte = 0x11;
		Command command = mock(Command.class);
		when(command.getData1()).thenReturn(expectedByte);
		
		CommandFrame testObject = new CommandFrame(command);
		byte found = (byte) testObject.getFirstParameter();
		
		verify(command).getData1();
		assertEquals(expectedByte, found);		
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.CommandFrame#getSecondParameter()}.
	 */
	@Test
	public void testGetSecondParameter() {
		byte expectedByte = 0x11;
		Command command = mock(Command.class);
		when(command.getData2()).thenReturn(expectedByte);
		
		CommandFrame testObject = new CommandFrame(command);
		byte found = (byte) testObject.getSecondParameter();
		
		verify(command).getData2();
		assertEquals(expectedByte, found);	
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.CommandFrame#parse(byte[])}.
	 */
	@Test
	public void testParse() {
		byte[] bytes = { (byte) 0xe2 };
		
		CommandFrame testObject = new CommandFrame();
		testObject.parse(bytes);
		
		assertTrue(testObject.isThreePhase());
		assertTrue(testObject.shouldDemandAck());
		assertTrue(testObject.shouldExtendAddress());
		assertNotNull(testObject.getCommand());
		assertEquals(UnitOn.class, testObject.getCommand().getClass());
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.CommandFrame#getCommand()}.
	 */
	@Test
	public void testGetCommand() {
		Command command = mock(Command.class);
		
		CommandFrame testObject = new CommandFrame(command);
		Command found = testObject.getCommand();
		
		assertSame(command, found);	
	}

}
