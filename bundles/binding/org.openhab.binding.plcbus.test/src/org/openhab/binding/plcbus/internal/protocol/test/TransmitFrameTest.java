/**
 * 
 */
package org.openhab.binding.plcbus.internal.protocol.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.plcbus.internal.protocol.Command;
import org.openhab.binding.plcbus.internal.protocol.CommandFrame;
import org.openhab.binding.plcbus.internal.protocol.Convert;
import org.openhab.binding.plcbus.internal.protocol.DataFrame;
import org.openhab.binding.plcbus.internal.protocol.Frame;
import org.openhab.binding.plcbus.internal.protocol.TransmitFrame;

import static org.mockito.Mockito.*;

/**
 * @author pinguin
 *
 */
public class TransmitFrameTest {

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
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.Frame#getStartByte()}.
	 */
	@Test
	public void testGetStartByte() {
		DataFrame dataFrame = mock(DataFrame.class);
		
		TransmitFrame frame = new TransmitFrame(dataFrame);
		byte found = frame.getStartByte();
		
		assertEquals(0x02, found);
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.Frame#getLength()}.
	 */
	@Test
	public void testGetLength() {
		List<Byte> expectedBytes = new ArrayList<Byte>();
		expectedBytes.add((byte) 0x01);
		expectedBytes.add((byte) 0x02);
		
		DataFrame dataFrame = mock(DataFrame.class);
		when(dataFrame.getBytes()).thenReturn(expectedBytes);
		
		TransmitFrame frame = new TransmitFrame(dataFrame);
		int found = frame.getLength();
		
		verify(dataFrame).getBytes();
		assertEquals(2, found);		
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.Frame#getData()}.
	 */
	@Test
	public void testGetData() {		
		DataFrame dataFrame = mock(DataFrame.class);
		
		TransmitFrame frame = new TransmitFrame(dataFrame);
		DataFrame found = frame.getData();
		
		assertSame(dataFrame, found);
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.Frame#getBytes()}.
	 */
	@Test
	public void testGetBytes() {
		List<Byte> dataBytes = new ArrayList<Byte>();
		dataBytes.add((byte) 0x01);
		dataBytes.add((byte) 0x02);
		
		List<Byte> expectedBytes = new ArrayList<Byte>();
		expectedBytes.add((byte) 0x02);
		expectedBytes.add((byte) 0x02);
		expectedBytes.add((byte) 0x01);
		expectedBytes.add((byte) 0x02);
		expectedBytes.add((byte) 0x03);
		
		DataFrame dataFrame = mock(DataFrame.class);
		when(dataFrame.getBytes()).thenReturn(dataBytes);
		
		TransmitFrame frame = new TransmitFrame(dataFrame);
		List<Byte> found = frame.getBytes();
		
		verify(dataFrame, times(2)).getBytes();
		assertArrayEquals(Convert.toByteArray(expectedBytes), Convert.toByteArray(found));
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.Frame#getCommand()}.
	 */
	@Test
	public void testGetCommand() {		
		Command command = mock(Command.class);
		DataFrame dataFrame = mock(DataFrame.class);
		when(dataFrame.getCommand()).thenReturn(command);
		
		TransmitFrame frame = new TransmitFrame(dataFrame);
		Command found = frame.getCommand();
		
		assertSame(command, found);
	}

}
