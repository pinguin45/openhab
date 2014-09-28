/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilmethods
 * @author Robin Lenz
 *
 */
public class Util {

	private static Logger logger = LoggerFactory.getLogger(Util.class);
	
	public static void printBytes(List<Byte> bytes) {
		
		String all = new String();
		
		for (Byte current : bytes)
		{
			all = all + String.format("%X ", current);
		}
		
		logger.info(all);
	}
	
	public static void printBytes(byte[] bytes) {
		
		String all = new String();
		
		for (byte current : bytes)
		{
			all = all + String.format("%X ", current);
		}
		
		logger.info(all);
	}
	
	public static String getByteStringFor(byte[] bytes) {
		String all = new String();
		
		for (byte current : bytes)
		{
			all = all + String.format("%02X ", current);
		}
		
		return all.trim();
	}
}
