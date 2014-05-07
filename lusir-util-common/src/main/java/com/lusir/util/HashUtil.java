package com.lusir.util;

import java.security.MessageDigest;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;

/**
 * 
 * @author NoDurex Zhang
 *
 */
public class HashUtil {
	
	public static String getHash(String message){
		return getMD5Hash(message.getBytes());
	}

	public static String getRandomHash(){
		Random ran=new Random();
		String mix=""+ran.nextDouble()+System.currentTimeMillis()+ran.nextDouble();
		return getMD5Hash(mix.getBytes());
	}
	
	public static String getRandomHash(String keyId){
		Random ran=new Random();
		String mix=keyId+ran.nextDouble()+System.currentTimeMillis()+ran.nextDouble();
		return getMD5Hash(mix.getBytes());
	}
	
	public static String getMD5(byte[] data) {
		return getMD5Hash(data);
	}
	
	private static String getMD5Hash(byte[] data) {
		if (data == null) {
			return null;
		}

		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(data);
			byte[] hash = digest.digest();
			return Hex.encodeHexString(hash);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}
	
	public static String getSHA256(byte[] data) {
		if (data == null) {
			return null;
		}

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(data);
			byte[] hash = digest.digest();
			return Hex.encodeHexString(hash);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}
	
	public static String getMd5WithTimeline(long time){
		String hash=getRandomHash();
		hash=hash.substring(0,19)+time;
		return hash;
	}
}
