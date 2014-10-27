import java.net.*;
import java.io.*;
public class Hello extends Agent implements Runnable, HelloGoodbyeInterface, Serializable
{
		/*
			Michael Bridges

			This is the Hello class, it broadcasts hello messages

			Class Variables:
				private String serverName
					the name of the server

				private InetSocketAddress serverLocation
					InetSocketAddress of the server location

			Constructors:
				public Hello(String serverName, InetSocketAddress serverLocation)
					initializes instance variables and creats this object as an agent
			Methods:
				public void serverSideRun(Socket socket, LogWriterInterface logWriter)
					writes a hello message to the server, then casts as an agent and calls the agents clientSideRun

				public void clientSideRun(Socket socket)

				public void run()
					writes itself to server, read agent from server.


		*/
	private static final long serialVersionUID = 1;
	private InetSocketAddress serverLocation;
	private String serverName;

	public Hello(String serverName, InetSocketAddress serverLocation)
	{
		super();
		this.serverName = serverName;
		this.serverLocation = serverLocation;
	}

	public void serverSideRun(Socket socket, LogWriterInterface logWriter)
	{
		ObjectOutputStream objectOutputStream;
		try
		{
			writeMyselfTo(socket);
			objectOutputStream = new ObjectOutputStream(super.getOutputStreamFrom(socket));
			objectOutputStream.writeObject(MediaCatalog.getInstance().getSongs());
		}
		catch(Exception e){ e.printStackTrace(); }
		super.closeEverything(socket);
	}

	public void clientSideRun(Socket socket)
	{
		Song[] songs;
		try
		{
			songs = (Song[])readObjectFrom(socket);

			PlayList.getInstance().loadSongsFrom(this.serverLocation, songs);
			System.out.println("\n\n>>>>>Just loaded " + PlayList.getInstance().getAllSongs(this.serverLocation).length + " songs from " + this.serverName);
		}
		catch(Exception e) { e.printStackTrace(); }
		super.closeEverything(socket);
	}

	public void run()
	{
		Agent agent;
		Socket socket;
		ObjectOutputStream objectOutputStream;
		Song[] songs;

		objectOutputStream = null;
		try
		{
			if(PlayList.getInstance().getAllSongs(this.serverLocation).length < 1)
			{
				socket = new Socket(this.serverLocation.getAddress(), serverLocation.getPort());
				super.writeMyselfTo(socket);

				agent = (Agent)super.readObjectFrom(socket);
				agent.clientSideRun(socket);
			}

		}
		catch(Exception e) { e.printStackTrace(); }
	}
}
