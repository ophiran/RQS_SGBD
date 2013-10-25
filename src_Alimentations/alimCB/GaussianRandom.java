package alimCB;

public class GaussianRandom {
	private static Double newRandom;
	private static Double previousMean;
	private static Double previousStdDev;
	
	
	public static double random(Double mean, Double stddev) {
		if (newRandom != null && mean.compareTo(previousMean) == 0 
				&& stddev.compareTo(previousStdDev) == 0) {
			double toReturn = newRandom;
			newRandom = null;
			return toReturn;
		} else {
			double theta = 2 * Math.PI * Math.random();
			double rho = Math.sqrt(-2 * Math.log(1 - Math.random()));
			double scale = stddev * rho;
			double x = mean + scale * Math.cos(theta);
			double y = mean + scale * Math.sin(theta);
			previousMean = mean;
			previousStdDev = stddev;
			newRandom = y;
			return x;
		}
		
		
	}

}
