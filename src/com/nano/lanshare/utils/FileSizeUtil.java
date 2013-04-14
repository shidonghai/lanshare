package com.nano.lanshare.utils;

import java.math.BigDecimal;

public class FileSizeUtil {
	public static final int BYTES = 0;
	public static final int KB = 1;
	public static final int MB = 2;
	public static final int GB = 3;

	public static String formatFromByte(long bytes) {
		float size = bytes;
		int suffix = 0;
		while (size > 512) {
			size /= 1024;
			suffix++;
		}
		StringBuilder result = new StringBuilder();
		BigDecimal format = new BigDecimal(Float.toString(size));
		result.append(format.setScale(2, BigDecimal.ROUND_HALF_UP));
		switch (suffix) {
		case BYTES:
			result.append("bytes");
			break;
		case KB:
			result.append("KB");
			break;
		case MB:
			result.append("MB");
			break;
		case GB:
			result.append("GB");
			break;
		}
		return result.toString();
	}
}
