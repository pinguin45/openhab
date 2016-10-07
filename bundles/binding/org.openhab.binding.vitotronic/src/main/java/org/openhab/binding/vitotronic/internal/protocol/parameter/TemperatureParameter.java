package org.openhab.binding.vitotronic.internal.protocol.parameter;

import org.openhab.binding.vitotronic.internal.protocol.ParameterDetails;
public class TemperatureParameter extends DecimalParameter {

	private int quotient;
	
	public TemperatureParameter(ParameterDetails details, int quotient) {
		super(details);
		
		this.quotient = quotient;
	}
	
	@Override
	public Double getValue() {
		Double value = super.getValue();
		
		return value / quotient;
	}

	@Override
	public void setValue(Double value) {
		Double valueMultipliedWithQuotient = value * quotient;
		
		super.setValue(valueMultipliedWithQuotient);
	}
}
