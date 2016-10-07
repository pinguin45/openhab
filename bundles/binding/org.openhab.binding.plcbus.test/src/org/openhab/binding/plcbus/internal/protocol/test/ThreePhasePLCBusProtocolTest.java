package org.openhab.binding.plcbus.internal.protocol.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.plcbus.internal.protocol.*;
import org.openhab.binding.plcbus.internal.protocol.commands.UnitOn;

public class ThreePhasePLCBusProtocolTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private void assertBytesEquals(String expectedByteString, byte[] foundBytes) {
		String foundByteString = Util.getByteStringFor(foundBytes);
		
		assertEquals(expectedByteString, foundByteString);
	}

	@Test
	public void testGetSendCommandBytes() {
		PLCUnit unit = new PLCUnit("D2", "A1");
		UnitOn command = new UnitOn();
		
		IPLCBusProtocol testObject = new ThreePhasePLCBusProtocol();
		IByteQueue foundBytesForSendCommand = testObject.getSendCommandBytes(unit, command);
		
		assertBytesEquals("02 05 D2 00 62 00 00 03", foundBytesForSendCommand.toByteArray());
	}

	@Test
	public void testExpectedSendCommandResponseSize() {
		IPLCBusProtocol testObject = new ThreePhasePLCBusProtocol();
		int foundSendCommandResponseSize = testObject.expectedSendCommandResponseSize();
		
		assertEquals(36, foundSendCommandResponseSize);
	}

	@Test
	public void testParseResponseFromActor() {
		String byteString = "02 06 D2 00 62 64 00 1C C1 " +
							"02 06 D2 00 62 64 00 20 BD " +
							"02 06 D2 00 22 64 00 0C 11 " +
							"02 06 D2 00 0D 64 03 0C 23";
		
		IPLCBusProtocol testObject = new ThreePhasePLCBusProtocol();
		ActorResponse responseFromActor = testObject.parseResponseFromActor(To.ByteQueue(byteString));
		
		assertNotNull(responseFromActor);
		assertEquals(true, responseFromActor.isAcknowledged());
	}
	
	@Test
	public void testParseReceiveFrameFromActorWithoutBytes() {
		String byteString = "";
		
		IPLCBusProtocol testObject = new ThreePhasePLCBusProtocol();
		ActorResponse responseFromActor = testObject.parseResponseFromActor(To.ByteQueue(byteString));
		
		assertNull(responseFromActor);
	}
}
