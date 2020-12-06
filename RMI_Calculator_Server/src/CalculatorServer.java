import java.awt.EventQueue;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class CalculatorServer extends UnicastRemoteObject implements Calculator 
{

	private JFrame myFrame;
	private JTextArea userText;


	public static void main(String[] args) 
        {
		EventQueue.invokeLater(new Runnable() 
                {
			public void run() 
                        {
				try 
                                {
                                    CalculatorServer myServer = new CalculatorServer();
                                    myServer.myFrame.setVisible(true);
				} 
                                catch (Exception e) 
                                {
                                    e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application
	 */
        
	public CalculatorServer() throws RemoteException 
        {
		initialize();
		bindToRegistry();
	}

	private void initialize() 
        {
		myFrame = new JFrame();
		myFrame.setBounds(100, 100, 450, 300);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.getContentPane().setLayout(null);
		
		userText = new JTextArea();
		userText.setEditable(false);
		userText.setBounds(0, 0, 436, 263);
		
		JScrollPane scrollPane = new JScrollPane(userText);
		scrollPane.setBounds(0, 0, 430, 260);
		myFrame.getContentPane().add(scrollPane);
	}
	
	/**
	 * Bind this class instance to the LocalHost registry, in order for us to use it via RMI
	 */
        
	private void bindToRegistry() 
        {
		try 
                {
			Registry myRegistry = LocateRegistry.createRegistry(1099);
			myRegistry.rebind("CalculatorServer", this);

			System.out.println("CalculatorServer bound in registry");
			userText.append("CalculatorServer bound in registry\n");
		} 
                catch (Exception e) 
                {
			System.out.println("Server error: " + e);
			userText.append("Server error: " + e);
			e.printStackTrace();
		}
	}

	@Override
	public float add(float op1, float op2) throws RemoteException 
        {
		userText.append(op1 + " + " + op2 + "\n");
		return op1 + op2;
	}

	@Override
	public float minus(float op1, float op2) throws RemoteException 
        {
		userText.append(op1 + " - " + op2 + "\n");
		return op1 - op2;
	}

	@Override
	public float multiply(float op1, float op2) throws RemoteException 
        {
		userText.append(op1 + " * " + op2 + "\n");
		return op1 * op2;
	}

	@Override
	public float divide(float op1, float op2) throws RemoteException 
        {
		userText.append(op1 + " / " + op2 + "\n");
		return op1 / op2;
	};
}
