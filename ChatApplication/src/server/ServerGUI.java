package server;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import chat_constants.Constants;

/**
 * The GUI for the server, which has two buttons to start and stop the server, 
 * and a log of server activity.
 * @author Bailey Duncan
 * @since May 18, 2017
 * @version 1.0
 */
public class ServerGUI {
	
	private Server server;
	private boolean online;
	private boolean serverStarted;
	
	// Components
	private JFrame f;
	private JPanel pane;
	private JTextArea serverLog;
	private JScrollPane jsp;
	private JLabel header, status, load, port, maxLoad, address, hostname, header2;
	private JButton start ,stop;
	private Box box;
		
	/**
	 * The constructor for the server GUI.
	 * @param s The back-end server class that the GUI is displaying information from.
	 * @param CLIENT_CAP The user specified maximum number of clients the server will support.
	 * @param serverPort The port which the server is being hosted on.
	 */
	public ServerGUI(Server s, int CLIENT_CAP, int serverPort)
	{
		server = s;
		online = false;
		initHeader();
		initButton();
		initPanel(CLIENT_CAP, serverPort);
		initLog();
		initFrame();
	}
	
	/**
	 * Starts or stops the chat server depending on the input arguement. Called when either "start server" 
	 * or "stop server" buttons are pressed. When stopped, the server will "kick" the clients connected and will stop 
	 * accepting new clients until started again. Once restarted, the chat will still retain all old messages for
	 * clients.
	 * @param disconnect false if the "start server button" is pressed, true is the "stop server" button is pressed.
	 */
	public void updateStatus(boolean disconnect)
	{
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss").format(new Date());
		if(!disconnect && !online)
		{
			online = true;
			status.setText("Status: Online");
			status.setForeground(Constants.GREEN);
			serverLog.append("\nServer Started: " + timeStamp );
			if(! serverStarted)
			{
				server.start();
				serverStarted = true;
			}
			else
				server.endShutdownMsg();
		}
		
		else if(disconnect && online)
		{
			online = false;
			status.setText("Status: Offline");
			status.setForeground(Constants.RED);
			serverLog.append("\nServer Stopped: " + timeStamp );
			server.shutdown();
		}
	}
	
	/**
	 * The value of the boolean "online" is returned.
	 * @return A boolean.
	 */
	public boolean isOnline()
	{
		return online;
	}
	
	/**
	 * Updates the GUI component displaying the number of clients connected to the server.
	 * @param clientLoad The number of clients connected to the server.
	 */
	public void updateLoad(int clientLoad)
	{
		load.setText("Current Load: " + clientLoad + " Users");
	}
	
	/**
	 * Changes the aesthetics of a JLabel.
	 * @param l The JLabel being modified.
	 */
	private void setLabelLook(JLabel l)
	{
		l.setFont(Constants.CHATFONT);
		l.setBackground(Constants.BLACK);
		l.setForeground(Constants.GREEN);
	}
	
	/**
	 * Initializes the title header for the GUI.
	 */
	public void initHeader()
	{
		header = new JLabel("Server Information");
		setLabelLook(header);
		header.setFont(Constants.TITLEFONT);
		header.setHorizontalAlignment(JLabel.CENTER);
	}
	
	/**
	 * Initialize the start and stop buttons for the server and their listeners.
	 */
	public void initButton()
	{
		start = new JButton("Start Server");
		start.setFont(Constants.TITLEFONT);
		start.setBackground(Constants.GREEN);
		
		stop = new JButton("Stop Server ");
		stop.setFont(Constants.TITLEFONT);
		stop.setBackground(Constants.GREEN);
		
		start.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				updateStatus(false);
			}
			
		});
		
		stop.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				updateStatus(true);
			}
			
		});
	}
	
	/**
	 * Initialize a vertical box to hold the server information labels.
	 */
	public void initBox()
	{
		box = Box.createVerticalBox();
		box.add(header2);
		box.add(Box.createVerticalStrut(10));
		box.add(status);
		box.add(port);
		box.add(address);
		box.add(hostname);
		box.add(load);
		box.add(maxLoad);
		box.add(Box.createVerticalStrut(25));
		box.add(start);
		box.add(Box.createVerticalStrut(15));
		box.add(stop);
	}
	
	/**
	 * Initializes a JPanel and most of the server information labels.
	 * @param CLIENT_CAP The user specified maximum number of clients the server will support. 
	 * @param serverPort The port which the server is being hosted on.
	 */
	public void initPanel(int CLIENT_CAP, int serverPort)
	{
		header2 = new JLabel("Stats");
		status = new JLabel("Status: Offline");
		load = new JLabel("Current Load: 0 Users");
		maxLoad = new JLabel("Maximum Load: " + CLIENT_CAP + " Users");
		port = new JLabel("Port: " + serverPort);
		
		try
		{
			address = new JLabel("IP Address: " + Inet4Address.getLocalHost().getHostAddress());
			hostname = new JLabel("Host Name: " + InetAddress.getLocalHost().getHostName());
		}
		catch(UnknownHostException e)
		{
			System.out.println("UnknownHostException!");
		}
		setLabelLook(header2);
		setLabelLook(status);
		setLabelLook(load);
		setLabelLook(maxLoad);
		setLabelLook(port);
		setLabelLook(address);
		setLabelLook(hostname);
		
		header2.setFont(Constants.TITLEFONT);
		status.setForeground(Constants.RED);
		initBox();
		
		pane = new JPanel();
		pane.setBackground(Constants.BLACK);
		pane.setPreferredSize(new Dimension(300, 300));
		pane.add(box);
	}
	
	/**
	 * Initializes the server log, a JTextField which holds information about when the server has been turned on or off.
	 */
	public void initLog()
	{
		serverLog = new JTextArea("Server Log");
		
		serverLog.setEditable(false);
		serverLog.setBackground(Constants.BLACK);
		serverLog.setForeground(Constants.GREEN);
		serverLog.setFont(Constants.CHATFONT);
		serverLog.setBorder(Constants.GREENBORDER);
		serverLog.setLineWrap(true);
		serverLog.setWrapStyleWord(true);
		
		jsp = new JScrollPane(serverLog, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED , ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(300, 300));
		jsp.setAutoscrolls(true);
		jsp.setBorder(new EmptyBorder(10 , 0, 25, 25));
		jsp.setBackground(Constants.BLACK);
	}
	
	/**
	 * Initializes the frame for the server GUI.
	 */
	public void initFrame()
	{
			f = new JFrame("Server");
			f.setSize(600, 300);
			f.setLayout(new BorderLayout());
			f.add(jsp, BorderLayout.EAST);
			f.add(pane, BorderLayout.WEST);
			f.add(header, BorderLayout.NORTH);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.getContentPane().setBackground(Constants.BLACK);
			f.setVisible(true);
	}
}
