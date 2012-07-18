package client.gui.statistics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;
import javax.swing.JFrame;

/**
 * A simple demonstration application showing how to create a bar chart.
 *
 */
public class PostOfTopicChart extends JFrame {

	private static MysqlConnect msq = new MysqlConnect();
	
	/**
	 * Creates a new demo instance.
	 *
	 * @param title  the frame title.
	 */
	public PostOfTopicChart(final String title, int[][] arr) {

		super(title);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		final CategoryDataset dataset = createDataset(arr);
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(chartPanel);

	}

	/**
	 * Returns a sample dataset.
	 * 
	 * @return The dataset.
	 */
	private CategoryDataset createDataset(int[][] arr) {

		// row keys...
		final String series1 = "Sayý";

		int[] categories = new int[arr.length];
		for (int i = 0; i < categories.length; i++) {
			categories[i] = arr[i][0];
		}

		// create the dataset...
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < arr.length; i++) {
			dataset.addValue(arr[i][1], series1,msq.getTopicName(categories[i]));
		}

		return dataset;

	}
	
	/**
	 * Creates a sample chart.
	 * 
	 * @param dataset  the dataset.
	 * 
	 * @return The chart.
	 */
	private JFreeChart createChart(final CategoryDataset dataset) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart(
				"Baþlýklarýn Aldýklarý Post Sayýsý",         // chart title
				null,               // domain axis label
				"Post Sayýsý",                  // range axis label
				dataset,                  // data
				PlotOrientation.VERTICAL, // orientation
				true,                     // include legend
				true,                     // tooltips?
				false                     // URLs?
				);


		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		// set up gradient paints for series...
		final GradientPaint gp0 = new GradientPaint(
				0.0f, 0.0f, Color.blue, 
				0.0f, 0.0f, Color.blue
				);

		renderer.setSeriesPaint(0, gp0);

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
				);

		return chart;
	}

	public static void run() {
		msq.connect();
		
		int size = msq.getTopicId().length;
		int array2d[][] = new int[size][2];


		for(int i = 0 ; i < size ; i++){
			array2d[i][0] = msq.getTopicId()[i];
			array2d[i][1] = msq.getReplyNumbers(msq.getTopicId()[i]); 
		}

		ArrayList<ikiBoyutluArray> sortedArr = new ArrayList<ikiBoyutluArray>();
		
		for (int i = 0; i < size; i++) {
			ikiBoyutluArray newArray = new ikiBoyutluArray();
			newArray.topic =  array2d[i][0];
			newArray.numberOfPost = array2d[i][1];
			sortedArr.add(newArray);
		}
		
		MysqlConnect.bubbleSort(sortedArr);
		
		for(int i=0 ; i<size ;i++){
			array2d[i][0] = sortedArr.get(i).topic;
			array2d[i][1] = sortedArr.get(i).numberOfPost;		}
		
		
		PostOfTopicChart demo = new PostOfTopicChart("Ýstatistikler", array2d);
		demo.setVisible(true);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
	}

}
