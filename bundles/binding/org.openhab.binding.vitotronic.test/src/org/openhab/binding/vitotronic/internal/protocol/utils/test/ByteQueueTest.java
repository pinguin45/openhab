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
		byte[] bytesToEnque = new byte[] { 0x00, 0x01, 0x02, 0x03 };
		
		for (int pos = 0; pos < 4; pos++)
		{
			byteQueue.enque(bytesToEnque[pos]);
		}
		
		return bytesToEnque;
	}

	@Test
	public void testEnqueAll() {
		IByteQueue testObject = new ByteQueue();
		testObject.enqueAll(null);		
		assertFalse(testObject.hasBytesEnqued());
		
		testObject.enqueAll(new ByteQueue());		
		assertFalse(testObject.hasBytesEnqued());
		
		IByteQueue byteQueueToEnqueInTestObject = new ByteQueue();
		byte[] enquedBytes = enqueBytesIn(byteQueueToEnqueInTestObject);		
		testObject.enqueAll(byteQueueToEnqueInTestObject);		
		assertArrayEquals(enquedBytes, testObject.toByteArray());
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
