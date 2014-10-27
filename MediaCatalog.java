import java.util.*;
import java.io.*;
public class MediaCatalog
{
/*
			Michael Bridges

			This is the MediaCatalog class it holds song/refrences

			Class Variables:
				public static MediaCatalog mediaCatalog
					the instance of mediaCatalog

				private static final Map<String, Song> catalog
					holds the songs with the Id as key

			Constructors:
				private MediaCatalog()
					sets mediaCatalog to this
			Methods:
				synchronized public static MediaCatalog newInstance()
					returns a new instance if the mediaCatalog refrence is null

				public void loadSongsFromFile(File file) throws IOException
					uses file processing to read songs from a file and create song objects with them

				public static Song getSong(String songId)
					returns a song using its Id

				public Song[] getSongs()
					gets the songArray

				public Song getRandomSong()
					gets a random song

	*/

	public static MediaCatalog mediaCatalog = null;
	private static final Map<String, Song> catalog = new Hashtable <String, Song> ();
	private MediaCatalog()
	{
		System.out.println("Playlist");
		mediaCatalog = this;
	}

	synchronized public static MediaCatalog newInstance()
	{
		if(MediaCatalog.mediaCatalog == null) { MediaCatalog.mediaCatalog = new MediaCatalog(); }
		return MediaCatalog.mediaCatalog;

	}

	public static MediaCatalog getInstance() { return newInstance(); }

	public void loadSongsFromFile(File file)
	{
		BufferedReader br;
		String currentLine;
		String[] currentSong;
		String tabHold;

		currentSong = new String[4];
		br = null;
		currentLine = "";


		try{ br = new BufferedReader(new FileReader(file)); }
		catch(FileNotFoundException fnfe) { fnfe.printStackTrace(); }

		while(currentLine != null)
		{
			try
			{
			currentLine = br.readLine();
			if(currentLine == null) break;

			currentSong[0] = currentLine.substring(0, currentLine.indexOf("\t"));	//Song
			currentLine = currentLine.substring(currentLine.indexOf("\t")+1, currentLine.length());

			currentSong[1] = currentLine.substring(0, currentLine.indexOf("\t"));	//Artist
			currentLine = currentLine.substring(currentLine.indexOf("\t")+1, currentLine.length());

			currentSong[2] = currentLine.substring(0, currentLine.indexOf("\t"));	//format
			currentLine = currentLine.substring(currentLine.indexOf("\t")+1, currentLine.length());

			currentSong[3] = currentLine.substring(0, currentLine.length());	//path

			for(int i = 0;i<currentSong.length;i++)
			{
				if(currentSong[i] == null || currentSong[i].length() < 1)
				{
					throw new InvalidFieldDataException("Field " + i + " must not be null.");
				}
			}

			Song s = new Song(currentSong[0],currentSong[1],currentSong[2], new File(currentSong[3]));
			MediaCatalog.catalog.put(s.getSongId(), s);
			}catch(Exception e) { e.printStackTrace(); }
		}
	}

	public static Song getSong(String songId)
	{
		return catalog.get(songId);
	}

	private class InvalidFieldDataException extends IllegalArgumentException
	{
		/*
		A custom exception that extends IllegalArgumentException.
		*/
		public InvalidFieldDataException(){ super(); }

		public InvalidFieldDataException(String message){ super(message); }

	}

	public Song[] getSongs()
	{
		return (Song[])catalog.values().toArray( new Song[catalog.size()]);
	}

	public Song getRandomSong()
	{
		int randomNum;
		randomNum =  (int)Math.floor((Math.random()*this.getSongs().length));
		return this.getSongs()[randomNum];
	}

}

