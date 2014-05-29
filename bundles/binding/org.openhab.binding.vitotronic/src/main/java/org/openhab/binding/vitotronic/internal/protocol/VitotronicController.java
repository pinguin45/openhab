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

import org.openhab.binding.vitotronic.internal.protocol.utils.IByteQueue;
import org.openhab.binding.vitotronic.internal.protocol.utils.ISerialPortGateway;

/**
 * @author Robin Lenz
 *
 */
public class VitotronicController implements IVitotronicController {
	private final static int MAX_INIT_RETRIES = 10;
	
	private ISerialPortGateway serialPortGateway;
	private IVitotronicProtocol vitotronicProtocol;
	
	public VitotronicController(IVitotronicProtocol vitotronicProtocol, ISerialPortGateway serialPortGateway) {
		this.vitotronicProtocol = vitotronicProtocol;
		this.serialPortGateway = serialPortGateway;
	}
	
	@Override
	public boolean init() {
		IByteQueue initRequestBytes = vitotronicProtocol.getByteQueueForInit();
		int expectedInitResponseSize = vitotronicProtocol.expectedInitResponseSize();
		
		int retries = 0;
		
		while (retries < MAX_INIT_RETRIES)
		{		
			if (tryInit(initRequestBytes, expectedInitResponseSize))
			{
				return true;
			}
			
			retries++;
		}	
		
		return false;
	}

	private boolean tryInit(IByteQueue initRequestBytes, int initResponseSize) {
		IByteQueue initResponse = serialPortGateway.sendBytesAndWaitForResponse(initRequestBytes, initResponseSize);
		
		if (vitotronicProtocol.isInitialized(initResponse))
		{
			return true;
		}
		
		return false;
	}

	@Override
	public void reset() {
		IByteQueue resetRequestBytes = vitotronicProtocol.getByteQueueForReset();
		int expectedResponseSizeInBytes = vitotronicProtocol.expectedResetResponseSize();
		
		serialPortGateway.sendBytesAndWaitForResponse(resetRequestBytes, expectedResponseSizeInBytes);
	}
	
	@Override
	public <TParameterValue> TParameterValue readValueOf(IParameterWithValue<TParameterValue> parameter) {
		IByteQueue readParameterBytes = vitotronicProtocol.getByteQueueForReadingParameter(parameter);
		int expectedResponseSize = vitotronicProtocol.expectedReadingParameterResponseSize(parameter);
		
		IByteQueue readParameterResponseBytes = serialPortGateway.sendBytesAndWaitForResponse(readParameterBytes, expectedResponseSize);
		
		IParameterWithValue<TParameterValue> parameterWithValue = vitotronicProtocol.parseReadParameterResponse(readParameterResponseBytes);
	
		return parameterWithValue.getValue();
	}	

	@Override
	public <TParameterValue> TParameterValue write(IParameterWithValue<TParameterValue> parameter) {	
		IByteQueue writeParameterBytes = vitotronicProtocol.getByteQueueForWritingParameter(parameter);
		int expectedResponseSize = vitotronicProtocol.expectedWritingParameterResponseSize(parameter);
		
		IByteQueue writeParameterResponseBytes = serialPortGateway.sendBytesAndWaitForResponse(writeParameterBytes, expectedResponseSize);
		
		IParameterWithValue<TParameterValue> parameterWithValue = vitotronicProtocol.parseWriteParameterResponse(writeParameterResponseBytes);
	
		return parameterWithValue.getValue();
	}
}
