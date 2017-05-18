
package chat_constants;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * A class containing several color, font and miscellaneous constants used in all the GUI classes.
 * @author Bailey Duncan
 * @since May 18, 2017
 * @version 1.0
 */
public final class Constants {
	public static Color BLACK = new Color(0);
	public static Color GREEN = new Color(0, 255, 0);
	public static Color RED = new Color(255, 0, 0);
	public static Font CHATFONT = new Font("Courier New", Font.PLAIN, 12);
	public static Font TITLEFONT = new Font("Courier New", Font.PLAIN, 16);
	public static Border GREENBORDER = BorderFactory.createLineBorder(GREEN);
	public static Border EMPTYBORDER = new EmptyBorder(25, 25, 25, 25);
	
	public static String SERVERFULL = "Server is Full. Please Try Again Later...";
	public static String SERVERSHUTDOWN = "The Server has been Shutdown by the Host.";
	
	/**
	 * Generate an error message dialog.
	 * @param error The message displayed in the dialog window.
	 */
	public static void errorPopup(String error)
	{
		JOptionPane.showMessageDialog(null, error, "Error!", JOptionPane.WARNING_MESSAGE);
	}
}
