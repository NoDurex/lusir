package com.lusir.util;

/**
 * 
 * @author NoDurex Zhang
 *
 */
public class UniqueRandomNumberGenerator {
	
	private long factor;
	
	private long hfactor;
	
	private long salt;
	
	private long offset;
	
	private long upperLimit;
	
	public UniqueRandomNumberGenerator(String factor, String salt, String offset) {
		this.factor = Long.parseLong(factor, 16);

		if (this.factor <= 1) {
			throw new RuntimeException("Invalid factor.");
		}
		
		this.hfactor = this.factor / 2;
		this.salt = Long.parseLong(salt, 16);
		this.offset = Long.parseLong(offset, 16);
		
		// calculate upper limit
		long f = this.factor;
		int count = 0;
		while(f > 0) {
			f >>= 1;
			count++;
		}
		
		this.upperLimit = (1 << count) - 1;

		if (this.salt < 1 || this.salt >= this.upperLimit) {
			throw new RuntimeException("Invalid salt.");
		}

		if (this.offset < 0 || this.offset >= this.upperLimit) {
			throw new RuntimeException("Invalid offset.");
		}
	}
	
	private long permute(long x) {
		if (x > factor)
			return x;

		long r = (x * x) % factor;

		if (x > hfactor)
			r = factor - r;

		return r;
	}
	
	public long generate(long seqid) {
		long r = permute(seqid) + this.offset;
		if (r > this.upperLimit) {
			r -= this.upperLimit;
		}
		return permute(r ^ salt);
	}
	
	public long getUpperLimit() {
		return this.upperLimit;
	}
}