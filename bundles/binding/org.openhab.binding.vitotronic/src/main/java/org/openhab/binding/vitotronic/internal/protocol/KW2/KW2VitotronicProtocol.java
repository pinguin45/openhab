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
package org.openhab.binding.vitotronic.internal.protocol.KW2;

import org.openhab.binding.vitotronic.internal.protocol.*;
import org.openhab.binding.vitotronic.internal.protocol.utils.*;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class KW2VitotronicProtocol implements IVitotronicProtocol {
	private final static byte START_BYTE = 0x41;
	private final static byte REQUEST_TELEGRAM_TYPE = 0x00;
	private final static byte RESPONSE_TELEGRAM_TYPE = 0x01;
	private final static byte READ_COMMAND = 0x01; 
	private final static byte WRITE_COMMAND = 0x02;
	private final static byte ACK = 0x06;
	private final static byte HEALTH_PING = 0x05;
	private final static int STATIC_PARAMETER_RESPONSE_SIZE = 6;
	
	private final IParameterFactory parameterFactory;
	private IByteQueue byteQueueToParse;
	
	public KW2VitotronicProtocol(IParameterFactory parameterFactory) {
		this.parameterFactory = parameterFactory;
	}
	
	@Override
	public IByteQueue getByteQueueForInit() {
		IByteQueue byteQueueForInit = new ByteQueue();
		
		byteQueueForInit.enque(0x16);
		byteQueueForInit.enque(0x00);
		byteQueueForInit.enque(0x00);
		
		return byteQueueForInit;
	}

	@Override
	public IByteQueue getByteQueueForReset() {
		IByteQueue byteQueueForReset = new ByteQueue();
		
		byteQueueForReset.enque(0x04);
		
		return byteQueueForReset;
	}

	@Override
	public IByteQueue getByteQueueForReadingParameter(IParameter parameter) {
		IByteQueue byteQueueForReadingParameter = new ByteQueue();
		
		addCommandRequestTo(byteQueueForReadingParameter, READ_COMMAND, parameter);
		
		return byteQueueForReadingParameter;
	}
	
	@Override
	public IByteQueue getByteQueueForWritingParameter(IParameter parameter) {
		IByteQueue byteQueueForWritingParameter = new ByteQueue();
		
		addCommandRequestTo(byteQueueForWritingParameter, WRITE_COMMAND, parameter);
		
		return byteQueueForWritingParameter;
	}

	private void addCommandRequestTo(IByteQueue byteQueue, byte command, IParameter parameter) {
		byteQueue.enque(START_BYTE);
		byteQueue.enque(getRequestSize(parameter));
		byteQueue.enque(REQUEST_TELEGRAM_TYPE);
		byteQueue.enque(command);
		byteQueue.enqueAll(parameter.getByteQueue());
		byteQueue.enque(getChecksumOf(byteQueue));
	}

	private byte getChecksumOf(IByteQueue byteQueue) {
		Byte result = 0x00;
		
		byte[] bytes = byteQueue.toByteArray();
		
		for (int pos = 0; pos < bytes.length; pos++)
		{			
			if (pos > 0)
			{
				result = (byte) (result + bytes[pos]);
			}
		}
		
		return result;
	}
	
	private byte getRequestSize(IParameter parameter) {
		IByteQueue parameterBytes = parameter.getByteQueue();
		
		return (byte)(parameterBytes.size() + 2);
	}
	
	@Override
	public int expectedInitResponseSize() {
		return 1;
	}
	
	@Override
	public int expectedResetResponseSize() {
		return 2;
	}
	
	@Override
	public int expectedReadingParameterResponseSize(IParameter parameter) {
		return STATIC_PARAMETER_RESPONSE_SIZE + parameter.addressSize() + parameter.dataSize();
	}
	
	@Override
	public int expectedWritingParameterResponseSize(IParameter parameter) {
		return STATIC_PARAMETER_RESPONSE_SIZE + parameter.addressSize() + parameter.dataSize();
	}

	@Override
	public boolean isInitialized(IByteQueue byteQueue) {
		if (byteQueue.size() < 1)
			return false;
		
		byte firstByteInQueue = byteQueue.deque();
		
		return firstByteInQueue == ACK;
	}

	@Override
	public boolean isReseted(IByteQueue byteQueue) {
		if (byteQueue.size() < 2)
			return false;
		
		byte firstByteInQueue = byteQueue.deque();
		byte secondByteInQueue = byteQueue.deque();
		
		return firstByteInQueue == ACK && secondByteInQueue == HEALTH_PING;
	}
	
	@Override
	public IParameter parseReadParameterResponse(IByteQueue byteQueue) {
		byteQueueToParse = byteQueue;
				
		byte checksum = getChecksumOf(getByteQueueForCalcCheckSum());
		
		ensureNextByteIs(ACK);		
		ensureNextByteIs(START_BYTE);
		ensureNextByteIs(expectedLength());
		ensureNextByteIs(RESPONSE_TELEGRAM_TYPE);
		ensureNextByteIs(READ_COMMAND);
		
		IParameter parameter = parseParameter();
		
		ensureNextByteIs(checksum);
		
		return parameter;
	}

	private IByteQueue getByteQueueForCalcCheckSum() {
		byte[] allBytesFromQueue = byteQueueToParse.toByteArray();
		
		return new ByteQueue(getPartOfArray(allBytesFromQueue, 1, allBytesFromQueue.length - 1));
	}

	private byte[] getPartOfArray(byte[] original, int start, int end) {
		byte[] part = new byte[end - start];
		
		for (int pos = 0; pos < part.length; pos++) 
		{
			part[pos] = original[pos + start];
		}
		
		return part;
	}

	private IParameter parseParameter() {
		int address = parseAddress();
		IParameter parameter = parameterFactory.createParameterFor(address);
		
		byte[] dataBytes = parseDataBytes(parameter.dataSize());
		parameter.parseDataBytes(dataBytes);
		
		return parameter;
	}

	/**
	 * @param dataSize
	 * @return
	 */
	private byte[] parseDataBytes(int dataSize) {
		byte[] dataBytes = new byte[dataSize];
		
		for (int pos = 0; pos < dataSize; pos++)
		{
			dataBytes[pos] = byteQueueToParse.deque();
		}
		
		return dataBytes;
	}

	/**
	 * @return
	 */
	private int parseAddress() {
		byte highByte = byteQueueToParse.deque();
		byte lowByte = byteQueueToParse.deque();
		
		return getAddressFrom(highByte, lowByte);
	}

	private int getAddressFrom(byte highByte, byte lowByte) {
		String addressString = String.format("%02x%02x", highByte, lowByte);
		
		return Integer.parseInt(addressString, 16);
	}

	private byte expectedLength() {
		return (byte)(byteQueueToParse.size() - 2);
	}

	private void ensureNextByteIs(byte expected) {
		byte nextByte = byteQueueToParse.deque();
		if (nextByte != expected) 
		{
			throw new RuntimeException(String.format("Parse error. Expected: %02X, Found: %02X", expected, nextByte));
		}
	}
}
