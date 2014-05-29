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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class TCPSerialPort implements ISerialPort {
	private static Logger logger = LoggerFactory.getLogger(TCPSerialPort.class);
	private Socket socket;
	private String host;
	private int port;
	
	private TCPSerialPort(String connectionString) throws SerialPortException {
		parse(connectionString);
	}
	
	public static ISerialPort Create(String connectionString) {
		try {
			return new TCPSerialPort(connectionString);
		} catch (SerialPortException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	private void parse(String connectionString) throws SerialPortException {
		logger.info(connectionString);
		
		StringTokenizer tk = new StringTokenizer(connectionString, ":");
		
		if (tk.countTokens() != 2) 
		{
			String error = "ConnectionString must have following syntax: host:port";
			
			logger.error(error);
			throw new SerialPortException(error);
		}
		
		host = tk.nextToken();
		port = Integer.parseInt(tk.nextToken());	
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#open()
	 */
	@Override
	public void open() throws SerialPortException {
		try {
			socket = new Socket(host, port);
		} catch (Exception e) {
			logger.error("Failed to open Socket: ", e);
			throw new SerialPortException("Failed to open socket");
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#setParameter(int, int, int, int)
	 */
	@Override
	public void setParameter(int baudrate, int databits, int stopbits,
			int parity) throws SerialPortException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#isOpen()
	 */
	@Override
	public boolean isOpen() {
		if (socket == null)
		{
			return false;
		}
		
		return socket.isConnected();
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#readBytes()
	 */
	@Override
	public byte[] readBytes() throws SerialPortException {
	
		return readBytes(getAvailableBytesCount());
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#readBytes(int)
	 */
	@Override
	public byte[] readBytes(int count) throws SerialPortException {
		byte[] bytes = new byte[count];
		
		try {
			if (waitFor(count))
			{
				DataInputStream in = new DataInputStream(socket.getInputStream());
				
				in.read(bytes);
			}
		} catch (IOException e) {
			logger.error("Error while reading byte: ", e);
			throw new SerialPortException(e.getMessage());
		}
		
		return bytes;
	}
	
	private boolean waitFor(int expectedBytes) throws SerialPortException {
		int counter = 0;
		
		while (counter < 1000)
		{
			if (getAvailableBytesCount() >= expectedBytes)
			{
				return true;
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				logger.error("Error while sleeping: ", e);
			}
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#getAvailableBytesCount()
	 */
	@Override
	public int getAvailableBytesCount() throws SerialPortException {
		int available = 0;
		try {
			InputStream in =  socket.getInputStream();
			
			available = in.available();
		} catch (IOException e) {
			logger.error("Error while reading byteCount: ", e);
			throw new SerialPortException(e.getMessage());
		}
		
		return available;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#writeBytes(byte[])
	 */
	@Override
	public boolean writeBytes(byte[] bytes) throws SerialPortException {
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			logger.error("Failed to write bytes: ", e);
			throw new SerialPortException(e.getMessage());
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPort#close()
	 */
	@Override
	public boolean close() throws SerialPortException {
		try {
			socket.close();
		} catch (IOException e) {
			logger.error("Error while closing port: ", e);
			throw new SerialPortException(e.getMessage());
		}
		
		return true;
	}

}
