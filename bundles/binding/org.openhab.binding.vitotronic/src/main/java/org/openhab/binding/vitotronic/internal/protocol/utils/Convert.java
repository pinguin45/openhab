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
package org.openhab.binding.vitotronic.internal.protocol.utils;
import java.util.*;

/**
 * Methods for converting Datatypes
 * @author Robin Lenz
 * @since 1.0.0
 */
public class Convert {
	
	/**
	 * Converts a int value to byte
	 * @param value int to convert
	 * @return byte representation of value
	 */
	public static byte toLowByte(int value) {
		return (byte) ((value & 0xFF));
	}
	
	/**
	 * Converts a int value to byte
	 * @param value int to convert
	 * @return byte representation of value
	 */
	public static byte toHighByte(int value) {
		return (byte) ((value & 0xFF00) >> 8);
	}

	/**
	 * Converts a byte value from string
	 * @param value string to convert
	 * @param dimension of result
	 * @return byte representation of value
	 */
	public static byte toByte(String value, int dimension){ 
		return (byte)Integer.parseInt(value, dimension);		
	}
	
	public static byte[] toByteArray(int value) {
	    return new byte[] {
	            (byte) value, 
	            (byte)(value >>> 8),
	            (byte)(value >>> 16),
	            (byte)(value >>> 24)};
	}
	
	/**
	 * Creates a byte array from a list of Byte
	 * @param list of Bytes
	 * @return byte array
	 */
	public static byte[] toByteArray(List<Byte> list) {
		byte[] result = new byte[list.size()];
		
		for (int pos = 0; pos < list.size(); pos++) {
			result[pos] = list.get(pos);
		}
		
		return result;
	}
	
	/**
	 * Create a List from byte array
	 * @param array
	 * @return list of Bytes
	 */
	public static List<Byte> toByteList(byte[] array) {
		List<Byte> result = new ArrayList<Byte>();
		
		for (int pos = 0; pos < array.length; pos++) {
			result.add(pos, array[pos]);
		}
		
		return result;
	}

	public static int toInteger(byte[] bytes) {
		String valueAsHexString = buildValueAsHexString(bytes);
		
		return Integer.parseInt(valueAsHexString, 16);
	}

	private static String buildValueAsHexString(byte[] bytes) {
		String valueAsHexStringFormat = getValueAsHexStringFormatForLength(bytes.length);
		 
		return String.format(valueAsHexStringFormat, getObjectArrayOfByteArray(bytes));
	}

	private static Object[] getObjectArrayOfByteArray(byte[] bytes) {
		Object[] objectArray = new Object[bytes.length];
		
		for (int pos = 0; pos < bytes.length; pos++) {
			objectArray[bytes.length - pos - 1] = bytes[pos];
		}
		
		return objectArray;
	}

	private static String getValueAsHexStringFormatForLength(int length) {
		String valueAsHexStringFormat = "";
		
		for (int pos = 0; pos < length; pos++)
		{
			valueAsHexStringFormat += "%02x";
		}
		
		return valueAsHexStringFormat;
	}
}
