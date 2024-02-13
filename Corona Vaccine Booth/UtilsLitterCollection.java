package Assignment1;

import java.util.Random;

/**
 * This is a "static" class for library routines. At the moment, it is only used for generating inter-arrival and service times. 
 * Note that when drawing from statistical distributions outside the course Simulation, it is better if you use a library like Apache Math, as it has an entire 
 * development team backing it up, and is thus correct, well-designed, and fast. 
 * 
 * @author Nemanja Milovanovic
 *
 */

public final class UtilsLitterCollection {

	private UtilsLitterCollection() {
		
	}
	
	/**
	 * @param random	{@link Random} object used to draw pseudo-random numbers
	 * @param lambda	Arrival rate
	 * @return			Returns a realization drawn from an exponential distribution, with rate {@code lambda}, 
	 * 					representing the next inter-arrival time.
	 */
	public static double nextInterArrivalTime(Random random, double lambda) {
		double r = random.nextDouble();
		return -Math.log(1-r)/lambda;
	}
	
	/**
	 * This method is identical to {@link #nextInterArrivalTime(Random, double)}.
	 * 
	 * @param random	{@link Random} object used to draw pseudo-random numbers
	 * @param mu		Service rate
	 * @return			Returns a realization drawn from an exponential distribution, with rate {@code mu}, 
	 * 					representing the next service time.
	 */
	public static double nextServiceTime(Random random, double mu) {
		return nextInterArrivalTime(random, mu);
	}
}
