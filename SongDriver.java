import java.io.*;
import java.net.*;
public class SongDriver
{
	public static void main(String[]args) throws IOException
	{
		Song song;
		song = new Song("California", "Phantom Planet", "mp3", new File("song.txt"));

		MediaCatalog.newInstance();
		MediaCatalog.getInstance().loadSongsFromFile(new File("song.txt"));
		MediaCatalog.getInstance().getRandomSong();

		PlayList p = new PlayList();
		p.loadSongsFrom(new InetSocketAddress(12345), MediaCatalog.getInstance().getSongs());
	}
}