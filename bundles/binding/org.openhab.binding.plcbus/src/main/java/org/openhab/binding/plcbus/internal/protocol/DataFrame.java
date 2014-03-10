/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

import java.util.*;

/**
 * DataFrame of PLCBus Protocol
 * 
 * @author Robin Lenz
 * @since 1.0.0
 */
public class DataFrame {
	
	private int userCode;
	private int home;
	private int unit;
	private CommandFrame commandFrame;

	/**
	 * Create a new DataFrame
	 */
	public DataFrame() {
	}

	/**
	 * Creates a new DataFrame with CommandFrame
	 * 
	 * @param commandFrame
	 *            for DataFrame
	 */
	public DataFrame(CommandFrame commandFrame) {
		this.commandFrame = commandFrame;
	}

	/**
	 * Sets the UserCode to DataFrame
	 * 
	 * @param userCode
	 *            for DataFrame
	 */
	public void setUserCode(String userCode) {
		this.userCode = Convert.toByte(userCode, 16);
	}

	/**
	 * Sets address to DataFrame
	 * 
	 * @param address
	 *            for DataFrame
	 */
	public void setAddress(String address) {

		this.home = getHomeFrom(address.charAt(0));
		this.unit = getUnitFrom(address.substring(1));
	}

	private int getHomeFrom(char home) {
		if (!dictHome.containsKey(home))
		{
			return 0;
		}
		
		return dictHome.get(home);
	}

	private int getUnitFrom(String unit) {
		if (!dictUnit.containsKey(unit))
		{
			return 0;
		}
		
		return dictUnit.get(unit);
	}

	/**
	 * Returns the Bytes of DataFrame
	 * 
	 * @return bytes of DataFrame
	 */
	public List<Byte> getBytes() {
		List<Byte> result = new ArrayList<Byte>();

		result.add(Convert.toByte(userCode));
		result.add(getHomeAndUnitByte());

		if (commandFrame != null) {
			result.addAll(commandFrame.getBytes());
		}

		return result;
	}

	/**
	 * Initializes DataFrame from byte array
	 * 
	 * @param data
	 *            byte of DataFrame
	 */
	public void parse(byte[] data) {
		if (data.length < 2)
		{
			return;
		}
		
		userCode = data[0];
		home = (data[1] & 0xF0) >> 4;
		unit = data[1] & 0x0F;

		if (data.length < 5)
		{
			return;
		}
		
		commandFrame = new CommandFrame();
		commandFrame.parse(new byte[] { data[2], data[3], data[4] });
	}

	/**
	 * Gets the first parameter of CommandFrame
	 * 
	 * @return first parameter
	 */
	public int getFirstParameter() {
		return commandFrame.getFirstParameter();
	}

	/**
	 * Gets the second parameter of CommandFrame
	 * 
	 * @return second parameter
	 */
	public int getSecondParameter() {
		return commandFrame.getSecondParameter();
	}

	/**
	 * Gets the Command of CommandFrame
	 * 
	 * @return Command of CommandFrame
	 */
	public Command getCommand() {
		return commandFrame.getCommand();
	}
	
	private byte getHomeAndUnitByte() {
		int result = home << 4;
		
		result = result + unit;
		
		return Convert.toByte(result);
	}
	
	private static final Map<Character, Integer> dictHome = new HashMap<Character, Integer>(){
        {
            put('A', 0);
            put('B', 1);
            put('C', 2);
            put('D', 3);
            put('E', 4);
            put('F', 5);
            put('G', 6);
            put('H', 7);
            put('I', 8);
            put('J', 9);
            put('K', 10);
        }
    };
    
    private static final Map<String, Integer> dictUnit = new HashMap<String, Integer>(){
        {
            put("1", 0);
            put("2", 1);
            put("3", 2);
            put("4", 3);
            put("5", 4);
            put("6", 5);
            put("7", 6);
            put("8", 7);
            put("9", 8);
            put("10", 9);
            put("11", 10);
        }
    };
}
