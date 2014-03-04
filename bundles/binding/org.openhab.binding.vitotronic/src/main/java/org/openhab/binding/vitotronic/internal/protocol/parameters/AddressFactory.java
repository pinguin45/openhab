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
package org.openhab.binding.vitotronic.internal.protocol.parameters;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.vitotronic.internal.protocol.Parameter;

/**
 * Factory for parameter addresses
 * @author Robin Lenz
 *
 */
public class AddressFactory {
	private static Map<Class<?>, Integer> addresses;
	private static Map<Integer, Class<?>> parameters;
	
	public static int getAddressFor(Class<?> type) {
		Map<Class<?>, Integer> dictAddresses = getAddresses();
		
		if (!dictAddresses.containsKey(type)) 
		{
			return 0x00;
		}
		
		return dictAddresses.get(type);
	}
	
	public static Parameter createParameterFor(int address) {
		Map<Integer, Class<?>> dictParameters = getParameters();
						
		if (!dictParameters.containsKey(address))
		{
			System.out.printf("No parameter found for address %x \n", address);
			return null;
		}
		
		return createParameterFor(dictParameters.get(address));
	}
	
	/**
	 * @param class1
	 * @return
	 */
	private static Parameter createParameterFor(Class<?> type) {
		try
        {			
			return (Parameter)type.getConstructor(null).newInstance(null);
        }
        catch (Exception e)
        {
            return null;
        }
	}

	private static Map<Class<?>, Integer> getAddresses() {
		if (addresses == null)
		{
			addresses = createAddresseDictionary();
		}
		
		return addresses;
	}
	
	private static Map<Integer, Class<?>> getParameters() {
		if (parameters == null) 
		{
			parameters = createParametersDictionary();
		}
		
		return parameters;
	}

	/**
	 * @return
	 */
	private static Map<Integer, Class<?>> createParametersDictionary() {
		Map<Integer, Class<?>> result = new HashMap<Integer, Class<?>>();
		
		Map<Class<?>, Integer> dictAddresses = getAddresses(); 
		
		for (Class<?> type : dictAddresses.keySet())
		{
			result.put(dictAddresses.get(type), type);
		}
		
		return result;
	}

	private static Map<Class<?>, Integer> createAddresseDictionary() {
		Map<Class<?>, Integer> result = new HashMap<Class<?>, Integer>();
		
		result.put(DeviceIdentitynumber.class, 0x00F8);
		result.put(Aussentemperatur.class, 0x5525);
		result.put(BurnerStarts.class, 0x088A);
		result.put(Betriebsstunden.class, 0x08A7);
		result.put(RaumtemperaturSoll.class, 0x2306);
		result.put(Brennerleistung.class, 0xA38F);
		result.put(Partybetrieb.class, 0x2303);
		result.put(KesseltemperaturIst.class, 0x0802);
		result.put(KesseltemperaturSoll.class, 0x555A);
		result.put(Speichertemperatur.class, 0x0804);
		result.put(InternePumpe.class, 0x7660);
		result.put(Sammelstoerung.class, 0x0847);
		result.put(ReduzierteRaumtemperaturSoll.class, 0x2307);
		result.put(Abgastemperatur.class, 0x0808);
//		result.put(RuecklaufTemperaturIst.class, 0x080A);
//		result.put(VorlauftemperaturIst.class, 0x080C);
		result.put(RuecklaufTemperaturIst.class, 0x2902);
		result.put(VorlauftemperaturIst.class, 0x2900);
		result.put(HeizkreislaufA1M1Pumpe.class, 0x7663);
		result.put(AktuelleBetriebsartA1M1.class, 0x2500);
		
		
		return result;
	}
}
