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
import org.openhab.binding.vitotronic.internal.protocol.utils.*;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.*;
import org.openhab.core.types.Command;
import org.osgi.service.cm.*;
import org.slf4j.*;

/**
 * Binding for Vitotronic
 * @author Robin Lenz
 *
 */
public class VitotronicBinding extends AbstractActiveBinding<VitotronicBindingProvider> implements ManagedService {

	private static Logger logger = LoggerFactory.getLogger(VitotronicBinding.class);
	
	private int refreshRate = 60000;
	private ISerialPortGateway serialPortGateway;
	private Lock controllerLock = new ReentrantLock();
	
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		
		if (config == null)
		{
			return;
		}
		
		String serialPortName = (String) config.get("port");
		
		logger.error("SerialPortName: " + serialPortName);
		
		if (!serialPortName.isEmpty())
		{		
			serialPortGateway = SerialPortGateway.create(TCPSerialPort.Create(serialPortName));
		}

		String refresh = (String)config.get("refresh");
		
		if (!refresh.isEmpty())
		{
			refreshRate = Integer.parseInt(refresh);
		}
				
		if (serialPortGateway == null)
		{
			logger.error("No Serialport config in openhab.cfg found");
		}
	}

	@Override
	public boolean isProperlyConfigured() {
		return serialPortGateway != null;
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
		
		if (serialPortGateway == null)
		{
			logger.error("Serialport is not initialized.");
			return;
		}
		
		IVitotronicController controller = VitotronicController.Create(serialPortGateway, new ReceiveByteProcessorFactory());
		
		if (controller == null) 
		{
			logger.error("No Vitotronic Controller found");
			return;
		}
		
		controllerLock.lock();
		
		controller.reset();
		
		if (controller.init())
		{			
			Parameter parameter = config.getParameter();
			
			if (parameter instanceof IWriteableBooleanParameter && command instanceof OnOffType)
			{
				OnOffType boolCommand = (OnOffType)command;
				
				IWriteableBooleanParameter boolParameter = (IWriteableBooleanParameter)parameter;
				boolParameter.set(boolCommand == OnOffType.ON);
				
				controller.write(boolParameter);
			}
		}
		
		controllerLock.unlock();
	}
	
	@Override
	protected void execute() {
		
		if (serialPortGateway == null)
		{
			logger.error("Serialport is not initialized.");
			return;
		}
		
		IVitotronicController controller = VitotronicController.Create(serialPortGateway, new ReceiveByteProcessorFactory());
		
		if (controller == null) 
		{
			logger.error("No Vitotronic Controller found");
			return;
		}
		
		controllerLock.lock();
		
		controller.reset();
		
		if (controller.init())
		{			
			List<VitotronicBindingConfig> parameterConfigs = getParameterConfigs();
			
			for (VitotronicBindingConfig parameterConfig : parameterConfigs)
			{
				execute(controller, parameterConfig);
			}
		}
		
		controllerLock.unlock();
	}

	private void execute(IVitotronicController controller, VitotronicBindingConfig parameterConfig) {
		Parameter parameter = parameterConfig.getParameter();
		
		if (parameter instanceof IStringParameter) 
		{			
			String value = controller.readValueOf((IStringParameter)parameterConfig.getParameter());
			
			eventPublisher.postUpdate(parameterConfig.getItemName(), new StringType(value));
		}
		
		if (parameter instanceof IDecimalParameter) 
		{			
			double value = controller.readValueOf((IDecimalParameter)parameterConfig.getParameter());
			
			eventPublisher.postUpdate(parameterConfig.getItemName(), new DecimalType(value));
		}
		
		if (parameter instanceof IIntegerParameter) 
		{			
			int value = controller.readValueOf((IIntegerParameter)parameterConfig.getParameter());
			
			eventPublisher.postUpdate(parameterConfig.getItemName(), new DecimalType(value));
		}
		
		if (parameter instanceof IBooleanParameter) 
		{			
			boolean value = controller.readValueOf((IBooleanParameter)parameterConfig.getParameter());
			
			eventPublisher.postUpdate(parameterConfig.getItemName(), (value) ? OnOffType.ON : OnOffType.OFF);
		}
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
		return refreshRate;
	}

	@Override
	protected String getName() {
		return "vitotronic";
	}
	
	@Override
	public void deactivate() {
		serialPortGateway.close();
	}
}
