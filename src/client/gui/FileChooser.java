
package client.gui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

import util.Utils;

import client.Client;

/*
 * FileChooserDemo2.java requires these files:
 *   ImageFileView.java
 *   ImageFilter.java
 *   ImagePreview.java
 *   Utils.java
 *   images/jpgIcon.gif (required by ImageFileView.java)
 *   images/gifIcon.gif (required by ImageFileView.java)
 *   images/tiffIcon.gif (required by ImageFileView.java)
 *   images/pngIcon.png (required by ImageFileView.java)
 */
public class FileChooser extends JPanel
 {	
	
	public static Client client;

	public FileChooser() {

		super(new BorderLayout());

		try {
			UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
					);
		} catch (Exception e) { }

		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new ImageFilter());
		//Add a custom file filter and disable the default
		//(Accept All) file filter.
		//fc.addChoosableFileFilter(new ImageFilter());
		fc.setAcceptAllFileFilterUsed(false);


		//Show it.
		int returnVal = fc.showDialog(FileChooser.this,
				"Select your profile picture");

		//Process the results.
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			client.getUser().setAvatar( Utils.getAvatar( file.getAbsolutePath()));            
		}
		else {
			JOptionPane.showMessageDialog(null, "Please select a picture with '.jpg' or '.jpeg' extension...");
		}    
		//Reset the file chooser for the next time it's shown.
		fc.setSelectedFile(null);
		
	}

	public static void createAndShowGUI(Client client) {
		FileChooser.client = client;
		//Create and set up the window.
		JFrame frame = new JFrame("File Chooser");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//Add content to the window.
		frame.getContentPane().add(new FileChooser());

		//Display the window.
		
	}

}
