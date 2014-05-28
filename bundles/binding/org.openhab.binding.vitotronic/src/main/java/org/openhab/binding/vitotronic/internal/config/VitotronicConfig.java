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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
@XmlRootElement(name = "vito")
@XmlAccessorType(XmlAccessType.FIELD)
public class VitotronicConfig {

	@XmlElementWrapper(name = "devices")
	@XmlElement(name = "device")
    private List<Device> devices;
	
	@XmlElementWrapper(name = "commands")
	@XmlElement(name = "command")
    private List<Command> commands;

	public VitotronicConfig() {
		devices = new ArrayList<Device>();
		commands = new ArrayList<Command>();
	}
	
	public void addDevice(Device device) {
		devices.add(device);
	}
	
	public void addCommand(Command command) {
		commands.add(command);
	}
	
	public Command getCommandBy(String name) {
		
		for (Command command : commands) {
			if (command.getName().equals(name)) {
				return command;
			}
		}
		
		return null;
	}
}
