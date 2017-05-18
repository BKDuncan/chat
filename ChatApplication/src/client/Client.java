package client;
import java.io.*;
import java.net.*;

import chat_constants.Constants;

/**
 * Connects and communicates to a server using sockets. It can connect to localhosted servers
 * or if the servers IP address or machine name are known, by LAN. It sends String messages back 
 * and forth between the server and the client GUI.
 * @author Bailey Duncan
 * @since May 18, 2017
 * @version 1.0
 */
public class Client implements Runnable{

	Socket sock;
	BufferedReader in;
	PrintWriter out;
	String read, handle;
	int lineNumber;
	ClientGUI chatClient;
	
	/**
	 * Contructor for the Client class, which connects the socket and opens its I/O streams.
	 * @param port The port the socket will connect to.
	 * @param address The IP address or host machine name that the server is run on (or "localhost").
	 * @param handle The user's username in the chat.
	 */
	public Client(int port, String address, String handle)
	{
		try{
			chatClient = new ClientGUI(this);
			sock = new Socket(address, port);
			this.handle = handle;
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);
			lineNumber = 0;
		}
		catch(IOException e)
		{
			Constants.errorPopup("No Chatroom Found!");
			chatClient.close();
		}
	}
	
	/**
	 * Checks if the socket is closed.
	 * @return True if socket is open, false if closed.
	 */
	public boolean isConnected()
	{
		if(sock.isClosed())
			return false;
		return true;
	}
	
	/**
	 * Closes the socket (and implicitly its I/O streams) and the chat client GUI.
	 * @throws IOException Let the method calling this handle the exception.
	 */
	public void close() throws IOException
	{
			sock.close();
			chatClient.close();
	}
	
	/**
	 * Checks if the read message is a server shutdown message. If so, then kick the client
	 * and close the socket connection.
	 * @param read The read message from the server.
	 * @return True if the server has been shutdown, false otherwise.
	 * @throws IOException Let the method calling this handle the exception appropriately.
	 */
	public boolean checkShutdown(String read) throws IOException
	{
		if(read.equals(Constants.SERVERSHUTDOWN))
		{
			Constants.errorPopup(read);
			out.println(handle + " has been kicked");
			out.println("QUIT");
			close();
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the read message is a server is full message. If so, then send an acknowledge message
	 * and close the socket connection.
	 * @param read The read message from the server.
	 * @return True if the server is full, false otherwise.
	 * @throws IOException Let the method calling this handle the exception appropriately.
	 */
	public boolean checkFull(String read) throws IOException
	{
		if(read.equals(Constants.SERVERFULL))
		{
			Constants.errorPopup(read);
			out.println("Acknowledge");
			close();
			return true;
		}
		return false;
	}
	
	/**
	 * Communicates between the server and client. If a "Server is full", or a "Server is shutdown"
	 * message is recieved, the client will tell the user and close. If the user send the message 
	 * "QUIT" or "quit" exactly then the client will inform the server they are leaving and close the client.
	 */
	public void run()
	{
		try{
			out.println(handle + " joined the chat");
			read = in.readLine();
			if(checkFull(read) || checkShutdown(read))
				return;
			chatClient.clearLog();
			chatClient.updateChat(read + "\n");
			while(true)
			{
				if(chatClient.sending)
				{
					read = handle + ": " + chatClient.getMsg();
					chatClient.sending = false;
					chatClient.clear();
				}
				else
					read = "0";
				
				if(read.equals(handle + ": " + "QUIT") || read.equals(handle + ": " + "quit"))
				{
					out.println(handle + " has left");
					chatClient.updateChat(in.readLine());
					out.println("QUIT");
					close();
					break;
				}
				
				out.println(read);
				read = in.readLine();
				while(!read.matches("\0"))
				{
					if(checkShutdown(read))
						return;
					chatClient.updateChat(read + "\n");
					read = in.readLine();
				}
			}
		}
		catch(IOException e)
		{
			Constants.errorPopup("IO Exception Occurred." + e.getMessage());
		}
		catch(Exception e)
		{
			Constants.errorPopup("Exception Occurred: " + e.getMessage());
		}	
	}
}
