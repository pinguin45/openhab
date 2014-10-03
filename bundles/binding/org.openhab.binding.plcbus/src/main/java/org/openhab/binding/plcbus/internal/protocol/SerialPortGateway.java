/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
 * @author Robin Lenz
 * @since 1.1.0
 */
public class SerialPortGateway implements ISerialPortGateway {
	private static final int RESPONSE_TIMEOUT = 1500;
	private static final int WAIT_BETWEEN_READS = 10;
	
	private static Logger logger = LoggerFactory.getLogger(SerialPortGateway.class);
	
	private ISerialPort serialPort;
	
	public SerialPortGateway(ISerialPort serialPort) {
		this.serialPort = serialPort;
	}

	@Override
	public IByteQueue sendBytesAndWaitForResponse(IByteQueue bytesToWrite, int expectedResponseSizeInBytes) {
		clearSerialPort();
		
		sendBytes(bytesToWrite);
		
		return waitForResponse(expectedResponseSizeInBytes);
	}
	
	private void clearSerialPort() {
		try
		{
			serialPort.readBytes();
		} catch (SerialPortException e) {
			logger.error("Error while clearing serialport: %s", e.getMessage());
		}
	}

	private void sendBytes(IByteQueue bytesToWrite) {
		try {
			Util.printBytes(bytesToWrite.toByteArray());
			
			serialPort.writeBytes(bytesToWrite.toByteArray());
		} catch (SerialPortException e) {
			logger.error("Error while writing bytes: %s", e.getMessage());
		}
	}

	private IByteQueue waitForResponse(int expectedResponseSizeInBytes) {
		IByteQueue responseBytes = new ByteQueue();
		
		int countOfMissingBytes = expectedResponseSizeInBytes;
		int durationInMilliseconds = 0;
		
		while (stillBytesMissingAndNotTimedout(countOfMissingBytes,	durationInMilliseconds)) {	
			readAndEnqueBytes(responseBytes, countOfMissingBytes);
				
			countOfMissingBytes = expectedResponseSizeInBytes - responseBytes.size();
			durationInMilliseconds += WAIT_BETWEEN_READS;
			
			waitBeforeNextRead();
		}
		
		Util.printBytes(responseBytes.toByteArray());
		
		return responseBytes;
	}

	private boolean stillBytesMissingAndNotTimedout(int countOfMissingBytes, int durationInMilliseconds) {
		return countOfMissingBytes > 0 && durationInMilliseconds < RESPONSE_TIMEOUT;
	}

	private void readAndEnqueBytes(IByteQueue byteQueue, int countOfBytesToRead) {
		byte[] readBytes = readBytes(countOfBytesToRead);
		
		byteQueue.enqueAll(readBytes);
	}

	private void waitBeforeNextRead() {
		try {
			Thread.sleep(WAIT_BETWEEN_READS);
		} catch (InterruptedException e) {
			logger.error("Thread.Sleep didn't work");
		}
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