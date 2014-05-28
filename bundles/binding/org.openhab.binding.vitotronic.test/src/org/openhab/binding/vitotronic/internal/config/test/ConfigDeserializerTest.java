/**
 * 
 */
package org.openhab.binding.vitotronic.internal.config.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.openhab.binding.vitotronic.internal.config.Command;
import org.openhab.binding.vitotronic.internal.config.ConfigDeserializer;
import org.openhab.binding.vitotronic.internal.config.Device;
import org.openhab.binding.vitotronic.internal.config.VitotronicConfig;

/**
 * @author pinguin
 *
 */
public class ConfigDeserializerTest {

	@Test
	public void parseConfig() {
		ConfigDeserializer parser = new ConfigDeserializer();
		VitotronicConfig config = parser.parseConfig("Configs/vito.xml");
		
		Command command = config.getCommandBy("TestCommandName1");
		
		assertNotNull(command);
		assertEquals(command.getName(), "TestCommandName1");
		assertEquals(command.getAddress(), "0001");
		assertEquals(command.getUnit(), "TestUnit1");
		assertEquals(command.getProtocolCommand(), "TestProtocolCommand1");
		assertEquals(command.getLength(), 1);
		assertEquals(command.getDescription(), "Testbeschreibung1");	
		
		command = config.getCommandBy("TestCommandName2");
		
		assertNotNull(command);
		assertEquals(command.getName(), "TestCommandName2");
		assertEquals(command.getAddress(), "0002");
		assertEquals(command.getUnit(), "TestUnit2");
		assertEquals(command.getProtocolCommand(), "TestProtocolCommand2");
		assertEquals(command.getLength(), 2);
		assertEquals(command.getDescription(), "Testbeschreibung2");
	}
}
