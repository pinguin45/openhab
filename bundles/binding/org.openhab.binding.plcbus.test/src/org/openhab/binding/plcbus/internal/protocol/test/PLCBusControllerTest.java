/**
 * 
 */
package org.openhab.binding.plcbus.internal.protocol.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.openhab.binding.plcbus.internal.protocol.Command;
import org.openhab.binding.plcbus.internal.protocol.DefaultOnePhaseReceiveFrameContainer;
import org.openhab.binding.plcbus.internal.protocol.IPLCBusController;
import org.openhab.binding.plcbus.internal.protocol.IReceiveFrameContainer;
import org.openhab.binding.plcbus.internal.protocol.IReceiveFrameContainerFactory;
import org.openhab.binding.plcbus.internal.protocol.ISerialPortGateway;
import org.openhab.binding.plcbus.internal.protocol.PLCBusController;
import org.openhab.binding.plcbus.internal.protocol.PLCUnit;
import org.openhab.binding.plcbus.internal.protocol.ReceiveFrame;
import org.openhab.binding.plcbus.internal.protocol.StatusRequestReceiveFrameContainer;
import org.openhab.binding.plcbus.internal.protocol.StatusResponse;
import org.openhab.binding.plcbus.internal.protocol.TransmitFrame;
import org.openhab.binding.plcbus.internal.protocol.commands.*;

import static org.mockito.Mockito.*;

/**
 * @author pinguin
 *
 */
public class PLCBusControllerTest {

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
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.PLCBusController#bright(org.openhab.binding.plcbus.internal.protocol.PLCUnit, int)}.
	 */
	@Test
	public void testBright() {
		int seconds = 10;

		IReceiveFrameContainerFactory factory = mock(IReceiveFrameContainerFactory.class);
		IReceiveFrameContainer container = mock(IReceiveFrameContainer.class);
		when(factory.createReceiveFrameContainerFor(eq(DefaultOnePhaseReceiveFrameContainer.class))).thenReturn(container);
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		PLCUnit unit = mock(PLCUnit.class);
		when(unit.getAddress()).thenReturn("A1");
		when(unit.getUsercode()).thenReturn("D1");
		ReceiveFrame receiveFrame = mock(ReceiveFrame.class);
		when(container.getAnswerFrame()).thenReturn(receiveFrame);
		when(receiveFrame.isAcknowledgement()).thenReturn(true);
		
		IPLCBusController controller = PLCBusController.create(gateway, factory);
		boolean found = controller.bright(unit, seconds);
		
		verify(gateway).send(argThat(new TransmitFrameMatcher(Bright.class)), same(container));
		assertEquals(true, found);		
	}
	
	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.PLCBusController#dim(org.openhab.binding.plcbus.internal.protocol.PLCUnit, int)}.
	 */
	@Test
	public void testDim() {
		int seconds = 10;

		IReceiveFrameContainerFactory factory = mock(IReceiveFrameContainerFactory.class);
		IReceiveFrameContainer container = mock(IReceiveFrameContainer.class);
		when(factory.createReceiveFrameContainerFor(eq(DefaultOnePhaseReceiveFrameContainer.class))).thenReturn(container);
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		PLCUnit unit = mock(PLCUnit.class);
		when(unit.getAddress()).thenReturn("A1");
		when(unit.getUsercode()).thenReturn("D1");
		ReceiveFrame receiveFrame = mock(ReceiveFrame.class);
		when(container.getAnswerFrame()).thenReturn(receiveFrame);
		when(receiveFrame.isAcknowledgement()).thenReturn(true);
		
		IPLCBusController controller = PLCBusController.create(gateway, factory);
		boolean found = controller.dim(unit, seconds);
		
		verify(gateway).send(argThat(new TransmitFrameMatcher(Dim.class)), same(container));
		assertEquals(true, found);
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.PLCBusController#switchOff(org.openhab.binding.plcbus.internal.protocol.PLCUnit)}.
	 */
	@Test
	public void testSwitchOff() {

		IReceiveFrameContainerFactory factory = mock(IReceiveFrameContainerFactory.class);
		IReceiveFrameContainer container = mock(IReceiveFrameContainer.class);
		when(factory.createReceiveFrameContainerFor(eq(DefaultOnePhaseReceiveFrameContainer.class))).thenReturn(container);
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		PLCUnit unit = mock(PLCUnit.class);
		when(unit.getAddress()).thenReturn("A1");
		when(unit.getUsercode()).thenReturn("D1");
		ReceiveFrame receiveFrame = mock(ReceiveFrame.class);
		when(container.getAnswerFrame()).thenReturn(receiveFrame);
		when(receiveFrame.isAcknowledgement()).thenReturn(true);
		
		IPLCBusController controller = PLCBusController.create(gateway, factory);
		boolean found = controller.switchOff(unit);
		
		verify(gateway).send(argThat(new TransmitFrameMatcher(UnitOff.class)), same(container));
		assertEquals(true, found);
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.PLCBusController#switchOn(org.openhab.binding.plcbus.internal.protocol.PLCUnit)}.
	 */
	@Test
	public void testSwitchOn() {

		IReceiveFrameContainerFactory factory = mock(IReceiveFrameContainerFactory.class);
		IReceiveFrameContainer container = mock(IReceiveFrameContainer.class);
		when(factory.createReceiveFrameContainerFor(eq(DefaultOnePhaseReceiveFrameContainer.class))).thenReturn(container);
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		PLCUnit unit = mock(PLCUnit.class);
		when(unit.getAddress()).thenReturn("A1");
		when(unit.getUsercode()).thenReturn("D1");
		ReceiveFrame receiveFrame = mock(ReceiveFrame.class);
		when(container.getAnswerFrame()).thenReturn(receiveFrame);
		when(receiveFrame.isAcknowledgement()).thenReturn(true);
		
		IPLCBusController controller = PLCBusController.create(gateway, factory);
		boolean found = controller.switchOn(unit);
		
		verify(gateway).send(argThat(new TransmitFrameMatcher(UnitOn.class)), same(container));
		assertEquals(true, found);
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.PLCBusController#fadeStop(org.openhab.binding.plcbus.internal.protocol.PLCUnit)}.
	 */
	@Test
	public void testFadeStop() {

		IReceiveFrameContainerFactory factory = mock(IReceiveFrameContainerFactory.class);
		IReceiveFrameContainer container = mock(IReceiveFrameContainer.class);
		when(factory.createReceiveFrameContainerFor(eq(DefaultOnePhaseReceiveFrameContainer.class))).thenReturn(container);
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		PLCUnit unit = mock(PLCUnit.class);
		when(unit.getAddress()).thenReturn("A1");
		when(unit.getUsercode()).thenReturn("D1");
		ReceiveFrame receiveFrame = mock(ReceiveFrame.class);
		when(container.getAnswerFrame()).thenReturn(receiveFrame);
		when(receiveFrame.isAcknowledgement()).thenReturn(true);
		
		IPLCBusController controller = PLCBusController.create(gateway, factory);
		boolean found = controller.fadeStop(unit);
		
		verify(gateway).send(argThat(new TransmitFrameMatcher(FadeStop.class)), same(container));
		assertEquals(true, found);
	}

	/**
	 * Test method for {@link org.openhab.binding.plcbus.internal.protocol.PLCBusController#requestStatusFor(org.openhab.binding.plcbus.internal.protocol.PLCUnit)}.
	 */
	@Test
	public void testRequestStatusFor() {
		
		IReceiveFrameContainerFactory factory = mock(IReceiveFrameContainerFactory.class);
		IReceiveFrameContainer container = mock(IReceiveFrameContainer.class);
		when(factory.createReceiveFrameContainerFor(eq(StatusRequestReceiveFrameContainer.class))).thenReturn(container);
		ISerialPortGateway gateway = mock(ISerialPortGateway.class);
		PLCUnit unit = mock(PLCUnit.class);
		when(unit.getAddress()).thenReturn("A1");
		when(unit.getUsercode()).thenReturn("D1");
		ReceiveFrame receiveFrame = mock(ReceiveFrame.class);
		when(container.getAnswerFrame()).thenReturn(receiveFrame);
		when(receiveFrame.isAcknowledgement()).thenReturn(true);
		Command command = mock(Command.class);
		when(receiveFrame.getCommand()).thenReturn(command);
		int firstParameter = 1;
		when(receiveFrame.getFirstParameter()).thenReturn(firstParameter);
		int secondParameter = 2;
		when(receiveFrame.getSecondParameter()).thenReturn(secondParameter);
		
		IPLCBusController controller = PLCBusController.create(gateway, factory);
		StatusResponse found = controller.requestStatusFor(unit);
		
		verify(gateway).send(argThat(new TransmitFrameMatcher(StatusRequest.class)), same(container));
		assertNotNull(found);
		assertEquals(firstParameter, found.getFirstParameter());
		assertEquals(secondParameter, found.getSecondParameter());
	}
	
	private class TransmitFrameMatcher extends ArgumentMatcher<TransmitFrame>
	{
		private Class<?> classOfCommand;
		
		/**
		 * Constructor
		 * @param classOfCommand
		 */
		public TransmitFrameMatcher(Class<?> classOfCommand) {
			this.classOfCommand = classOfCommand;
		}
		
		@Override
		public boolean matches(Object argument) {
			TransmitFrame frame = (TransmitFrame)argument;
			
			Command command =  frame.getCommand();
			
			if (command == null)
			{
				return false;
			}
			
			return command.getClass() == classOfCommand;
		}		
	}
}
