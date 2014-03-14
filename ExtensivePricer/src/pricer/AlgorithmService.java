package pricer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import pricer.spi.Algorithm;

public class AlgorithmService {
	private static AlgorithmService service;
	private ServiceLoader<Algorithm> loader;

	private AlgorithmService() {
		
	}

	public static synchronized AlgorithmService getInstance() {
		if (service == null) {
			service = new AlgorithmService();
		}
		return service;
	}

	public ArrayList<Algorithm> loadAlgorithms(File file) {
		ArrayList<Algorithm> algorithmArrayList = new ArrayList<Algorithm>();
		
		System.out.println("1");
		URL[] urls = new URL[1];
		try {
			urls[0]=file.toURI().toURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("urls:"+urls[0].toString());
        URLClassLoader ucl = new URLClassLoader(urls);
        loader = ServiceLoader.load(Algorithm.class, ucl);

		try {
			Iterator<Algorithm> algorithms = loader.iterator();
			while (algorithms.hasNext()) {
				
				Algorithm a = algorithms.next();
				System.out.println("new Algorithms" + a.getAlgorithmName());
				algorithmArrayList.add(a);
			}
		} catch (ServiceConfigurationError serviceError) {
			algorithmArrayList = null;
			serviceError.printStackTrace();

		}

		return algorithmArrayList;
	}

}
