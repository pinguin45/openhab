package org.openhab.binding.vitotronic.internal.protocol.utils.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.vitotronic.internal.protocol.utils.*;

public class ByteQueueTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHasBytesEnquedAndEnque() {
		IByteQueue testObject = new ByteQueue();
		assertFalse(testObject.hasBytesEnqued());

		testObject.enque(0xFF);
		
		assertTrue(testObject.hasBytesEnqued());
	}

	@Test
	public void testDeque() {
		IByteQueue testObject = new ByteQueue();
		
		assertThatDequeThrowsNullPointerException(testObject);
		
		testObject.enque(0xFF);
				
		assertEquals((byte)0xFF, testObject.deque());
		assertFalse(testObject.hasBytesEnqued());
	}

	private void assertThatDequeThrowsNullPointerException(IByteQueue byteQueue) {
		boolean nullPointerExceptionThrown = false;
		try
		{
			byteQueue.deque();
		}
		catch (NullPointerException e)
		{
			nullPointerExceptionThrown = true;
		}
		
		if (!nullPointerExceptionThrown)
			fail("NullPointerException expected for 'receive' on emtpy ByteQueue");		
	}

	@Test
	public void testToByteArray() {
		IByteQueue testObject = new ByteQueue();
		
		assertArrayEquals(new byte[0], testObject.toByteArray());
		
		byte[] enquedBytes = enqueBytesIn(testObject);
		
		assertArrayEquals(enquedBytes, testObject.toByteArray());
	}

	private byte[] enqueBytesIn(IByteQueue byteQueue) {
		byte[] bytesToEnque = To.ByteArray("00 01 02 03");
		
		for (int pos = 0; pos < 4; pos++)
		{
			byteQueue.enque(bytesToEnque[pos]);
		}
		
		return bytesToEnque;
	}

	@Test
	public void testEnqueAll() {
		IByteQueue testObject = new ByteQueue();
		testObject.enqueAll((IByteQueue)null);		
		assertFalse(testObject.hasBytesEnqued());
		
		testObject.enqueAll((byte[])null);		
		assertFalse(testObject.hasBytesEnqued());
		
		testObject.enqueAll(new ByteQueue());		
		assertFalse(testObject.hasBytesEnqued());
		
		testObject.enqueAll(new byte[0]);		
		assertFalse(testObject.hasBytesEnqued());
		
		IByteQueue byteQueueToEnqueInTestObject = new ByteQueue();
		byte[] enquedBytes = enqueBytesIn(byteQueueToEnqueInTestObject);		
		testObject.enqueAll(byteQueueToEnqueInTestObject);		
		assertArrayEquals(enquedBytes, testObject.toByteArray());
		
		byte[] bytesToEnque = To.ByteArray("01 02 03 04");
		testObject = new ByteQueue();
		testObject.enqueAll(bytesToEnque);
		assertArrayEquals(bytesToEnque, testObject.toByteArray());
	}
	
	@Test
	public void testSize() {
		IByteQueue testObject = new ByteQueue();
		assertEquals(0, testObject.size());
		
		enqueBytesIn(testObject);
		assertEquals(4, testObject.size());
		
		testObject.deque();
		assertEquals(3, testObject.size());		
	}
}
