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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.NRSerialPort;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class NRSerialPortAdapter implements ISerialPort {

	private NRSerialPort serialPort;
	
	public NRSerialPortAdapter(String serialPortName) {
		serialPort = new NRSerialPort(serialPortName, 4800);
	}
	
	@Override
	public void open() throws SerialPortException {
		serialPort.connect();
	}

	@Override
	public void setParameter(int baudrate, int databits, int stopbits,
			int parity) throws SerialPortException {
		//serialPort.connect(8, 2, 2);
	}

	@Override
	public boolean isOpen() {
		return serialPort.isConnected();
	}

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

	@Override
	public byte[] readBytes(int count) throws SerialPortException {
		InputStream in = serialPort.getInputStream();
		
		byte[] bytes = new byte[count];
		
		try {
			in.read(bytes);
		} catch (IOException e) {
			throw new SerialPortException(e.getMessage());
		}
		
		return bytes;
	}

	@Override
	public int getAvailableBytesCount() throws SerialPortException {
		InputStream in = serialPort.getInputStream();
		
		try {
			return in.available();
		} catch (IOException e) {
			throw new SerialPortException(e.getMessage());
		}
	}

	@Override
	public boolean writeBytes(byte[] bytes) throws SerialPortException {
		OutputStream out = serialPort.getOutputStream();
		
		try {
			out.write(bytes);
		} catch (IOException e) {
			throw new SerialPortException(e.getMessage());
		}
		
		return true;
	}

	@Override
	public boolean close() throws SerialPortException {
		serialPort.disconnect();
		
		return true;
	}
}
