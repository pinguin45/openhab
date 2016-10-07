package org.openhab.binding.vitotronic.internal.protocol.parameter;

import org.openhab.binding.vitotronic.internal.protocol.IDecimalParameter;
import org.openhab.binding.vitotronic.internal.protocol.ParameterDetails;
import org.openhab.binding.vitotronic.internal.protocol.utils.Convert;

public class DecimalParameter extends Parameter implements IDecimalParameter {

	/**
	 * @param details
	 */
	public DecimalParameter(ParameterDetails details) {
		super(details);
	}

	@Override
	public Double getValue() {
		return (double) Convert.toInteger(getDataBytes());
	}

	@Override
	public void setValue(Double value) {
		byte[] dataBytes = Convert.toByteArray(value.intValue());
		
		setDataBytes(dataBytes);
	}

}
