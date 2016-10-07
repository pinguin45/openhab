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
package org.openhab.binding.vitotronic.internal;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class VitotronicOpenhabConfig {
	private final int DEFAULT_REFRESH_RATE = 60000;
	
	private String serialPortName;
	private int refreshRate;
	
	public VitotronicOpenhabConfig(Dictionary<String, ?> config) throws ConfigurationException {
		this.serialPortName = parseSerialPortNameOf(config);
		this.refreshRate = parseRefreshRateOf(config);
	}

	private int parseRefreshRateOf(Dictionary<String, ?> config) throws ConfigurationException {
		String refreshString = getConfigEntry(config, "refresh");
		
		if (refreshString.isEmpty()) {
			return DEFAULT_REFRESH_RATE;
		}
		
		return Integer.parseInt(refreshString);
	}

	private String parseSerialPortNameOf(Dictionary<String, ?> config) throws ConfigurationException {
		return getConfigEntry(config, "port");
	}
	
	public String getSerialPortName() {
		return serialPortName;
	}
	
	public int getRefreshRate() {
		return refreshRate;
	}

	private String getConfigEntry(Dictionary<String, ?> config, String entryName) throws ConfigurationException {
		Object entry = config.get(entryName);
		
		if (entry == null)
			throw new ConfigurationException(entryName, "Not found");
		
		return (String) entry;
	}
}
