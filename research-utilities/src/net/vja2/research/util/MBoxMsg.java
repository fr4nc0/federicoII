/* $Id: MBoxMsg.java 10 2005-12-25 01:55:01Z vja2 $ */
package net.vja2.research.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author vja2
 *
 */
public class MBoxMsg {
	public MBoxMsg(String id, String content, boolean isSpam)
	{
		this.fromLine = id;
		this.message = content;
		this.isSpam = isSpam;
	}
	public MBoxMsg(String id, String content)
	{
		this(id, content, false);
	}
	
	public boolean isSpam() { return this.isSpam; }
	
	public String getid() { return this.fromLine; }
	public String getmsg() { return this.message; }
	
	public String toString() { return this.getmsg(); }
	
	public static ArrayList<MBoxMsg> readmbox(File file) throws IOException { return readmbox(file, false); }
	
	public static ArrayList<MBoxMsg> readmbox(String filename) throws IOException { return readmbox(new File(filename)); }
	
	public static ArrayList<MBoxMsg> readmbox(String filename, boolean isSpam) throws IOException { return readmbox(new File(filename), isSpam); }
	
	public static ArrayList<MBoxMsg> readmbox(File file, boolean isSpam) throws IOException
	{
		ArrayList<MBoxMsg> messages = new ArrayList<MBoxMsg>();
		
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = null, message = new String(),id = null;
		boolean lastLineIsBlank = true;

		while(in.ready())
		{
			line = in.readLine();
			if(lastLineIsBlank && line.startsWith("From "))
			{
				if(id != null)
				{
					messages.add(new MBoxMsg(id, message, isSpam));
					message = new String();
				}
				id = line;
			}
			else
				message = message.concat(line.concat("\n"));
			lastLineIsBlank = (line.length() == 0);
		}
		messages.add(new MBoxMsg(id, message, isSpam));
		in.close();
		
		return messages;
	}
	
	private String fromLine;
	private String message;
	private boolean isSpam;
}

