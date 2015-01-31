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

import java.util.*;
import java.util.concurrent.locks.*;

import org.openhab.binding.vitotronic.*;
import org.openhab.binding.vitotronic.internal.protocol.*;
import org.openhab.binding.vitotronic.internal.protocol.P300.P300VitotronicProtocol;
import org.openhab.binding.vitotronic.internal.protocol.utils.*;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.*;
import org.openhab.core.types.Command;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.*;
import org.slf4j.*;

/**
 * Binding for Vitotronic
 * @author Robin Lenz
 *
 */
public class VitotronicBinding extends AbstractActiveBinding<VitotronicBindingProvider> implements ManagedService {

	private static Logger logger = LoggerFactory.getLogger(VitotronicBinding.class);
	
	//private ISerialPort serialPort;
	private Lock controllerLock = new ReentrantLock();
	
	private VitotronicOpenhabConfig openhabConfig;
	private IParameterFactory parameterFactory = new StaticParameterFactory();
	
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		
		createVitotronicOpenhabConfigFor(config);
		
		//initializeSerialPort();
		
		setProperlyConfigured(true);
	}

	private void createVitotronicOpenhabConfigFor(Dictionary<String, ?> config) throws ConfigurationException {
		openhabConfig = new VitotronicOpenhabConfig(config);
	}
	
	/*private void initializeSerialPort() {
		serialPort = createAndOpenSerialport();
	}*/

	private ISerialPort createAndOpenSerialport() {
		//ISerialPort serialPort = new JsscSerialPort(openhabConfig.getSerialPortName());
		ISerialPort serialPort = TCPSerialPort.Create("192.168.178.42:2000");
		
		openSerialPort(serialPort);
		
		return serialPort;
	}

	private void openSerialPort(ISerialPort serialPort) {
		try {
			serialPort.open();
			serialPort.setParameter(4800, 8, 2, 2);
		} catch (SerialPortException e) {
			logger.error("Could not open serialport: " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		VitotronicBindingConfig config = tryGetConfigFor(itemName);

		if (config == null) {
			logger.error("No config found for item %s", itemName);
			return;
		}

		controllerLock.lock();
	
		ISerialPort serialPort = createAndOpenSerialport();

		try	{
			IVitotronicController controller = createVitotronicController(serialPort);
		
			if (controller == null) 
			{
				logger.error("No Vitotronic Controller found");
				return;
			}
		
			controller.reset();
			
			if (controller.init())
			{			
				IParameter parameter = getParameterByAddress(config.getAddress());
				
				if (parameter instanceof IBooleanParameter && command instanceof OnOffType)	{
					OnOffType boolCommand = (OnOffType)command;
					
					IBooleanParameter boolParameter = (IBooleanParameter)parameter;
					boolParameter.setValue(boolCommand == OnOffType.ON);
					
					controller.write(boolParameter);
				} else if (parameter instanceof IDecimalParameter && command instanceof DecimalType) {
					DecimalType decimalCommand = (DecimalType)command;
					
					IDecimalParameter decimalParameter = (IDecimalParameter)parameter;
					decimalParameter.setValue(decimalCommand.doubleValue());
					
					controller.write(decimalParameter);
				} else if (parameter instanceof IIntegerParameter && command instanceof DecimalType) {
					DecimalType decimalCommand = (DecimalType)command;
					
					IIntegerParameter integerParameter = (IIntegerParameter)parameter;
					integerParameter.setValue(decimalCommand.intValue());
					
					controller.write(integerParameter);
				}
			}
		} finally {
			closeSerialPort(serialPort);
			controllerLock.unlock();
		}
	}

	private void closeSerialPort(ISerialPort serialPort) {
		try {
			serialPort.close();
		} catch (SerialPortException e) {
			logger.error(e.getMessage());
		}
	}

	private IVitotronicController createVitotronicController(ISerialPort serialPort) {
		
		EnsureThatSerialPortIsOpen(serialPort);
		
		ISerialPortGateway serialPortGateway = new SerialPortGateway(serialPort);
			
		IVitotronicController controller = new VitotronicController(new P300VitotronicProtocol(parameterFactory), serialPortGateway);
		return controller;
	}
	
	private void EnsureThatSerialPortIsOpen(ISerialPort serialPort) {
		if (!serialPort.isOpen()) {
			openSerialPort(serialPort);
		}
	}

	@Override
	protected void execute() {

		controllerLock.lock();
		ISerialPort serialPort = createAndOpenSerialport();
		try {	
		
			IVitotronicController controller = createVitotronicController(serialPort);
			
			if (controller == null) 
			{
				logger.error("No Vitotronic Controller found");
				return;
			}
		
			controller.reset();
			
			if (controller.init())
			{			
				List<VitotronicBindingConfig> parameterConfigs = getParameterConfigs();
				
				for (VitotronicBindingConfig parameterConfig : parameterConfigs)
				{
					try
					{
						execute(controller, parameterConfig);
					} catch (Exception  e) {
						logger.error(e.getMessage());
						eventPublisher.postUpdate(parameterConfig.getItemName(), UnDefType.UNDEF);
					}
				}
			}
		} finally {
			closeSerialPort(serialPort);
			controllerLock.unlock();
		}
	}

	private void execute(IVitotronicController controller, VitotronicBindingConfig parameterConfig) {
		IParameter parameter = getParameterByAddress(parameterConfig.getAddress());
		
		if (parameter instanceof IStringParameter) 
		{			
			String value = controller.readValueOf((IStringParameter)parameter);
			
			eventPublisher.postUpdate(parameterConfig.getItemName(), new StringType(value));
		}
		
		if (parameter instanceof IDecimalParameter) 
		{			
			double value = controller.readValueOf((IDecimalParameter)parameter);
			
			eventPublisher.postUpdate(parameterConfig.getItemName(), new DecimalType(value));
		}
		
		if (parameter instanceof IIntegerParameter) 
		{			
			int value = controller.readValueOf((IIntegerParameter)parameter);
			
			eventPublisher.postUpdate(parameterConfig.getItemName(), new DecimalType(value));
		}
		
		if (parameter instanceof IBooleanParameter) 
		{			
			boolean value = controller.readValueOf((IBooleanParameter)parameter);
			
			eventPublisher.postUpdate(parameterConfig.getItemName(), (value) ? OnOffType.ON : OnOffType.OFF);
		}
	}

	private IParameter getParameterByAddress(int address) {
		return parameterFactory.createParameterFor(address);
	}

	private List<VitotronicBindingConfig> getParameterConfigs() {
		List<VitotronicBindingConfig> result = new ArrayList<VitotronicBindingConfig>();
		
		for (VitotronicBindingProvider provider : this.providers) {
			Collection<VitotronicBindingConfig> configs = provider.getConfigurations();
			
			result.addAll(configs);
		}
		
		return result;
	}
	
	private VitotronicBindingConfig tryGetConfigFor(String itemName) {
		for (VitotronicBindingProvider provider : this.providers) {
			VitotronicBindingConfig config = provider.getConfigFor(itemName);
			if (config != null) {
				return config;
			}
		}
		return null;
	}

	@Override
	protected long getRefreshInterval() {
		return openhabConfig.getRefreshRate();
	}

	@Override
	protected String getName() {
		return "vitotronic";
	}
	
	@Override
	public void deactivate() {
	}
}
