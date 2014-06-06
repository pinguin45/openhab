package org.openhab.binding.vitotronic.internal.protocol.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.vitotronic.internal.protocol.TemperatureTransformer;
import org.openhab.binding.vitotronic.internal.protocol.utils.test.To;

public class TemperatureTransformerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTransformFromBytes() {
		TemperatureTransformer testObject = new TemperatureTransformer();
		
		assertEquals(0.0, testObject.transformFromBytes(To.ByteArray("00 00")), 0.0);
		assertEquals(20.0, testObject.transformFromBytes(To.ByteArray("C8 00")), 0.0);
		assertEquals(36.0, testObject.transformFromBytes(To.ByteArray("68 01")), 0.0);
		assertEquals(40.5, testObject.transformFromBytes(To.ByteArray("95 01")), 0.0);
	}

	@Test
	public void testTransformToBytes() {
		TemperatureTransformer testObject = new TemperatureTransformer();
		
		assertArrayEquals(To.ByteArray("00 00"), testObject.transformToBytes(0.0));
		assertArrayEquals(To.ByteArray("C8 00"), testObject.transformToBytes(20.0));
		assertArrayEquals(To.ByteArray("68 01"), testObject.transformToBytes(36.0));
		assertArrayEquals(To.ByteArray("95 01"), testObject.transformToBytes(40.5));
	}
}
