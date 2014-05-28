	package org.openhab.binding.vitotronic.internal.protocol.test;
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


import static org.junit.Assert.*;

import org.junit.*;
import org.mockito.*;
import org.openhab.binding.vitotronic.internal.protocol.*;
import org.openhab.binding.vitotronic.internal.protocol.utils.*;
import static org.mockito.Mockito.*;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class VitotronicControllerTest {
	
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
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.VitotronicController#init()}.
	 */
	@Test
	public void testInit() {
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		IReceiveByteProcessorFactory factory = mock(IReceiveByteProcessorFactory.class);
		AcknowledgementProcessor processor = mock(AcknowledgementProcessor.class);
		when(factory.createAcknowledgementProcessor()).thenReturn(processor);
		when(processor.gotAcknowledgment()).thenReturn(true);
		
		IVitotronicController controller = VitotronicController.Create(gateway, factory);
		
		controller.init();
		
		verify(gateway).send(isA(Init.class),eq(processor));
	}
	
	/**
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.VitotronicController#init()}.
	 */
	@Test
	public void testInitWithRetry() {
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		IReceiveByteProcessorFactory factory = mock(IReceiveByteProcessorFactory.class);
		AcknowledgementProcessor processor = mock(AcknowledgementProcessor.class);
		when(factory.createAcknowledgementProcessor()).thenReturn(processor);
		when(processor.gotAcknowledgment()).thenReturn(false, true);
		
		IVitotronicController controller = VitotronicController.Create(gateway, factory);
		
		controller.init();
		
		verify(gateway, times(2)).send(isA(Init.class),eq(processor));
	}

	/**
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.VitotronicController#reset()}.
	 */
	@Test
	public void testReset() {
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		IReceiveByteProcessorFactory factory = mock(IReceiveByteProcessorFactory.class);
		ResetProcessor processor = mock(ResetProcessor.class);
		when(factory.createResetProcessor()).thenReturn(processor);
		
		IVitotronicController controller = VitotronicController.Create(gateway, factory);
		
		controller.reset();
		
		verify(gateway).send(isA(Reset.class),eq(processor));
	}

	/**
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.VitotronicController#readValueOf(org.openhab.binding.vitotronic.internal.protocol.parameters.Parameter)}.
	 */
	@Test
	public void testReadValueOf() {

		String expectedParameterValue = "testValue";
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		IReceiveByteProcessorFactory factory = mock(IReceiveByteProcessorFactory.class);
		VitotronicParameterProcessor<IStringParameter> processor = mock(VitotronicParameterProcessor.class);
		IStringParameter inputParameter = mock(IStringParameter.class);
		IStringParameter outputParameter = mock(IStringParameter.class);
		
		when(factory.<IStringParameter>createVitotronicParameterProcessor()).thenReturn(processor);
		when(processor.getParameter()).thenReturn(outputParameter);
		when(outputParameter.getValue()).thenReturn(expectedParameterValue);
		
		IVitotronicController controller = VitotronicController.Create(gateway, factory);		
		String found = controller.readValueOf(inputParameter);
		
		verify(gateway).send(isA(Request.class),eq(processor));
		verify(processor).getParameter();
		verify(outputParameter).getValue();
		assertEquals(expectedParameterValue, found);
	}

	/**
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.VitotronicController#readValueOf(org.openhab.binding.vitotronic.internal.protocol.parameters.Parameter)}.
	 */
	@Test
	public void testReadDecimalOf() {

		double expectedParameterValue = 22.5;;
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		IReceiveByteProcessorFactory factory = mock(IReceiveByteProcessorFactory.class);
		VitotronicParameterProcessor<IDecimalParameter> processor = mock(VitotronicParameterProcessor.class);
		IDecimalParameter inputParameter = mock(IDecimalParameter.class);
		IDecimalParameter outputParameter = mock(IDecimalParameter.class);
		
		when(factory.<IDecimalParameter>createVitotronicParameterProcessor()).thenReturn(processor);
		when(processor.getParameter()).thenReturn(outputParameter);
		when(outputParameter.getValue()).thenReturn(expectedParameterValue);
		
		IVitotronicController controller = VitotronicController.Create(gateway, factory);		
		double found = controller.readValueOf(inputParameter);
		
		verify(gateway).send(isA(Request.class),eq(processor));
		verify(processor).getParameter();
		verify(outputParameter).getValue();
		assertEquals(expectedParameterValue, found, 0);
	}
	
	/**
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.VitotronicController#readValueOf(org.openhab.binding.vitotronic.internal.protocol.parameters.Parameter)}.
	 */
	@Test
	public void testReadIntegerOf() {

		int expectedParameterValue = 22;;
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		IReceiveByteProcessorFactory factory = mock(IReceiveByteProcessorFactory.class);
		VitotronicParameterProcessor<IIntegerParameter> processor = mock(VitotronicParameterProcessor.class);
		IIntegerParameter inputParameter = mock(IIntegerParameter.class);
		IIntegerParameter outputParameter = mock(IIntegerParameter.class);
		
		when(factory.<IIntegerParameter>createVitotronicParameterProcessor()).thenReturn(processor);
		when(processor.getParameter()).thenReturn(outputParameter);
		when(outputParameter.getValue()).thenReturn(expectedParameterValue);
		
		IVitotronicController controller = VitotronicController.Create(gateway, factory);		
		int found = controller.readValueOf(inputParameter);
		
		verify(gateway).send(isA(Request.class),eq(processor));
		verify(processor).getParameter();
		verify(outputParameter).getValue();
		assertEquals(expectedParameterValue, found, 0);
	}
	/**
	 * Test method for {@link org.openhab.binding.vitotronic.internal.protocol.VitotronicController#write(org.openhab.binding.vitotronic.internal.protocol.parameters.Parameter)}.
	 */
	@Test
	public void testWrite() {
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		IReceiveByteProcessorFactory factory = mock(IReceiveByteProcessorFactory.class);
		AcknowledgementProcessor processor = mock(AcknowledgementProcessor.class);
		IWriteableParameter inputParameter = mock(IWriteableParameter.class);
		
		when(factory.createAcknowledgementProcessor()).thenReturn(processor);
		
		IVitotronicController controller = VitotronicController.Create(gateway, factory);		
		controller.write(inputParameter);
		
		verify(gateway).send(argThat(new IsWriteRequestWith(inputParameter)),eq(processor));
	}
	
	class IsWriteRequestWith extends ArgumentMatcher<Request<IWriteableParameter>> {
		
		private IWriteableParameter parameter;
		
		public IsWriteRequestWith(IWriteableParameter parameter) {
			this.parameter = parameter;
		}

		@Override
		public boolean matches(Object argument) {
			if (!(argument instanceof Request<?>))
			{
				return false;
			}
			
			Request<IWriteableParameter> request = (Request<IWriteableParameter>) argument;
			
			Command<IWriteableParameter> command = request.getCommand();
			
			if (!(command instanceof Write<?>)) 
			{
				return false;
			}
			
			return request.getParameter().equals(parameter);
		}
	}
}
