package pricer;

import java.util.ArrayList;
import java.util.TreeMap;

import pricer.spi.Algorithm;

public class ProductAlgorithmManager {
	private TreeMap<String, TreeMap<String, Algorithm>> productMap;
	private TreeMap<String, Algorithm> algorithmMap;
	private static ProductAlgorithmManager manager;

	private ProductAlgorithmManager() {
		productMap = new TreeMap<String, TreeMap<String, Algorithm>>();
		algorithmMap = new TreeMap<String, Algorithm>();
		Algorithm AsianSimulation = new AsianSimulation();

	    algorithmMap.put("AsianSimulation", AsianSimulation);
//		productMap.put("AsianOption", new TreeMap<String, Algorithm>());
		productMap.put("AsianOption", algorithmMap);
	}

	public static synchronized ProductAlgorithmManager getInstance() {
		if (manager == null) {
			manager = new ProductAlgorithmManager();
		}
		return manager;

	}

	public ArrayList<String> getProductList() {
		ArrayList<String> productList = new ArrayList<String>();
		for (String keyName : productMap.keySet()) {
			productList.add(keyName);
		}
		return productList;
	}

	public ArrayList<String> getAlgorithmList(String productName) {
		ArrayList<String> algorithmList = new ArrayList<String>();
		TreeMap<String, Algorithm> algorithmMap = productMap.get(productName);
		if (algorithmMap != null) {
			for (String keyName : algorithmMap.keySet()) {
				algorithmList.add(keyName);
			}
		}
		return algorithmList;
	}

	public Algorithm getAlgorithmSelected(String productName,
			String algorithmName) {
		Algorithm algorithm = productMap.get(productName).get(algorithmName);
		return algorithm;
	}

	public ArrayList<String> getParameterList(String productName,
			String algorithmName) {
		TreeMap<String, Double> parameterMap = getAlgorithmSelected(
				productName, algorithmName).getParameterMap();
		ArrayList<String> parameterList = new ArrayList<String>();
		for (String parameterName : parameterMap.keySet()) {
			parameterList.add(parameterName);
		}
		return parameterList;
	}

	public boolean addProducttoMap(String productName) {
		if (!productMap.containsKey(productName)) {
			productMap.put(productName, new TreeMap<String, Algorithm>());
			return true;
		}
		return false;
	}

	public boolean addAlgorithmtoMap(ArrayList<Algorithm> newAlgorithms) {
		boolean status=false;
		System.out.println(newAlgorithms.size());
		for (int i = 0; i < newAlgorithms.size(); i++) {
			ArrayList<String> productName = newAlgorithms.get(i)
					.getProductName();
			for (int k = 0; k < productName.size(); k++) {
					status = addProducttoMap(productName.get(k));
				productMap.get(productName.get(k)).put(newAlgorithms.get(i).getAlgorithmName(),newAlgorithms.get(i));
				status = true;
			}
		}
		return status;
	}

	public void dropProduct(String productName) {
		productMap.remove(productName);
	}

	public void dropAlgorithm(String productName, String algorithmName) {
		productMap.get(productName).remove(algorithmName);
	}

	public void saveProductMaptoFile() {

	}

	public void getProductMaptoFile() {

	}

	public void notifyAlgorithmChange() {
		
	}
}
