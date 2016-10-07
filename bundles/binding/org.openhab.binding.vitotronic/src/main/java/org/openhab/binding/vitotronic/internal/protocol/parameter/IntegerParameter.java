package org.openhab.binding.vitotronic.internal.protocol.parameter;

import org.openhab.binding.vitotronic.internal.protocol.IIntegerParameter;
import org.openhab.binding.vitotronic.internal.protocol.ParameterDetails;
import org.openhab.binding.vitotronic.internal.protocol.utils.Convert;

public class IntegerParameter extends Parameter implements IIntegerParameter {

	public IntegerParameter(ParameterDetails details) {
		super(details);
	}

	@Override
	public Integer getValue() {
		return Convert.toInteger(getDataBytes());
	}

	@Override
	public void setValue(Integer value) {		
		byte[] dataBytes = Convert.toByteArray(value);
		
		setDataBytes(dataBytes);
	}
}
