package com.lusir.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * UUID Generater(different from every jvms && servers)
 * 
 * @author NoDurex Zhang
 *
 */
public class UUIDUtil{
	
	private static final char[] DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] DIGITS_NOCASE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private boolean noCase;
    private String instanceId;
    private AtomicInteger counter;

    public UUIDUtil() {
        this(true);
    }

    public UUIDUtil(boolean noCase) {
        // 1. Machine ID
        byte[] machineId = getLocalHostAddress();

        // 2. JVM ID
        byte[] jvmId = getRandomizedTime();

        this.instanceId = bytesToString(machineId, noCase) + "-" + bytesToString(jvmId, noCase);

        // counter
        this.counter = new AtomicInteger();

        this.noCase = noCase;
    }
    
    /**
     * 将一个byte数组转换成62进制的字符串。
     */
    public static String bytesToString(byte[] bytes) {
        return bytesToString(bytes, false);
    }

    /**
     * 将一个byte数组转换成62进制的字符串。
     */
    public static String bytesToString(byte[] bytes, boolean noCase) {
        char[] digits = noCase ? DIGITS_NOCASE : DIGITS;
        int digitsLength = digits.length;

        if (bytes == null || bytes.length <= 0) {
            return String.valueOf(digits[0]);
        }

        StringBuilder strValue = new StringBuilder();
        int value = 0;
        int limit = Integer.MAX_VALUE >>> 8;
        int i = 0;

        do {
            while (i < bytes.length && value < limit) {
                value = (value << 8) + (0xFF & bytes[i++]);
            }

            while (value >= digitsLength) {
                strValue.append(digits[value % digitsLength]);
                value = value / digitsLength;
            }
        } while (i < bytes.length);

        if (value != 0 || strValue.length() == 0) {
            strValue.append(digits[value]);
        }

        return strValue.toString();
    }

    /**
     *
     * @return
     */
    private byte[] getLocalHostAddress() {
        Method getHardwareAddress;

        try {
            getHardwareAddress = NetworkInterface.class.getMethod("getHardwareAddress");
        } catch (Exception e) {
            getHardwareAddress = null;
        }

        byte[] addr;

        try {
            InetAddress localHost = InetAddress.getLocalHost();
            if (getHardwareAddress != null) {
                addr = (byte[]) getHardwareAddress.invoke(NetworkInterface.getByInetAddress(localHost)); // maybe null
            } else {
                addr = localHost.getAddress();
            }
        } catch (Exception e) {
            addr = null;
        }
        if (addr == null) {
            addr = new byte[] { 127, 0, 0, 1 };
        }
        return addr;
    }

	/**
	 *
	 * @return
	 */
    private byte[] getRandomizedTime() {
        long jvmId = System.currentTimeMillis();
        long random = new SecureRandom().nextLong();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeLong(jvmId);
            dos.writeLong(random);
        } catch (Exception e) {
        }finally{
        	try{
        		if(dos != null){
            		dos.close();
            	}
        	}catch(Exception e){}
        }
        return baos.toByteArray();
    }

    public String nextID() {
        return instanceId + "-" + longToString(System.currentTimeMillis(), noCase) + "-" + longToString(counter.getAndIncrement(), noCase);
    }
    
    /**
     * 将一个长整形转换成62进制的字符串。
     */
    public static String longToString(long longValue) {
        return longToString(longValue, false);
    }

    /**
     * 将一个长整形转换成62进制的字符串。
     */
    public static String longToString(long longValue, boolean noCase) {
        char[] digits = noCase ? DIGITS_NOCASE : DIGITS;
        int digitsLength = digits.length;

        if (longValue == 0) {
            return String.valueOf(digits[0]);
        }

        if (longValue < 0) {
            longValue = -longValue;
        }

        StringBuilder strValue = new StringBuilder();

        while (longValue != 0) {
            int digit = (int) (longValue % digitsLength);
            longValue = longValue / digitsLength;

            strValue.append(digits[digit]);
        }

        return strValue.toString();
    }
    
    public static void main(String[] args){
    	UUIDUtil uuid=new UUIDUtil();
    	long start=System.currentTimeMillis();
    	for(int i=0;i < 1000000;i++){
    		uuid.nextID();	
    	}
    	long end=System.currentTimeMillis();
    	System.out.println(end-start);
    	System.out.println(uuid.nextID());
    	System.out.println(uuid.nextID().length());
    }
}
