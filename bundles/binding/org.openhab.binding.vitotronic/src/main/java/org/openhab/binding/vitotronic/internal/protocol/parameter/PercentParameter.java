package org.openhab.binding.vitotronic.internal.protocol.parameter;

import org.openhab.binding.vitotronic.internal.protocol.ParameterDetails;
import org.openhab.binding.vitotronic.internal.protocol.utils.Convert;

public class PercentParameter extends IntegerParameter {

	/**
	 * @param details
	 */
	public PercentParameter(ParameterDetails details) {
		super(details);
	}
	
	@Override
	public Integer getValue() {
		Integer value = super.getValue();
		
		return value / 2;
	}

	@Override
	public void setValue(Integer value) {		
		Integer newValue = value * 2;
		
		super.setValue(newValue);
	} 
}
