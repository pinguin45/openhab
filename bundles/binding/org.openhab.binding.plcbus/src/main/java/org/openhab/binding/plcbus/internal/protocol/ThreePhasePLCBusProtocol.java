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
package org.openhab.binding.plcbus.internal.protocol;

/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class ThreePhasePLCBusProtocol implements IPLCBusProtocol {

	@Override
	public IByteQueue getSendCommandBytes(PLCUnit unit, Command command) {
		TransmitFrame transmitFrame = createTransmitFrame(unit.getUsercode(), unit.getAddress(), command);
		
		return new ByteQueue(Convert.toByteArray(transmitFrame.getBytes()));
	}

	private TransmitFrame createTransmitFrame(String usercode, String address, Command command) {
		CommandFrame commandFrame = new CommandFrame(command);
		commandFrame.setThreePhaseTo(true);
		commandFrame.setDemandAckTo(true);

		DataFrame data = new DataFrame(commandFrame);
		data.setUserCode(usercode);
		data.setAddress(address);

		TransmitFrame frame = new TransmitFrame(data);

		return frame;
	}

	@Override
	public int expectedSendCommandResponseSize() {
		return 36;
	}

	@Override
	public ActorResponse parseResponseFromActor(IByteQueue receivedBytes) {
		if (receivedBytes.size() != expectedSendCommandResponseSize()) {
			return null;
		}
				
		getReceiveFrameOf(receivedBytes);
		getReceiveFrameOf(receivedBytes);
		getReceiveFrameOf(receivedBytes);
		ReceiveFrame receiveFrameFromActor = getReceiveFrameOf(receivedBytes);
		
		if (receiveFrameFromActor == null) {
			return null;
		}
		
		return new ActorResponse
				(
						true,
						receiveFrameFromActor.getCommand(),
						receiveFrameFromActor.getFirstParameter(),
						receiveFrameFromActor.getSecondParameter()
				);
	}
	
	private ReceiveFrame getReceiveFrameOf(IByteQueue receivedBytes) {
		byte currentByte = receivedBytes.deque();

		if (currentByte == Frame.START_BYTE) {
			int length = receivedBytes.deque();

			if (length > 0) {
				byte[] data = receivedBytes.deque(length + 1);

				ReceiveFrame frame = new ReceiveFrame();
				frame.parse(data);

				return frame;
			}
		}
		
		return null;
	}
}
