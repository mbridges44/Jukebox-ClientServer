import java.net.*;
import java.io.*;
public class Goodbye implements Serializable, HelloGoodbyeInterface, Runnable
{
		/*
			Michael Bridges

			This is the goodbye class, it broadcasts hellogoodbyemessages

			Class Variables:
				private String serverName
					the name of the server

				private InetSocketAddress serverLocation
					InetSocketAddress of the server location

			Constructors:
				public Goodbye(String serverName, InetSocketAddress serverLocation)
					initializes instance variables and creats this object as an agent
			Methods:
				public void run()
					displays goodbye and deletes songs

	*/
	private static final long serialVersionUID = 1;
	private String serverName;
	private InetSocketAddress serverLocation;

	public Goodbye(String serverName, InetSocketAddress serverLocation)
	{
		this.serverName = serverName;
		this.serverLocation = serverLocation;
	}

	public void run()
	{
		System.out.println("Goodbye, deleting songs.");
		PlayList.newInstance().removeSongsFrom(this.serverLocation);
		System.out.println("Deleted " + PlayList.getInstance().getAllSongs(this.serverLocation).length + " songs from " + this.serverName);

	}

}