import java.net.*;
import java.io.*;
public class PlayRandomSong extends PlaySong implements Serializable
{
		/*
			Michael Bridges

			This is the PlayRandomSong class plays a random song

			Class Variables:
				private static final long serialVersionUID = 1
					to implement Serializable

			Constructors:
				public PlayRandomSong(Song song)
					calls the super constructor


			Methods:
				public void serverSideRun(Socket socket, LogWriterInterface logWriter)
					calls the super server side run after setting the song to a random song
	*/
	private static final long serialVersionUID = 1;
	public PlayRandomSong()
	{
		super();
	}

	public void serverSideRun(Socket socket, LogWriterInterface logWriter)
	{
		super.setSong( MediaCatalog.getInstance().getRandomSong());
		System.out.println("PlayRandomSong.serverSideRun(): " + super.getSong());
		super.serverSideRun(socket, logWriter);
	}
}