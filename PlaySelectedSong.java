import java.net.*;
import java.io.*;
public class PlaySelectedSong extends PlaySong implements Serializable
{
		/*
			Michael Bridges

			This is the PlaySelectedSong class plays the selected song

			Class Variables:
				private static final long serialVersionUID = 1
					to implement Serializable

			Constructors:
				public PlaySelectedSong(Song song)
					calls the super constructor and passes song

			Methods:
				public void serverSideRun(Socket socket, LogWriterInterface logWriter)
					sets the song to the song passed and
					calls the super server side run of PlaySong
	*/
	private static final long serialVersionUID = 1;
	public PlaySelectedSong(Song song)
	{
			super(song);
	}

	public void serverSideRun(Socket socket, LogWriterInterface logWriter)
	{
		Song song;

		song = super.getSong();
		song = MediaCatalog.getSong(song.getSongId());
		super.setSong(song);
		super.serverSideRun(socket, logWriter);
	}

}