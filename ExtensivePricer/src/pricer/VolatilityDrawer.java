package pricer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import pricer.VolatilityDrawer;

public class VolatilityDrawer {

	public String getNumofTrial() {
		return null;
	}

	public void displayVolatility(double[][] volatilityPair) {


		
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries Series = new XYSeries("hehehe");
		for(int i=0; i < volatilityPair[0].length; i++){
			Series.add(volatilityPair[0][i], volatilityPair[1][i]);
		}
		
		
		dataset.addSeries(Series);
		
		JFreeChart chart = ChartFactory.createXYLineChart("Volatility Smile",
				"xAxisLabel", "yAxisLabel", dataset, PlotOrientation.VERTICAL,
				true, true, false);
		
		 ChartFrame ChartFrame = new ChartFrame("Volatility Smile", chart);  
	        ChartFrame.pack();  
	        ChartFrame.setVisible(true); 

	}
}