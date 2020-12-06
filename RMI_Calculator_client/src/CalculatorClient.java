import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;

public class CalculatorClient {

	
	private JFrame frame;
	private JTextPane displayRes;
	private JTextArea displayText;
	
	private Calculator calculator;
	private ArrayList<String> calculatorHistory; // Most recently selected numbers and operations: [number, operation, number]
	
	/**
	 * Launch the application.
	 */
        
	public static void main(String[] args) 
        {
		EventQueue.invokeLater(new Runnable() 
                {
			public void run()
                        {
				try 
                                {
                                    CalculatorClient client = new CalculatorClient();
                                    client.frame.setVisible(true);
				} catch (Exception e) 
                                {
                                    e.printStackTrace();
				}
			}
		});
	}
        
	public CalculatorClient() 
        {
		clearCalculatorHistory();
		initialize();
		getCalculatorHandler();
	}

	/**
	 * Initialise frame
	 */
        
	private void initialize() 
        {
		frame = new JFrame();
		frame.setBounds(100, 100, 210, 370);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		displayRes = new JTextPane();
		displayRes.setEditable(false);
		displayRes.setBounds(10, 10, 180, 80);
		frame.getContentPane().add(displayRes);
		
		JButton btnDiv = new JButton("/");
		btnDiv.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) 
                        {
				operationPressed("/");
			}
		});
		btnDiv.setBounds(10, 90, 45, 40);
		frame.getContentPane().add(btnDiv);
		
		JButton btnMul = new JButton("*");
		btnMul.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) 
                        {
				operationPressed("*");
			}
		});
		btnMul.setBounds(10, 130, 45, 40);
		frame.getContentPane().add(btnMul);
		
		JButton btnMin = new JButton("-");
		btnMin.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) 
                        {
				operationPressed("-");
			}
		});
		btnMin.setBounds(10, 170, 45, 40);
		frame.getContentPane().add(btnMin);
		
		JButton btnAdd = new JButton("+");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
                        {
				operationPressed("+");
			}
		});
		btnAdd.setBounds(10, 210, 45, 40);
		frame.getContentPane().add(btnAdd);
		
		JButton btn7 = new JButton("7");
		btn7.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) 
                        {
				numberPressed("7");
			}
		});
		btn7.setBounds(55, 90, 45, 40);
		frame.getContentPane().add(btn7);
		
		JButton btn8 = new JButton("8");
		btn8.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) 
                        {
				numberPressed("8");
			}
		});
		btn8.setBounds(100, 90, 45, 40);
		frame.getContentPane().add(btn8);
		
		JButton btn9 = new JButton("9");
		btn9.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e)
                        {
				numberPressed("9");
			}
		});
		btn9.setBounds(145, 90, 45, 40);
		frame.getContentPane().add(btn9);
		
		JButton btn4 = new JButton("4");
		btn4.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) 
                        {
				numberPressed("4");
			}
		});
		btn4.setBounds(55, 130, 45, 40);
		frame.getContentPane().add(btn4);
		
		JButton btn5 = new JButton("5");
		btn5.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) 
                        {
				numberPressed("5");
			}
		});
		btn5.setBounds(100, 130, 45, 40);
		frame.getContentPane().add(btn5);
		
		JButton btn6 = new JButton("6");
		btn6.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) 
                        {
				numberPressed("6");
			}
		});
		btn6.setBounds(145, 130, 45, 40);
		frame.getContentPane().add(btn6);
		
		JButton btn1 = new JButton("1");
		btn1.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) 
                        {
				numberPressed("1");
			}
		});
		btn1.setBounds(55, 170, 45, 40);
		frame.getContentPane().add(btn1);
		
		JButton btn2 = new JButton("2");
		btn2.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) 
                        {
				numberPressed("2");
			}
		});
		btn2.setBounds(100, 170, 45, 40);
		frame.getContentPane().add(btn2);
		
		JButton btn3 = new JButton("3");
		btn3.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) 
                        {
				numberPressed("3");
			}
		});
		btn3.setBounds(145, 170, 45, 40);
		frame.getContentPane().add(btn3);
		
		JButton btn0 = new JButton("0");
		btn0.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) 
                        {
				numberPressed("0");
			}
		});
		btn0.setBounds(55, 210, 45, 40);
		frame.getContentPane().add(btn0);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) 
                        {
				doCalculation();
			}
		});
		btnSend.setBounds(100, 210, 90, 40);
		frame.getContentPane().add(btnSend);
		
		displayText = new JTextArea();
		displayText.setEditable(false);
		displayText.setBounds(10, 250, 180, 80);
		
		JScrollPane scrollPane = new JScrollPane(displayText);
		scrollPane.setBounds(10, 250, 180, 80);
		frame.getContentPane().add(scrollPane);
	}
	
	/**
	 * Retrieves the calculator handler instance via RMI.
	 */
        
	private void getCalculatorHandler() 
        {
		try 
                {
			calculator = (Calculator) Naming.lookup("//localhost/CalculatorServer");
			
			System.out.println("Successfully retrieved calculator instance bound in CalculatorServer");
			displayText.append("\n" +
"			System.out.println(\"Successfully retrieved calculator instance bound in CalculatorServer\n");
		} 
                catch (Exception e) 
                {
			System.out.println("Client error: " + e);
			displayText.append("Client error: " + e + "\n");
			// e.printStackTrace();
			
			System.exit(0);
		}
	};
	
	/**
	 * Displays the current calculator history onto the input/output screen and logs any changes to the console.
	 */
	private void updateInputOutputScreens() 
        {
		
		String resultString = "";
		for (String s : calculatorHistory) 
                {
			resultString += s + " ";
		}
		
		displayRes.setText(resultString);
		displayText.append(resultString + "\n");
	}
	
	/**
	 * Displays the result to user

	 */
	private void updateInputOutputScreens(String resultString) 
        {
		displayRes.setText(resultString);
		displayText.append(resultString + "\n");
	}
	
	/**
	 * Performs the calculation process using the RMI instances calculator.
	 */
        
	private void doCalculation() 
        {
		
		ArrayList<String> myCalculator = (ArrayList<String>) calculatorHistory.clone();
		
		if (myCalculator.size() == 0) 
                {
			displayText.append("No input given\n");
			return;
		};
		
		try 
                {
                       //Operands used for calculation
			String[] myOps = {"*", "/", "+", "-"};
			
			for (int i = 0; i < myOps.length; i++) {
				String currentOperation = myOps[i];
				int nextIndex = myCalculator.indexOf(currentOperation);
				
				while (nextIndex != -1) 
                                {
			
					// Get the two numbers for the operation (operation - 1, operation + 1)
					if (nextIndex != myCalculator.size() - 1) 
                                        {
						float op1 = Float.parseFloat(myCalculator.get(nextIndex - 1));
						float op2 = Float.parseFloat(myCalculator.get(nextIndex + 1));
						float result = 0;
						
						// Perform requested calculation
						switch(currentOperation) 
                                                {
							case "*":
								result = calculator.multiply(op1, op2);
								break;
								
							case "/":
								result = calculator.divide(op1, op2);
								break;
								
							case "+":
								result = calculator.add(op1, op2);
								break;
								
							case "-":
								result = calculator.minus(op1, op2);
								break;
						}
						
						// Replace with new result
						myCalculator.remove(nextIndex + 1);
						myCalculator.remove(nextIndex);
						myCalculator.set(nextIndex - 1, Float.toString(result));
					} 
                                        else 
                                        {
						myCalculator.remove(nextIndex);
					};
					
					nextIndex = myCalculator.indexOf(currentOperation);
				}
			}
			
			updateInputOutputScreens(myCalculator.get(0));
			clearCalculatorHistory();
		} catch (RemoteException e) {
			System.out.println("Client error: " + e);
			// e.printStackTrace();
		}
	}
	
	/**
	 * Resets the calculator history.
	 */
	private void clearCalculatorHistory() {
		calculatorHistory = new ArrayList<String>();
	}
	
	/**
	 * Triggered when a number button in the calculator is pressed.
	 * Adds result to the calculator history for usage in operation later.
	 */
        
	private void numberPressed(String number) 
        {
		
		if (calculatorHistory.isEmpty()) 
                {
			calculatorHistory.add(number);
		} 
                else 
                {
			
			int size = calculatorHistory.size();
			int lastIndex = size - 1;
			String recent = calculatorHistory.get(lastIndex);
			
			if (recent.equals("+") || recent.equals("-") || recent.equals("*") || recent.equals("/")) {
				calculatorHistory.add(number);
			} else {
				calculatorHistory.set(lastIndex, recent + "" + number);
			}
		};
		
		updateInputOutputScreens();
	}
	
	/**
	 * Triggered when an operation button in the calculator is pressed.
	 * Adds it to the calculator history for usage in operation later.
	 */
	private void operationPressed(String operation) 
        {
		if (calculatorHistory.isEmpty()) 
                {
			return;
		} 
                else 
                {
			
			int size = calculatorHistory.size();
			int lastIndex = size - 1;
			String recent = calculatorHistory.get(lastIndex);
			
			if (!(recent.equals("+") || recent.equals("-") || recent.equals("*") || recent.equals("/"))) {
				calculatorHistory.add(operation);
			}
		};
		
		updateInputOutputScreens();
	}
}
