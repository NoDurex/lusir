package com.lusir.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {
	private static final Logger log = LoggerFactory.getLogger(StringUtil.class);

	public static final String DEFAULT_CHARSET = "utf-8";
	
	public static boolean isBlank(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

	public static byte[] getBytes(String literal) {
		if (literal == null) {
			return null;
		} else {
			try {
				return literal.getBytes(DEFAULT_CHARSET);
			} catch (UnsupportedEncodingException e) {
				log.error("String conversion error. ", e);
				return null;
			}
		}
	}
	
	public static boolean isLong(String str){
		try{
			Long.parseLong(str);
		}catch(Exception e){
			return false;	
		}
		return true;
	}

	public static String fromBytes(byte[] data) {
		if (data == null) {
			return null;
		} else {
			try {
				return new String(data, DEFAULT_CHARSET);
			} catch (UnsupportedEncodingException e) {
				log.error("String conversion error. ", e);
				return null;
			}
		}
	}

	public static String getMD5Hash(String literal)
			throws NoSuchAlgorithmException {
		return getMD5Hash(getBytes(literal));
	}

	public static String getMD5Hash(byte[] buffer)
			throws NoSuchAlgorithmException {
		MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
		mdAlgorithm.update(buffer);

		byte[] digest = mdAlgorithm.digest();
		StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < digest.length; i++) {
			String plainText = Integer.toHexString(0xFF & digest[i]);
			if (plainText.length() < 2) {
				plainText = "0" + plainText;
			}
			hexString.append(plainText);
		}

		return hexString.toString();
	}
	
	/**
	 * check if the number string is a positive integer string. without exception thrown.
	 * 
	 * @param number
	 * @return if it is, return the number value, else return -1.
	 */
	public static int isPositiveInteger(String number) {
		int radix = 10;
        if (number == null) return -1;

        int result = 0;
        boolean negative = false;
        int i = 0, len = number.length();
        int limit = -Integer.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = number.charAt(0);
            if (firstChar < '0') { // Possible leading "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
                } else return -1;

                if (len == 1) return -1;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(number.charAt(i++),radix);
                if (digit < 0 || result < multmin) return -1;
                result *= radix;
                if (result < limit + digit) return -1;
                result -= digit;
            }
        } else {
            return -1;
        }
        
        return negative ? -1 : -result;
	}
	
	/**
	 * concatenate all the strings in an array with the given delimiter
	 * 
	 * @param array
	 * @param delimiter
	 * @return
	 */
	public static <T> String concatFromArray(T[] array, String delimiter) {
		if (array == null || array.length == 0) {
			return "";
		}

		StringBuilder result = new StringBuilder();
		int len = array.length;

		for (int i = 0; i < array.length; i++) {
			result.append(array[i]);

			if (i < len - 1)
				result.append(delimiter);
		}

		return result.toString();
	}
	
	public static String concat(String... params) {
		StringBuilder builder = new StringBuilder();
		for(String str : params) {
			if(str != null) builder.append(str);
		}
		
		return builder.toString();
	}
	
	public static boolean isStringValid(String value, int maxLength) {
		if (value == null || value.isEmpty() ||value.length() > maxLength) {
			return false;
		}
		
		return true;
	}
}