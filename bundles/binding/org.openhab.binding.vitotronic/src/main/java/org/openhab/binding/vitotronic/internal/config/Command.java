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
package org.openhab.binding.vitotronic.internal.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
@XmlRootElement(name = "commands")
@XmlAccessorType(XmlAccessType.FIELD)
public class Command {

	@XmlAttribute
	private String name;
	
	@XmlAttribute(name = "protocmd")
	private String protocolCommand;
	
	@XmlElement(name = "addr")
	private String address;
	
	@XmlElement(name = "len")
	private int length;
	
	@XmlElement
	private String unit;
	
	@XmlElement
	private String description;
	
	public String getName() {
		return this.name;
	}
	
	public String getProtocolCommand() {
		return this.protocolCommand;
	}
	
	public int getAddress() {
		return Integer.parseInt(this.address, 16);
	}
	
	public int getLength() {
		return this.length;
	}
	
	public String getUnitShortcut() {
		return this.unit;
	}
	
	public String getDescription() {
		return this.description;
	}
}
