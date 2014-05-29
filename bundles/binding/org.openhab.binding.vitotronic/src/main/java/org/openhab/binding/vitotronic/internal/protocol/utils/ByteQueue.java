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
package org.openhab.binding.vitotronic.internal.protocol.utils;

import java.util.ArrayDeque;
import java.util.Queue;


/**
 * @author Robin Lenz
 * @since 1.0.0
 */
public class ByteQueue implements IByteQueue {

	private Queue<Byte> queue;
	
	public ByteQueue() {
		queue = new ArrayDeque<Byte>();
	}
	
	public ByteQueue(byte[] bytes) {
		this();
		
		for (byte currentByte : bytes)
		{
			enque(currentByte);
		}
	}
	
	@Override
	public boolean hasBytesEnqued() {
		return size() > 0;
	}
	
	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public void enque(byte item) {
		queue.add(item);
	}

	@Override
	public void enque(int item) {
		queue.add((byte)item);
	}
	
	@Override
	public void enqueAll(IByteQueue otherByteQueue) {
		if (otherByteQueue == null) {
			return;
		}
		
		byte[] bytesFromOtherByteQueue = otherByteQueue.toByteArray();
		
		enqueAll(bytesFromOtherByteQueue);
	}
	
	@Override
	public void enqueAll(byte[] bytesToEnque) {
		if (bytesToEnque == null) {
			return;
		}
		
		for (byte currentByte : bytesToEnque)
		{
			enque(currentByte);
		}
	}

	@Override
	public byte deque() {
		return queue.poll();
	}

	@Override
	public byte[] toByteArray() {
		byte[] array = new byte[queue.size()];
		
		int pos = 0;
		
		for (Byte currentByte : queue)
		{
			array[pos] = currentByte;
			pos++;
		}
		
		return array;
	}
}
