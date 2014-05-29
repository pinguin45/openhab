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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gateway to the Serialport
 * @author Robin Lenz
 *
 */
public class SerialPortGateway implements ISerialPortGateway {
	
	private static Logger logger = LoggerFactory.getLogger(SerialPortGateway.class);
	private ISerialPort serialPort;
	
	public SerialPortGateway(ISerialPort serialPort) {
		this.serialPort = serialPort;
	}

	@Override
	public IByteQueue sendBytesAndWaitForResponse(IByteQueue bytesToWrite, int expectedResponseSizeInBytes) {
		sendBytes(bytesToWrite);
		
		return waitForResponse(expectedResponseSizeInBytes);
	}

	private void sendBytes(IByteQueue bytesToWrite) {
		try {
			serialPort.writeBytes(bytesToWrite.toByteArray());
		} catch (SerialPortException e) {
			logger.error("Error while writing bytes: %s", e.getMessage());
		}
	}

	private IByteQueue waitForResponse(int expectedResponseSizeInBytes) {
		IByteQueue responseBytes = new ByteQueue();
		
		int countOfMissingBytes = expectedResponseSizeInBytes;
		
		while (countOfMissingBytes > 0) {	
			byte[] readBytes = readBytes(countOfMissingBytes);
			
			responseBytes.enqueAll(readBytes);
			
			countOfMissingBytes = expectedResponseSizeInBytes - responseBytes.size();
		}
		
		return responseBytes;
	}

	private byte[] readBytes(int bytesToRead) {
		try {
			return serialPort.readBytes(getCountOfBytesToRead(bytesToRead));
		} catch (SerialPortException e) {
			logger.error("Error while reading bytes (Count: %d): %s", bytesToRead, e.getMessage());
		}
		
		return new byte[0];
	}

	private int getCountOfBytesToRead(int countOfMissingBytes) {
		int availableBytesCount = getAvailableBytesCount();
		
		if (availableBytesCount > countOfMissingBytes) {
			return countOfMissingBytes;
		} else {
			return availableBytesCount;
		}
	}

	private int getAvailableBytesCount() {
		try {
			return serialPort.getAvailableBytesCount();
		} catch (SerialPortException e) {
			logger.error("Error getting available byte count: %s", e.getMessage());
		}
		
		return 0;
	}
}
