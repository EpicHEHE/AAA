package pricer;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.event.ListSelectionListener;

import javax.swing.JTextField;

import pricer.spi.Algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.JList;


public class Pricer extends JFrame {

	private JPanel contentPane;
	private static Pricer pricer;

	Choice choiceProduct = new Choice();
	Choice choiceAlgorithm = new Choice();
	JPanel panelCalculator = new JPanel();
	JPanel panelExtension = new JPanel();
	JLabel labelPut = new JLabel();
	JLabel labelCall = new JLabel();
	JButton calculateButton = new JButton("Calculate");
	TreeMap<String, JTextField> textFieldMap = new TreeMap<String, JTextField>();
	TreeMap<String, Double> parameterInputMap = new TreeMap<String, Double>();
	ArrayList<String> parameterList = new ArrayList<String>();
	JButton addProductButton = new JButton("add Product");
	JTextField productNameField = new JTextField();
	JButton removeProductButton = new JButton("remove");
	JButton removeAlgorithmButton = new JButton("remove");
	JList<String> productJList = new JList<String>();
	JList<String> algorithmList = new JList<String>();
	DefaultListModel<String> productListModel = new DefaultListModel<String>();	
	DefaultListModel<String> algorithmListModel = new DefaultListModel<String>();	
	
	public static void main(String[] args) {

		System.out.println(System.getProperty("java.class.path"));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Pricer frame = new Pricer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Pricer() {
		panelExtension.setLayout(null);
		productNameField.setBounds(225, 20, 120, 25);
		panelExtension.add(productNameField);
		productNameField.setColumns(10);
		displayGUI();
	}

	public static synchronized Pricer getInstance() {

		if (pricer == null) {
			pricer = new Pricer();
		}
		return pricer;
	}

	public void displayGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane();
		contentPane.add(tabbedPane);

		tabbedPane.addTab("Calculator", null, panelCalculator, null);
		panelCalculator.setLayout(null);

		tabbedPane.addTab("Extension", null, panelExtension, null);
		addProductButton.setBounds(225, 55, 120, 25);
		panelExtension.add(addProductButton);
		addProductButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String productName = productNameField.getText();
				boolean status = ProductAlgorithmManager.getInstance().addProducttoMap(productName);
				System.out.println("add product: "+status);
				productListModel = loadProductListModel();
			}
		});
		
		productListModel = loadProductListModel();
		productJList = new JList<String>(productListModel);		
		productJList.setBounds(55, 20, 130, 185);
		panelExtension.add(productJList);
		productJList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent arg0) {
				algorithmListModel = loadAlgorithmListModel();				
			}
			
		});
		
		removeProductButton.setBounds(225, 90, 120, 25);
		panelExtension.add(removeProductButton);
		removeProductButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String removedProduct = productJList.getSelectedValue();
				if (removedProduct != null) {
					ProductAlgorithmManager.getInstance().dropProduct(removedProduct);
					productListModel = loadProductListModel();									
				}
			}
		});		
		
		JButton addJARButton = new JButton("add JAR");
		addJARButton.setBounds(550, 20, 120, 25);
		panelExtension.add(addJARButton);
		
		algorithmList = new JList<String>(algorithmListModel);
		algorithmList.setBounds(380, 20, 130, 185);
		panelExtension.add(algorithmList);
		
		removeAlgorithmButton.setBounds(550, 55, 120, 25);		
		panelExtension.add(removeAlgorithmButton);
		removeAlgorithmButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String removedProduct = productJList.getSelectedValue();
				String removedAlgorithm = algorithmList.getSelectedValue();
				if (removedProduct != null && removedAlgorithm != null) {
					ProductAlgorithmManager.getInstance().dropAlgorithm(removedProduct, removedAlgorithm);
					algorithmListModel = loadAlgorithmListModel();									
				}
			}
		});
				
		addJARButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"jar", "jar");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(Pricer.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("You chose to open this file: "
							+ chooser.getSelectedFile().getName());
					ArrayList<Algorithm> algorithmList = AlgorithmService
							.getInstance().loadAlgorithms(
									chooser.getSelectedFile());
					boolean status = ProductAlgorithmManager.getInstance()
							.addAlgorithmtoMap(algorithmList);
					if (status) {
						ProductListRefresh();
					}

				}
			}
		});
		
		JLabel labelType = new JLabel("Type");
		labelType.setBounds(30, 20, 150, 20);
		panelCalculator.add(labelType);

		choiceProduct.setBounds(180, 20, 150, 20);
		panelCalculator.add(choiceProduct);
		ArrayList<String> ProductList = loadProductList();
		for (int i = 0; i < ProductList.size(); i++) {
			choiceProduct.add(ProductList.get(i));
		}
		choiceProduct.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				System.out.println("selected product "
						+ choiceProduct.getSelectedItem());
				ArrayList<String> AlgorithmList = loadAlgorithmList(choiceProduct.getSelectedItem());
				System.out.println("AlgorithmList: " + AlgorithmList.toString());
				choiceAlgorithm.removeAll();
				for (int i = 0; i < AlgorithmList.size(); i++) {
					choiceAlgorithm.add(AlgorithmList.get(i));
				}

			}
		});
		JLabel labelAlgorithm = new JLabel("Algorithm");
		labelAlgorithm.setBounds(30, 50, 150, 20);
		panelCalculator.add(labelAlgorithm);
		choiceAlgorithm.setBounds(180, 50, 150, 20);
		panelCalculator.add(choiceAlgorithm);
		// if(choiceProduct.getSelectedItem()!=null){
		// ArrayList<String> AlgorithmList = loadAlgorithmList();
		// for (int i = 0; i < AlgorithmList.size(); i++){
		// choiceAlgorithm.add(AlgorithmList.get(i));
		// }
		//
		// }
		choiceAlgorithm.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {

				if (choiceAlgorithm.getSelectedItem() != null) {
					parameterList = loadParameter(
							choiceProduct.getSelectedItem(),
							choiceAlgorithm.getSelectedItem());
				}
				System.out.println("Num of Parameter " + parameterList.size());
				for (int j = 0; j < parameterList.size(); j++) {
					JLabel label = new JLabel(parameterList.get(j));
					label.setBounds(30, 80 + 30 * j, 150, 20);
					panelCalculator.add(label);

					JTextField textFieldName = new JTextField();
					textFieldName.setBounds(180, 80 + 30 * j, 150, 20);
					panelCalculator.add(textFieldName);
					textFieldName.setColumns(10);

					textFieldMap.put(parameterList.get(j), textFieldName);

				}
				calculateButton.setBounds(180, 80 + 30 * parameterList.size(),
						100, 25);
				panelCalculator.validate();
				panelCalculator.repaint();

			}
		});

		// JButton calculateButton = new JButton();
		calculateButton.setBounds(180, 80 + 30 * parameterList.size(), 100, 25);
		panelCalculator.add(calculateButton);

		// the label to show the price for put and call option price
		JLabel put = new JLabel("Put");
		JLabel call = new JLabel("Call");
		put.setBounds(400, 20, 100, 20);
		call.setBounds(500, 20, 100, 20);
		panelCalculator.add(put);
		panelCalculator.add(call);

		labelPut.setBounds(400, 50, 100, 20);
		labelCall.setBounds(500, 50, 100, 20);
		panelCalculator.add(labelPut);
		panelCalculator.add(labelCall);

		calculateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				double[] priceArray = new double[2];
				priceArray = calculatePrice();
				labelPut.setText(String.valueOf(priceArray[0]));
				labelCall.setText(String.valueOf(priceArray[1]));
			}
		});

		JButton volatilityButton = new JButton("Show Volatility Smile");
		volatilityButton
				.setBounds(400, 80 + 30 * parameterList.size(), 200, 25);
		panelCalculator.add(volatilityButton);
		volatilityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae1) {
				volatilityCalculate();
			}
		});

	}

	public DefaultListModel<String> loadProductListModel() {
		ArrayList<String> thisProductList = loadProductList();
		productListModel.removeAllElements();
		for (int n=0;n<thisProductList.size();n++){
			productListModel.addElement(thisProductList.get(n));
		}		
		return productListModel;
	}

	public DefaultListModel<String> loadAlgorithmListModel() {
		String thisproduct = productJList.getSelectedValue();
		if(thisproduct != null){
			ArrayList<String> thisAlgorithmlist = loadAlgorithmList(thisproduct);
			algorithmListModel.removeAllElements();
			for (int n=0;n<thisAlgorithmlist.size();n++){
				algorithmListModel.addElement(thisAlgorithmlist.get(n));
			}					
		}
		System.out.println(algorithmListModel.size());
		return algorithmListModel;
	}


	public double[] calculatePrice() {
		double[] priceArray = new double[2];
		// priceArray[0] = 1.0;
		// priceArray[1] = 2.0;

		String productName = choiceProduct.getSelectedItem();
		String algorithmName = choiceAlgorithm.getSelectedItem();
		
		if(validateInput()){
			Algorithm algorithm = ProductAlgorithmManager.getInstance()
					.getAlgorithmSelected(productName, algorithmName);
			algorithm.setParameter(parameterInputMap);
			priceArray = algorithm.calculate();
		}
		

		return priceArray;
	}

	public ArrayList<String> loadProductList() {
		ArrayList<String> ProductList = new ArrayList<String>();
		// Collections.addAll(ProductList,
		// "American Option","European Option","Asian Option");
		ProductList = ProductAlgorithmManager.getInstance().getProductList();
		return ProductList;
	}

	public ArrayList<String> loadAlgorithmList(String product) {
		ArrayList<String> Algorithmlist = new ArrayList<String>();
//		String product = choiceProduct.getSelectedItem();
		Algorithmlist = ProductAlgorithmManager.getInstance().getAlgorithmList(
				product);

		return Algorithmlist;
	}

	public ArrayList<String> loadParameter(String productName,
			String algorithmName) {
		ArrayList<String> parameterList = new ArrayList<String>();

		parameterList = ProductAlgorithmManager.getInstance().getParameterList(
				productName, algorithmName);

		return parameterList;
	}

	public void ProductListRefresh() {
		choiceProduct.removeAll();
		ArrayList<String> ProductList = loadProductList();
		for (int i = 0; i < ProductList.size(); i++) {
			choiceProduct.add(ProductList.get(i));

		}
	}

	public void volatilityCalculate(){
		final VolatilityDrawer VD = new VolatilityDrawer();
		double[][] aaa = {{2,16,4,7}, {6,7,8,23}};
		VD.displayVolatility(aaa);
	}
	
	public boolean validateInput(){
		boolean status=true;
		for (String parameterName : textFieldMap.keySet()) {
			try{
				parameterInputMap.put(parameterName, Double
						.parseDouble(textFieldMap.get(parameterName).getText()));
			}catch(Exception e){
				System.out.println("illegal input: "+e.getMessage());
				status = false;
			}
		
		}
		return status;
	}
}
