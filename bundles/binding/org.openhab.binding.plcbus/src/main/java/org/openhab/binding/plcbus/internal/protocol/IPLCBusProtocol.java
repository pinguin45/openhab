package org.openhab.binding.plcbus.internal.protocol;

public interface IPLCBusProtocol {
	IByteQueue getSendCommandBytes(PLCUnit unit, Command command);
	
	int expectedSendCommandResponseSize();
	
	ActorResponse parseResponseFromActor(IByteQueue receivedBytes);
}
