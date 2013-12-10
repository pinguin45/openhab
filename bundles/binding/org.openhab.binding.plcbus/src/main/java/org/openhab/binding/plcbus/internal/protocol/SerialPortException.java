/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class SerialPortException extends Exception {

	/**
	 * @param arg0
	 */
	public SerialPortException(String message) {
		super(message);
	}

}
