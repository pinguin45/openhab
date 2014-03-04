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
package org.openhab.binding.vitotronic.internal.protocol.parameters;

import org.openhab.binding.vitotronic.internal.protocol.IIntegerParameter;
import org.openhab.binding.vitotronic.internal.protocol.Parameter;

/**
 * Gets the number of burner starts
 * @author Robin Lenz
 *
 */
public class BurnerStarts extends Parameter implements IIntegerParameter {

	/* 
	 * {@inheritDoc}
	 */
	@Override
	protected byte getExpectedValueSize() {
		return 4;
	}

	/* 
	 * {@inheritDoc}
	 */
	public String getValueString() {
		return String.format("%d", getCount());
	}
	
	/**
	 * @return the count of burner starts
	 */
	public int getCount() {
		return getInteger();
	}

	@Override
	public int getValue() {
		return getCount();
	}
}
