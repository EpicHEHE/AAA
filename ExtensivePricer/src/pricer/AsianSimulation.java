package pricer;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import pricer.spi.Algorithm;

public class AsianSimulation implements Algorithm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String algorithmName;
	private ArrayList<String> productName=new ArrayList<String>();
	TreeMap<String, Double> parameter = new TreeMap<String, Double>();
	

	public AsianSimulation() {
		algorithmName = "AsianSimulation";
		productName.add("AsianOption");
		parameter.put("sNaught", 0.0);
		parameter.put("strike", 0.0);
		parameter.put("volatility", 0.0);
		parameter.put("riskFreeRate", 0.0);
		parameter.put("term", 0.0);
		parameter.put("numTimeIntervals", 0.0);
		parameter.put("numTrials", 0.0);

	}

	@Override
	public String getAlgorithmName() {
		return algorithmName;
	}

	@Override
	public TreeMap<String, Double> getParameterMap() {
		return parameter;
	}

	@Override
	public ArrayList<String> getProductName() {
		return productName;
	}

	@Override
	public double[] calculate() {
		double sNaught = parameter.get("sNaught");
		double strike = parameter.get("strike");
		double volatility = parameter.get("volatility");
		double riskFreeRate = parameter.get("riskFreeRate");
		double term = parameter.get("term");
		double numTimeIntervals = parameter.get("numTimeIntervals");
		double numTrials = parameter.get("numTrials");

		int i, trialCount;
		double deltaT = term / (double) numTimeIntervals;
		double trialRunningSum, trialAverage, trialCallPrice, trialPutPrice;
		double simulationSumCallPrice, simulationSumPutPrice, simulationAverageCallPrice, simulationAveragePutPrice;
		double s;
		double[] result = new double[2];
		Random rand = new Random();
		simulationSumCallPrice = 0.0;
		simulationSumPutPrice = 0.0;
		for (trialCount = 1; trialCount <= numTrials; trialCount++) {
			s = sNaught;
			trialRunningSum = 0.0;
			double nns = 0;
			for (i = 0; i < numTimeIntervals; i++) {
				nns = rand.nextGaussian();
				s = s
						* Math.exp((riskFreeRate - volatility * volatility / 2)
								* deltaT + volatility * nns * Math.sqrt(deltaT));
				trialRunningSum += s;

			}
			trialAverage = trialRunningSum / numTimeIntervals;
			// if (side == true) // call option
			trialCallPrice = Math.max(trialAverage - strike, 0.0);
			// else // put option
			trialPutPrice = Math.max(strike - trialAverage, 0.0);
			simulationSumCallPrice += trialCallPrice;
			simulationSumPutPrice += trialPutPrice;
		}
		simulationAverageCallPrice = simulationSumCallPrice / numTrials;
		simulationAveragePutPrice = simulationSumPutPrice / numTrials;

		
		result[0] = simulationAverageCallPrice * Math.exp(-riskFreeRate * term);
		result[1] = simulationAveragePutPrice * Math.exp(-riskFreeRate * term);
		return result;
	}

	@Override
	public void setParameter(TreeMap<String, Double> a) {
		parameter=a;
	}

}
