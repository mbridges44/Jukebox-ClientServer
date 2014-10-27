import java.util.*;
import java.io.*;
import java.net.*;

public class PlayList
{
		/*
			Michael Bridges

			This is the Hello class, it broadcasts hello messages

			Class Variables:
				private static PlayList playList
					the Playlist variable that holds the only PlayList that can be made

				private static final Map<String, Song[]> songTable
					the HashMap for the song table, has a string to a server ip + port number as the key, song array as value


			Constructors:
				public PlayList()
					does nothing

			Methods:
				public void loadSongsFrom(InetSocketAddress server, Song[] songs)
					takes in a server and song array and places them into the hashmap

				public Song[] getAllSongsFrom(InetSocketAddress server)
					gets the song array from InetSocktAddress

				public void removeSongsFrom(InetSocketAddress server)
					removes the Song[] from that key in the HashMap

				public String[] getAllServers()
					returns the keys to the hashmap

				private String getKeyFrom(InetSocketAddress server)
					returns "Ip:Socket" as string

				public Song[] getAllSongs(String server)
					gets all songs from server

				public static PlayList newInstance()
					returns getInstance()

				public static PlayList getInstance()
					returns the refrence to the PlayList variable
	*/

	private static final PlayList playList = new PlayList();
	private static final Map<String, Song[]> songTable = new Hashtable <String, Song[]> ();

	public PlayList()
	{

	}

	public void loadSongsFrom(InetSocketAddress server, Song[] songs)
	{
		songTable.put(getKeyFrom(server) , songs);
	}

	public Song[] getAllSongs(InetSocketAddress server)
	{
		return getAllSongs(getKeyFrom(server));
	}

	public Song[] getAllSongs(String server)
	{
		SongList songList;
        Song[] songs;


		songList = new SongList(songTable.get(server));

		if(songList == null)
		{
			songs = new Song[0];
		}
		else songs = songList.getSongs();

		return songs;
	}

	public void removeSongsFrom(InetSocketAddress server)
	{
		songTable.remove(getKeyFrom(server));
	}

	public String[] getAllServers()
	{
		return songTable.keySet().toArray(new String[songTable.size()]);
	}

	private String getKeyFrom(InetSocketAddress server)
	{
		if(server != null)
		{
			return server.getAddress().getHostAddress().toString() + ":" + server.getPort();
		}
		else
		{
			throw new IllegalArgumentException("Cannot get key from server: Server is null");
		}
	}


	public static PlayList getInstance()
	{
		return newInstance();
	}

	public static PlayList newInstance()
	{
		return playList;
	}

	private class SongList
	{
		/*
			Michael Bridges

			

			Class Variables:
				private Song[] songArray
					the song array that holds the songs

			Constructors:
				public SongList(Song[] songArray)
					if song array isnt empty, stores it in the songArray class variable

			Methods:
				public int getNumberOfSongs()
					returns the number of songs

				public Song[] getSongs()
					accessor for the instance variable songArray

				public Song getSongUsingSongId(String songId)
					if songId corresponds to an actual song, returns the song
		*/

		private Song[] songArray;

		public SongList(Song[] songArray)
		{
			if(songArray != null)
			{
				this.songArray = new Song[songArray.length];
				System.arraycopy(songArray, 0, this.songArray, 0, songArray.length);
			}
			else this.songArray = new Song[0];
		}

		public int getNumberOfSongs()
		{
			return this.songArray.length;
		}

		public Song[] getSongs()
		{
			Song[] copy;
			copy = new Song[this.songArray.length];
			System.arraycopy(this.songArray, 0, copy, 0, this.songArray.length);
			return copy;
		}

		public Song getSongUsingSongId(String songId)
		{
			for(int i = 0;i<this.getNumberOfSongs();i++)
			{
				if(songId.equals(this.getSongs()[i]))
				{
					return this.getSongs()[i];
				}
			}
			throw new IllegalArgumentException("Song not found");
		}

	}
}