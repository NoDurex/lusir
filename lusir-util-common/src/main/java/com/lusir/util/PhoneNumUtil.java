package com.lusir.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

/**
 * 
 * @author NoDurex Zhang
 *
 */
public class PhoneNumUtil {
	static final Logger log = LoggerFactory.getLogger(PhoneNumUtil.class.getName());

	/**
	 * Check phone number if it is valid.
	 * 
	 * @param phone
	 * @return if the phone number is valid, return international format string, else return null.
	 */
	public static String checkPhone(String phone) {
		String countryCode = "86";
		return checkPhone(phone, countryCode);
	}
	
	/**
	 * 
	 * @param phone
	 * @return
	 */
	public static String checkPhoneWithnoCountrycode(String phone){
		String result =  checkPhone(phone);
		if(StringUtil.isBlank(result)){
			return null;
		}
		return phone.trim();
	}
	
	/**
	 * Check phone number if it is valid.
	 * 
	 * @param phone
	 * @param countryCode
	 * @return if the phone number is valid, return international format string, else return null.
	 */
	public static String checkPhone(String phone, String countryCode) {
		return checkPhone(phone, countryCode, false);
	}
	
	/**
	 * Check phone number if it is valid.
	 * 
	 * @param phone
	 * @param national
	 * @return if the phone number is valid, return null while phone number is invalid.
	 */
	public static String checkPhone(String phone, boolean national) {
		return checkPhone(phone, "86", national);
	}
	
	/**
	 * Check phone number if it is valid.
	 * 
	 * @param phone
	 * @param countryCode
	 * @param national
	 * @return if the phone number is valid, return null while phone number is invalid.
	 */
	public static String checkPhone(String phone, String countryCode, boolean national) {
		PhoneNumberUtil util = PhoneNumberUtil.getInstance();
		String region = null;
		try {
			if(countryCode == null || countryCode.isEmpty()) countryCode = "86";
			region = util.getRegionCodeForCountryCode(Integer.valueOf(countryCode));
			
			PhoneNumber number = util.parse(phone, region);
			if(!util.isValidNumber(number)) {
				return null;
			}
			
			String formatPhone = util.format(number,
					national ? PhoneNumberUtil.PhoneNumberFormat.NATIONAL
							: PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
			return PhoneNumberUtil.normalizeDigitsOnly(formatPhone);
			
		} catch (Exception e) {
			log.error("Fail to check phone number.", e);
		} 
		
		return null;
	}
}