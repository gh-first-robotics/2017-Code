package org.usfirst.frc.team5530.robot;

public class Util {
	/**
	 * Clamps a double value to between a minimum and maximum value
	 * @param v the value to clamp
	 * @param min the minimum value
	 * @param max the maximum value
	 * @return the clamped value
	 */
	public static double clamp(double v, double min, double max) {
		if (v <= min)
			return min;
		
		if (v >= max)
			return max;
		
		return v;
	}
	
	/**
	 * Calculates the minimum value out of any number of values
	 * @param a the first value
	 * @param all all the rest
	 * @return the minimum value
	 */
	public static double min(double a, double... all) {
		double min = a;
		
		for (int i = 0; i < all.length; i++) {
			min = Math.min(min, all[i]);
		}
		
		return min;
	}
	
//	private static Timer timer = new Timer("Delay timer");
//	/**
//	 * Schedules a callback to be triggered after a delay
//	 * @param callback the callback
//	 * @param delay the delay
//	 */
//	public static void delay(Callback callback, int delay) {
//		timer.schedule(new TimerTask() {
//			public void run() {
//				callback.apply();
//			}
//		}, delay);
//	}
//	
//	public static void schedule(Callback cb, int between) {
//		timer.schedule(new TimerTask() {
//			public void run() {
//				cb.apply();
//			}
//		}, between, between);
//	}
}
