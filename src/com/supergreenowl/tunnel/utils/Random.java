package com.supergreenowl.tunnel.utils;

/**
 * Provides access to static instance of {@code java.util.Random} to prevent lots of classes creating their own instances.
 * @author luke
 *
 */
public class Random {

	private static java.util.Random generator = new java.util.Random();
	
	private Random() {
		// You can't instantiate me.
	}
	
	/**
	 * Returns a pseudo-random number {@code x} such that {@code 0 <= x < n}.
	 * @param n Exclusive maximum limit.
	 * @return Integer.
	 */
	public static int nextInt(int n) {
		return generator.nextInt(n);
	}
	
}
