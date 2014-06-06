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
package org.openhab.binding.vitotronic.internal.protocol;

import java.util.*;

import org.openhab.binding.vitotronic.internal.config.*;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class XMLConfigParameterFactory implements IParameterFactory {

	private final VitotronicConfig vitotronicConfig;
	private final VitotronicControlConfig vitotronicControlConfig;
	
	private final Map<String, Class<?>> dataTypeMapping = new HashMap<String, Class<?>>() {{
		  put( "short", Double.class);
		}}; 
	
	public XMLConfigParameterFactory(VitotronicConfig vitotronicConfig, VitotronicControlConfig vitotronicControlConfig)
	{
		this.vitotronicConfig = vitotronicConfig;
		this.vitotronicControlConfig = vitotronicControlConfig;
	}
	
	
	@Override
	public <TParameterValue> IParameterWithValue<TParameterValue> createParameterFor(int address) {
		Command command = getCommandForAddress(address);
		
		return createParameterForCommand(command);
	}


	private Command getCommandForAddress(int address) {
		Command command = vitotronicConfig.getCommandByAddress(address);
		
		if (command == null)
			throw new RuntimeException(String.format("No parameter found with address: '%04X'", address));
		
		return command;
	}
	
	private <TParameterValue> IParameterWithValue<TParameterValue> createParameterForCommand(Command command) {
		
	}

}
