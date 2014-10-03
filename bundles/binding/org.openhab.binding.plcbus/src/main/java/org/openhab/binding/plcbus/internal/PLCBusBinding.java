/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal;

import java.util.Dictionary;

import org.openhab.binding.plcbus.PLCBusBindingProvider;
import org.openhab.binding.plcbus.internal.protocol.*;
import org.openhab.core.binding.*;
import org.openhab.core.library.types.*;
import org.openhab.core.types.*;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding for PLC Bus
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class PLCBusBinding extends AbstractBinding<PLCBusBindingProvider> implements
		ManagedService {

	private static Logger logger = LoggerFactory.getLogger(PLCBusBinding.class);

	private ISerialPort serialPort;
	
	private IPLCBusController plcBusController;

	public void activate(ComponentContext componentContext) {
	}

	public void deactivate(ComponentContext componentContext) {
		for (PLCBusBindingProvider provider : providers) {
			provider.removeBindingChangeListener(this);
		}
		providers.clear();

		if (serialPort != null) {
			try {
				serialPort.close();
			} catch (SerialPortException e) {
				logger.error(e.getMessage());
			}
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized void internalReceiveCommand(String itemName, Command command) {

		PLCBusBindingConfig config = tryGetConfigFor(itemName);

		if (config == null) {
			logger.error("No config found for item %s", itemName);
			return;
		}

		if (command == OnOffType.ON) {
			plcBusController.switchOn(config.getUnit());
		} else if (command == OnOffType.OFF) {
			plcBusController.switchOff(config.getUnit());
		} else if (command == IncreaseDecreaseType.INCREASE) {
			plcBusController.bright(config.getUnit(), config.getSeconds());
		} else if (command == IncreaseDecreaseType.DECREASE) {
			plcBusController.dim(config.getUnit(), config.getSeconds());
		} else if (command == StopMoveType.STOP) {
			plcBusController.fadeStop(config.getUnit());
		} else if (command == UpDownType.UP) {
			plcBusController.switchOn(config.getUnit());
		} else if (command == UpDownType.DOWN) {
			plcBusController.switchOff(config.getUnit());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized void internalReceiveUpdate(String itemName, State newState) {

		PLCBusBindingConfig config = tryGetConfigFor(itemName);

		if (config == null) {
			logger.error("No config found for %s", itemName);
			return;
		}

		if (newState == UnDefType.UNDEF) {
			StatusResponse response = plcBusController.requestStatusFor(config.getUnit());

			State status = (response.isUnitOn()) ? OnOffType.ON : OnOffType.OFF;
			this.eventPublisher.postUpdate(itemName, status);
		}
	}

	private PLCBusBindingConfig tryGetConfigFor(String itemName) {
		for (PLCBusBindingProvider provider : this.providers) {
			PLCBusBindingConfig config = provider.getConfigFor(itemName);
			if (config != null) {
				return config;
			}
		}
		return null;
	}

	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {

		if (config == null) {
			return;
		}
		
		createSerialPort(config);
		
		createPLCBusController(config);
	}

	private void createPLCBusController(Dictionary<String, ?> config) {
		String configuredProtocol = (String) config.get("protocol");
		
		if (configuredProtocol == null) {
			configuredProtocol = "OnePhase";
		}
		
		IPLCBusProtocol protocol = configuredProtocol.equals("ThreePhase") ? new ThreePhasePLCBusProtocol() : new OnePhasePLCBusProtocol();		
		
		plcBusController = PLCBusController.create(new SerialPortGateway(serialPort), protocol);
	}

	private void createSerialPort(Dictionary<String, ?> config) {
		serialPort = new NRSerialPortAdapter((String) config.get("port"), 9600);

		if (serialPort == null) {
			logger.error("No Serialport config in openhab.cfg found");
		}
		
		try {
			serialPort.open();
		} catch (SerialPortException e) {
			logger.error("Failed to open Serialport: %s", e.getMessage());
		}
	}
	
}
