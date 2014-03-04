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

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.vitotronic.internal.protocol.parameters.AddressFactory;
import org.openhab.binding.vitotronic.internal.protocol.utils.Util;

/**
 * Baseclass for commands
 * @author Robin Lenz
 *
 */
public abstract class Command<TParameter extends IParameter> {
	private TParameter parameter;
	
	/**
	 * Constructor
	 */
	public Command() {
		
	}
	
	/**
	 * Constructor with parameter
	 * @param parameter
	 */
	public Command(TParameter parameter) {
		this.parameter = parameter;
	}
	
	/**
	 * @return Parameter of command
	 */
	public TParameter getParameter() {
		return parameter;
	}
	
	/*
	 * Type of command
	 */
	protected abstract byte getType();

	/**
	 * @return bytes of command
	 */
	public List<Byte> getBytes() {
		List<Byte> result = new ArrayList<Byte>();
		
		result.add(getType());
		
		if (parameter != null)
		{
			result.addAll(parameter.getBytes());
		}
		
		return result;
	}

	/**
	 * @param subList
	 */
	public void parse(List<Byte> bytes) {
		
		int address = getAddressFrom(bytes);
		
		parameter = (TParameter)AddressFactory.createParameterFor(address);
		
		if (parameter == null)
		{
			return;
		}
		
		parameter.parse(bytes.subList(2, bytes.size()));
	}

	/**
	 * @param bytes
	 * @return
	 */
	private int getAddressFrom(List<Byte> bytes) {
		
		String addressString = String.format("%02x%02x", bytes.get(0), bytes.get(1));
		
		return Integer.parseInt(addressString, 16);
	}
}
