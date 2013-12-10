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

import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Robin Lenz
 * @since 1.1.0
 */
public class SerialPortGateway implements ISerialPortGateway {

	private static Logger logger = LoggerFactory.getLogger(SerialPortGateway.class);
	private ISerialPort serialPort;

	private SerialPortGateway(ISerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public static ISerialPortGateway create(ISerialPort serialPort) {
		return new SerialPortGateway(serialPort);
	}

	private boolean connect() {
		try {
			this.serialPort.open();
		} catch (SerialPortException e) {
			logger.error("Failed to open SerialPort: "  + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	@Override
	public void send(TransmitFrame frame,
			IReceiveFrameContainer receivedFrameContainer) {
		try {
			
			connect();
			
			byte[] paket = Convert.toByteArray(frame.getBytes());

			serialPort.writeBytes(paket);

			try {
				receivedFrameContainer.process(SerialPortByteProvider.create(serialPort));
			} catch (Exception e) {
				logger.error("Error while processing: " + e.getMessage());
			}

		} catch (Exception e) {
			logger.info("Error in write methode: " + e.getMessage());
		} finally {
			close();
		}
	}

	public void close() {
		try {
			serialPort.close();
		} catch (SerialPortException e) {
			logger.error("Close SerialPort failed: " + e.getMessage());
		}
	}
	
}