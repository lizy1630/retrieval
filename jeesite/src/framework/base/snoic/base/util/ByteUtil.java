/**
 * Copyright 2010 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package framework.base.snoic.base.util;


/**
 * 
 * @author 
 *
 */
public class ByteUtil {
	private ByteUtil() {

	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0xFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}

	public static String byto2hex2(byte[] bin) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < bin.length; ++i) {
			int x = bin[i] & 0xFF, h = x >>> 4, l = x & 0x0F;
			buf.append((char) (h + ((h < 10) ? '0' : 'a' - 10)));
			buf.append((char) (l + ((l < 10) ? '0' : 'a' - 10)));
		}
		return buf.toString();
	}
}
