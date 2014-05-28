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
package org.openhab.binding.vitotronic.internal.protocol;

import org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPortGateway;

/**
 * @author Robin Lenz
 *
 */
public class VitotronicController implements IVitotronicController {

	private ISerialPortGateway serialPortGateway;
	private IReceiveByteProcessorFactory receiveByteProcessorFactory;
	
	/**
	 * Constructor
	 */
	private VitotronicController(ISerialPortGateway serialPortGateway, IReceiveByteProcessorFactory receiveByteProcessorFactory) {
		this.serialPortGateway = serialPortGateway;
		this.receiveByteProcessorFactory = receiveByteProcessorFactory;
	}
	
	/**
	 * Creates a new instance.
	 * @param serialPortGateway
	 * @return new instance
	 */
	public static IVitotronicController Create(ISerialPortGateway serialPortGateway, IReceiveByteProcessorFactory receiveByteProcessorFactory) {
		return new VitotronicController(serialPortGateway, receiveByteProcessorFactory);
	}
	
	private <TParameter extends IParameter> TParameter read(TParameter parameter) {
		Telegram<TParameter> frame = createReadTelegramWith(parameter);
		
		VitotronicParameterProcessor<TParameter> processor = receiveByteProcessorFactory.createVitotronicParameterProcessor();
		serialPortGateway.send(frame, processor);
				
		return processor.getParameter();
	}
	
	private <TParameter extends IParameter> Telegram<TParameter> createReadTelegramWith(TParameter parameter) {
		Read<TParameter> command = new Read<TParameter>(parameter);
			
		return new Request<TParameter>(command);
	}
		
	private <TParameter extends IWriteableParameter> Telegram<TParameter> createWriteTelegramWith(TParameter parameter) {
		Write<TParameter> command = new Write<TParameter>(parameter);
			
		return new Request<TParameter>(command);
	}
		
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public boolean init() {
		AcknowledgementProcessor processor = receiveByteProcessorFactory.createAcknowledgementProcessor();
		
		int counter = 0;
		
		while (counter < 10)
		{		
			serialPortGateway.send(new Init(), processor);
			
			if (processor.gotAcknowledgment())
			{
				return true;
			}
			
			counter++;
		}	
		
		return false;
	}

	/* 
	 * {@inheritDoc}
	 */
	@Override
	public void reset() {
		ResetProcessor processor = receiveByteProcessorFactory.createResetProcessor();
		serialPortGateway.send(new Reset(), processor);
	}

	@Override
	public String readValueOf(IStringParameter parameter) {
		parameter = read(parameter);
		
		if (parameter == null)
		{
			return "Fehler";
		}
		
		return parameter.getValue();
	}

	@Override
	public double readValueOf(IDecimalParameter parameter) {
		parameter = read(parameter);
		
		if (parameter == null)
		{
			return 0;
		}
		
		return parameter.getValue();
	}

	@Override
	public int readValueOf(IIntegerParameter parameter) {
		parameter = read(parameter);
		
		if (parameter == null)
		{
			return 0;
		}
		
		return parameter.getValue();
	}

	@Override
	public boolean readValueOf(IBooleanParameter parameter) {
		parameter = read(parameter);
		
		if (parameter == null)
		{
			return false;
		}
		
		return parameter.getValue();
	}

	@Override
	public void write(IWriteableParameter parameter) {
		Telegram<IWriteableParameter> frame = createWriteTelegramWith(parameter);
		
		AcknowledgementProcessor processor = receiveByteProcessorFactory.createAcknowledgementProcessor();
		serialPortGateway.send(frame, processor);		
	}

}
