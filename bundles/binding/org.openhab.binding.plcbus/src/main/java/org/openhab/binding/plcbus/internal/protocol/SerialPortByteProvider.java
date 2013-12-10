/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ByteProvider from SerialPort
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class SerialPortByteProvider implements IByteProvider {

	private static Logger logger = 
		LoggerFactory.getLogger(SerialPortGateway.class);
	private ISerialPort serialPort;

	/**
	 * Constructor
	 * 
	 * @param serialPort
	 */
	private SerialPortByteProvider(ISerialPort serialPort) {
		this.serialPort = serialPort;
	}

	/**
	 * Create a new instance
	 */
	public static IByteProvider create(ISerialPort serialPort) {
		return new SerialPortByteProvider(serialPort);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.binding.vitotronic.internal.protocol.utils.IByteProvider#
	 * getByte()
	 */
	@Override
	public byte getByte() throws Exception {
		byte[] bytes =  getBytes(1);
		
		if (bytes.length != 1)
		{
			throw new Exception("Expected Bytes 1, found: " + bytes.length);
		}
		
		return bytes[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.binding.vitotronic.internal.protocol.utils.IByteProvider#
	 * getBytes(int)
	 */
	@Override
	public byte[] getBytes(int count) throws Exception {
		int counter = 0;
		
		while (counter < 100)
    	{
			if (!serialPort.isOpen())
			{
				throw new Exception("SerialPort is not open.");
			}
				
			int availableBytes = serialPort.getAvailableBytesCount();
			
			if (availableBytes >= count)
			{
				byte[] result =  serialPort.readBytes(count);
									
				return result;
			}
		
			Thread.sleep(50);
		
			counter++;
    	}
		
		throw new Exception("GetBytes Timed out");
	}

}
