package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import chat_constants.Constants;

/**
 * Opens the sockets I/O streams to a client, and communicates with them.
 * The clients messages are added to the chatroom when recieved and if the message counter called "MsgID"
 * is less than the number of messages in the chatroom, it sends the appropriate number of messages back.
 * The newest message will be a null character.
 * @author Bailey Duncan
 * @since May 18, 2017
 * @version 1.0
 */
public class ServerThread extends Thread{
	private Server server;
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private String read;
	private Chatroom chat;
	private int MsgID = -1;
	private int threadID;
	
	/**
	 * Constructor for the ServerThread, opens the sockets I/O streams.
	 * @param s The socket connected to the client.
	 * @param chatroom The arraylist containing the chat messages.
	 * @param server The back-end server class.
	 * @param ID This threads ID number (index number in the server's array of ServerThreads)
	 */
	public ServerThread(Socket s, Chatroom chatroom, Server server, int ID)
	{
		try
		{
			client = s;
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
			this.chat = chatroom;
			this.server = server;
			this.threadID = ID;
		}
		catch(IOException e)
		{
			Constants.errorPopup("IOException Occurred!" + e.getMessage());
		}
	}
	
	/**
	 * Closes the socket (and its I/O streams) and decrements the client count.
	 * @throws Exception
	 */
	public void close() throws Exception
	{
		client.close();
		server.endConnection(threadID);
	}
	
	/**
	 * Concatenates all the messages the client has not recieved from the chatroom 
	 * separated by newline characters and null terminated.
	 * @return All the chat messages to send to the client.
	 */
	public String updateClient()
	{
		String msgToAdd = "";
		while(MsgID != chat.getSize() - 1)
		{
			MsgID++;
			msgToAdd += chat.getMsg(MsgID) + "\n";
		}
		return msgToAdd + "\0";
	}
	
	/**
	 * Communicates with the client, reading and appending the messages to the chatroom when
	 * necessary.
	 */
	public void run()
	{
		try
		{
			while(client.isConnected())
			{
				read = in.readLine();
				if(read.equals("0"))
					out.println(updateClient());
				else if(read.equals("QUIT"))
				{
					close();
					break;
				}
				else 
				{
					chat.addMsg(read);
					out.println(updateClient());
				}
			}
		}
		catch(IOException e)
		{
			Constants.errorPopup("IOException Occurred in serverThread Run!" + e.getMessage());
		}
		catch(Exception e)
		{
			Constants.errorPopup("Exception Occurred in serverThread Run! " + e.getMessage());
		}
	}
}
