

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

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.vitotronic.internal.protocol.utils.IByteProtocolFrame;

/**
 * Baseclass for telegrams
 * @author Robin Lenz
 *
 */
public abstract class Telegram<TParameter extends IParameter> implements IByteProtocolFrame {
	/**
	 * Startbyte of a telegram
	 */
	public final static byte START_BYTE = 0x41;
	
	private Command<TParameter> command;
	
	/**
	 * Flag if Telegram is Valid
	 */
	public boolean isValid;
	
	/**
	 * Constructor
	 */
	public Telegram() {
		isValid = true;
	}
	
	/**
	 * Constructor
	 */
	public Telegram(Command<TParameter> command) {
		this();
		this.command = command;
	}

	public List<Byte> getBytes() {
		List<Byte> result = new ArrayList<Byte>();
		
		result.add(START_BYTE);
		
		if (command != null)
		{
			List<Byte> commandBytes = command.getBytes();
			result.add((byte) (commandBytes.size() + 1));
			result.add(getType());
			result.addAll(commandBytes);
			result.add(getPruefsummeOf(result));
		}
		
		return result;
	}
	
	/**
	 * Parses the byte array
	 * @param bytes
	 */
	public void parse(List<Byte> bytes) {
		
		if (bytes.size() > 0) 
		{
			command = createCommandByte(bytes.get(0));
			
			if (command == null)
			{
				return;
			}
			
			command.parse(bytes.subList(1, bytes.size()));
			
			/* TODO checksum*/
		}
	}
	
	/**
	 * @param b
	 * @return
	 */
	private Command<TParameter> createCommandByte(byte commandByte) {
		if (commandByte == 0x01) 
		{
			return new Read<TParameter>();
		}
		else if (commandByte == 0x02) 
		{
			return new Write<TParameter>();
		}
		else if (commandByte == 0x07)
		{
			return new FunctionCall<TParameter>();
		}
		
		return null;
	}

	private Byte getPruefsummeOf(List<Byte> bytes) {
		Byte result = 0x00;
		
		for (int pos = 0; pos < bytes.size(); pos++)
		{			
			if (pos > 0)
			{
				result = (byte) (result + bytes.get(pos));
			}
		}
		
		return result;
	}

	/**
	 * Return the type of TelegramData
	 */
	protected abstract byte getType();

	/**
	 * @return the parameter
	 */
	public TParameter getParameter() {
		if (command == null) 
		{
			return null;
		}
		
		return command.getParameter();
	}
	
	/**
	 * @return the command
	 */
	public Command<TParameter> getCommand()
	{
		return command;
	}
}
