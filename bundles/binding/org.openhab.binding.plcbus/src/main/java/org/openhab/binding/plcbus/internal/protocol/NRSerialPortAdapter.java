/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.NRSerialPort;
import gnu.io.SerialPort;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class NRSerialPortAdapter implements ISerialPort {

	private static Logger logger = LoggerFactory.getLogger(NRSerialPortAdapter.class);
	private NRSerialPort serialPort;
	
	public NRSerialPortAdapter(String serialPortName, int baudrate) {
		serialPort = new NRSerialPort(serialPortName, baudrate);
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#open()
	 */
	@Override
	public void open() throws SerialPortException {
		serialPort.connect();
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#setParameter(int, int, int, int)
	 */
	@Override
	public void setParameter(int baudrate, int databits, int stopbits,
			int parity) throws SerialPortException {
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#isOpen()
	 */
	@Override
	public boolean isOpen() {
		return serialPort.isConnected();
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#readBytes()
	 */
	@Override
	public byte[] readBytes() throws SerialPortException {
		InputStream in =  serialPort.getInputStream();
		
		int available;
		try {
			available = in.available();
		} catch (IOException e) {
			throw new SerialPortException(e.getMessage());
		}
		
		return readBytes(available);
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#readBytes(int)
	 */
	@Override
	public byte[] readBytes(int count) throws SerialPortException {
		InputStream in = serialPort.getInputStream();
		
		logger.info("Read Bytes: ");
		
		byte[] bytes = new byte[count];
		
		try {
			in.read(bytes);
		} catch (IOException e) {
			throw new SerialPortException(e.getMessage());
		}
		
		Util.printBytes(bytes);
		
		return bytes;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#getAvailableBytesCount()
	 */
	@Override
	public int getAvailableBytesCount() throws SerialPortException {
		InputStream in = serialPort.getInputStream();
		
		try {
			return in.available();
		} catch (IOException e) {
			throw new SerialPortException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#writeBytes(byte[])
	 */
	@Override
	public boolean writeBytes(byte[] bytes) throws SerialPortException {
		OutputStream out = serialPort.getOutputStream();

		logger.info("Write Bytes: ");
		Util.printBytes(bytes);
		
		try {
			out.write(bytes);
		} catch (IOException e) {
			throw new SerialPortException(e.getMessage());
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#close()
	 */
	@Override
	public boolean close() throws SerialPortException {
		serialPort.disconnect();
		
		return true;
	}

}
