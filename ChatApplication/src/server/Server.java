package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import chat_constants.Constants;


/**
 * Accepts clients as long as the server has not reached its capacity 
 * and the server is online. Once a client is accepted, a new ServerThread is started and 
 * is passed the client's socket connection.
 * @author Bailey Duncan
 * @since May 18, 2017
 * @version 1.0
 */
public class Server extends Thread{
	
	private ServerSocket ss;
	private Socket sock;
	private ServerThread clients[];
	private ServerGUI serverGUI;
	private Chatroom chat;
	private int shutdownMsgID;
	
	private String chatName;
	private boolean  clientConnected[];
	private int CLIENT_CAP;
	private int clientCount;
	
	/**
	 * Constructor for the Server class.
	 * @param port The port number which the server operates on.
	 * @param chatname The title of the chatroom.
	 * @param cap The maximum number of connected clients allowed on the server
	 */
	public Server(int port, String chatname, int cap)
	{
		try{
			this.CLIENT_CAP = cap;
			chatName = chatname;
			ss = new ServerSocket(port);
			clients = new ServerThread[CLIENT_CAP];
			clientConnected = new boolean[CLIENT_CAP];
			clientCount = 0;
			chat = new Chatroom();
			serverGUI = new ServerGUI(this, CLIENT_CAP, port);
		}
		catch(IOException e)
		{
			Constants.errorPopup("IOException Occurred! " + e.getMessage());
		}
	}
	
	/**
	 * Replaces the shutdown message in the chat, allowing clients to connect to the server again.
	 */
	public void endShutdownMsg()
	{
		if(chat.getMsg(shutdownMsgID).equals(Constants.SERVERSHUTDOWN))
		{
			chat.setMsg(shutdownMsgID, "Server was shutdown");
			chat.addMsg("Server Restarted");
		}
	}
	
	/**
	 * Generates a shutdown message in the chat, which will cause the clients to be kicked when read.
	 */
	public void shutdown()
	{
		shutdownMsgID = chat.getSize();
		chat.addMsg(Constants.SERVERSHUTDOWN);
	}
	
	/**
	 * Increments or decrements the number of clients connected to the server.
	 * @param increment True if incrementing (new client connected), false if decrementing (client left or was kicked).
	 */
	public synchronized void updateClientCount(boolean increment)
	{
		if(increment)
			clientCount++;
		else
			clientCount--;
	}
	
	/**
	 * Decrements the client count in both the server and serverGUI, and changes 
	 * the "clientConnected" value at the thread ID number to false. This allows 
	 * that ID to be available for newly connected clients.
	 * @param clientNum The thread ID (array index) assigned to the client.
	 */
	public synchronized void endConnection(int clientNum)
	{
		clientConnected[clientNum] = false;
		updateClientCount(false);
		serverGUI.updateLoad(clientCount);
	}
	
	/**
	 * Accepts clients as long as the server is online and the server is not full, and starts a ServerThread to
	 * communicate with clients. 
	 */
	public void run()
	{
		chat.addMsg("Welcome to " + chatName);
		while(true)
		{
				try
				{
					if(serverGUI.isOnline())
					{
						sock = ss.accept();
						if(clientCount == CLIENT_CAP)
						{
							BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
							PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
							in.readLine();
							out.println(Constants.SERVERFULL);
							in.readLine(); // Wait for acknowledge
							sock.close();
							sock = null;
						}
						else
						{
							for(int i = 0; i < CLIENT_CAP; i++)
							{
								if(!clientConnected[i])
								{
									clientConnected[i] = true;
									clients[i] = new ServerThread(sock, chat, this, i);
									clients[i].start();
									sock = null;
									updateClientCount(true);
									serverGUI.updateLoad(clientCount);
									break;
								}
							}
						}
					}
					else
					{
						Thread.sleep(500);
					}
				}
			catch(IOException e)
			{
				Constants.errorPopup("IO Exception Occurred! " + e.getMessage());
			}
			catch(Exception e)
			{
				Constants.errorPopup("An Exception Occurred! " + e.getMessage());
			}
		}
	}
}
