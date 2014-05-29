/**
 * 
 */
package org.openhab.binding.vitotronic.internal.protocol.utils.test;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.vitotronic.internal.protocol.utils.*;

/**
 * @author Robin Lenz
 *
 */
public class To {
	public static IByteQueue ByteQueue(String hexString) {
		return new ByteQueue(hexStringToByteArray(hexString));
	}

	public static byte[] ByteArray(String hexString) {
		return hexStringToByteArray(hexString);
	}

	private static byte[] hexStringToByteArray(String hexString) {
		return DatatypeConverter.parseHexBinary(hexString.replaceAll(" ", ""));
	}
}
