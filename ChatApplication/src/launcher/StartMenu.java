package launcher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import chat_constants.Constants;
import client.Client;
import server.Server;

/**
 * The menu created on launch. The menu can create one or more chat client windows which connect to servers, 
 * or it can create a new server.
 * @author Bailey Duncan
 * @since May 18, 2017
 * @version 1.0
 */
public class StartMenu {

	//Components
	JFrame f;
	JButton serverButton, clientButton;
	JLabel header;
	Box box;
	JPanel filler, clientPopup, serverPopup;
	// 'C' for client text field, 'S' for server text field
	JTextField portFieldC, portFieldS, addressField, chatTitleField, capField, handleField;
	
	/**
	 * Creates the start menu GUI and listeners.
	 */
	public StartMenu()
	{
		UIManager.put("OptionPane.background", Constants.BLACK);
		UIManager.getLookAndFeelDefaults().put("Panel.background", Constants.BLACK);
		UIManager.getLookAndFeelDefaults().put("OptionPane.messageForeground", Constants.GREEN);
		initHeader();
		initButtons();
		initFrame();
		initPopups();
		initListeners();
	}
	
	/**
	 * Adds anonymous listeners to the two buttons which either start a server or a chat client. 
	 * When either button is pressed a popup window will appear and ask for user input.
	 */
	public void initListeners()
	{
		serverButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int option = JOptionPane.showConfirmDialog(null, serverPopup, "Server Setup", JOptionPane.OK_CANCEL_OPTION);
				if(option == 0)
				{
					startServer();
				}
			}
			
		});
		
		clientButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int option = JOptionPane.showConfirmDialog(null, clientPopup, "Chat Finder", JOptionPane.OK_CANCEL_OPTION);
				if(option == 0)
				{
					startClient();
				}
			}
		});
		
		// Consume extra characters typed to restrict the size of the JTextFields
		
		handleField.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e)
			{
				if(handleField.getText().length() > 19)
					e.consume();
			}
		});
		
		portFieldC.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e)
			{
				if(portFieldC.getText().length() > 3)
					e.consume();
			}
		});
		
		portFieldS.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e)
			{
				if(portFieldS.getText().length() > 3)
					e.consume();
			}
		});
		
		addressField.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e)
			{
				if(addressField.getText().length() > 14)
					e.consume();
			}
		});
		
		chatTitleField.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e)
			{
				if(chatTitleField.getText().length() > 14)
					e.consume();
			}
		});
		
		capField.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e)
			{
				if(capField.getText().length() > 2)
					e.consume();
			}
		});
		
		
	}
	
	/**
	 * Starts the chat client, which uses user input values for the port number, address, and handle (username).
	 */
	public void startClient()
	{
		try{
			int port = Integer.parseInt(portFieldC.getText());
			Client c = new Client(port, addressField.getText(), handleField.getText());
			Thread clientThread = new Thread(c);
			clientThread.start();
		}
		catch(NumberFormatException e)
		{
			Constants.errorPopup("Invalid Port Number!");
			return;
		}
	}
	
	/**
	 * Starts the server on a user specified port number, with a user specified maximum client capacity.
	 */
	public void startServer()
	{
		int port, clientCap;
		try
		{
			port = Integer.parseInt(portFieldS.getText());
			if(port < 1024)
			{
				Constants.errorPopup("Invalid Port Number, try one between 1024 and 9999.");
				return;
			}
		}
		catch(NumberFormatException e)
		{
			Constants.errorPopup("Invalid Port Number!");
			return;
		}
		try
		{
			clientCap = Integer.parseInt(capField.getText());
			if(clientCap < 1) 
			{
				Constants.errorPopup("Server Capacity Must be a Positive Number!");
				return;
			}
		}
		catch(NumberFormatException e)
		{
			Constants.errorPopup("Invalid Capacity!");
			return;
		}
		
		String chatname = chatTitleField.getText();
		Server server = new Server(port, chatname, clientCap);
	}
	
	/**
	 * Initializes the components that appear in a popup when the user hits either button in the menu. 
	 */
	public void initPopups()
	{	
		handleField = new JTextField();
		handleField.setPreferredSize(new Dimension(120, 30));
		
		portFieldC = new JTextField();
		portFieldC.setPreferredSize(new Dimension(45, 30));
		
		addressField = new JTextField("localhost");
		addressField.setPreferredSize(new Dimension(100, 30));
		
		portFieldS = new JTextField();
		portFieldS.setPreferredSize(new Dimension(45, 30));
		
		chatTitleField = new JTextField();
		chatTitleField.setPreferredSize(new Dimension(100, 30));
		
		capField = new JTextField();
		capField.setPreferredSize(new Dimension(50, 30));
		
		clientPopup = new JPanel();
		clientPopup.setBackground(Constants.BLACK);
		clientPopup.add(new JLabel("Username:"));
		clientPopup.add(handleField);
		clientPopup.add(new JLabel("Port Number:"));
		clientPopup.add(portFieldC);
		clientPopup.add(new JLabel("IP Address/Host Name:"));
		clientPopup.add(addressField);
		
		serverPopup = new JPanel();
		serverPopup.setBackground(Constants.BLACK);
		serverPopup.add(new JLabel("Port Number:"));
		serverPopup.add(portFieldS);
		serverPopup.add(new JLabel("Chat Name:"));
		serverPopup.add(chatTitleField);
		serverPopup.add(new JLabel("Capacity:"));
		serverPopup.add(capField);
		
		for(int i = 0; i < 6; i++)
		{
			clientPopup.getComponent(i).setBackground(Constants.BLACK);
			clientPopup.getComponent(i).setForeground(Constants.GREEN);
			serverPopup.getComponent(i).setBackground(Constants.BLACK);
			serverPopup.getComponent(i).setForeground(Constants.GREEN);
		}
		
	}
	
	/**
	 * Initializes the menu buttons that start the chat client and server.
	 */
	public void initButtons()
	{
		serverButton = new JButton("Start a new Server");
		serverButton.setFont(Constants.TITLEFONT);
		serverButton.setForeground(Constants.BLACK);
		serverButton.setBackground(Constants.GREEN);
		serverButton.setPreferredSize(new Dimension(225, 0));
		
		clientButton = new JButton("Join a Chat");
		clientButton.setFont(Constants.TITLEFONT);
		clientButton.setForeground(Constants.BLACK);
		clientButton.setBackground(Constants.GREEN);
		clientButton.setPreferredSize(new Dimension(225, 0));
	}
	
	/**
	 * Initializes the frame.
	 */
	public void initFrame()
	{
		f = new JFrame("Chat Launcher");
		f.setSize(new Dimension(500,300));
		f.getContentPane().setBackground(Constants.BLACK);
		f.setLayout(new BorderLayout());
		f.add(box, BorderLayout.NORTH);
		f.add(clientButton, BorderLayout.WEST);
		f.add(serverButton, BorderLayout.EAST);
		f.add(filler, BorderLayout.SOUTH);
		f.setVisible(true);
	}
	
	/**
	 * Initializes the title header and an empty JPanel for aesthetics.
	 */
	public void initHeader()
	{
		header = new JLabel("Welcome to this Chat Application");
		header.setFont(Constants.TITLEFONT);
		header.setForeground(Constants.GREEN);
		header.setBackground(Constants.BLACK);
		header.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		box = Box.createVerticalBox();
		box.add(header);
		box.add(Box.createVerticalStrut(25));
		
		filler = new JPanel();
		filler.setBackground(Constants.BLACK);
		filler.setPreferredSize(new Dimension(500, 150));
	}
	
}
