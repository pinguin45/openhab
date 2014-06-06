/**
 * 
 */
package org.openhab.binding.vitotronic.internal.config.test;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;
import org.openhab.binding.vitotronic.internal.config.*;

/**
 * @author pinguin
 *
 */
public class ConfigDeserializerTest {

	@Test
	public void testParseVitotronicConfig() {
		ConfigDeserializer<VitotronicConfig> parser = new ConfigDeserializer<VitotronicConfig>(VitotronicConfig.class);
		VitotronicConfig config = parser.parseConfig(getConfigInputStream("Configs/vito.xml"));
		
		Command command = config.getCommandByAddress(1);
		
		assertNotNull(command);
		assertEquals(command.getName(), "TestCommandName1");
		assertEquals(command.getAddress(), 1);
		assertEquals(command.getUnitShortcut(), "TestUnit1");
		assertEquals(command.getProtocolCommand(), "TestProtocolCommand1");
		assertEquals(command.getLength(), 1);
		assertEquals(command.getDescription(), "Testbeschreibung1");	
		
		command = config.getCommandByAddress(2);
		
		assertNotNull(command);
		assertEquals(command.getName(), "TestCommandName2");
		assertEquals(command.getAddress(), 2);
		assertEquals(command.getUnitShortcut(), "TestUnit2");
		assertEquals(command.getProtocolCommand(), "TestProtocolCommand2");
		assertEquals(command.getLength(), 2);
		assertEquals(command.getDescription(), "Testbeschreibung2");
	}

	private InputStream getConfigInputStream(String filename) {
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
		return stream;
	}
	
	@Test
	public void testParseVitotronicControlConfig() {
		ConfigDeserializer<VitotronicControlConfig> parser = new ConfigDeserializer<VitotronicControlConfig>(VitotronicControlConfig.class);
		VitotronicControlConfig config = parser.parseConfig(getConfigInputStream("Configs/vcontrold.xml"));
		
		Unit unit1 = config.getUnitByShortcut("TU1");
		
		assertNotNull(unit1);
		assertEquals("short", unit1.getType());
		assertEquals("TU1", unit1.getShortcut());
		assertEquals("V/10", unit1.getInputFormula());
		assertEquals("V*10", unit1.getOutputFormula());
	}
}
