package org.openhab.binding.vitotronic.internal.protocol.test;

import static org.junit.Assert.*;

import org.junit.*;
import org.openhab.binding.vitotronic.internal.config.*;
import org.openhab.binding.vitotronic.internal.protocol.*;
import org.openhab.binding.vitotronic.internal.protocol.utils.test.To;

import static org.mockito.Mockito.*;

public class XMLConfigParameterFactoryTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = RuntimeException.class)
	public void testCreateCommandForWithoutCommandForAddress() {
		int address = 0x0815;
		VitotronicConfig vitotronicConfig = mock(VitotronicConfig.class);
		VitotronicControlConfig vitotronicControlConfig = mock(VitotronicControlConfig.class);
		IValueTransformerFactory valueTransformerFactory = mock(IValueTransformerFactory.class);
		
		IParameterFactory testObject = new XMLConfigParameterFactory(vitotronicConfig, vitotronicControlConfig, valueTransformerFactory);
		testObject.createParameterFor(address);
	}	

	@Test(expected = RuntimeException.class)
	public void testCreateCommandForWithoutUnitForCommand() {
		int address = 0x0815;
		VitotronicConfig vitotronicConfig = mock(VitotronicConfig.class);
		VitotronicControlConfig vitotronicControlConfig = mock(VitotronicControlConfig.class);
		IValueTransformerFactory valueTransformerFactory = mock(IValueTransformerFactory.class);
		Command command = mock(Command.class);
		String unitShortcut = "TestUnitShortcut";
				
		when(vitotronicConfig.getCommandByAddress(address)).thenReturn(command);
		when(command.getUnitShortcut()).thenReturn(unitShortcut);
		
		IParameterFactory testObject = new XMLConfigParameterFactory(vitotronicConfig, vitotronicControlConfig, valueTransformerFactory);
		testObject.createParameterFor(address);
	}	

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateCommandForWith() {
		int address = 0x0815;
		VitotronicConfig vitotronicConfig = mock(VitotronicConfig.class);
		VitotronicControlConfig vitotronicControlConfig = mock(VitotronicControlConfig.class);
		IValueTransformerFactory valueTransformerFactory = mock(IValueTransformerFactory.class);
		Command command = mock(Command.class);
		String unitShortcut = "TestUnitShortcut";
		Unit unit = mock(Unit.class);
		IValueTransformer<String> valueTransformer = mock(IValueTransformer.class);
				
		when(vitotronicConfig.getCommandByAddress(address)).thenReturn(command);
		when(command.getUnitShortcut()).thenReturn(unitShortcut);
		when(command.getAddress()).thenReturn(address);
		when(command.getLength()).thenReturn(2);
		when(vitotronicControlConfig.getUnitByShortcut(unitShortcut)).thenReturn(unit);
		when(valueTransformerFactory.<String>createForUnit(unit)).thenReturn(valueTransformer);
		
		IParameterFactory testObject = new XMLConfigParameterFactory(vitotronicConfig, vitotronicControlConfig, valueTransformerFactory);
		IParameterWithValue<String> createdParameter = testObject.createParameterFor(address);
		
		assertNotNull(createdParameter);
		assertArrayEquals(To.ByteArray("08 15 02 00 00"), createdParameter.getByteQueue().toByteArray());
	}

}
