package pricer;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import pricer.spi.Algorithm;


public class BinomialTree implements Algorithm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String algorithmName;
	private ArrayList<String> productName=new ArrayList<String>();
	TreeMap<String, Double> parameter = new TreeMap<String, Double>();
	
	public BinomialTree(){
		algorithmName = "binomialTree";
		productName.add("AmericanOption");
		productName.add("EuropeanOption");
		parameter.put("sNaught", 0.0);
		parameter.put("strike", 0.0);
		parameter.put("volatility", 0.0);
		parameter.put("riskFreeRate", 0.0);
		parameter.put("term", 0.0);
		parameter.put("numTimeIntervals", 0.0);
	
	}

	@Override
	public TreeMap<String, Double> getParameterMap() {
		return parameter;
	}

	@Override
	public void setParameter(TreeMap<String, Double> a) {
		parameter=a;
	}

	@Override
	public double[] calculate() {
		double sNaught = parameter.get("sNaught");
		double strike = parameter.get("strike");
		double volatility = parameter.get("volatility");
		double riskFreeRate = parameter.get("riskFreeRate");
		double term = parameter.get("term");
		double numTimeIntervalsDouble = parameter.get("numTimeIntervals");
		int numTimeIntervals = (int)numTimeIntervalsDouble;
		double[] result = new double[2];
		final class Price {
			public double stockPrice;
			public double optionPrice;
		}
		int i, j = 0;
		double deltaT	= term / numTimeIntervals;
		// Original values suggested by Hull (actually, from Cox, Ross and Rubinstein (1979))
		//double up		= exp(volatility * sqrt(deltaT));
		//double down	= 1 / up;
		//double a		= exp(riskFreeRate * deltaT);
		//double upProb	= (a - down) / (up - down);
		//double downProb = 1.0 - upProb;
		// End Hull
		// Alternate values suggested by Prof. Hrusa
		double up		= 1.0 + riskFreeRate * deltaT + (volatility*Math.sqrt(deltaT));
		double down		= 1.0 + riskFreeRate * deltaT - (volatility*Math.sqrt(deltaT));
		double upProb	= 0.5;
		double downProb = 0.5;
		// End Hrusa
		double binomValue;
		Price[][] binomialTreePut = new Price[numTimeIntervals+1][numTimeIntervals+1];
		Price[][] binomialTreeCall = new Price[numTimeIntervals+1][numTimeIntervals+1];
		for (i = 0; i <= numTimeIntervals; i++)
			for (j = 0; j <= numTimeIntervals; j++)
				binomialTreePut[i][j] = new Price();
				binomialTreeCall[i][j]= new Price();

		// Fill the stockPrice component of the binomialTree
		for (i = 0; i <= numTimeIntervals; i++) {
			for (j = 0; j <= i; j++) {
				binomialTreePut[i][j].stockPrice = sNaught * Math.pow(up, j) * Math.pow(down, i-j);
				binomialTreeCall[i][j].stockPrice = sNaught * Math.pow(up, j) * Math.pow(down, i-j);
			}
		}
		// Fill the optionPrices at the terminal nodes
		for (j = 0; j <= numTimeIntervals; j++) {
		
				binomialTreePut[numTimeIntervals][j].optionPrice =
				Math.max(strike - binomialTreePut[numTimeIntervals][j].stockPrice, 0.0);
				
				binomialTreeCall[numTimeIntervals][j].optionPrice =
						Math.max(binomialTreeCall[numTimeIntervals][j].stockPrice-strike, 0.0);
		}
		// Now work backwards, filling optionPrices in the rest of the tree
		double discount = Math.exp(-riskFreeRate*deltaT);
		for (i = numTimeIntervals-1; i >= 0; i--) {
			for (j = 0; j <= i; j++) {
				
					binomialTreePut[i][j].optionPrice = 
						Math.max(strike - binomialTreePut[i][j].stockPrice,		
							discount*(upProb*binomialTreePut[i+1][j+1].optionPrice +
							downProb*binomialTreePut[i+1][j].optionPrice));
					
					binomialTreeCall[i][j].optionPrice = 
							Math.max(binomialTreeCall[i][j].stockPrice - strike,		
								discount*(upProb*binomialTreeCall[i+1][j+1].optionPrice +
								downProb*binomialTreeCall[i+1][j].optionPrice));
			}
		}
	
		result[0] = binomialTreePut[0][0].optionPrice;
		result[1] = binomialTreeCall[0][0].optionPrice;
		return result;
	}

	@Override
	public String getAlgorithmName() {
		return algorithmName;
	}

	@Override
	public ArrayList<String> getProductName() {
		return productName;
	}

}
