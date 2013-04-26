package com.nano.lanshare.friend.logic;

import java.io.BufferedInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import android.content.Context;
import android.text.TextUtils;

public class PinYin {
	public static final int BUFFER_SIZE = 13;

	public static final String TABLE_PATH = "pinyin.txt";

	public static final String TABLE_PATH_DUO_YIN = "duoyinzi.txt";

	private static PinYin instance = new PinYin();

	private static Context mContext;

	InputStream inputStream = null;

	InputStream inputStreamDuoYin = null;

	private Properties unicodeToHanyuPinyinTable = null;

	private String tempStr = "";

	private Vector<String> vec = new Vector<String>();

	public synchronized static PinYin getInstance(Context context) {
		PinYin.mContext = context;
		return instance;
	}

	/**
	 * 
	 * get the line string of the input stream file.
	 * 
	 * @param stream
	 *            the input stream to be got line string.
	 * @param index
	 *            the line number
	 * @return line string
	 */
	private synchronized String getLineStr(InputStream stream, int index) {
		try {
			stream.reset();
			String line = new String(getBytes(stream, index));
			// Parse the current line...
			return line;
		} catch (IOException e) {
			return "NULL";
		}
	}

	/**
	 * 
	 * get the line bytes of the input stream
	 * <p>
	 * Firstly, the input stream should skip the data before the index line.
	 * Then put the current line data into a buffer which size is BUFFER_SIZE.
	 * The BUFFER_SIZE is the size of each line. Finally, write the buffer into
	 * byteArrayOutputStream and return
	 * </P>
	 * 
	 * @param in
	 *            the target input stream
	 * @param index
	 *            line number
	 * @return the data of the index line.
	 * @throws IOException
	 */
	private synchronized byte[] getBytes(InputStream in, int index)
			throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];

		in.skip(index * (BUFFER_SIZE));
		in.read(buffer, 0, BUFFER_SIZE);

		return buffer;
	}

	/**
	 * 
	 * Get the index for the pinyin converting.
	 * <p>
	 * The pinyin table is increasing sequence from. Get the high byte and low
	 * byte of the char and use the formula followed. index = (1 + (high -
	 * 0x4E)*256 + low); Expect the first line which is 3007 to be return
	 * directed.
	 * <p>
	 * 
	 * @param nameChar
	 *            the char to be converted pinyin.
	 * @return the index(or line number) point to pinyin
	 *         table(unicode_to_changed.txt).
	 */
	private int getUnicodeIndex(char nameChar) {
		int hNum = 0;
		int lNum = 0;

		if (nameChar > 256) {
			hNum = (nameChar & 0xff00) >> 8;
			lNum = (nameChar & 0xff);

			if (hNum == 0x30) {
				return 0;
			} else {
				return (1 + (hNum - 78) * 256 + lNum);
			}
		}

		return -1;
	}

	/**
	 * 判断是否为中文.
	 * 
	 * @param c
	 *            : 需要判断的字符.
	 * @return
	 */
	public static boolean isChinese(char c) {
		if (c == '\u3007' || (c >= '\u4E00' && c <= '\u9FA5')
				|| (c >= '\uF900' && c <= '\uFA2D')) {
			return true;
		}
		return false;
	}

	/**
	 * Test if the specified string has a Chinese char.
	 * 
	 * @param s
	 * @return
	 */
	public static boolean hasChinese(String s) {
		if (null == s) {
			return false;
		}
		char[] sChar = s.toCharArray();
		for (char c : sChar) {
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回文件流(unicode_to_hanyu_pinyin.txt)
	 * 
	 * @param paramString
	 * @return
	 */
	private BufferedInputStream getResourceInputStream(String paramString) {
		return new BufferedInputStream(
				PinYin.class.getResourceAsStream(paramString));
	}

	/**
	 * 装载pinyin.txt和duoyin.txt文件数据至InputStream中
	 */
	private void loadFileToStream() {
		try {
			if (inputStreamDuoYin == null) {
				// 读取duoyinzi.txt文件(assets文件夹下)
				inputStreamDuoYin = mContext.getAssets().open(
						TABLE_PATH_DUO_YIN);
				if (unicodeToHanyuPinyinTable == null) {
					// 初始化
					unicodeToHanyuPinyinTable = new Properties();

					// 装载数据(数据已key和value的形式存入)
					unicodeToHanyuPinyinTable.load(inputStreamDuoYin);
				}
			}
			if (inputStream == null) {
				// 读取pinyin.txt文件(assets文件夹下)
				inputStream = mContext.getAssets().open(TABLE_PATH);
			}
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
	}

	/**
	 * 非多音字查询转拼音
	 */
	private void dealNonDuoyinStrting(char nameChar) {
		int index = -1;
		int spaceIndex = 0;
		int commIndex = 0;
		// 先到map里面去查找
		tempStr = FriendDataManager.getFriendDataManager().findPinyinInCache(
				String.valueOf(nameChar));
		if (null == tempStr) {
			if (inputStream != null) {
				// 取出对应pinyin.txt里面对应的line
				index = getUnicodeIndex(nameChar);

				// get the line string from the inputStream
				if (index != -1) {
					// 通过line去查找对应的字符
					tempStr = getLineStr(inputStream, index);
				} else {
					tempStr = "";
				}

				// cat the pinyin string from line string
				spaceIndex = tempStr.indexOf(" ");
				commIndex = tempStr.indexOf(",");

				if (spaceIndex != -1 && commIndex != -1) {
					// 依据分隔符获取到对应的拼音
					tempStr = tempStr.substring(spaceIndex + 1, commIndex);
					// 添加至map中
					FriendDataManager.getFriendDataManager().cachePinyin(
							String.valueOf(nameChar), tempStr);
				}
			}
		}
		vec.add(tempStr);

	}

	/**
	 * Get the pinyin string array of the name string.
	 * 
	 * @param nameStr
	 *            the name string to be converted to pinyin
	 * @return the pinyin string array
	 */
	public String[] getPinYinStringArray(String nameStr) {
		if (nameStr == null) {
			return null;
		}

		loadFileToStream();
		vec.clear();

		if (unicodeToHanyuPinyinTable != null) {
			try {
				char[] nameChar = nameStr.toCharArray();
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < nameChar.length; i++) {
					// 如果不是中文则直接加进来
					if (!isChinese(nameChar[i])) {
						// 如果是空格也加进来
						if (nameChar[i] == ' ') {
							vec.add(" ");
						} else {
							// 不是空格的话应该直接按照顺序加在名字拼音后面
							buffer.append(nameChar[i]);
							vec.add(buffer.toString());
							buffer.delete(0, buffer.length());
						}
						continue;
					}
					// 如果是中文字符
					String str1 = getHanyuPinyinRecordFromChar(nameChar[i]);
					if (null != str1) {
						int m = str1.indexOf("(");
						int j = str1.lastIndexOf(")");
						String str2 = str1.substring(m + "(".length(), j);
						vec.add(str2.replaceAll("[1-5]", ""));
					}
					// 说明多音字txt文件中没有此字，也就是说这个不是多音字
					else {
						dealNonDuoyinStrting(nameChar[i]);
					}
				}
			} catch (Exception e) {
				// log
			}
		}

		mContext = null;
		String[] strs = new String[vec.size()];
		for (int i = 0; i < vec.size(); i++) {
			strs[i] = vec.get(i);
		}
		return strs;
	}

	private boolean isValidRecord(String paramString) {
		return (null != paramString) && (!paramString.equals("(none0)"))
				&& (paramString.startsWith("(")) && (paramString.endsWith(")"));
	}

	/**
	 * 通过key值获取文件中的value
	 * 
	 * @param paramChar
	 * @return
	 */
	private String getHanyuPinyinRecordFromChar(char paramChar) {
		// 每个中文char对应一个整形
		int i = paramChar;
		// 将此整形数转为16进制的字符串
		String str1 = Integer.toHexString(i).toUpperCase();
		// 通过key值去Property中查value值，其实就是去unicode_to_hanyu_pinyin.txt里面查找
		String str2 = unicodeToHanyuPinyinTable.getProperty(str1);
		return isValidRecord(str2) ? str2 : null;
	}

	/**
	 * Get the pinyin string array of the name string.
	 * 
	 * @param nameStr
	 *            the name string to be converted to pinyin
	 * @return the pinyin string array
	 */
	public String[] getFriendPinYinStringArray(String nameStr) {
		if (nameStr == null) {
			return null;
		}

		Vector<String> vec = new Vector<String>();
		String tempStr = "";
		try {
			if (inputStream == null) {
				// 读取pinyin.txt文件(assets文件夹下)
				inputStream = mContext.getAssets().open(TABLE_PATH);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (inputStream != null) {
			try {
				char[] nameChar = nameStr.toCharArray();

				int index = -1;

				int spaceIndex = 0;
				int commIndex = 0;

				for (int i = 0; i < nameChar.length; i++) {
					// 如果不是中文则直接加进来
					if (!isChinese(nameChar[i])) {
						vec.add(String.valueOf(nameChar[i]).toLowerCase());
						continue;
					}

					tempStr = FriendDataManager.getFriendDataManager()
							.findPinyinInCache(String.valueOf(nameChar[i]));

					if (TextUtils.isEmpty(tempStr)) {
						// 如果是中文字符
						// 取出对应pinyin.txt里面对应的line
						index = getUnicodeIndex(nameChar[i]);

						// get the line string from the inputStream
						if (index != -1) {
							// 通过line去查找对应的字符
							tempStr = getLineStr(inputStream, index);
						} else {
							tempStr = "";
						}

						// cat the pinyin string from line string
						spaceIndex = tempStr.indexOf(" ");
						commIndex = tempStr.indexOf(",");

						if (spaceIndex != -1 && commIndex != -1) {
							// 依据分隔符获取到对应的拼音
							tempStr = tempStr.substring(spaceIndex + 1,
									commIndex);

							FriendDataManager.getFriendDataManager()
									.cachePinyin(String.valueOf(nameChar[i]),
											tempStr);
						}
					}

					vec.add(tempStr);
				}

			} catch (Exception e) {
				// log
			}
		}

		mContext = null;
		String[] strs = new String[vec.size()];
		for (int i = 0; i < vec.size(); i++) {
			strs[i] = vec.get(i);
		}

		return strs;
	}

	/**
	 * 
	 * Get the pinyin string of the name string.
	 * 
	 * @param nameStr
	 *            the name string to be converted to pinyin
	 * @return the pinyin string
	 */
	public String getPinyinString(String nameStr) {
		if (nameStr == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer("");
		String tempStr = "";
		try {
			if (inputStream == null) {
				inputStream = mContext.getAssets().open(TABLE_PATH);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (inputStream != null) {
			try {
				char[] nameChar = nameStr.toCharArray();

				int index = -1;

				// the beginIndex and endIndex of subString from lineStr
				int spaceIndex = 0;
				int commIndex = 0;

				for (int i = 0; i < nameChar.length; i++) {
					if (!isChinese(nameChar[i])) {
						sb.append(nameChar[i]);
						continue;
					}

					index = getUnicodeIndex(nameChar[i]);

					// get the line string from the inputStream
					if (index != -1) {
						tempStr = getLineStr(inputStream, index);
					} else {
						tempStr = "";
					}

					// cat the pinyin string from line string
					spaceIndex = tempStr.indexOf(" ");
					commIndex = tempStr.indexOf(",");

					if (spaceIndex != -1 && commIndex != -1) {
						tempStr = tempStr.substring(spaceIndex + 1, commIndex);
					}

					sb.append(tempStr);
				}

			} catch (Exception e) {
				// log
				sb.append("");
			}
		}
		mContext = null;
		return sb.toString();
	}

	public String getPinyinString(char nameChar) {
		StringBuffer sb = new StringBuffer("");
		String tempStr = "";
		if (inputStream == null) {
			try {
				inputStream = mContext.getAssets().open(TABLE_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (inputStream != null) {
			try {
				int index = -1;

				// the beginIndex and endIndex of subString from lineStr
				int spaceIndex = 0;
				int commIndex = 0;

				index = getUnicodeIndex(nameChar);

				// get the line string from the inputStream
				if (index != -1) {
					tempStr = getLineStr(inputStream, index);
				} else {
					tempStr = "";
				}

				// cat the pinyin string from line string
				spaceIndex = tempStr.indexOf(" ");
				commIndex = tempStr.indexOf(",");

				if (spaceIndex != -1 && commIndex != -1) {
					tempStr = tempStr.substring(spaceIndex + 1, commIndex);
				}

				sb.append(tempStr);

			} catch (Exception e) {
				sb.append("");
			}
		}

		return sb.toString();
	}

	public void free() {
		if (inputStream != null) {
			try {
				inputStream.close();
				inputStream = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (inputStreamDuoYin != null) {
			try {
				inputStreamDuoYin.close();
				inputStreamDuoYin = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (unicodeToHanyuPinyinTable != null) {
			unicodeToHanyuPinyinTable.clear();
			unicodeToHanyuPinyinTable = null;
		}
		mContext = null;
	}

	/**
	 * judge that the phone number is valid or not.
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isValidPhoneNumber(String text) {
		char[] sChar = text.toCharArray();
		for (char c : sChar) { // “,;*#+�?
			if ((c >= '0' && c <= '9') || c == ',' || c == ';' || c == '*'
					|| c == '#' || c == '+') {
				continue;
			} else {
				return false;
			}
		}

		return true;
	}

	/**
	 * filter invalid characters from a number.
	 * 
	 * @param originalText
	 * @return
	 */
	public static String filterInvalidNumbers(String originalText) {
		StringBuffer buffer = new StringBuffer(originalText);
		for (int i = 0; i < buffer.length(); i++) {
			char c = buffer.charAt(i);
			if ((c >= '0' && c <= '9') || c == ',' || c == ';' || c == '*'
					|| c == '#' || c == '+') {
				continue;
			} else {
				buffer.deleteCharAt(i);
			}
		}
		return buffer.toString();
	}
}
