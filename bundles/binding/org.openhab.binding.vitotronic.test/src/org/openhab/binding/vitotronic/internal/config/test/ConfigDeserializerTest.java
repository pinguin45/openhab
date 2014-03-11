/**
 * 
 */
package org.openhab.binding.vitotronic.internal.config.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openhab.binding.vitotronic.internal.config.ConfigDeserializer;
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
	}

}
