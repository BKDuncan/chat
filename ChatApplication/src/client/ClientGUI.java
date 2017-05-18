package client;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import chat_constants.Constants;

/**
 * The GUI for the (chat) client, which has a chat console and a log of chat messages.
 * @author Bailey Duncan
 * @since May 18, 2017
 * @version 1.0
 */
public class ClientGUI {
	
	// Components
	private JFrame f;
	private Box box;
	private JTextArea chatConsole, chatLog;
	private JScrollPane jsp;
	private JLabel header;
	public boolean sending;
	
	/**
	 * The constructor for the client GUI.
	 * @param c The back-end client class which handles communication to the server.
	 */
	public ClientGUI(Client c)
	{	
		initConsole();
		initLog();
		initHeader();
		initBox();
		initFrame();		
		addListeners();
	}
	
	/**
	 * Initializes the chat console which users type messages into.
	 */
	public void initConsole()
	{
		chatConsole = new JTextArea ();
		chatConsole.setBackground(Constants.BLACK);
		chatConsole.setForeground(Constants.GREEN);
		chatConsole.setFont(Constants.CHATFONT);
		chatConsole.setPreferredSize(new Dimension(450, 150));
		chatConsole.setLineWrap(true);
		chatConsole.setWrapStyleWord(true);
		chatConsole.setBorder(Constants.GREENBORDER);
	}
	
	/**
	 * Initializes the chat log which displays all the messages in the chat.
	 */
	public void initLog()
	{
		chatLog = new JTextArea("No Server Response.");
		chatLog.setEditable(false);
		chatLog.setBackground(Constants.BLACK);
		chatLog.setForeground(Constants.GREEN);
		chatLog.setFont(Constants.CHATFONT);
		chatLog.setLineWrap(true);
		chatLog.setWrapStyleWord(true);
		
		jsp = new JScrollPane(chatLog, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED , ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setAutoscrolls(true);
		jsp.setPreferredSize(new Dimension(450, 400));
	}
	
	/**
	 * Initializes the title header for the chat GUI.
	 */
	public void initHeader()
	{
		header = new JLabel("CHAT");
		header.setFont(Constants.TITLEFONT);
		header.setBackground(Constants.BLACK);
		header.setForeground(Constants.GREEN);
	}
	
	/**
	 * Initializes a vertical box to hold all the chat GUI components.
	 */
	public void initBox()
	{
		box = Box.createVerticalBox();
		box.setBorder(Constants.EMPTYBORDER);
		box.add(header);
		box.add(Box.createVerticalStrut(25));
		box.add(jsp);
		box.add(Box.createVerticalStrut(25));
		box.add(chatConsole);
		box.add(Box.createVerticalStrut(25));
		
	}
	
	/**
	 * Initializes the JFrame for the GUI.
	 */
	public void initFrame()
	{
		f = new JFrame("Chatroom");
		f.setSize(500, 700);
		f.add(box);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.getContentPane().setBackground(Constants.BLACK);
		f.setVisible(true);
	}
	
	/**
	 * Clears all text from the chat console.
	 */
	public void clear()
	{
		chatConsole.setText("");
	}
	
	/**
	 * Clears all text from the chat log.
	 */
	public void clearLog()
	{
		chatLog.setText("");
	}
	
	/**
	 * Adds a message (String) to the chat log.
	 * @param s The string to append to the chat log.
	 */
	public void updateChat(String s)
	{
		chatLog.append(s);
	}
	
	/**
	 * Gets the user-typed text from the chat console.
	 * @return A String.
	 */
	public String getMsg()
	{
		String message = chatConsole.getText().replaceAll("\n", "");
		return message;
	}
	
	/**
	 * Generates a Window Closing event.
	 */
	public void close()
	{
		f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
	}
	
	/**
	 * Adds listeners for the ENTER key and window closing events. On window closing event, the
	 * program sends a quit message to the server. On an ENTER key event, the program sends the text
	 * in the chat console to the server.
	 */
	public void addListeners()
	{
		f.addWindowListener(new WindowListener()
				{

					@Override
					public void windowActivated(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowClosed(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowClosing(WindowEvent w) {
						// TODO Auto-generated method stub
						chatConsole.setText("QUIT");
						sending = true;
					}

					@Override
					public void windowDeactivated(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowDeiconified(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowIconified(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowOpened(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					
				});
		
		chatConsole.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER && sending == false)
				{
					if(!getMsg().matches(""))
					{
						sending = true;
					}
				}
			}
			
			@Override
			public void keyTyped(KeyEvent e)
			{}
			
			@Override
			public void keyReleased(KeyEvent e)
			{}
		});
	}
}
