/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

import org.openhab.binding.plcbus.internal.protocol.commands.*;

/**
 * Controller for the PLCBus
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class PLCBusController implements IPLCBusController {

	private ISerialPortGateway serialPortGateway;
	private IPLCBusProtocol plcBusProtocol;

	private PLCBusController(ISerialPortGateway serialPortGateway, IPLCBusProtocol plcBusProtocol) {
		this.serialPortGateway = serialPortGateway;
		this.plcBusProtocol = plcBusProtocol;
	}

	public static IPLCBusController create(ISerialPortGateway serialPortGateway, IPLCBusProtocol plcBusProtocol) {
		return new PLCBusController(serialPortGateway, plcBusProtocol);
	}

	private boolean sendWithoutAnswer(PLCUnit unit, Command command) {
		ActorResponse answer = send(unit, command);

		if (answer == null) {
			return false;
		}

		return answer.isAcknowledged();
	}

	private ActorResponse send(PLCUnit unit, Command command) {
		IByteQueue bytesToSend = plcBusProtocol.getSendCommandBytes(unit, command);
		int expectedResponseSizeInBytes = plcBusProtocol.expectedSendCommandResponseSize();
		
		IByteQueue receivedBytes = serialPortGateway.sendBytesAndWaitForResponse(bytesToSend, expectedResponseSizeInBytes);

		ActorResponse answer = plcBusProtocol.parseResponseFromActor(receivedBytes);
		return answer;
	}

	@Override
	public boolean bright(PLCUnit unit, int seconds) {
		Bright command = new Bright();
		command.setSeconds(seconds);
		return sendWithoutAnswer(unit, command);
	}

	@Override
	public boolean dim(PLCUnit unit, int seconds) {
		Dim command = new Dim();
		command.setSeconds(seconds);
		return sendWithoutAnswer(unit, command);
	}

	@Override
	public boolean switchOff(PLCUnit unit) {
		return sendWithoutAnswer(unit, new UnitOff());
	}

	@Override
	public boolean switchOn(PLCUnit unit) {
		return sendWithoutAnswer(unit, new UnitOn());
	}

	@Override
	public boolean fadeStop(PLCUnit unit) {
		return sendWithoutAnswer(unit, new FadeStop());
	}

	@Override
	public StatusResponse requestStatusFor(PLCUnit unit) {
		ActorResponse answer = send(unit, new StatusRequest());

		if (answer == null) {
			return null;
		}

		return new StatusResponse(answer.isAcknowledged(),
				answer.getCommand(), answer.getFirstParameter(),
				answer.getSecondParameter());
	}

}
