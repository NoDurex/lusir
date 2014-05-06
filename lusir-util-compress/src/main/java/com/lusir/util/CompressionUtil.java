package com.lusir.util;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

public class CompressionUtil {
	
	private static Logger log = LoggerFactory.getLogger(CompressionUtil.class);
	
	private static LZ4Factory factory = null;
	
	private static LZ4Compressor compressor = null;
	
	private static LZ4FastDecompressor decompressor = null;
	
	static {
		factory = LZ4Factory.fastestJavaInstance();
		compressor = factory.fastCompressor();
		decompressor = factory.fastDecompressor();
	}
	
	public static byte[] compress(byte[] rawData){
		if (rawData == null) {
			return null;
		}
		
		int rawLength = rawData.length;
		int maxCompressedLength = compressor.maxCompressedLength(rawData.length);
		
		// compress
		byte[] compressed = new byte[maxCompressedLength];
		int compressedLength = compressor.compress(rawData, 0, rawLength, compressed, 0, maxCompressedLength);
		
		// trim & push raw length
		byte[] trimmedBuffer = new byte[compressedLength + 4];
		byte[] len = ByteBuffer.allocate(4).putInt(rawLength).array();

		System.arraycopy(len, 0, trimmedBuffer, 0, 4);
		System.arraycopy(compressed, 0, trimmedBuffer, 4, compressedLength);
		
		return trimmedBuffer;
	}

	public static byte[] compress(String raw) {
		if(raw == null){
			return null;
		}
		byte[] rawData = StringUtil.getBytes(raw);
		return compress(rawData);
	}
	
	public static String decompress(byte[] compressed) {
		if(compressed == null){
			return null;
		}
		int length = ByteBuffer.allocate(4).put(compressed, 0, 4).getInt(0);
	    byte[] restored = decompressor.decompress(compressed, 4, length);
	    return StringUtil.fromBytes(restored);
	}
	
	public static byte[] decompress2Byte(byte[] compressed) {
		if(compressed == null){
			return null;
		}
		int length = ByteBuffer.allocate(4).put(compressed, 0, 4).getInt(0);
		
		if (length <= 0 || length > 0x4ffff) {
			log.error("decompress2Byte error compressed data.");
			return compressed;
		}
	    return decompressor.decompress(compressed, 4, length);
	}
}
