/**
 * 
 */
package org.openhab.binding.plcbus.internal.protocol.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.plcbus.internal.protocol.*;
import org.openhab.binding.plcbus.internal.protocol.commands.*;

/**
 * @author pinguin
 *
 */
public class OnePhasePLCBusProtocolTest {

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
	
	private void assertBytesEquals(String expectedByteString, byte[] foundBytes) {
		String foundByteString = Util.getByteStringFor(foundBytes);
		
		assertEquals(expectedByteString, foundByteString);
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.OnePhasePLCBusProtocol#getSendCommandBytes(org.openhab.binding.plcbus.internal.protocol.PLCUnit, org.openhab.binding.plcbus.internal.protocol.Command)}.
	 */
	@Test
	public void testGetSendCommandBytes() {
		PLCUnit unit = new PLCUnit("D2", "A1");
		UnitOn command = new UnitOn();
		
		IPLCBusProtocol testObject = new OnePhasePLCBusProtocol();
		IByteQueue foundBytesForSendCommand = testObject.getSendCommandBytes(unit, command);
		
		assertBytesEquals("02 05 D2 00 22 00 00 03", foundBytesForSendCommand.toByteArray());
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.OnePhasePLCBusProtocol#expectedSendCommandResponseSize()}.
	 */
	@Test
	public void testExpectedSendCommandResponseSize() {
		IPLCBusProtocol testObject = new OnePhasePLCBusProtocol();
		int foundSendCommandResponseSize = testObject.expectedSendCommandResponseSize();
		
		assertEquals(18, foundSendCommandResponseSize);
	}

	@Test
	public void testParseResponseFromActor() {
		String byteString = "02 06 D2 00 22 64 00 1C 01 " +
							"02 06 D2 00 22 64 00 20 FD";
		
		IPLCBusProtocol testObject = new OnePhasePLCBusProtocol();
		ActorResponse responseFromActor = testObject.parseResponseFromActor(To.ByteQueue(byteString));
		
		assertNotNull(responseFromActor);
		assertEquals(true, responseFromActor.isAcknowledged());
	}
	
	@Test
	public void testParseResponseFromActorWithoutBytes() {
		String byteString = "";
		
		IPLCBusProtocol testObject = new OnePhasePLCBusProtocol();
		ActorResponse responseFromActor = testObject.parseResponseFromActor(To.ByteQueue(byteString));
		
		assertNull(responseFromActor);
	}

}
