package server;
import java.util.ArrayList;

/**
 * Stores an arraylist of chat messages, each with an ID (arraylist index) and 
 * a message (String).
 * @author Bailey Duncan
 * @since May 18, 2017
 * @version 1.0
 */
public class Chatroom {
	private ArrayList<String> chatroom;
	
	/**
	 * Constructor for the chatroom arraylist.
	 */
	public Chatroom()
	{
		chatroom = new ArrayList<String>();
	}
	
	/**
	 * Gets the size of the arraylist.
	 * @return The arraylist size.
	 */
	public int getSize()
	{
		return chatroom.size();
	}
	
	/**
	 * Fetches the message at index "msgID" from the arraylist.
	 * @param msgID The index of message in the arraylist.
	 * @return A String message.
	 */
	public String getMsg(int msgID)
	{
		return chatroom.get(msgID);
	}
	
	/**
	 * Overwrites the message at arraylist index 'msgID' with 'msg'.
	 * @param msgID The messages index in the arraylist.
	 * @param msg The new message which will replace the old one.
	 */
	public void setMsg(int msgID, String msg)
	{
		chatroom.set(msgID, msg);
	}
	
	/**
	 * Adds a message to the chat in a synchronized way to make the list more thread safe.
	 * @param msg The message to be added.
	 */
	public synchronized void addMsg(String msg)
	{
		chatroom.add(msg);
	}
	
}
