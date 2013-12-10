/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
