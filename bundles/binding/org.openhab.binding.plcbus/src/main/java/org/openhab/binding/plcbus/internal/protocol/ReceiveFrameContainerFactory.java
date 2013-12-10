/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class ReceiveFrameContainerFactory implements
		IReceiveFrameContainerFactory {
	private Map<Class<?>, ReceiveFrameCreater> creators;
	
	public ReceiveFrameContainerFactory() {
		creators = new HashMap<Class<?>, ReceiveFrameCreater>();		
		creators.put(DefaultOnePhaseReceiveFrameContainer.class, new ReceiveFrameCreater() { public IReceiveFrameContainer create() { return new DefaultOnePhaseReceiveFrameContainer(); }});		
		creators.put(StatusRequestReceiveFrameContainer.class, new ReceiveFrameCreater() { public IReceiveFrameContainer create() { return new StatusRequestReceiveFrameContainer(); }});
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.binding.plcbus.internal.protocol.IReceiveFrameContainerFactory#createReceiveFrameContainer()
	 */
	@Override
	public IReceiveFrameContainer createReceiveFrameContainerFor(Class<? extends IReceiveFrameContainer> containerClass) {
		if  (creators.containsKey(containerClass))
		{
			return creators.get(containerClass).create();
		}
		
		return null;
	}
	
	private interface ReceiveFrameCreater
	{
		IReceiveFrameContainer create();
	}
}
