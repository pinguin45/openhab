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
package org.openhab.binding.vitotronic.internal;

import org.openhab.binding.vitotronic.internal.protocol.Parameter;
import org.openhab.core.binding.BindingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration for a VitotronicBinding
 * @author Robin Lenz
 *
 */
public class VitotronicBindingConfig implements BindingConfig {
	
	private static Logger logger = LoggerFactory.getLogger(VitotronicBinding.class);
	private String parameterNamespace = "org.openhab.binding.vitotronic.internal.protocol.parameters.";
	private CommandTyp command;
	private Parameter parameter;
	private String itemName;

	/**
	 * Konstruktor
	 * @param config
	 */
	public VitotronicBindingConfig(String itemName, String config) {
		this.itemName = itemName;
		parse(config);
	}
	
	public CommandTyp getCommand() {
		return command;
	}
	
	public Parameter getParameter() {
		return parameter;
	}
	
	public String getItemName() {
		return itemName;
	}
		
	private void parse(String config) {
		String[] parts = config.split(":");
		
		if (parts.length >= 1)
		{
			 if (parts[0].equals("write")) 
			 {
				 command = CommandTyp.WRITE;
			 }
			 else
			 {
				 command = CommandTyp.READ;
			 }
			
			 if (parts.length >= 2)
			 {
				 parameter = createParameterBy(parts[1]);
			 }			
		}
	}
	
	private Parameter createParameterBy(String name) {
		try {
			Class<?> parameterType = Class.forName(parameterNamespace + name);
									
			return (Parameter) parameterType.getConstructor(null).newInstance(null);
			
		} catch (Exception e) {
			logger.error("Kein Parameter mit dem Namen '%s'", name);
		}
		
		return null;
	}

	/**
	 * Enum für den Parameter typ
	 * @author Robin Lenz
	 * @since 1.0.0
	 */
	public enum CommandTyp
	{
		READ,
		WRITE;
	}	
}