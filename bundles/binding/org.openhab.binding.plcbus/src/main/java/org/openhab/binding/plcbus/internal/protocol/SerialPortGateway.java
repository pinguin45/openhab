/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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