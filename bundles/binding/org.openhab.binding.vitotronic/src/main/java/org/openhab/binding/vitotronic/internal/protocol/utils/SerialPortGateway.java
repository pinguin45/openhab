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
	
	private SerialPortGateway(ISerialPort serialPort) {
		this.serialPort = serialPort;
	}

	private void connect() {
		try {
			logger.error("Try to open SerialPort");
			this.serialPort.open();
			logger.error("SerialPort opened.");
			this.serialPort.setParameter(4800, 8, 2, 2);
			logger.error("Parameter configured");
		} catch (SerialPortException e) {
			logger.error("Failed to open serialport: " + e.getMessage());
		}
	}
	
	public static ISerialPortGateway create(ISerialPort serialPort) {
		return new SerialPortGateway(serialPort);
	}

	@Override
	public void send(IByteProtocolFrame frame, IReceiveByteProcessor receivedByteProcessor) {
		try
		{			
			if (!serialPort.isOpen())
			{
				logger.error("Port is not open. Reconnect");
				connect();
			}
			
			byte[] paket = Convert.toByteArray(frame.getBytes());
			
			serialPort.readBytes();
			boolean bytesWritten = serialPort.writeBytes(paket);
			
			if (!bytesWritten)
			{
				return;
			}
			
			try {
				receivedByteProcessor.process(SerialPortByteProvider.Create(serialPort));
				
			} catch (Exception e) {
				logger.error("Error while processing: " + e.getMessage());
			}
						
		} catch (SerialPortException e)
		{			
			logger.error("Fehler in write methode: ", e);
		}
	}

	@Override
	public void close() {
		try {
			serialPort.close();
		} catch (SerialPortException e) {
			logger.error("Failed to close serialport");
		}
	}

	@Override
	public boolean isConnected() {
		return serialPort.isOpen();
	}
}
