/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
