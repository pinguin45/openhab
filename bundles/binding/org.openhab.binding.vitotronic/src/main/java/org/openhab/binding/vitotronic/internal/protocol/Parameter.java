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
import org.openhab.binding.vitotronic.internal.protocol.utils.Convert;
import org.openhab.binding.vitotronic.internal.protocol.utils.Util;

/**
 * Baseclass for Parameters
 * @author Robin Lenz
 *
 */
public abstract class Parameter implements IParameter {
	
	private List<Byte> values;
	
	/**
	 * Constructor
	 */
	public Parameter() {
		values = new ArrayList<Byte>();
	}
	
	/**
	 * @return this bytes of parameter
	 */
	public List<Byte> getBytes() {
		List<Byte> result = new ArrayList<Byte>();
		
		result.add(Convert.toHighByte(getAddress()));
		result.add(Convert.toByte(getAddress()));
		result.add(getExpectedValueSize());
		
		if (values.size() > 0)
		{
			result.addAll(values);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param bytes
	 */
	public void parse(List<Byte> bytes) {
		
		int valueSize = bytes.get(0);
				
		if (valueSize > 0)
		{
			values.addAll(bytes.subList(1, valueSize + 1));
		}
	}
	
	/**
	 * Inserts the values at the index position
	 * @param index to put values
	 * @param value to insert
	 */
	protected void insertValueAt(int index, byte value) {
				
		if (index < values.size())
		{
			values.set(index, value);
		}
		else
		{
			values.clear();
			values.add(index, value);
		}		
	}
	
	protected byte getValueFrom(int index) {
		return values.get(index);
	}
	
	protected void setValueTo(int index, byte value) {
		
		insertValueAt(index, value);
	}
	
	protected int getIntegerValueFrom(int index) {
		String countAsString = String.format("%02x", getValueFrom(index));
		
		return Integer.parseInt(countAsString, 16);
	}
	
	/**
	 * @return the address of Parameter
	 */
	protected int getAddress() {
		return AddressFactory.getAddressFor(this.getClass());
	}
	
	/**
	 * @return the integer of 4 bytes
	 */
	protected int getInteger() {
		String countAsString = String.format("%02x%02x%02x%02x", getValueFrom(3), getValueFrom(2), getValueFrom(1), getValueFrom(0));
		
		return Integer.parseInt(countAsString, 16);
	}
	
	/**
	 * @return the integer of 2 bytes
	 */
	protected int getShortInteger() {
		String countAsString = String.format("%02x%02x", getValueFrom(1), getValueFrom(0));
		
		return Integer.parseInt(countAsString, 16);
	}
	
	/**
	 * @return the integer of a byte
	 */
	protected int getByteInteger() {
		String countAsString = String.format("%02x", getValueFrom(0));
		
		return Integer.parseInt(countAsString, 16);
	}
	
	/**
	 * @return expected length of values 
	 */
	protected abstract byte getExpectedValueSize();
	
	/**
	 * @return the value as String
	 */
	public String getValueString() {
		String result = "";
		
		int expectedSize = getExpectedValueSize();
		
		for (int pos = expectedSize - 1; pos >=0; pos--)
		{
			result = result + String.format("%02x", getValueFrom(pos));
		}
		
		return result;
	}
}
