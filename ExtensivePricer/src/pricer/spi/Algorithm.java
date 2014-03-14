package pricer.spi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public interface Algorithm extends Serializable {
	
	// public String algorithmName="";
	TreeMap<String, Double> parameter = new TreeMap<String, Double>();

	public TreeMap<String, Double> getParameterMap();

	public void setParameter(TreeMap<String, Double> a);
	public String getAlgorithmName();
	public ArrayList<String> getProductName();

	double[] calculate();

}
