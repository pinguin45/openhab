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

import jssc.SerialPort;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class JsscSerialPort implements ISerialPort {

	private SerialPort serialPort;
	
	/**
	 * 
	 */
	public JsscSerialPort(String serialPortName) {
		this.serialPort = new SerialPort(serialPortName);
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#open()
	 */
	@Override
	public void open() throws SerialPortException {
		try {
			serialPort.openPort();
		} catch (jssc.SerialPortException e) {
			throw new SerialPortException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#setParameter(int, int, int, int)
	 */
	@Override
	public void setParameter(int baudrate, int databits, int stopbits,
			int parity) throws SerialPortException {
		try {
			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			serialPort.setParams(baudrate, databits, stopbits, parity);
		} catch (jssc.SerialPortException e) {
			throw new SerialPortException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#isOpen()
	 */
	@Override
	public boolean isOpen() {
		return serialPort.isOpened();
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#readBytes()
	 */
	@Override
	public byte[] readBytes() throws SerialPortException {
		try {
			return serialPort.readBytes();
		} catch (jssc.SerialPortException e) {
			throw new SerialPortException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#readBytes(int)
	 */
	@Override
	public byte[] readBytes(int count) throws SerialPortException {
		try {
			return serialPort.readBytes(count);
		} catch (jssc.SerialPortException e) {
			throw new SerialPortException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#getAvailableBytesCount()
	 */
	@Override
	public int getAvailableBytesCount() throws SerialPortException {
		try {
			return serialPort.getInputBufferBytesCount();
		} catch (jssc.SerialPortException e) {
			throw new SerialPortException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#writeBytes(byte[])
	 */
	@Override
	public boolean writeBytes(byte[] bytes) throws SerialPortException {
		try {
			return serialPort.writeBytes(bytes);
		} catch (jssc.SerialPortException e) {
			throw new SerialPortException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#close()
	 */
	@Override
	public boolean close() throws SerialPortException {
		try {
			return serialPort.closePort();
		} catch (jssc.SerialPortException e) {
			throw new SerialPortException(e.getMessage());
		}
	}

}
