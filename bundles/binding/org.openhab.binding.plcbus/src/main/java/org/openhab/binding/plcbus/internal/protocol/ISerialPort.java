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
package org.openhab.binding.plcbus.internal.protocol;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public interface ISerialPort {
	/**
	 * Opens the SerialPort
	 * @throws SerialPortException
	 */ 
	void open() throws SerialPortException;
	
	/**
	 * Setup the SerialPort
	 * @param baudrate
	 * @param databits
	 * @param stopbits
	 * @param parity
	 * @throws SerialPortException 
	 */
	void setParameter(int baudrate, int databits, int stopbits, int parity) throws SerialPortException;
	
	/**
	 * True, if SerialPort is open.
	 * @return
	 */
	boolean isOpen();
	
	/**
	 * Reads all available Bytes from SerialPort
	 * @return All available Bytes
	 */
	byte[] readBytes() throws SerialPortException;
	
	/**
	 * Reads count - available Bytes from SerialPort
	 * @param count
	 * @return
	 * @throws SerialPortException
	 */
	byte[] readBytes(int count) throws SerialPortException;
	
	/**
	 * Returns the count of available Bytes
	 * @return
	 * @throws SerialPortException 
	 */
	int getAvailableBytesCount() throws SerialPortException;
	
	/**
	 * Writes the bytes to SerailPort
	 * @param bytes
	 * @return
	 * @throws SerialPortException
	 */
	boolean writeBytes(byte[] bytes) throws SerialPortException;
	
	/**
	 * Closes the SerialPort
	 * @return 
	 * @throws SerialPortException
	 */
	boolean close() throws SerialPortException;
}
