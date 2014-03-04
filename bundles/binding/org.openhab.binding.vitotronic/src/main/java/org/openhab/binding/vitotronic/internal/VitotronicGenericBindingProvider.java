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

import java.util.ArrayList;
import java.util.Collection;

import org.openhab.binding.vitotronic.VitotronicBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Implementation of a VitotronicBindingProvider
 * @author Robin Lenz
 *
 */
public class VitotronicGenericBindingProvider extends AbstractGenericBindingProvider implements VitotronicBindingProvider {
	
	@Override
	public String getBindingType() {
		return "vitotronic";
	}

	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		VitotronicBindingConfig config = new VitotronicBindingConfig(item.getName(), bindingConfig);
		
		addBindingConfig(item, config);
	}

	@Override
	public Collection<VitotronicBindingConfig> getConfigurations() {
		Collection<VitotronicBindingConfig> result = new ArrayList<VitotronicBindingConfig>();
		
		for (BindingConfig config : bindingConfigs.values())
		{
			result.add((VitotronicBindingConfig) config);
		}
				
		return result;
	}

	@Override
	public VitotronicBindingConfig getConfigFor(String itemName) {
		return (VitotronicBindingConfig) bindingConfigs.get(itemName);
	}

}
