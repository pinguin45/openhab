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

import org.openhab.binding.vitotronic.internal.protocol.parameter.*;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class StaticParameterFactory implements IParameterFactory {

	@SuppressWarnings("unchecked")
	@Override
	public <TParameterValue> IParameterWithValue<TParameterValue> createParameterFor(
			int address) {
		if (address == 0x5525) {
			return (IParameterWithValue<TParameterValue>)new TemperatureParameter(new ParameterDetails(0x5525, 2, 2), 10);
		} else if (address == 0x2306) {
			return (IParameterWithValue<TParameterValue>)new TemperatureParameter(new ParameterDetails(0x2306, 2, 1), 1);
		} else if (address == 0x0802) {
			return (IParameterWithValue<TParameterValue>)new TemperatureParameter(new ParameterDetails(0x0802, 2, 2), 10);
		} else if (address == 0x080A) {
			return (IParameterWithValue<TParameterValue>)new TemperatureParameter(new ParameterDetails(0x080A, 2, 2), 10);
		} else if (address == 0x2900) {
			return (IParameterWithValue<TParameterValue>)new TemperatureParameter(new ParameterDetails(0x2900, 2, 2), 10);
		} else if (address == 0x08A7) {
			return (IParameterWithValue<TParameterValue>)new IntegerParameter(new ParameterDetails(0x08A7, 2, 4));
		} else if (address == 0x088A) {
			return (IParameterWithValue<TParameterValue>)new IntegerParameter(new ParameterDetails(0x088A, 2, 4));
		} else if (address == 0x2323) {
			return (IParameterWithValue<TParameterValue>)new IntegerParameter(new ParameterDetails(0x2323, 2, 1));
		} else if (address == 0x2501) {
			return (IParameterWithValue<TParameterValue>)new IntegerParameter(new ParameterDetails(0x2501, 2, 1));
		} else if (address == 0xA38F) {
			return (IParameterWithValue<TParameterValue>)new PercentParameter(new ParameterDetails(0xA38F, 2, 1));
		}
		
		throw new RuntimeException("No parameter found for address.");
	}

}
