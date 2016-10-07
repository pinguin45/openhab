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
package org.openhab.binding.vitotronic.internal.protocol.parameter;

import org.openhab.binding.vitotronic.internal.protocol.IParameter;
import org.openhab.binding.vitotronic.internal.protocol.ParameterDetails;
import org.openhab.binding.vitotronic.internal.protocol.utils.ByteQueue;
import org.openhab.binding.vitotronic.internal.protocol.utils.Convert;
import org.openhab.binding.vitotronic.internal.protocol.utils.IByteQueue;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class Parameter implements IParameter {
	private byte[] dataBytes;
	
	private ParameterDetails details;
	
	public Parameter(ParameterDetails details) {
		this.dataBytes = new byte[details.getDataSize()]; 
		this.details = details;
	}
	
	@Override
	public int getAddressSize() {
		return details.getAddressSize();
	}

	@Override
	public int getDataSize() {
		return details.getDataSize();
	}

	@Override
	public IByteQueue getByteQueueForRequest() {
		IByteQueue result = new ByteQueue();
		
		result.enque(Convert.toHighByte(details.getAddress()));
		result.enque(Convert.toLowByte(details.getAddress()));
		result.enque(getDataSize());
		
		return result;
	}
	
	@Override
	public IByteQueue getByteQueueForWriting() {
		IByteQueue result = new ByteQueue();
		
		result.enque(Convert.toHighByte(details.getAddress()));
		result.enque(Convert.toLowByte(details.getAddress()));
		result.enque(getDataSize());
		
		if (getDataSize() > 0)
		{
			result.enqueAll(dataBytes);
		}
		
		return result;
	}

	@Override
	public void parseDataBytes(byte[] dataBytes) {
		if (dataBytes.length != getDataSize())
			throw new RuntimeException("Wrong size of data bytes");
		
		this.dataBytes = dataBytes;
	}
	
	protected byte[] getDataBytes() {
		return dataBytes;
	}
	
	protected void setDataBytes(byte[] dataBytes) {
		for (int pos = 0; pos < getDataSize(); pos++) {
			this.dataBytes[pos] = dataBytes[pos];
		}
	}
}
