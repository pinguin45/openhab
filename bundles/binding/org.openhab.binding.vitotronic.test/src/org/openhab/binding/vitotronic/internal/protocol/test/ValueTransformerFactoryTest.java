/**
 * 
 */
package org.openhab.binding.vitotronic.internal.protocol.test;

import static org.junit.Assert.*;

import org.junit.*;
import org.openhab.binding.vitotronic.internal.config.Unit;
import org.openhab.binding.vitotronic.internal.protocol.*;

import static org.mockito.Mockito.*;


/**
 * @author pinguin
 *
 */
public class ValueTransformerFactoryTest {

	@Test
	public void testCreateForUnitWithoutUnit() {
		IValueTransformerFactory testObject = new ValueTransformerFactory();
		IValueTransformer<String> foundValueTransformer = testObject.createForUnit(null);
		
		assertNull(foundValueTransformer);
	}
	
	@Test
	public void testCreateForUnitWithTemperatureUnit() {
		Unit unit = mock(Unit.class);
		
		when (unit.getShortcut()).thenReturn("UT");
		
		IValueTransformerFactory testObject = new ValueTransformerFactory();
		IValueTransformer<String> foundValueTransformer = testObject.createForUnit(unit);
		
		assertNotNull(foundValueTransformer);
		assertEquals(TemperatureTransformer.class, foundValueTransformer.getClass());
	}

}
