package org.openhab.binding.vitotronic.internal.protocol.P300.test;

import static org.junit.Assert.*;

import org.junit.*;
import org.openhab.binding.vitotronic.internal.protocol.*;
import org.openhab.binding.vitotronic.internal.protocol.P300.*;
import org.openhab.binding.vitotronic.internal.protocol.utils.*;
import org.openhab.binding.vitotronic.internal.protocol.utils.test.To;

import static org.mockito.Mockito.*;

public class P300VitotronicProtocolTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetByteQueueForInit() {
		IVitotronicProtocol testObject = new P300VitotronicProtocol(unconfiguredParameterFactory());
		byte[] foundBytesForInit = testObject.getByteQueueForInit().toByteArray();
		
		assertBytesEquals("16 00 00", foundBytesForInit);
	}

	@Test
	public void testGetByteQueueForReset() {
		IVitotronicProtocol testObject = new P300VitotronicProtocol(unconfiguredParameterFactory());
		byte[] foundBytesForReset = testObject.getByteQueueForReset().toByteArray();
		
		assertBytesEquals("04", foundBytesForReset);
	}

	@Test
	public void testGetByteQueueForReadingParameter() {
		IParameter parameter = configureParameterForReading();
		
		IVitotronicProtocol testObject = new P300VitotronicProtocol(unconfiguredParameterFactory());
		byte[] foundBytesForReadingParameter = testObject.getByteQueueForReadingParameter(parameter).toByteArray();
		
		assertBytesEquals("41 05 00 01 00 F8 02 00", foundBytesForReadingParameter);
		verify(parameter, times(1)).getByteQueueForRequest();
	}

	private void assertBytesEquals(String expectedByteString, byte[] foundBytes) {
		String foundByteString = Util.getByteStringFor(foundBytes);
		
		assertEquals(expectedByteString, foundByteString);
	}

	@Test
	public void testGetByteQueueForWritingParameter() {
		IParameter parameter = configureParameterForWriting();
		
		IVitotronicProtocol testObject = new P300VitotronicProtocol(unconfiguredParameterFactory());
		byte[] foundBytesForWritingParameter = testObject.getByteQueueForWritingParameter(parameter).toByteArray();
		
		assertBytesEquals("41 07 00 02 00 F8 02 20 CB EE", foundBytesForWritingParameter);
		verify(parameter, times(1)).getByteQueueForWriting();
	}
	
	private IParameter configureParameterForReading() {
		IParameter parameterMock = mock(IParameter.class);
		
		when(parameterMock.getByteQueueForRequest()).thenReturn(toByteQueue("00 F8 02"));
		when(parameterMock.getAddressSize()).thenReturn(2);
		when(parameterMock.getDataSize()).thenReturn(2);
		
		return parameterMock;
	}

	private IParameter configureParameterForWriting() {
		IParameter parameterMock = mock(IParameter.class);
		
		when(parameterMock.getByteQueueForWriting()).thenReturn(toByteQueue("00 F8 02 20 CB"));
		when(parameterMock.getAddressSize()).thenReturn(2);
		when(parameterMock.getDataSize()).thenReturn(2);
		
		return parameterMock;
	}
	
	@Test
	public void testExpectedInitResponseSize() {
		IVitotronicProtocol testObject = new P300VitotronicProtocol(unconfiguredParameterFactory());
		
		assertEquals(1, testObject.expectedInitResponseSize());
	}
	
	@Test
	public void testExpectedResetResponseSize() {
		IVitotronicProtocol testObject = new P300VitotronicProtocol(unconfiguredParameterFactory());
		
		assertEquals(1, testObject.expectedResetResponseSize());
	}
	
	@Test
	public void testExpectedReadingParameterResponseSize() {
		IParameter parameter = configureParameterForReading();
		
		IVitotronicProtocol testObject = new P300VitotronicProtocol(unconfiguredParameterFactory());
		
		assertEquals(11, testObject.expectedReadingParameterResponseSize(parameter));
		verify(parameter).getAddressSize();
		verify(parameter).getDataSize();
	}
	
	@Test
	public void testExpectedWritingParameterResponseSize() {
		IParameter parameter = configureParameterForWriting();
		
		IVitotronicProtocol testObject = new P300VitotronicProtocol(unconfiguredParameterFactory());
		
		assertEquals(11, testObject.expectedWritingParameterResponseSize(parameter));
		verify(parameter).getAddressSize();
		verify(parameter).getDataSize();
	}
	
	@Test
	public void testIsInitialized() {
		IVitotronicProtocol testObject = new P300VitotronicProtocol(unconfiguredParameterFactory());
		
		assertTrue(testObject.isInitialized(toByteQueue("06")));
		assertFalse(testObject.isInitialized(toByteQueue("05")));
		assertFalse(testObject.isInitialized(toByteQueue("")));
	}
	
	@Test
	public void testIsReseted() {
		IVitotronicProtocol testObject = new P300VitotronicProtocol(unconfiguredParameterFactory());
		
		assertTrue(testObject.isReseted(toByteQueue("06 05")));
		assertFalse(testObject.isReseted(toByteQueue("06 06")));
		assertFalse(testObject.isReseted(toByteQueue("06")));
		assertFalse(testObject.isReseted(toByteQueue("")));
	}
	
	private IParameterFactory unconfiguredParameterFactory() {
		return mock(IParameterFactory.class);
	}

	@Test
	public void testParseReadParameterResponse() {
		IVitotronicProtocol testObject = new P300VitotronicProtocol(configuredParameterFactory());
		IByteQueue bytesToParse = toByteQueue("06 41 07 01 01 00 F8 02 20 CB EE");
		IParameter parsedParameter = testObject.parseReadParameterResponse(bytesToParse);
		
		assertNotNull(parsedParameter);
		verify(parsedParameter).parseDataBytes(toByteArray("20 CB"));
	}

	@Test
	public void testParseWriteParameterResponse() {
		IVitotronicProtocol testObject = new P300VitotronicProtocol(configuredParameterFactory());
		IByteQueue bytesToParse = toByteQueue("06 41 07 01 02 00 F8 02 20 CB EF");
		IParameter parsedParameter = testObject.parseWriteParameterResponse(bytesToParse);
		
		assertNotNull(parsedParameter);
		verify(parsedParameter).parseDataBytes(toByteArray("20 CB"));
	}

	private byte[] toByteArray(String hexString) {
		return To.ByteArray(hexString);
	}
	
	@SuppressWarnings("unchecked")
	private IParameterFactory configuredParameterFactory() {
		IParameterWithValue<String> parameter = mock(IParameterWithValue.class);
		IParameterFactory parameterFactory = mock(IParameterFactory.class);
		
		when(parameterFactory.<String>createParameterFor(0x00F8)).thenReturn(parameter);
		when(parameter.getDataSize()).thenReturn(2);
		
		return parameterFactory;
	}

	private IByteQueue toByteQueue(String hexString) {
		 return To.ByteQueue(hexString);
	}
}
